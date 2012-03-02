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

import gov.nasa.javaGenes.core.Fitness;
import gov.nasa.javaGenes.core.FitnessFunction;
import gov.nasa.javaGenes.core.FitnessDouble;

public class TestFitnessFunction1 extends FitnessFunction {

public Fitness evaluateFitness (gov.nasa.javaGenes.core.Evolvable evolvable) {
	EvolvableDoubleList list = (EvolvableDoubleList)evolvable;
	double fitness = 0;
	double valueSum = 0;
	for(int i = 0; i < list.getSize(); i++) {
		double value = list.getDoubleValue(i);
		valueSum += value; // used to couple the values
		fitness += Math.abs(Math.sin(i + value*2*Math.PI + valueSum)); // get lots of local minima at different phases and wavelengths
	}
	fitness /= list.getSize(); // normalize for length
	return new FitnessDouble(fitness);
}
}
