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
package gov.nasa.javaGenes.chemistry;

import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.graph.*;
import gov.nasa.javaGenes.core.*;

public class MoleculeParameters extends GraphParameters {
private void setParameters(Molecule target) {
	//RandomNumber.setSeed(923002162750L); // used to generate repeatable runs. See seed.txt for seed of last run
	// set size of run and restart or not
	populationSize = 20;
	randomIndividualTriesPerSpecification = 3;
	maximumGenerations = 0;
	frequencyOfASCIIPopulations = 1;
	stopAtPerfection = false;

	// set fitness function
	Weighted0to1 f = new Weighted0to1();
	f.add(2.0,new VertexPairsDistanceSimilarity(target));
	f.add(1.0,new ExtendedTypeSimilarity(target));
	f.add(1.0,new CyclesSimilarity(target));
	fitnessFunction = f;

	breeder = new BreederSteadyState(this);
	childMakerProvider.add(new MoleculeTwoVertexCrossover());
	childMakerProvider.add(new AddEdge(provider));
	childMakerProvider.add(new AddVertex(provider));
	childMakerProvider.add(new MutateEdge(provider));
	childMakerProvider.add(new MutateVertex(provider));
	//childMakerProvider.add(new GraphGenerator(this));

	// graph layout
	layoutGraph2d = true;
	layout.iterations = 100;
	layout.maxLineSearchIterations = 100;
}


/**
Sets up all parameters with a default target graph.
*/
public MoleculeParameters() {
	this(new Benzene());
}
/**
Sets up all parameters with a target molecule read from a mol file. Must be a molecule.

@see molFormat
*/
public MoleculeParameters(String filename) {
	this(molFormat.read(filename));
}
/**
Sets up all parameters with target Graph target
*/
public MoleculeParameters(Molecule target) {
	// automatically set initial individuals size range
  int vertices = target.getVerticesSize();
  verticesInterval.set(Math.max(2,vertices/2),vertices*2);
  cyclesInterval.set(0,target.getNumberOfCycles()*2);

	// insure that all possible vertex and edge types are in initial population in roughly equal quantities
	java.util.Hashtable elements = new java.util.Hashtable();
	for(VertexIterator v = target.getVertexIterator(); v.more(); v.next()){
		String s = v.vertex().toString();
		if (!elements.containsKey(s)){
			elements.put(s,s);
			try {
				provider.add((Vertex)v.vertex().clone());
			} catch (CloneNotSupportedException e) {
        		Error.fatal("can't clone vertex: " + e);
    		}
		}
	}
	for(EdgeIterator e = target.getEdgeIterator(); e.more(); e.next()){
		String s = e.edge().toString();
		if (!elements.containsKey(s)){
			elements.put(s,s);
			try {
				provider.add((Edge)e.edge().clone());
			} catch (CloneNotSupportedException ee) {
        		Error.fatal("can't clone edge: " + ee);
    		}
		}
	}
  setParameters(target);
}

} 