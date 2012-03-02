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
import gov.nasa.javaGenes.core.FitnessDouble;
import gov.nasa.javaGenes.chemistry.Molecule;
import gov.nasa.javaGenes.chemistry.molFormat;
import gov.nasa.javaGenes.chemistry.Atom;

/**
fitness is the distance from a target graph. All-pairs-shortest-path comparison using
the Tanimoto coefficient is used.

@see apsp
@see Tanimoto
@see Graph#distanceFrom
*/
public class VertexPairsDistanceSimilarity extends FitnessFunction {
/**
target graph
*/
protected Graph target;
/**
@param graph the target
*/
public VertexPairsDistanceSimilarity(Graph graph){
    target = graph;
}
/**
set the target to a test molecule
*/
public VertexPairsDistanceSimilarity(){
    target = testMolecule();
}
/**
@return 0 for closest, 1 for farthest from target.
@param evolvable must be a Graph

@see Graph
*/
public Fitness evaluateFitness (Evolvable evolvable){
	return new FitnessDouble(target.distanceFrom(evolvable));
}
public String toString() {
    return getClass() + ": match shortest trails between extended vertex types. Target = " + target;
}
/**
write out the target as file target.mol
*/
public void makeFiles() {
	if (target instanceof Molecule)
		molFormat.writeFile((Molecule)target,"target.mol");
}

private Graph testMolecule(){
	Molecule m = new Molecule();
	m.add(new Atom(6));
    m.add(new Atom(6));
    m.add(new Atom(7));
    m.add(new Atom(8));

    m.makeBond(1, 2, 2);
    m.makeBond(2, 3, 1);
    m.makeBond(3, 1, 1);
	m.makeBond(4, 1, 1);
    return m;
}
}
