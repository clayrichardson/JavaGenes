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
import junit.tests.WasRun;

/**
 * A test case testing the extensions to the testing framework.
 *
 */
public class ExtensionTest extends TestCase {
	static class TornDown extends TestSetup { 
		boolean fTornDown= false;
		
		TornDown(Test test) {
			super(test);
		}
		protected void tearDown() {
			fTornDown= true;
		}
	}
	public void testRunningErrorInTestSetup() {
		TestCase test= new TestCase("failure") {
			public void runTest() {
				fail();
			}
		};

		TestSetup wrapper= new TestSetup(test);

		TestResult result= new TestResult();
		wrapper.run(result);
		assertTrue(!result.wasSuccessful());
	}
	public void testRunningErrorsInTestSetup() {
		TestCase failure= new TestCase("failure") {
			public void runTest() {
				fail();
			}
		};

		TestCase error= new TestCase("error") {
			public void runTest() {
				throw new Error();
			}
		};

		TestSuite suite= new TestSuite();
		suite.addTest(failure);
		suite.addTest(error);
		
		TestSetup wrapper= new TestSetup(suite);

		TestResult result= new TestResult();
		wrapper.run(result);

		assertEquals(1, result.failureCount());
		assertEquals(1, result.errorCount());
	}
	public void testSetupErrorDontTearDown() {
		WasRun test= new WasRun();

		TornDown wrapper= new TornDown(test) {
			public void setUp() {
				fail();
			}
		};

		TestResult result= new TestResult();
		wrapper.run(result);

		assertTrue(!wrapper.fTornDown);
	}
	public void testSetupErrorInTestSetup() {
		WasRun test= new WasRun();

		TestSetup wrapper= new TestSetup(test) {
			public void setUp() {
				fail();
			}
		};

		TestResult result= new TestResult();
		wrapper.run(result);

		assertTrue(!test.fWasRun);
		assertTrue(!result.wasSuccessful());
	}
}