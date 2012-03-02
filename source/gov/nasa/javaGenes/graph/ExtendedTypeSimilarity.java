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
a fitness function consisting of the Tanimoto distance to a target. Each graph
is located in space by the extended types of its vertices.

@see Vertex
@see Vertex#getExtendedTypeString
@see Vertex#getExtendedTypeObject
@see Tanimoto
*/
public class ExtendedTypeSimilarity extends FitnessFunction {
protected Graph target;
/**
@param graph the target
*/
public ExtendedTypeSimilarity(Graph graph){
    target = graph;
}
/**
@return the Tanimoto distance between evolvable and the target. 0 is best, 1 is
worst fitness.
*/
public Fitness evaluateFitness (Evolvable evolvable){
	double d = Tanimoto.distance(target.getExtendedVertexTypesCounter(),
		((Graph)evolvable).getExtendedVertexTypesCounter());
  return new FitnessDouble(d);
}
public String toString() {
    return getClass() + ": match extended vertex types. Target = " + target;
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


