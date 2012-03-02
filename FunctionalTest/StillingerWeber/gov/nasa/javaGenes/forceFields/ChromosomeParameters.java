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

import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.alsUtility.IntegerInterval;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.javaGenes.core.WeightedSumFitness;
import gov.nasa.javaGenes.core.FitnessFunction;
import gov.nasa.javaGenes.core.BreederSteadyState;
import gov.nasa.javaGenes.core.ChildMaker;

public class ChromosomeParameters extends gov.nasa.javaGenes.core.Parameters {

public AlleleTemplate alleles;

public String lengthsAndAnglesFilename = "lengthsAndAngles.tsd";
public String assumedParametersFilename = "assumedParameters.tsd";
public AssumedParameters assumed;
public String immigrantsFilename = "immigrants.tsd";
public Immigrants immigrants;

public DoubleInterval exponentMinimumInterval = new DoubleInterval(-6,0);
public DoubleInterval exponentMaximumInterval = new DoubleInterval(12,24);
public DoubleInterval exponentInterval = new DoubleInterval(0,24);
public DoubleInterval factorMinimumInterval = new DoubleInterval(-100,-50);
public DoubleInterval factorMaximumInterval = new DoubleInterval(75,150);
public DoubleInterval factorInterval = new DoubleInterval(-100,100);
public DoubleInterval cutoffMaximumInterval = new DoubleInterval(3,8);
public DoubleInterval cutoffInterval = new DoubleInterval(0,8);

public DoubleInterval tournamentProbabilityInterval = new DoubleInterval(0.1,1.0);

public IntegerInterval intervalCrossoverFrequencyInterval = new IntegerInterval(1,10);
public DoubleInterval extensionInterval = new DoubleInterval(0.3,3);
public IntegerInterval crossoverFrequencyInterval = new IntegerInterval(0,5);
public IntegerInterval arrayCrossoverFrequencyInterval = new IntegerInterval(0,0);
public IntegerInterval mutationTransmissionFrequencyInterval = new IntegerInterval(1,30);
public ManyMultiBodiesForOneEnergy energiesToExamineBestIndividual;

public String SiFAllFilename = "../SiFAllSmall.xyz";
public String Cfilename = "../C.xyz";

public Potential potential;

private void setParameters() {
    RandomNumber.setSeed(990639400907L); // used to generate repeatable runs. See seed.txt for seed of last run
    populationSize = 10;
    kidsPerGeneration = 20;
    maximumGenerations = 5;
    tournamentProbability = tournamentProbabilityInterval.random();
    frequencyOfASCIIPopulations = 1;
    stopAtPerfection = false;
    

    ManyMultiBodiesForOneEnergy C = new ManyMultiBodiesForOneEnergy(Cfilename);
    ManyMultiBodiesForOneEnergy SiFAll = new ManyMultiBodiesForOneEnergy(SiFAllFilename);
    potential = new StillingerWeber(factorInterval,exponentInterval,cutoffInterval);
    potential.mustModel(SiFAll);
    potential.mustModel(C);
    
    SiFAll.removeBodiesAboveCutoff(potential);
    ((StillingerWeber)potential).setCutoff(new TwoBody("C","C"), 7);
    C.removeBodiesAboveCutoff(potential);
    
    alleles = potential.getAlleles();
    
    WeightedSumFitness fitness = new WeightedSumFitness();
    FitnessFunction f;
    f = new ManyMoleculesEnergyFitness(potential,SiFAll);
    f.setName("SiFAll");
    fitness.add(1.0,f);
    f = new ManyMoleculesEnergyFitness(potential,C);
    f.setName("C");
    fitness.add(1.0,f);
    fitnessFunction = fitness;
    
  breeder = new BreederSteadyState(this);
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
