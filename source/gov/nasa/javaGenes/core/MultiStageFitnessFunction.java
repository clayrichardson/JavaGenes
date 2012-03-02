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
import gov.nasa.alsUtility.Error;

/**
 this fitness function will return the the first objective with a fitness value at least as great as it's weight, or the
 last fitness value.  Useful when you want to do some quick checks for a decent evolavable before executing
 an expensive test for the fitness function.
*/
public class MultiStageFitnessFunction extends FitnessFunctionMultiObjectiveToOne {
public Fitness evaluateFitness (Evolvable evolvable) {
    for(int i = 0; i < weights.size(); i++) {
        Fitness fitness = getFitnessFunction(i).evaluateFitness(evolvable);
        if (i == weights.size()-1 || fitness.asDouble() >= getWeight(i))
            return fitness;
    }
    Error.assertTrue(false); // should never get here
    return null; // should never happen
}
}
