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
package junit.tests.extensions;

import junit.framework.*;
import junit.extensions.RepeatedTest;

/**
 * Testing the RepeatedTest support.
 */

public class RepeatedTestTest extends TestCase {
	private TestSuite fSuite;

	public static class SuccessTest extends TestCase {

		public void runTest() {
		}
	}

	public RepeatedTestTest(String name) {
		super(name);
		fSuite= new TestSuite();
		fSuite.addTest(new SuccessTest());
		fSuite.addTest(new SuccessTest());
	}

	public void testRepeatedOnce() {
		Test test= new RepeatedTest(fSuite, 1);
		assertEquals(2, test.countTestCases());
		TestResult result= new TestResult();
		test.run(result);
		assertEquals(2, result.runCount());
	}

 	public void testRepeatedMoreThanOnce() {
		Test test= new RepeatedTest(fSuite, 3);
		assertEquals(6, test.countTestCases());
		TestResult result= new TestResult();
		test.run(result);
		assertEquals(6, result.runCount());
	}

 	public void testRepeatedZero() {
		Test test= new RepeatedTest(fSuite, 0);
		assertEquals(0, test.countTestCases());
		TestResult result= new TestResult();
		test.run(result);
		assertEquals(0, result.runCount());
	}

 	public void testRepeatedNegative() {
 		try {
			new RepeatedTest(fSuite, -1);
 		} catch (IllegalArgumentException e) {
 			return;
 		}
 		fail("Should throw an IllegalArgumentException");
	}
}