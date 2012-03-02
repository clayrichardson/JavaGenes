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

import junit.framework.TestCase;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.alsUtility.IntegerInterval;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.javaGenes.core.Parameters;
import gov.nasa.javaGenes.core.FitnessFunctionRandom;
import gov.nasa.javaGenes.core.FitnessDouble;
import gov.nasa.javaGenes.core.Population;
import gov.nasa.javaGenes.core.Individual;
import gov.nasa.javaGenes.evolvableDoubleList.RandomEvolvableDoubleListProducer;
import gov.nasa.javaGenes.evolvableDoubleList.CrossoverOnePoint;
import gov.nasa.javaGenes.evolvableDoubleList.MutationFixedStdDev;
import gov.nasa.javaGenes.evolvableDoubleList.Mutation3parents;
import gov.nasa.javaGenes.evolvableDoubleList.SelectAll;

public class SteadyStateDiscreteBreederTest extends TestCase {

public SteadyStateDiscreteBreederTest(String name) {super(name);}

public void testRandom() {
Error.warning("test");

	RandomNumber.setSeed(990639400906L);
	test(4,17,1,1);

	for(int i = 0; i < 100; i++)
		test(
			new IntegerInterval(4,10).random(), 
			new IntegerInterval(40,100).random(), 
			new IntegerInterval(5,10).random(),
			new IntegerInterval(50,500).random()
		);
}
private void test(int numberOfSubBreeders, int populationSize, int generations, int kidsPerGeneration) {
	Parameters parameters = new Parameters();
	parameters.fitnessFunction = new FitnessFunctionRandom(new DoubleInterval(0,100));
	parameters.childMakerProvider.add(new CrossoverOnePoint());
	parameters.childMakerProvider.add(new  MutationFixedStdDev(new SelectAll(), 0.1));
	parameters.childMakerProvider.add(new  Mutation3parents(new SelectAll()));
	
	RandomEvolvableDoubleListProducer evolvableProducer = new RandomEvolvableDoubleListProducer(new IntegerInterval(5,10));
	Population population = new Population(populationSize);
	for(int i = 0; i < population.getSize(); i++)
		population.setIndividual(i,new Individual(evolvableProducer.getRandomEvolvable(), parameters.fitnessFunction));

	SubBreeder[] subBreeders = new SubBreeder[numberOfSubBreeders];
	subBreeders[0] = new BottomSubBreeder(parameters.childMakerProvider,parameters.fitnessFunction);
	for(int i = 1; i < numberOfSubBreeders-1; i++)
		subBreeders[i] = new MiddleSubBreeder(parameters.childMakerProvider,parameters.fitnessFunction);
	subBreeders[numberOfSubBreeders-1] = new TopSubBreeder(parameters.childMakerProvider,parameters.fitnessFunction);
	SteadyStateDiscreteBreeder breeder = new SteadyStateDiscreteBreeder(parameters, population, 1, subBreeders, evolvableProducer, new FitnessDouble(50));

	breeder.assertStatusValid(population);
	for(int i = 0; i < generations; i++) {
		breeder.breed(population,kidsPerGeneration);
		breeder.assertStatusValid(population);
	}
}
}