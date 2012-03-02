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
package gov.nasa.javaGenes.graph;

import gov.nasa.javaGenes.core.FitnessFunction;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.javaGenes.core.Fitness;
import gov.nasa.alsUtility.Tanimoto;
import gov.nasa.javaGenes.core.FitnessDouble;
import gov.nasa.javaGenes.chemistry.Molecule;
import gov.nasa.javaGenes.chemistry.molFormat;
/**
fitness function that compares the number of cycles in an individual with the
number of cycles in a target using the Tanimoto coefficient.

@see Tanimoto
@see Graph
*/
public class CyclesSimilarity extends FitnessFunction {
/**
the target graph against which Individuals will be measured
*/
protected Graph target;

/**
@param graph the target
*/
public CyclesSimilarity(Graph graph){
    target = graph;
}
/**
@return 0 best fitness, 1 worst fitness
*/
public Fitness evaluateFitness (Evolvable evolvable){
	double d = Tanimoto.distance(target.getNumberOfCycles(),((Graph)evolvable).getNumberOfCycles());
  return new FitnessDouble(d);
}
public String toString() {
    return getClass() + ": match cycles. Target = " + target;
}
/**
create a mol file of the target. Only works for molecules and we really shouldn't
create a file here. Should be done in some sort of reporter.
*/
public void makeFiles() {
	if (target instanceof Molecule)
		molFormat.writeFile((Molecule)target,"target.mol");
}
}


