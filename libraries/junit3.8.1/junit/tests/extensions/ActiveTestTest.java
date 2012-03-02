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
import junit.extensions.*;
 
/**
 * Testing the ActiveTest support
 */

public class ActiveTestTest extends TestCase {

	public static class SuccessTest extends TestCase {		
		public void runTest() {
		}
	}
		
	public void testActiveTest() {		
		Test test= createActiveTestSuite(); 
		TestResult result= new TestResult();
		test.run(result);
		assertEquals(100, result.runCount());
		assertEquals(0, result.failureCount());
		assertEquals(0, result.errorCount());
	}
	
	public void testActiveRepeatedTest() {		
		Test test= new RepeatedTest(createActiveTestSuite(), 5);
		TestResult result= new TestResult();
		test.run(result);
		assertEquals(500, result.runCount());
		assertEquals(0, result.failureCount());
		assertEquals(0, result.errorCount());
	}
	
	public void testActiveRepeatedTest0() {		
		Test test= new RepeatedTest(createActiveTestSuite(), 0);
		TestResult result= new TestResult();
		test.run(result);
		assertEquals(0, result.runCount());
		assertEquals(0, result.failureCount());
		assertEquals(0, result.errorCount());
	}

	public void testActiveRepeatedTest1() {		
		Test test= new RepeatedTest(createActiveTestSuite(), 1);
		TestResult result= new TestResult();
		test.run(result);
		assertEquals(100, result.runCount());
		assertEquals(0, result.failureCount());
		assertEquals(0, result.errorCount());
	}

	ActiveTestSuite createActiveTestSuite() {
		ActiveTestSuite suite= new ActiveTestSuite();
		for (int i= 0; i < 100; i++) 
			suite.addTest(new SuccessTest());
		return suite;
	}

}