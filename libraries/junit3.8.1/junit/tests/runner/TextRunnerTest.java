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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

public class TextRunnerTest extends TestCase {
	
	public void testFailure() throws Exception {
		execTest("junit.tests.framework.Failure", false);
	}

	public void testSuccess() throws Exception {
		execTest("junit.tests.framework.Success", true);
	}

	public void testError() throws Exception {
		execTest("junit.tests.BogusDude", false);
	}
	
	void execTest(String testClass, boolean success) throws Exception {
		String java= System.getProperty("java.home")+File.separator+"bin"+File.separator+"java";
		String cp= System.getProperty("java.class.path");
		//use -classpath for JDK 1.1.7 compatibility
		String [] cmd= { java, "-classpath", cp, "junit.textui.TestRunner", testClass}; 
		Process p= Runtime.getRuntime().exec(cmd);
		InputStream i= p.getInputStream();
		int b;
		while((b= i.read()) != -1) 
			; //System.out.write(b); 
		assertTrue((p.waitFor() == 0) == success);
		if (success)
			assertEquals(junit.textui.TestRunner.SUCCESS_EXIT, p.exitValue());
		else
			assertEquals(junit.textui.TestRunner.FAILURE_EXIT, p.exitValue());
	}
	
	public void testRunReturnsResult() {
		PrintStream oldOut= System.out;
		System.setOut(new PrintStream (
			new OutputStream() {
				public void write(int arg0) throws IOException {
				}
			}
		));
		try {
			TestResult result= junit.textui.TestRunner.run(new TestSuite());
			assertTrue(result.wasSuccessful());
		} finally {
			System.setOut(oldOut);
		}
	}
		

}