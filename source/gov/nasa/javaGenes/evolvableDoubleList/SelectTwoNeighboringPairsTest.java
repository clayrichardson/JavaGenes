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
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.Error;

public class SelectTwoNeighboringPairsTest extends TestCase {

public SelectTwoNeighboringPairsTest(String name) {super(name);}

public void testRandom() {
    RandomNumber.setSeed(990739400906L);   // to get deterministic results
	test(6,1,0);
	test(6,2,0);
	test(6,2,1);
	test(12,5,3);
	test(1,1,0);
	test(2,1,0);
}
private void test(int length, int modulo, int offset) {
	final int TRIES = 1000;
	// these can be checked in the debugger for reasonableness
	int zeroLength = 0;
	int twoLong = 0;
	int fourLong = 0;
	
	SelectTwoNeighboringPairs selector = new SelectTwoNeighboringPairs(modulo, offset);
	for(int i = 0; i < TRIES; i++) {
		int[] array = selector.getIndicesArray(new EvolvableDoubleList(length));
		Error.assertTrue(array.length == 0 || array.length == 2 || array.length == 4);
		for(int j = 0; j < array.length; j++)
			Error.assertTrue(array[j] < length);
		if (array.length == 0) {
			zeroLength++;
			continue;
		}
		Error.assertTrue((array[0]-offset) % modulo == 0);
		Error.assertTrue(array[0] + 1 == array[1]);
		if (array.length == 2) {
			twoLong++;
			continue;
		}
		Error.assertTrue((array[2]-offset) % modulo == 0);
		Error.assertTrue(array[1] <= array[2]);
		Error.assertTrue(array[2] + 1 == array[3]);
		fourLong++;
	}
}
}