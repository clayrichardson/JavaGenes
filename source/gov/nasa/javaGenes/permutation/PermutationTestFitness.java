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
package gov.nasa.javaGenes.permutation;

import gov.nasa.javaGenes.core.FitnessFunction;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.javaGenes.core.Fitness;
import gov.nasa.javaGenes.core.FitnessDouble;

public class PermutationTestFitness extends FitnessFunction {

// how close to assending order are the indices in the permutation
public Fitness evaluateFitness (Evolvable evolvable){
  PermutationEvolvable p = (PermutationEvolvable)evolvable;
  int last = -1;
  int count = 0;
  for(int i = 0; i < p.getSize(); i++) {
    int current = p.getIndexAt(i);
    if (current < last)
      count++;
    last = current;
  }
	return new FitnessDouble((double)count);
}
public String toString() {
	return getClass() + "";
}
}