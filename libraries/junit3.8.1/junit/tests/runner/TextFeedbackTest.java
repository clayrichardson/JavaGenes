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

package junit.tests.runner;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.ResultPrinter;
import junit.textui.TestRunner;

public class TextFeedbackTest extends TestCase {
	OutputStream output;
	TestRunner runner;
	
	class TestResultPrinter extends ResultPrinter {
		TestResultPrinter(PrintStream writer) {
			super(writer);
		}
		
		/* Spoof printing time so the tests are deterministic
		 */
		protected String elapsedTimeAsString(long runTime) {
			return "0";
		}
	}
	
	public static void main(String[] args) {
		TestRunner.run(TextFeedbackTest.class);
	}
	
	public void setUp() {
		output= new ByteArrayOutputStream();
		runner= new TestRunner(new TestResultPrinter(new PrintStream(output)));
	}
	
	public void testEmptySuite() {
		String expected= expected(new String[]{"", "Time: 0", "", "OK (0 tests)", ""});
		runner.doRun(new TestSuite());
		assertEquals(expected.toString(), output.toString());
	}

	
	public void testOneTest() {
		String expected= expected(new String[]{".", "Time: 0", "", "OK (1 test)", ""});
		TestSuite suite = new TestSuite();
		suite.addTest(new TestCase() { public void runTest() {}});
		runner.doRun(suite);
		assertEquals(expected.toString(), output.toString());
	}
	
	public void testTwoTests() {
		String expected= expected(new String[]{"..", "Time: 0", "", "OK (2 tests)", ""});
		TestSuite suite = new TestSuite();
		suite.addTest(new TestCase() { public void runTest() {}});
		suite.addTest(new TestCase() { public void runTest() {}});
		runner.doRun(suite);
		assertEquals(expected.toString(), output.toString());
	}

	public void testFailure() {
		String expected= expected(new String[]{".F", "Time: 0", "Failures here", "", "FAILURES!!!", "Tests run: 1,  Failures: 1,  Errors: 0", ""});
		ResultPrinter printer= new TestResultPrinter(new PrintStream(output)) {
			public void printFailures(TestResult result) {
				getWriter().println("Failures here");
			}
		};
		runner.setPrinter(printer);
		TestSuite suite = new TestSuite();
		suite.addTest(new TestCase() { public void runTest() {throw new AssertionFailedError();}});
		runner.doRun(suite);
		assertEquals(expected.toString(), output.toString());
	}
	
	public void testError() {
		String expected= expected(new String[]{".E", "Time: 0", "Errors here", "", "FAILURES!!!", "Tests run: 1,  Failures: 0,  Errors: 1", ""});
		ResultPrinter printer= new TestResultPrinter(new PrintStream(output)) {
			public void printErrors(TestResult result) {
				getWriter().println("Errors here");
			}
		};
		runner.setPrinter(printer);
		TestSuite suite = new TestSuite();
		suite.addTest(new TestCase() { public void runTest() throws Exception {throw new Exception();}});
		runner.doRun(suite);
		assertEquals(expected.toString(), output.toString());
	}
	
	private String expected(String[] lines) {
		OutputStream expected= new ByteArrayOutputStream();
		PrintStream expectedWriter= new PrintStream(expected);
		for (int i= 0; i < lines.length; i++)
			expectedWriter.println(lines[i]);
		return expected.toString(); 
	}

}
