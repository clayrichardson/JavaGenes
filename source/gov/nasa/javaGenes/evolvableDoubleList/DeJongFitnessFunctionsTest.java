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
package gov.nasa.javaGenes.evolvableDoubleList;

import junit.framework.TestCase;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.alsUtility.IntegerInterval;
import gov.nasa.javaGenes.core.Fitness;

public class DeJongFitnessFunctionsTest extends TestCase {

public DeJongFitnessFunctionsTest(String name) {super(name);}
private static final int randomRepetitions = 10000;

public void testSphere() {
	DeJongFitnessFunctions sphere = new DeJongFitnessFunctions(DeJongFitnessFunctions.SPHERE);
	
	// minimum
	int size = 10;
	Fitness minimum = sphere.evaluateFitness(new EvolvableDoubleList(size,0.5));
	Error.assertTrue("min " + minimum.asDouble(), Utility.nearlyEqual(minimum.asDouble(),0));
	RandomNumber.setSeed(990639400906L); // used to generate repeatable runs.
	for(int i = 0; i < randomRepetitions; i++) {
		EvolvableDoubleList evolvable = new EvolvableDoubleList(size);
		Fitness fitness = sphere.evaluateFitness(evolvable);
		Error.assertTrue("above min " + i ,minimum.fitterThan(fitness));
	}
	
	for(int i = 0; i < randomRepetitions; i++) {
		EvolvableDoubleList evolvable = new EvolvableDoubleList(size);
		int index = RandomNumber.getIndex(size);
		Fitness first = sphere.evaluateFitness(evolvable);
		double newValue = evolvable.getDoubleValue(index);
		if (newValue == 0 || newValue == 1)
			continue;
		if (newValue < 0.5)
			newValue -= 0.01;
		if (newValue >= 0.5)
			newValue += 0.01;
		newValue = new DoubleInterval(0,1).limitTo(newValue);
		evolvable.setDoubleValue(index,newValue);
		Fitness second = sphere.evaluateFitness(evolvable);
		Error.assertTrue("better " + i, first.fitterThan(second));
	}
}
public void testRosenbrock() {
	DeJongFitnessFunctions rosenbrock = new DeJongFitnessFunctions(DeJongFitnessFunctions.ROSENBROCK);
	
	int size = 10;
	
	Fitness minimum = rosenbrock.evaluateFitness(new EvolvableDoubleList(size,DeJongFitnessFunctions.rosenbrockRange.terpolate0to1(1)));
	Error.assertTrue("minimum " + minimum.asDouble(), Utility.nearlyEqual(minimum.asDouble(),0,0.01));

	for(int i = 0; i < randomRepetitions; i++) {
		EvolvableDoubleList evolvable = new EvolvableDoubleList(size);
		Fitness fitness = rosenbrock.evaluateFitness(evolvable);
		Error.assertTrue("above min " + i ,minimum.fitterThan(fitness));
	}
}
public void testStep() {
	DeJongFitnessFunctions step = new DeJongFitnessFunctions(DeJongFitnessFunctions.STEP);
	
	int size = 10;
	
	Fitness minimum = step.evaluateFitness(new EvolvableDoubleList(size,DeJongFitnessFunctions.rosenbrockRange.terpolate0to1(-5.1)));
	Error.assertTrue("minimum " + minimum.asDouble(), Utility.nearlyEqual(minimum.asDouble(),0,0.01));

	for(int i = 0; i < randomRepetitions; i++) {
		EvolvableDoubleList evolvable = new EvolvableDoubleList(size);
		Fitness fitness = step.evaluateFitness(evolvable);
		Error.assertTrue("above min " + i ,minimum.fitterThan(fitness));
	}
}
public void testShekelFoxholes() {
	DeJongFitnessFunctions foxholes = new DeJongFitnessFunctions(DeJongFitnessFunctions.SHEKEL_FOXHOLES);
	
	int size = 10;
	Fitness flatBit = foxholes.evaluateFitness(new EvolvableDoubleList(size,0.1));
	Error.assertTrue("flatBit " + flatBit.asDouble(), Utility.nearlyEqual(flatBit.asDouble(),500,0.001));
	
	Fitness minimum = foxholes.evaluateFitness(new EvolvableDoubleList(size,DeJongFitnessFunctions.shekelFoxholesRange.terpolate0to1(-32)));
	Error.assertTrue("minimum " + minimum.asDouble(), Utility.nearlyEqual(minimum.asDouble(),1,0.01));

	for(int i = 0; i < randomRepetitions; i++) {
		EvolvableDoubleList evolvable = new EvolvableDoubleList(size);
		Fitness fitness = foxholes.evaluateFitness(evolvable);
		Error.assertTrue("above min " + i ,minimum.fitterThan(fitness));
	}
}

public void testForCrash() {
	RandomNumber.setSeed(990449400906L); // used to generate repeatable runs.
	final IntegerInterval sizeRange = new IntegerInterval(1,40);
	DeJongFitnessFunctions[] fitnessFunctions = new DeJongFitnessFunctions[5];
	for(int f = 1; f <= 5; f++)
		fitnessFunctions[f-1] = new DeJongFitnessFunctions(f);
	for(int i = 0; i < randomRepetitions; i++) {
		EvolvableDoubleList evolvable = new EvolvableDoubleList(sizeRange.random());
		for(int f = 0; f < fitnessFunctions.length; f++)
			fitnessFunctions[f].evaluateFitness(evolvable);
	}
}
/* there's a lot more bugs in the test procedures than the code, so no more tests here */
}
