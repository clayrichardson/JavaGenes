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
package gov.nasa.alsUtility;

import junit.framework.TestCase;

public class KeyCounterTest extends TestCase {

public KeyCounterTest(String name) {super(name);}

public void testAdd() {
	KeyCounter counter = new KeyCounter();
	int[][] first = {{1,4},{2,3},{3,2},{4,1}};
	for(int i = 0; i < first.length; i++)
		counter.add(new integer(first[i][0]), first[i][1]);
	for(int i = 0; i < first.length; i++)
		Error.assertTrue(counter.getIntCount(new integer(first[i][0])) == first[i][1]);
	counter.addKeyCounter(counter);
	//System.out.println("\n" + counter.toString() + "\n");
	for(int i = 0; i < first.length; i++)
		Error.assertTrue(counter.getIntCount(new integer(first[i][0])) == 2*first[i][1]);
	for(int i = 0; i < first.length; i++)
		counter.add(new integer(first[i][0]));
	for(int i = 0; i < first.length; i++)
		Error.assertTrue(counter.getIntCount(new integer(first[i][0])) == 2*first[i][1] + 1);
}
}
