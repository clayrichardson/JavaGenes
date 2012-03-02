//
// Copyright (C) 2005 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA.txt at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//
//  Created by Al Globus on Thu Aug 29 2002.
package gov.nasa.javaGenes.main;

import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.IO;
import java.io.PrintWriter;
import gov.nasa.javaGenes.chemistry.Molecule;
import gov.nasa.javaGenes.forceFields.StillingerWeber;
import gov.nasa.javaGenes.forceFields.MultiBodiesForOneEnergy;
import gov.nasa.javaGenes.forceFields.ManyMultiBodiesForOneEnergy;
import gov.nasa.javaGenes.forceFields.Immigrant;
import gov.nasa.javaGenes.forceFields.Chromosome;
import gov.nasa.javaGenes.chemistry.xyzFormat;
import gov.nasa.alsUtility.RandomNumber;

public class LatticeConstantEnergies {
private static final String format = "java main.LatticeConstantEnergies input.xyz outputDirectory parametersFile numberOfSamples smallestUnitCellScale largestUnitCellScale [[numberOfPerturbedMolecules distanceToRandomize [numberOfAtomsToRandomize [seed]]]";

private static StillingerWeber potential;
private static String inputFile;
private static String outputDirectory;
private static String parametersFile;
private static int numberOfSamples;
private static double smallestUnitCellScale;
private static double largestUnitCellScale;

private static int numberOfPerturbedMolecules;
private static double distanceToRandomize;
private static int numberOfAtomsToRandomize;

private static double results[][];

public static void main(String[] arguments) {
    final int cellOnly = 6;
    final int perturbAlso = 8;
    final int limitAtomsToRandomize = 9;
    final int withSeed = 10;

    assertTrue(arguments.length == cellOnly || arguments.length == perturbAlso || arguments.length == limitAtomsToRandomize || arguments.length == withSeed);
    inputFile = arguments[0]; assertTrue(inputFile);
    outputDirectory = arguments[1]; assertTrue(outputDirectory);
    Utility.makeDirectory(outputDirectory);
    parametersFile = arguments[2]; assertTrue(parametersFile);
    numberOfSamples = Utility.string2integer(arguments[3]); assertTrue(numberOfSamples > 0, "numberOfSamples must be > 0");
    smallestUnitCellScale = Utility.string2double(arguments[4]); assertTrue(smallestUnitCellScale > 0);
    largestUnitCellScale = Utility.string2double(arguments[5]); assertTrue(largestUnitCellScale > smallestUnitCellScale);

    boolean doPerturbations = arguments.length >= perturbAlso;
    boolean doAtomsLimit = arguments.length >= limitAtomsToRandomize;

    if (doPerturbations) {
    	numberOfPerturbedMolecules = Utility.string2integer(arguments[6]); assertTrue(numberOfPerturbedMolecules > 0);
    	distanceToRandomize = Utility.string2double(arguments[7]); assertTrue(distanceToRandomize > 0);
    }
    if (doAtomsLimit)
    	numberOfAtomsToRandomize = Utility.string2integer(arguments[8]); assertTrue(numberOfAtomsToRandomize > 0);
    if (arguments.length == withSeed)
        RandomNumber.setSeed(Utility.string2long(arguments[9]));

    // set up array of results.  Columns are molecules, rows are scaled values.  First column is scales
    results = new double[2 + numberOfPerturbedMolecules][];
    double scaleIncrement = (largestUnitCellScale - smallestUnitCellScale) / (numberOfSamples-1);
    for(int i = 0; i < results.length; i++)
        results[i] = new double[numberOfSamples+1];
    // put scale facotrs in first column
    results[0][0] = 1.0;
    for(int i = 1; i < results[0].length; i++)
        results[0][i] = smallestUnitCellScale + ((i-1) * scaleIncrement);
    
    potential = new StillingerWeber();
    potential.mustModel(new ManyMultiBodiesForOneEnergy(inputFile));
    Chromosome chromosome = potential.getChromosome();
    Immigrant values = new Immigrant(parametersFile);
    values.setupChromosome(chromosome,potential.getAlleles());
    potential.setChromosome(chromosome);

    Molecule molecule = null;
    try {
        molecule = xyzFormat.read(inputFile);
    } catch (Exception e) {
        assertTrue(false, inputFile + " not in proper xyz format");
    }
    molecule.setPropertiesFromComment();
    assertTrue(molecule.hasUnitCell(), inputFile + " must have unit cell");
    
    makeCurve(molecule,"minimum",1);
    if (!doPerturbations)
        System.exit(0);
    for(int i = 1; i <= numberOfPerturbedMolecules; i++) {
        Molecule m = molecule.copy();
        if (doAtomsLimit)
            m.randomizeAtomicPositionsBy(distanceToRandomize,false,numberOfAtomsToRandomize);
        else
            m.randomizeAtomicPositionsBy(distanceToRandomize,false);
        makeCurve(m,i+"",i+1);
    }
    
    // output energies in ascii format
    String energyFilename = outputDirectory + Utility.fileSeparator() + "energies" + ".tsd";
    PrintWriter energyFile = gov.nasa.alsUtility.IO.getPrintWriter(energyFilename);
    energyFile.print("scaleFactor\tminimum");
    for(int i = 1; i < results.length-1; i++)
        energyFile.print("\t" + i);
    energyFile.println();
    for(int i = 0; i < results[0].length; i++) {
        energyFile.print(results[0][i]+"");
        for(int j = 1; j < results.length; j++)
            energyFile.print("\t" + results[j][i]);
        energyFile.println();
    }
    energyFile.close();
    System.exit(0);
}
private static void makeCurve(Molecule molecule, String filenameBase, int resultsColumn) {
    String moleculeFilename = outputDirectory + Utility.fileSeparator() + filenameBase + ".xyz";

    MultiBodiesForOneEnergy multibodies = new  MultiBodiesForOneEnergy(molecule);
    molecule.setEnergy(potential.getEnergy(multibodies));
    molecule.setCommentFromProperties();
    xyzFormat.writeToFile(molecule,moleculeFilename);
    
    for(int i = 0; i < results[resultsColumn].length; i++) {
        Molecule m = molecule.copy();
        m.scaleBy(results[0][i]);
        multibodies = new  MultiBodiesForOneEnergy(m);
    	results[resultsColumn][i] = potential.getEnergy(multibodies);
    }
}
private static void assertTrue(boolean condition) {
    Error.userAssert(condition,format);
}
private static void assertTrue(boolean condition, String s) {
    Error.userAssert(condition, s + "\n" + format);
}
private static void assertTrue(String s) {
    Error.userAssert(s != null,format);
    Error.userAssert(s.length() > 0,format);
}

}


