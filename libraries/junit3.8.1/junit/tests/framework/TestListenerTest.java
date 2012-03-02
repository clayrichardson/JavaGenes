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

/**
 * Test class used in SuiteTest
 */
import junit.framework.*;

public class TestListenerTest extends TestCase implements TestListener {
	private TestResult fResult;
	private int fStartCount;
	private int fEndCount;
	private int fFailureCount;
	private int fErrorCount;

	public void addError(Test test, Throwable t) {
		fErrorCount++;
	}
	public void addFailure(Test test, AssertionFailedError t) {
		fFailureCount++;
	}
	public void endTest(Test test) {
		fEndCount++;
	}
	protected void setUp() {
		fResult= new TestResult();
		fResult.addListener(this);
	
		fStartCount= 0;
		fEndCount= 0;
		fFailureCount= 0;
	}
	public void startTest(Test test) {
		fStartCount++;
	}
	public void testError() {
		TestCase test= new TestCase("noop") {
			public void runTest() {
				throw new Error();
			}
		};
		test.run(fResult);
		assertEquals(1, fErrorCount);
		assertEquals(1, fEndCount);
	}
	public void testFailure() {
		TestCase test= new TestCase("noop") {
			public void runTest() {
				fail();
			}
		};
		test.run(fResult);
		assertEquals(1, fFailureCount);
		assertEquals(1, fEndCount);
	}
	public void testStartStop() {
		TestCase test= new TestCase("noop") {
			public void runTest() {
			}
		};
		test.run(fResult);
		assertEquals(1, fStartCount);
		assertEquals(1, fEndCount);
	}
}