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
package gov.nasa.javaGenes.evolvableDoubleList.DeJongExperiment;

import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.IntegerInterval;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.Utility;
import gov.nasa.javaGenes.core.BreederSteadyState2;
import gov.nasa.javaGenes.evolvableDoubleList.ChildMakerProviderRandom;
import gov.nasa.javaGenes.evolvableDoubleList.ChildMakerProviderRandomMutations;
import gov.nasa.javaGenes.evolvableDoubleList.DeJongFitnessFunctions;
import gov.nasa.javaGenes.evolvableDoubleList.EvolvableDoubleList;
import gov.nasa.javaGenes.core.Tournament;
import gov.nasa.javaGenes.core.AntiTournament;
import gov.nasa.javaGenes.core.TournamentLocal;
import gov.nasa.javaGenes.core.AntiTournamentLocal;
import gov.nasa.javaGenes.core.Population;
import gov.nasa.javaGenes.core.Reporter;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.javaGenes.core.Individual;
import gov.nasa.javaGenes.simulatedAnnealing.Accepter;
import gov.nasa.javaGenes.simulatedAnnealing.Breeder;
import gov.nasa.javaGenes.core.HFC.*;
import gov.nasa.javaGenes.core.FitnessFunctionMultiObjective;
import gov.nasa.javaGenes.core.FitnessFunction;
import gov.nasa.javaGenes.core.FitnessDouble;
import gov.nasa.javaGenes.core.ChildMakerEvolvingProvider2;

public class Parameters extends gov.nasa.javaGenes.core.Parameters {
static {
    RandomNumber.setSeed(990639400906L); // used to generate repeatable runs. See seed.txt for seed of last run
} 

// experimental parameters
public static int evolvableSize = 10;
public static int numberOfSubBreeders = 5;

public Population population; // should probably migrate to core package.  Put here to make initial population generation part of parameterization
public Reporter reporter;     // should probably migrate to core package.  Put here to make reporting part of parameterization

public final int numberOfChildMakers = 10;
public final int numberOfGetsBetweenEvolution = 100;
public final int numberToChildMakersToKill = 3;

private void setParameters() {
	SubBreeder.MAX_TRIES = 500;
	populationSize = 100;
    kidsPerGeneration = 100;
    maximumGenerations = 100;
	
	reportVariationOperatorPerformanceEachGeneration = true;
    separateLogAndEvolvableFiles = true;
    frequencyOfASCIIPopulations = 10;
    stopAtPerfection = false;

    fitnessFunction = new DeJongFitnessFunctions(1);
    fitnessFunction.setName("fitness");
	evaluationFunction = new FitnessFunctionMultiObjective();
	for(int i = 1; i <= 5; i++) {
		FitnessFunction f = new DeJongFitnessFunctions(i);
		f.setName(DeJongFitnessFunctions.names[i]);
		((FitnessFunctionMultiObjective)evaluationFunction).add(1.0,f);
	}	

	childMakerProvider = new ChildMakerEvolvingProvider2(numberOfGetsBetweenEvolution,numberOfChildMakers,numberToChildMakersToKill,0.5,new ChildMakerProviderRandom(0,evolvableSize));
	childMakerProvider.setFitnessFunction(fitnessFunction); 

    population = new Population(populationSize);
	for(int populationIndex = 0; populationIndex < populationSize; populationIndex++) {
		final Evolvable evolvable = new EvolvableDoubleList(evolvableSize);
		final Individual individual = new Individual(evolvable, fitnessFunction);
		population.setIndividual(populationIndex,individual);
	}
    reporter = new Reporter(this);
	boolean killParents = true;
	int initialFitnessInterval = 1;
	breeder = new SteadyStateDiscreteBreeder(this, population, initialFitnessInterval, numberOfSubBreeders, childMakerProvider, fitnessFunction, killParents, new RandomEvolvableProducer(), new FitnessDouble(1000));
}
protected class RandomEvolvableProducer extends gov.nasa.javaGenes.core.RandomEvolvableProducer {
	public gov.nasa.javaGenes.core.Evolvable getRandomEvolvable() {
		return new EvolvableDoubleList(evolvableSize);
	}
}

public Parameters() {setParameters();}
public void makeFiles() {}

}