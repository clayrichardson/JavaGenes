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
import gov.nasa.javaGenes.core.*;
import gov.nasa.alsUtility.RandomNumber;

public class ChromosomeParameters extends gov.nasa.javaGenes.core.Parameters {
/**
defines the alleles including their limits
*/
public AlleleTemplate alleles;
public DoubleInterval mutationStandardDeviationInterval = new DoubleInterval(0.1,0.9);
public DoubleInterval mutationFrequencyInterval = new DoubleInterval(0.1,0.9);
public double mutationStandardDeviation = mutationStandardDeviationInterval.random();  // fraction of interval
public double mutationFrequency = mutationFrequencyInterval.random();


public String lengthsAndAnglesFilename = "lengthsAndAngles.tsd";
public String assumedParametersFilename = "input/assumedParameters.tsd";
public AssumedParameters assumed;
public String immigrantsFilename = "input/immigrants.tsd";
public Immigrants immigrants;

public DoubleInterval exponentMinimumInterval = new DoubleInterval(-6,0);
public DoubleInterval exponentMaximumInterval = new DoubleInterval(12,24);
  public DoubleInterval exponentInterval = new DoubleInterval(-6,24);
public DoubleInterval factorMinimumInterval = new DoubleInterval(-100,-50);
public DoubleInterval factorMaximumInterval = new DoubleInterval(75,150);
  public DoubleInterval factorInterval = new DoubleInterval(-100,-50);
public DoubleInterval cutoffMaximumInterval = new DoubleInterval(3,8);
  public DoubleInterval cutoffInterval = new DoubleInterval(0,8);


public ManyMultiBodiesForOneEnergy energiesToExamineBestIndividual;

public String F3Filename = "../F3Energies.xyz";
public String SiFAllFilename = "../SiFAll.xyz";
public String Si5Filename = "../Si5_51_SEenergies.xyz";
    public String SiCrystalsFilename = "../SiCrystals.xyz";

public Potential potential;
// randomize as many GA parameters as possible
private void setParameters() {
	RandomNumber.setSeed(990639400906L); // used to generate repeatable runs. See seed.txt for seed of last run
	populationSize = 8;
	kidsPerGeneration = 2;
	maximumGenerations = 3;
	frequencyOfASCIIPopulations = 1;
	stopAtPerfection = false;

	AtomicSpecies[] species = {new AtomicSpecies("Si",1.8), new AtomicSpecies("F",2.086182)};
	potential = new StillingerWeber(species,factorInterval,exponentInterval,cutoffInterval);
	((StillingerWeber)potential).setCutoff(new ThreeBody("F","F","F"), 1.622586, 2.086182);
	ManyMultiBodiesForOneEnergy Si5 = new ManyMultiBodiesForOneEnergy(Si5Filename,StillingerWeberPartialSiF.getLengthScale());
	ManyMultiBodiesForOneEnergy SiFAll = new ManyMultiBodiesForOneEnergy(SiFAllFilename,StillingerWeberPartialSiF.getLengthScale());
	ManyMultiBodiesForOneEnergy F3 = new ManyMultiBodiesForOneEnergy(F3Filename,StillingerWeberPartialSiF.getLengthScale());

  Si5.removeBodiesAboveCutoff(potential);
  SiFAll.removeBodiesAboveCutoff(potential);
  F3.removeBodiesAboveCutoff(potential);

  alleles = potential.getAlleles();

  FitnessFunctionMultiObjective fitness = new FitnessFunctionMultiObjective();
  fitness.setName("fitness");
  FitnessFunction f;
  f = new ManyMoleculesEnergyFitness(potential,Si5);
  f.setName("Si5");
  fitness.add(1.0,f);
  f = new ManyMoleculesEnergyFitness(potential,F3);
  f.setName("F3");
  fitness.add(1.0,f);
  fitnessFunction = fitness;

  FitnessFunctionMultiObjective eval = new FitnessFunctionMultiObjective();
  f = new ManyMoleculesEnergyFitness(potential,SiFAll);
  f.setName("SiFAll");
  eval.add(f);
  f = new ManyMoleculesEnergyFitness(potential,F3);
  f.setName("F3");
  eval.add(f);
  f = new LowestToHighestEnergyFitness(potential,new ManyMultiBodiesForOneEnergy(potential,SiCrystalsFilename));
  f.setName("SiCrystals");
  eval.add(f);
  evaluationFunction = eval;

  breeder = new BreederSteadyState(this);
  for(int i = 3; i > 0; i--) {
    ChromosomeIntervalCrossover c = new ChromosomeIntervalCrossover(alleles);
    c.setLimitToOriginalInterval(RandomNumber.getBoolean());
    c.setExtension(2.0);
  	childMakerProvider.add(c);
  }
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
