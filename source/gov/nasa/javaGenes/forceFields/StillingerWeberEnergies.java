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
package gov.nasa.javaGenes.forceFields;

import gov.nasa.javaGenes.chemistry.Molecule;
import gov.nasa.javaGenes.chemistry.xyzFormat;
import gov.nasa.javaGenes.chemistry.Molecules;

public class StillingerWeberEnergies {
private static final String format = "java StillingerWeberEnergies input.xyz output.xyz [assumedParametersFile]";
public static void main(String[] arguments) {
  double maxEnergy = -java.lang.Double.MAX_VALUE;
  if (2 > arguments.length || arguments.length > 3) {
    System.out.println(format);
    System.exit(1);
  }
  String inputFile = arguments[0];
  String outputFile = arguments[1];


  StillingerWeber potential = null;
  Chromosome chromosome = null;

  boolean useSiFfromPaper = arguments.length < 3;
  if (!useSiFfromPaper) {
    potential = new StillingerWeber();
    potential.mustModel(new ManyMultiBodiesForOneEnergy(inputFile));
    chromosome = potential.getChromosome();
    Immigrant values = new Immigrant(arguments[2]);
    values.setupChromosome(chromosome,potential.getAlleles());
  } else {
    potential =  new StillingerWeberSiF();
    chromosome = potential.getChromosome();
  }
  potential.setChromosome(chromosome);

  Molecules molecules = xyzFormat.readMolecules(inputFile);
  for(int i = 0; i < molecules.size(); i++) {
    Molecule next = molecules.get(i);
    next.setPropertiesFromComment();
    MultiBodiesForOneEnergy multibodies = new  MultiBodiesForOneEnergy(next);
    if (useSiFfromPaper)
        multibodies.scaleLengthsBy(StillingerWeberSiF.getLengthScale());

    double energy = potential.getEnergy(multibodies);
    if (energy > maxEnergy) maxEnergy = energy;
    next.setEnergy(energy);
    next.setCommentFromProperties();
    if (i == 0)
      xyzFormat.writeToFile(next,outputFile);
    else
      xyzFormat.appendToFile(next,outputFile);
  }
  System.out.println("Maximum energy = " + maxEnergy);
}
}

