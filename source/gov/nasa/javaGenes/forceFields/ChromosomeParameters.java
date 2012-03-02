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

import gov.nasa.javaGenes.forceFields.crystals.*;
import gov.nasa.javaGenes.core.Parameters;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.javaGenes.chemistry.UnitCell;
import gov.nasa.javaGenes.core.WeightedSumFitness;
import gov.nasa.javaGenes.core.FitnessFunctionMultiObjective;
import gov.nasa.javaGenes.core.FitnessFunction;
import gov.nasa.javaGenes.core.BreederSteadyState;
import gov.nasa.alsUtility.Error;

public class ChromosomeParameters extends Parameters {

static {
   // RandomNumber.setSeed(990639400906L); // used to generate repeatable runs. See seed.txt for seed of last run
}
public AlleleTemplate alleles;
public String lengthsAndAnglesFilename = "lengthsAndAngles.tsd";
public AssumedParameters assumed;
public Immigrants immigrants;
public AtomicSpecies[] species;
public ManyMultiBodiesForOneEnergy energiesToExamineBestIndividual;

public Potential potential;
public int whichPotential = Potential.STILLINGER_WEBER;
public static final double SiCutoff = 3.77;
public static final double DiamondLatticConstant = 5.430300236;
public static final double BCCLatticConstant = 3.420850039;
public static final double FCCLatticConstant = 4.309999943;
public static final double SimpleCubicLatticConstant = 2.71510005;

public final String fileDirectory = "../SiInput/";
public String farWallFilename = fileDirectory + "SiDimerFarWall.xyz";
public String nearWallFilename = fileDirectory + "SiDimerNearWall.xyz";
public String minimumFilename = fileDirectory + "SiDimerMinimum.xyz";
public String forceFilename = fileDirectory + "SiDimerTail.forces";
public String bondLengthFilename = fileDirectory + "bondLengthTest.xyz";

public FitnessFunctionMultiObjective eval = new FitnessFunctionMultiObjective();

private void setParameters() {
    DoubleInterval exponentInterval = new DoubleInterval(-6,24);
    DoubleInterval factorInterval = new DoubleInterval(-100,150);
    DoubleInterval cutoffInterval = new DoubleInterval(0,8);
    String Si8UnitCellFilename = "input/Si8UnitCell.xyz";

    populationSize = 5;
    kidsPerGeneration = 6;
    maximumGenerations = 5;
    frequencyOfASCIIPopulations = 1;
    stopAtPerfection = false;
    
    species = new AtomicSpecies[1];
    species[0] = new AtomicSpecies("Si",SiCutoff,0.3);

    switch(whichPotential) {
        case Potential.STILLINGER_WEBER: 
            potential = new StillingerWeber(species,factorInterval,exponentInterval,cutoffInterval);
            break;
        default:
            Error.assertTrue(true);
    }
    alleles = potential.getAlleles();
    
    ManyMultiBodiesForOneEnergy Si8 = new ManyMultiBodiesForOneEnergy(potential,Si8UnitCellFilename);
    potential.mustModel(Si8);    
    Si8.removeBodiesAboveCutoff(potential);
    
        
    WeightedSumFitness fitness = new WeightedSumFitness();
    FitnessFunction f;
    f = new ManyMoleculesEnergyFitness(potential,Si8);
    f.setName("Si8");
    fitness.add(1.0,f);

    ManyMoleculesEnergyFitness mf = new ManyMoleculesEnergyFitness(potential,new ManyMultiBodiesForOneEnergy(),true);
    Diamond diamond = new Diamond("Si", SiCutoff/DiamondLatticConstant);
    diamond.scaleLengthsBy(3.77);
    mf.add(diamond,-4.63);
    mf.setName("diamond");
    fitness.add(1.0,mf);
    eval.add(1.0,mf);

    BodyCenteredCubic bcc = new BodyCenteredCubic("Si", SiCutoff/BCCLatticConstant);
    FaceCenteredCubic fcc = new FaceCenteredCubic("Si", SiCutoff/FCCLatticConstant);
    SimpleCubic simpleCubic = new SimpleCubic("Si", SiCutoff/SimpleCubicLatticConstant);
    
    for(int i = -10; i <= 10; i += 2) {
        double factor = 1 + i * 0.01;
        ManyMultiBodiesForOneEnergy m = new ManyMultiBodiesForOneEnergy();
        m.add(diamond.copy().scaleLengthsByAndReturnThis(factor));
        m.add(bcc.copy().scaleLengthsByAndReturnThis(factor));
        m.add(fcc.copy().scaleLengthsByAndReturnThis(factor));
        m.add(simpleCubic.copy().scaleLengthsByAndReturnThis(factor));
        LowestToHighestEnergyFitness lf = new LowestToHighestEnergyFitness(potential, m);
        lf.setDoPerAtomEnergies(true);
        lf.setName("CrystalOrderScaleBy=" + factor);
        fitness.add(1.0,lf);
        eval.add(1.0,lf);
    }

    ManyMultiBodiesForOneEnergy m = new ManyMultiBodiesForOneEnergy(potential,nearWallFilename);
    LowestToHighestEnergyFitness lf = new LowestToHighestEnergyFitness(potential, m);
    lf.setDoPerAtomEnergies(true);
    lf.setName("FarWallOrder");
    fitness.add(1.0,lf);
    eval.add(1.0,lf);
    
    MMEFreferenceRMS rf = new MMEFreferenceRMS(potential,new ManyMultiBodiesForOneEnergy(potential,fileDirectory + "SiSEenergy3.xyz"),true);
    rf.setName("Si3");
    fitness.add(1.0,rf);
    eval.add(1.0,rf);
    
    fitnessFunction = fitness;
    evaluationFunction = eval;

    breeder = new gov.nasa.javaGenes.core.BreederSteadyState(this);
    alleles = potential.getAlleles();
    childMakerProvider.add(new Mutation3Parents(alleles));
}

/**
Sets up all parameters
*/
public ChromosomeParameters() {
  setParameters();
}
public void makeFiles() {
   fitnessFunction.makeFiles();
   alleles.makeFiles();
}

}