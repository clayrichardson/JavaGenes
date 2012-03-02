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
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.RandomNumber;

public class CrossoverPickOneTest extends TestCase {

public CrossoverPickOneTest(String name) {super(name);}

public void testFixedIndices() {
	int[] indices = {2,4,6};
	testFixedIndices(8,12,indices);
	testFixedIndices(16,11,indices);
	int[] indices2 = {0,1,2};
	testFixedIndices(8,12,indices);
	testFixedIndices(3,4,indices);
}
private void testFixedIndices(int dadLength, int momLength, int[] indicies) {
	Error.assertTrue(dadLength != momLength);
	CrossoverPickOne selector = new CrossoverPickOne(new SelectFixedIndices(indicies));
	EvolvableDoubleList[] parents = {new EvolvableDoubleList(dadLength), new EvolvableDoubleList(momLength)};
	EvolvableDoubleList child = (EvolvableDoubleList)selector.makeChildren(parents)[0];
	EvolvableDoubleList dad = null;
	EvolvableDoubleList mom = null;
	if (child.getSize() == parents[0].getSize()) {
		dad = parents[0];
		mom = parents[1];
	} else {
		dad = parents[1];
		mom = parents[0];
	}
	int indiciesIndex = 0;
	for(int i = 0; i < child.getSize(); i++)
		if (indiciesIndex < indicies.length && indicies[indiciesIndex] == i) {
			Error.assertTrue(child.getDoubleValue(i) == mom.getDoubleValue(i));
			indiciesIndex++;
		} else
			Error.assertTrue(child.getDoubleValue(i) == dad.getDoubleValue(i));
}
public void testRandom() {
    RandomNumber.setSeed(990739400906L);   // to get deterministic results
	testRandom(4,4);
	testRandom(4,6);
	testRandom(25,3);
	testRandom(1,10);
	testRandom(1,1);
}
public void testRandom(int dadLength, int momLength) {
	final int MAX_TRIES = 1000;
	CrossoverPickOne selector = new CrossoverPickOne();
	EvolvableDoubleList[] parents = {new EvolvableDoubleList(dadLength), new EvolvableDoubleList(momLength)};
	EvolvableDoubleList child = (EvolvableDoubleList)selector.makeChildren(parents)[0];
	Error.assertTrue(child.getSize() == parents[0].getSize() || child.getSize() == parents[1].getSize());
	if (!(parents[0].getSize() == 1 && parents[1].getSize() ==1))
		Error.assertTrue(!child.isEqual(parents[0]) && !child.isEqual(parents[1]));
}
}
