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
package gov.nasa.javaGenes.core;

import junit.framework.TestCase;
import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.evolvableDoubleList.EvolvableDoubleList;

public class EvolvableTest extends TestCase {

public EvolvableTest(String name) {super(name);}

public void testGetSmallest() {
    int[] a1 = {1,2,3};
    testGetSmallest(a1, 1);
    int[] a2 = {11,2,3};
    testGetSmallest(a2, 2);
    int[] a3 = {3};
    testGetSmallest(a3, 3);
    int[] a4 = {3,2,1};
    testGetSmallest(a4, 1);
}
private void testGetSmallest(int[] sizes, int correctSize) {
	Evolvable[] evolvables = new Evolvable[sizes.length];
	for(int i = 0; i < sizes.length; i++)
		evolvables[i] = new EvolvableDoubleList(sizes[i]);
	Evolvable smallest = Evolvable.getSmallest(evolvables);
	Error.assertTrue(smallest.getSize() == correctSize);
}
}