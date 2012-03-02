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

import junit.framework.*;
import junit.runner.BaseTestRunner;

/**
 * TestSuite that runs all the sample tests
 *
 */
public class AllTests {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}
	
	public static Test suite() { // Collect tests manually because we have to test class collection code
		TestSuite suite= new TestSuite("Framework Tests");
		suite.addTestSuite(StackFilterTest.class);
		suite.addTestSuite(SorterTest.class);
		suite.addTestSuite(SimpleTestCollectorTest.class);
		suite.addTestSuite(BaseTestRunnerTest.class);
		suite.addTestSuite(TextFeedbackTest.class);
		if (!BaseTestRunner.inVAJava()) {
			suite.addTestSuite(TextRunnerTest.class);
			if (!isJDK11())
				suite.addTest(new TestSuite(TestCaseClassLoaderTest.class));
		}
		return suite;
	}
	
	static boolean isJDK11() {
		String version= System.getProperty("java.version");
		return version.startsWith("1.1");
	}
}