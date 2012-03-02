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

import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.javaGenes.core.Fitness;
import gov.nasa.javaGenes.core.FitnessFunction;
import gov.nasa.javaGenes.core.FitnessDouble;

/**
The well know De Jong fitness functions.  Taken from http://home.ku.edu.tr/~dyuret/pub/aitr1569/node19.html and http://cs.gmu.edu/~eclab/kdj_thesis/appendices.pdf
*/
public class DeJongFitnessFunctions extends FitnessFunction {
public static final int SPHERE = 1;
public static final int ROSENBROCK = 2;
public static final int STEP = 3;
public static final int QUARTIC_NOISE = 4;
public static final int SHEKEL_FOXHOLES = 5;

public static final String[] names = { "", "sphere", "rosenbrock", "step", "quarticNoise", "shekelFoxholes"};

public static final DoubleInterval sphereRange = new DoubleInterval(-5.12,5.12);
public static final DoubleInterval rosenbrockRange = new DoubleInterval(-2.048,2.048);
public static final DoubleInterval stepRange = new DoubleInterval(-5.12,5.12);
public static final DoubleInterval quarticNoiseRange = new DoubleInterval(-1.28,1.28);
public static final DoubleInterval shekelFoxholesRange = new DoubleInterval(-65.536,65.536);

protected int number = 1;

public DeJongFitnessFunctions(int number) {
	Error.assertTrue(1 <= number && number <= 5);
	this.number = number;
}
public Fitness evaluateFitness (gov.nasa.javaGenes.core.Evolvable evolvable) {
	EvolvableDoubleList list = (EvolvableDoubleList)evolvable;	
	double fitness = 0;
	switch(number) {
	case SPHERE: fitness = sphere(list); break;
	case ROSENBROCK: fitness = rosenbrock(list); break;
	case STEP: fitness = step(list); break;
	case QUARTIC_NOISE: fitness = quarticNoise(list); break;
	case SHEKEL_FOXHOLES: fitness = shekelFoxholes(list); break;
	default: Error.fatal("bad case " + number);
	}
	return new FitnessDouble(fitness);
}
/**
min = f(0,...,0) == 0

originally for list.getSize() = 3
*/
public double sphere(EvolvableDoubleList list) {
	DoubleInterval range = sphereRange;
	double fitness = 0;
	for(int i = 0; i < list.getSize(); i++) {
		double xi = list.interpolateInto(i,range);
		fitness += xi*xi;
	}
	return fitness;
}
/** 
min = f(1,...1) = 0.  in the converted space (-2.048 to 2.048).

Note: the references disagree on the order of the subtractions.  Went with the original thesis.

originally for list.getSize() = 2
*/
public double rosenbrock(EvolvableDoubleList list) {
	DoubleInterval range = rosenbrockRange;
	double fitness = 0;
	for(int i = 0; i < list.getSize()-1; i++) { // should be to n-1?
		double xi = list.interpolateInto(i,range);
		double xiPlusOne = list.interpolateInto(i+1,range);
		double firstElement = xi*xi - xiPlusOne;
		double secondElement = 1 - xi;
		fitness += 100 *firstElement*firstElement + secondElement*secondElement;
	}
	return fitness;
}
/**
min = f({-5.12,-5],...,[-5.12,-5]) == 0

originally for list.getSize() = 5

Again the references disagree, but this time I use the second one since the min is always 0 regarless of dimensionality
*/
public double step(EvolvableDoubleList list) {
	DoubleInterval range = stepRange;
	int xiSum = 0;
	for(int i = 0; i < list.getSize(); i++) {
		double xi = list.interpolateInto(i,range);
		xiSum += Math.floor(xi);
	}
	double fitness = 6 * list.getSize() + xiSum;
	return fitness;
}
/**
min = f(0,...,0) == 0

originally for list.getSize() = 30
*/
public double quarticNoise(EvolvableDoubleList list) {
	DoubleInterval range = quarticNoiseRange;
	double fitness = 0;
	for(int i = 0; i < list.getSize(); i++) {
		double xi = list.interpolateInto(i,range);
		double xiSquared = xi*xi;
		fitness += i*xiSquared*xiSquared ;
	}
	fitness += gauss();
	return fitness;
}
protected double gauss() {
	return RandomNumber.getGaussian(1.0);
}
protected static double[][] foxholes = new double[25][2];
static {
	final double[] foxholeValues = {-32,-16,0,16,32};
	for(int i = 0; i < foxholes.length; i++) {
		foxholes[i][0] = foxholeValues[i%5];
		foxholes[i][1] = foxholeValues[i/5];
	}
}

/**
min = f(-32,-32) -- and four others ~= 1, the numbers are in a strange space, see code
originally for list.getSize() = 2 with 25 local minima
*/
public double shekelFoxholes(EvolvableDoubleList list) {
	// convert it into a 2d search space
	double evenSum = 0;
	double oddSum = 0;
	for(int i = 0; i < list.getSize(); i++) {
		double xi = list.getDoubleValue(i);
		if (i%2 == 0)
			evenSum += xi;
		else
			oddSum += xi;
	}
	DoubleInterval range = shekelFoxholesRange;
	double[] x = {
		new EvolvableDouble(evenSum / (list.getSize()/2 + list.getSize()%2)).interpolateInto(range),
		new EvolvableDouble(oddSum / (list.getSize()/2)).interpolateInto(range)
	};
	
	double fitnessInverse = 1.0/500.0;
	for(int j = 0; j < foxholes.length; j++) { // use j for consistency with De Jong's thesis
		double fjSum = 0;
		for(int i = 0; i < x.length; i++) {
			double v = x[i] - foxholes[j][i];
			double vv = v*v;
			fjSum += vv * vv * vv;
		}
		double fj = j+1 + fjSum;
		fitnessInverse += 1/fj;
	}
	return 1/fitnessInverse;
}
public String toString() {
	return "DeJongFitnessFunction number = " + number;
}

}
