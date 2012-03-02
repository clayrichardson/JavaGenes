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

public class SelectOneTest extends TestCase {

public SelectOneTest(String name) {super(name);}

public void test() {
    RandomNumber.setSeed(990739410906L);   // to get deterministic results
    SelectOne selector = new SelectOne();
	for(int i = 1; i < 100; i++) {
		int[] result = selector.getIndicesArray(i);
		assertTrue(i + " size " + Utility.toString(result), result.length == 1);
		assertTrue(i + Utility.toString(result), 0 <= result[0] && result[0] < i);
	}
	EvolvableDoubleList list = new EvolvableDoubleList();
	int[] result = selector.getIndicesArray(list.getSize());
	assertTrue("0", result.length == 0);
}
}