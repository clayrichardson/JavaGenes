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
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.IntegerInterval;
import gov.nasa.javaGenes.evolvableDoubleList.EvolvableDoubleList;

public class FitnessFunctionBadSizeTest extends TestCase {

public FitnessFunctionBadSizeTest(String name) {super(name);}

public void test() {
	test("1", 2, 4, 6, 2);
	test("2", 5, 4, 6, 0);
	test("3", 9, 4, 6, 3);
	test("4", 2, 2, 6, 0);
	test("5", 2, 2, 2, 0);
	test("6", 1, 2, 2, 1);
}
private void test(String name, int evolvableLength, int low, int high, int distance) {
	Evolvable evolvable = new EvolvableDoubleList(evolvableLength);
	FitnessFunctionBadSize ff = new FitnessFunctionBadSize(new IntegerInterval(low,high));
	assertTrue(name, ff.evaluateFitness(evolvable).asDouble() == distance);
}
}
