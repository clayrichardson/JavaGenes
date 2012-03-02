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
package gov.nasa.javaGenes.core.HFC;

import gov.nasa.alsUtility.Utility;
import java.util.Vector;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.LogFile;
import gov.nasa.javaGenes.core.Parameters;
import gov.nasa.javaGenes.core.Population;
import gov.nasa.javaGenes.core.Checkpointer;
import gov.nasa.javaGenes.core.Individual;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.javaGenes.core.RandomEvolvableProducer;
import gov.nasa.javaGenes.core.Fitness;
import gov.nasa.javaGenes.core.NotFoundException;
import gov.nasa.javaGenes.core.FitnessDouble;
import gov.nasa.javaGenes.core.ChildMakerProvider;
import gov.nasa.javaGenes.core.FitnessFunction;

/**
implements a version of the Hierarchical Fair Competition Model (HFC, see http://www.egr.msu.edu/~hujianju/HFC.htm) 
with steady state breeding in discrete sub-popuations.  Sub-populations are implemented as sub-breeders since
the gov.nasa.javaGenes.core.Population class is subclassed by many representations of populations.
*/
public class SteadyStateDiscreteBreeder extends Breeder {
protected SubBreeder[] subBreeders;
protected Fitness considerOnlyHigherFitness; // used for initializing fitness thresholds to avoid considering very poor  fitness
protected int populationSize; // can't change

public SteadyStateDiscreteBreeder(Parameters p, Population population, double initialFitnessInterval, int numberOfSubBreeders, ChildMakerProvider childMakerProvider, FitnessFunction fitnessFunction, boolean killParents, RandomEvolvableProducer randomEvolvableProducer, Fitness considerOnlyHigherFitness) {
	this(p,population,initialFitnessInterval,createSubBreeders(numberOfSubBreeders,childMakerProvider,fitnessFunction,killParents),randomEvolvableProducer,considerOnlyHigherFitness);
}
static protected SubBreeder[] createSubBreeders(int numberOfSubBreeders, ChildMakerProvider childMakerProvider, FitnessFunction fitnessFunction, boolean killParents) {
	Error.assertTrue(numberOfSubBreeders > 0);
	Error.assertNotNull(childMakerProvider);
	Error.assertNotNull(fitnessFunction);
	SubBreeder[] subBreeders = new SubBreeder[numberOfSubBreeders];
	subBreeders[0] = new BottomSubBreeder(childMakerProvider,fitnessFunction,killParents);
	for(int i = 1; i < numberOfSubBreeders-1; i++)
		subBreeders[i] = new MiddleSubBreeder(childMakerProvider,fitnessFunction);
	subBreeders[numberOfSubBreeders-1] = new TopSubBreeder(childMakerProvider,fitnessFunction);
	return subBreeders;
}

/**
@param subBreeders must be ordered with bottom at 0 and top at length-1
*/
public SteadyStateDiscreteBreeder(Parameters p, Population population, double initialFitnessInterval, SubBreeder[] subBreeders, RandomEvolvableProducer randomEvolvableProducer, Fitness considerOnlyHigherFitness) {
	super(p,randomEvolvableProducer);
	Error.assertNotNull(considerOnlyHigherFitness);
	this.considerOnlyHigherFitness = considerOnlyHigherFitness;
	Error.assertNotNull(subBreeders);
	Error.assertTrue(subBreeders.length > 0);
	for(int i = 0; i < subBreeders.length; i++)
		Error.assertNotNull(subBreeders[i]);
	this.subBreeders = subBreeders;
	for(int i = 0; i < subBreeders.length; i++)
		subBreeders[i].setBreeder(this);
	Error.assertTrue(initialFitnessInterval > 0);
	initializeSubBreeders(population, initialFitnessInterval);
	Error.assertNotNull(population);
	populationSize = population.getSize();
}
protected void initializeSubBreeders(Population population, double initialFitnessInterval) {
	Error.assertTrue(population.getSize() >= 2);
	initializePromotionReferences();
	initializeBestFitnessAllowed(population,initialFitnessInterval);
	initializeIndexRange(population);
	promoteTooFitIndividuals(population);
}
protected void initializePromotionReferences() {
	for(int i = 0; i < numberOfSubBreeders()-1; i++)
		getSubBreeder(i).setPromotionsTo(getSubBreeder(i+1));
}
protected void initializeBestFitnessAllowed(Population population, double initialFitnessInterval) {
	double worstFitness = population.worstFitness().asDouble();
	getSubBreeder(0).setBestFitnessAllowed(worstFitness);
	for(int i = 1; i < numberOfSubBreeders(); i++)
		getSubBreeder(i).setBestFitnessAllowed(worstFitness - i*initialFitnessInterval); // assumes smaller fitness is better
}
protected void 	initializeIndexRange(Population population) {
	int populationIncrement = population.getSize() / numberOfSubBreeders(); 
	for(int i = 0; i < numberOfSubBreeders(); i++) {
		int highIndex = i == numberOfSubBreeders()-1 ? population.getSize()-1 : (i+1)*populationIncrement - 1; // makes top population potentially larger
		getSubBreeder(i).setIndexRange(i*populationIncrement, highIndex);
	}
}
protected void 	promoteTooFitIndividuals(Population population) {
	for(int i = numberOfSubBreeders()-2; i >= 0; i--) {
		getSubBreeder(i).promoteTooFitIndividuals(population);
	}
}
public Population breed (Population parents, int kidsPerGeneration) {
	generationJustStarting();
	Error.assertTrue(populationSize == parents.getSize());

	while (generationNotComplete(kidsPerGeneration)){
		SubBreeder subBreeder = getRandomSubBreeder();
		subBreeder.breedOnce(parents);
		Checkpointer.ok();
	}
	generationIsComplete();
	return parents;
}
/** fail is anything wrong.  Used for testing */
public void assertStatusValid(Population population) {
	Error.assertTrue(getSubBreeder(0).indicesRange.low() == 0);
	Error.assertTrue(getSubBreeder(numberOfSubBreeders()-1).indicesRange.high() == populationSize-1);
	for(int i = 0; i < numberOfSubBreeders(); i++) {
		if (i < numberOfSubBreeders()-1) {
			Error.assertTrue(getSubBreeder(i).bestFitnessAllowed.asDouble() >= getSubBreeder(i+1).bestFitnessAllowed.asDouble());
			Error.assertTrue(getSubBreeder(i).indicesRange.high()+1 == getSubBreeder(i+1).indicesRange.low());
			Error.assertTrue(getSubBreeder(i).indicesRange.interval() <= getSubBreeder(i+1).indicesRange.interval());
		}
		if (i < numberOfSubBreeders()-2) {
			Error.assertTrue(getSubBreeder(i).indicesRange.interval() == getSubBreeder(i+1).indicesRange.interval());
		}
		//getSubBreeder(i).assertIndividualsInFitnessRange(population);  because can't be guarenteed in initial population
	}
}
public void topSubPopulationReceivedNewBestIndividual(Individual individual) {
	SubBreeder top = getSubBreeder(numberOfSubBreeders()-1);
	double newBestFitness = individual.getFitness().asDouble();
	double oldBestFitness = top.getBestFitnessAllowed().asDouble();
	Error.assertTrue(Utility.lessThanOrEqual(newBestFitness,oldBestFitness));
	double fitnessIncrement = (newBestFitness - oldBestFitness)/numberOfSubBreeders();
	for(int i = 0; i < numberOfSubBreeders()-1; i++)
		getSubBreeder(i).incrementBestFitnessAllowed(fitnessIncrement*(i+1));
	top.setBestFitnessAllowed(newBestFitness); // to avoid accumulating numerical error
}
public SubBreeder getRandomSubBreeder() {
	return getSubBreeder(RandomNumber.getIndex(numberOfSubBreeders()));
}
public SubBreeder getSubBreeder(int i) {
	Error.assertTrue(i >= 0);
	Error.assertTrue( i < numberOfSubBreeders());
	return subBreeders[i];
}
public int numberOfSubBreeders() {return subBreeders.length;}
public void reportOnGeneration(int generation, String filename) {
	LogFile out = new LogFile(filename);
	out.print(generation + "\t");
	for(int i = 0; i < numberOfSubBreeders(); i++) {
		out.print(""+getSubBreeder(i).bestFitnessAllowed.asDouble());
		if (i != numberOfSubBreeders()-1)
			out.print("\t");
	}
	out.println();
	out.close();
}

public String toString() {
	return "SteadyStateDiscreteBreeder considerOnlyHigherFitness=" + considerOnlyHigherFitness + " populationSize=" + populationSize + "\n" + Utility.toString(subBreeders) + "\n";
}
}

