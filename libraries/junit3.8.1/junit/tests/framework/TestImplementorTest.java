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
package junit.tests.framework;

import junit.framework.*;

/**
 * Test an implementor of junit.framework.Test other than TestCase or TestSuite
 */
public class TestImplementorTest extends TestCase {
	public static class DoubleTestCase implements Test {
		private TestCase fTestCase;
		
		public DoubleTestCase(TestCase testCase) {
			fTestCase= testCase;
		}
		
		public int countTestCases() {
			return 2;
		}
		
		public void run(TestResult result) {
			result.startTest(this);
			Protectable p= new Protectable() {
				public void protect() throws Throwable {
					fTestCase.runBare();
					fTestCase.runBare();
				}
			};
			result.runProtected(this, p);
			result.endTest(this);
		}
	}
	
	private DoubleTestCase fTest;
	
	public TestImplementorTest() {
		TestCase testCase= new TestCase() {
			public void runTest() {
			}
		};
		fTest= new DoubleTestCase(testCase);
	}
	
	public void testSuccessfulRun() {
		TestResult result= new TestResult();
		fTest.run(result);
		assertEquals(fTest.countTestCases(), result.runCount());
		assertEquals(0, result.errorCount());
		assertEquals(0, result.failureCount());
	}
}
