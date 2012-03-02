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
package gov.nasa.javaGenes.evolvableDoubleList;

import gov.nasa.alsUtility.Error;

/** contains useful stuff for one and two point crossover classes */
public class Crossover extends ChildMaker {
int[] indicesArray; // used to save indices for case where the same indices are used for both parents

public Crossover(NeighboringPairsSelector selector) {super(selector);}

public gov.nasa.javaGenes.core.Evolvable[] makeChildren(gov.nasa.javaGenes.core.Evolvable[] parents) {
    Error.assertTrue(parents.length == numberOfParents());
    for(int i = 0; i < parents.length; i++)
        Error.assertNotNull(parents[i]);

    EvolvableDoubleList parentOne = (EvolvableDoubleList)parents[0];
	EvolvableDoubleList child = (EvolvableDoubleList)parentOne.copyForEvolution();
    EvolvableDoubleList parentTwo = (EvolvableDoubleList)parents[1];
	int[] parentOneIndices = getFirstParentIndicesArray(parents);
	int[] parentTwoIndices = getSecondParentIndicesArray(parents);
	crossover(child,parentOne,parentTwo,parentOneIndices,parentTwoIndices);

    EvolvableDoubleList[] children = {child};
    return children;
}
protected void crossover(EvolvableDoubleList child, EvolvableDoubleList parentOne, EvolvableDoubleList parentTwo, int[] parentOneIndices, int[] parentTwoIndices) {
	Error.mustBeImplementedBySubclass();
}
/** @return indicies come in neighboring pairs. The crossover points are in between each pair. The default is use same indices for both parents. */
protected int[] getFirstParentIndicesArray(gov.nasa.javaGenes.core.Evolvable[] parents) {
	indicesArray = getIndicesArray(getSmallerParent(parents));
	return indicesArray;
}
/** @return indicies come in neighboring pairs. The crossover points are in between each pair. The default is use same indices for both parents. */
protected int[] getSecondParentIndicesArray(gov.nasa.javaGenes.core.Evolvable[] parents) {
	Error.assertNotNull(indicesArray);
	return indicesArray;
}
protected int[] getIndicesArray(EvolvableDoubleList parent) {return getNeighboringPairsSelector().getIndicesArray(parent);}
public EvolvableDoubleList getSmallerParent(gov.nasa.javaGenes.core.Evolvable[] parents) {
	Error.assertTrue(parents.length == numberOfParents());
	if (parents[0].getSize() <= parents[1].getSize())
		return (EvolvableDoubleList)parents[0];
	return (EvolvableDoubleList)parents[1];
}
public NeighboringPairsSelector getNeighboringPairsSelector() {return (NeighboringPairsSelector)getSelector();}	
public int numberOfParents() {return 2;}
}


