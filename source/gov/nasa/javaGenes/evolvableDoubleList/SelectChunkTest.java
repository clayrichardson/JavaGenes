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
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;

public class SelectChunkTest extends TestCase {

public SelectChunkTest(String name) {super(name);}

public void testSelectFixedChunk() {
    RandomNumber.setSeed(990739400906L);   // to get deterministic results
    int[] choose = {3,4};
    SelectChunk selector = new SelectChunk(choose);
    int[] result = selector.getIndicesArray(10);
    //Utility.printIntArray(result); //-- used to determine correct result
    int[] correct = {5,6,7};
    assertTrue("1", Utility.equals(correct,result)); // will fail if you change random number generation
}
public void testMultipleChunks() {
	int[] choose = {2};
	int[] select = {1,4};
    SelectChunk selector = new SelectChunk(new SelectFixedIndices(select),choose,1,0);
    int[] result = selector.getIndicesArray(10);
	int[] correct = {1,2,4,5};
	Error.assertTrue(Utility.equals(result,correct));

	select[1] = 2;
    selector = new SelectChunk(new SelectFixedIndices(select),choose,1,0);
    result = selector.getIndicesArray(10);
	int[] correct2 = {1,2,3};
	Error.assertTrue(Utility.equals(result,correct2));
}
public void testRandom() {
    RandomNumber.setSeed(990739400906L);   // to get deterministic results
	int[] numberToChoose = {2,3};
	testRandom(6,numberToChoose,1,0);
	testRandom(6,numberToChoose,2,0);
	testRandom(6,numberToChoose,2,1);
	testRandom(12,numberToChoose,5,3);
	testRandom(1,numberToChoose,1,0);
}
private void testRandom(int evolvableLength, int[] numberToChoose, int modulo, int offset) {
	final int TRIES = 1000;

	SelectChunk selector = new SelectChunk(numberToChoose,modulo, offset);
	for(int i = 0; i < TRIES; i++) {
		int[] array = selector.getIndicesArray(evolvableLength);
		if (array.length > 0)
			Error.assertTrue((array[0]-offset) % modulo == 0);
		for(int j = 0; j < array.length; j++) {
			Error.assertTrue(array[j] < evolvableLength);
			if (j < array.length-1)
				Error.assertTrue(array[j] + 1 == array[j+1]);
		}
	}
}

}