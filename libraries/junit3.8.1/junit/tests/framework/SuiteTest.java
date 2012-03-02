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
 * A fixture for testing the "auto" test suite feature.
 *
 */
public class SuiteTest extends TestCase {
	protected TestResult fResult;
	public SuiteTest(String name) {
		super(name);
	}
	protected void setUp() {
		fResult= new TestResult(); 
	}
	public static Test suite() {
		TestSuite suite= new TestSuite("Suite Tests");
		// build the suite manually, because some of the suites are testing
		// the functionality that automatically builds suites
		suite.addTest(new SuiteTest("testNoTestCaseClass"));
		suite.addTest(new SuiteTest("testNoTestCases"));
		suite.addTest(new SuiteTest("testOneTestCase"));
		suite.addTest(new SuiteTest("testNotPublicTestCase"));
		suite.addTest(new SuiteTest("testNotVoidTestCase"));
		suite.addTest(new SuiteTest("testNotExistingTestCase"));
		suite.addTest(new SuiteTest("testInheritedTests"));
		suite.addTest(new SuiteTest("testShadowedTests"));
		suite.addTest(new SuiteTest("testAddTestSuite"));
		
		return suite;
	}
	public void testInheritedTests() {
		TestSuite suite= new TestSuite(InheritedTestCase.class);
		suite.run(fResult);
		assertTrue(fResult.wasSuccessful());
		assertEquals(2, fResult.runCount());
	}
	public void testNoTestCaseClass() {
		Test t= new TestSuite(NoTestCaseClass.class);
		t.run(fResult);
		assertEquals(1, fResult.runCount());  // warning test
		assertTrue(! fResult.wasSuccessful());
	}
	public void testNoTestCases() {
		Test t= new TestSuite(NoTestCases.class);
		t.run(fResult);
		assertTrue(fResult.runCount() == 1);  // warning test
		assertTrue(fResult.failureCount() == 1);
		assertTrue(! fResult.wasSuccessful());
	}
	public void testNotExistingTestCase() {
		Test t= new SuiteTest("notExistingMethod");
		t.run(fResult);
		assertTrue(fResult.runCount() == 1);  
		assertTrue(fResult.failureCount() == 1);
		assertTrue(fResult.errorCount() == 0);
	}
	public void testNotPublicTestCase() {
		TestSuite suite= new TestSuite(NotPublicTestCase.class);
		// 1 public test case + 1 warning for the non-public test case
		assertEquals(2, suite.countTestCases());
	}
	public void testNotVoidTestCase() {
		TestSuite suite= new TestSuite(NotVoidTestCase.class);
		assertTrue(suite.countTestCases() == 1);
	}
	public void testOneTestCase() {
		Test t= new TestSuite(OneTestCase.class);
		t.run(fResult);
		assertTrue(fResult.runCount() == 1);  
		assertTrue(fResult.failureCount() == 0);
		assertTrue(fResult.errorCount() == 0);
		assertTrue(fResult.wasSuccessful());
	}
	public void testShadowedTests() {
		TestSuite suite= new TestSuite(OverrideTestCase.class);
		suite.run(fResult);
		assertEquals(1, fResult.runCount());
	}
	public void testAddTestSuite() {
		TestSuite suite= new TestSuite();
		suite.addTestSuite(OneTestCase.class);
		suite.run(fResult);
		assertEquals(1, fResult.runCount());
	}
}