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

public class Parameters extends gov.nasa.javaGenes.core.Parameters {
static {
    RandomNumber.setSeed(990639400906L); // used to generate repeatable runs. See seed.txt for seed of last run
} 

// experimental parameters
public static int deJongNumber = 1;
public static boolean useLocalBreeder = true;
public static int evolvableSize = 10;

public Population population; // should probably migrate to core package.  Put here to make initial population generation part of parameterization
public Reporter reporter; // should probably migrate to core package.  Put here to make reporting part of parameterization

// variation operator parameters
public final int numberOfChildMakers = 10;

private void setParameters() {
    populationSize = 100;
    kidsPerGeneration = 1000;
    maximumGenerations = 1000;
	// for quick testing
    kidsPerGeneration /= 100;
    maximumGenerations /= 100;
	
	reportVariationOperatorPerformanceEachGeneration = false;
    separateLogAndEvolvableFiles = true;
    frequencyOfASCIIPopulations = 200;
    stopAtPerfection = false;

    fitnessFunction = new DeJongFitnessFunctions(deJongNumber);
    fitnessFunction.setName(DeJongFitnessFunctions.names[deJongNumber]);

	childMakerProvider = new ChildMakerProviderRandom(numberOfChildMakers,evolvableSize);
	childMakerProvider.setFitnessFunction(fitnessFunction); 

    population = new Population(populationSize);
	for(int populationIndex = 0; populationIndex < populationSize; populationIndex++) {
		final Evolvable evolvable = new EvolvableDoubleList(evolvableSize);
		final Individual individual = new Individual(evolvable, fitnessFunction);
		population.setIndividual(populationIndex,individual);
	}
    reporter = new Reporter(this);
	if (useLocalBreeder)
		breeder = new gov.nasa.javaGenes.core.BreederSteadyState2(this, new TournamentLocal(2,3), new AntiTournamentLocal(2,2));
	else
		breeder = new gov.nasa.javaGenes.core.BreederSteadyState2(this, new Tournament(2), new AntiTournament(2)); 
}

public Parameters() {setParameters();}
public void makeFiles() {}

}