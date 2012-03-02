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

import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.LogFile;

/**
 this fitness function will run another fitness function N times on variations of the Evolvable.  Usually the variations
 will be in the phenotype.  The worst fitness of the variations will be the fitness of the Evolvable.  This
 version was devised specifically for antennas.  It assumes the first evolvable variation is the original.
 Antennas have the problem that some variations may be unsimulatable or violate physical constraints that
 wouldn't happen in real manufacturing.
*/
public class FitnessFunctionWorstFitnessManyTries extends FitnessFunctionWorstFitness {
protected int maxVariationsNeeded;
protected Fitness threshold;
/**
@arg threshold if the first evolvable is this bad or worse, just return it.  If any of the subsequent guys are this bad, ignore it
@arg maxVariationsNeeded number of variations desired beyond the original
*/
public FitnessFunctionWorstFitnessManyTries(int maxVariationsNeeded, Fitness threshold, FitnessFunction fitnessFunction, PhenotypeChanger phenotypeChanger) {
	super(fitnessFunction,phenotypeChanger);
    this.maxVariationsNeeded = maxVariationsNeeded;
    this.threshold = threshold;
}
protected Fitness getWorst(Evolvable[] allEvolvables, Fitness[] forDebuging) {
	Fitness worstSoFar = null;
	int goodVariationsSoFar = 0;
    for(int i = 0; i < allEvolvables.length; i++) {
        Fitness fitness = fitnessFunction.evaluateFitness(allEvolvables[i]);
		forDebuging[i] = fitness;
        if (worstSoFar == null) {
			worstSoFar = fitness;
			if (!worstSoFar.fitterThan(threshold))
				return worstSoFar;
		} else if (fitness.fitterThan(threshold)) {
				goodVariationsSoFar++;
				if (worstSoFar.fitterThan(fitness))
					worstSoFar = fitness;
		}
		if (goodVariationsSoFar >= maxVariationsNeeded)
			return worstSoFar;
    }
	return worstSoFar;
}
public String toString() {
	return "FitnessFunctionWorstFitnessManyTries fitnessFunction=" 
		+ fitnessFunction.toString() 
		+ " maxVariationsNeeded=" + maxVariationsNeeded
		+ " threshold=" + threshold.asDouble()
		+ "\nphenotypeChanger=" + phenotypeChanger.toString();
}

}