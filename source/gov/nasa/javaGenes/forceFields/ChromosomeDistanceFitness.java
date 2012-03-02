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
package gov.nasa.javaGenes.forceFields;

import gov.nasa.javaGenes.core.FitnessFunction;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.javaGenes.core.Fitness;
import gov.nasa.javaGenes.core.FitnessDouble;

/**
fitness is the distance from a target chromosome. A
@see Chromosome#distanceFrom
*/
public class ChromosomeDistanceFitness extends FitnessFunction {
/**
target chromosome
*/
protected Chromosome target;
/**
@param chromosome the target
*/
public ChromosomeDistanceFitness(Chromosome chromosome){target = chromosome;}
/**
@return 0 for closest, larger numbers for farthest from target.
@param evolvable must be a Chromosome
@see Chromosome
*/
public Fitness evaluateFitness (Evolvable evolvable){
	return new FitnessDouble(target.distanceFrom(evolvable));
}
public String toString() {
	return getClass() + ": distance from a target chromosome. Target = " + target;
}
}