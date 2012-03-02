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


/**
 this fitness function is used penalizes large evolvables. Returns 1 for the smallest
 evolvables, greater numbers for larger ones. Is meant to be used with MultiplyFitnessFunction.
*/
public class ParsimonyFitness extends FitnessFunction  {
protected int minimumSize = 0;
protected double factor = 1.01;
public ParsimonyFitness() {}
/**
@param m minimum size of evolvable for penalties to be assessed
@param f the factor to be assessed for each increments in evolvable size above the minimum
*/
public ParsimonyFitness(int m, double f) {
	minimumSize = m;
  factor = f;
}

public Fitness evaluateFitness (Evolvable evolvable) {
	int size = Math.max(0,evolvable.getSize() - minimumSize);
  return new FitnessDouble(Math.pow(factor,size));
}

public String toString() {
	return "Parsimony fitness: minimumSize = " + minimumSize + " factor = " + factor;
}
}

