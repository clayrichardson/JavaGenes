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

/**
 this fitness function is a weighted sum of fitness functions
*/
public class WeightedSumFitness extends FitnessFunctionMultiObjectiveToOne  {
protected boolean normalizeByWeight = true;
public WeightedSumFitness() {this(true);}
public WeightedSumFitness(boolean normalizeByWeight) {
    setNormalizeByWeight(normalizeByWeight);
}
public Fitness evaluateFitness (Evolvable evolvable) {
	double fitness = 0;
	double weightSum = 0;
	for(int i = 0; i < weights.size(); i++){
		double weight = getWeight(i);
		if (weight != 0.0) {
			weightSum += weight;
			double f = getFitnessFunction(i).evaluateFitness(evolvable).asDouble();
			fitness += f*weight;
		}
	}
	if (weightSum == 0.0)
		return new FitnessDouble(0.0);
        if (normalizeByWeight)
            fitness /= weightSum;
	return new FitnessDouble(fitness);
}
public void setNormalizeByWeight(boolean value) {normalizeByWeight = value;}
}
