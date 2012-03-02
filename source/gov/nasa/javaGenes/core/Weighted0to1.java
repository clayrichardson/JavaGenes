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
package gov.nasa.javaGenes.core;

import java.lang.Double;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.ExtendedVector;
/**
 this fitness function is a weighted sum of fitness functions that return
 0 for perfection and 1 for worst possible
*/
public class Weighted0to1 extends FitnessFunction  {
protected ExtendedVector weights = new ExtendedVector();
protected ExtendedVector fitnessFunctions = new ExtendedVector();
/**
@param weight the weight for this fitness function. Larger values indicate more influence
@param function the fitness function. Must return 0 for best and 1 for worst
*/
public void add(double weight, FitnessFunction function) {
	Error.assertTrue(weight >= 0);
	weights.addElement(new Double(weight));
	fitnessFunctions.addElement(function);
}
/**
@return the fitness of evolvable.
*/
public Fitness evaluateFitness (Evolvable evolvable) {
	double fitness = 0;
	double weightSum = 0;
	for(int i = 0; i < weights.size(); i++){
		double weight = ((Double)weights.elementAt(i)).doubleValue();
		if (weight != 0.0) {
			weightSum += weight;
			double f = 1.0 - ((FitnessFunction)fitnessFunctions.elementAt(i)).evaluateFitness(evolvable).asDouble();
			fitness += f*weight;
		}
	}
	if (weightSum == 0.0)
		return new FitnessDouble(0.0);
	fitness /= weightSum;
	return new FitnessDouble(1.0 - fitness);
}
/**
tells the constituent fitness functions to makeFiles()
*/
public void makeFiles() {
	for(int i = 0; i < fitnessFunctions.size(); i++){
		((FitnessFunction)fitnessFunctions.elementAt(i)).makeFiles();
	}
}
public String toString() {
	String s = new String("Weighted 0-1 fitness:" + Utility.lineSeparator());
	for(int i = 0; i < fitnessFunctions.size(); i++){
		s += "\t" + weights.elementAt(i).toString() + " " + fitnessFunctions.elementAt(i).toString();
		if (i != fitnessFunctions.size()-1)
			s += Utility.lineSeparator();
	}
	return s;
}
}
