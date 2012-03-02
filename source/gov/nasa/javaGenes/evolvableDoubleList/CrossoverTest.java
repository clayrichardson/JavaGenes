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

import junit.framework.TestCase;

public class CrossoverTest extends TestCase {

public CrossoverTest(String name) {super(name);}

public void testGetSmallerParent() {
	EvolvableDoubleList mom = new EvolvableDoubleList(3);
	EvolvableDoubleList dad = new EvolvableDoubleList(4);
	EvolvableDoubleList[] parents1 = {mom,dad};
	Crossover crossover = new Crossover(new SelectOneNeighboringPair());
	assertTrue("1", mom == crossover.getSmallerParent(parents1));
	EvolvableDoubleList[] parents2 = {dad,mom};
	assertTrue("2", mom == crossover.getSmallerParent(parents1));
}
public void testParentIndicesArray() {
	Crossover crossover = new Crossover(new SelectOneNeighboringPair());
	EvolvableDoubleList mom = new EvolvableDoubleList(3);
	EvolvableDoubleList dad = new EvolvableDoubleList(4);
	EvolvableDoubleList[] parents1 = {mom,dad};
	int[] indices = crossover.getFirstParentIndicesArray(parents1);
	assertTrue("1", indices == crossover.getSecondParentIndicesArray(parents1));
}
}