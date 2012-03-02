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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.runner.BaseTestRunner;

public class BaseTestRunnerTest extends TestCase {
	
	public class MockRunner extends BaseTestRunner {
		protected void runFailed(String message) {
		}

		public void testEnded(String testName) {
		}

		public void testFailed(int status, Test test, Throwable t) {
		}

		public void testStarted(String testName) {
		}
	}
	
	public static class NonStatic {
		public Test suite() {
			return null;
		}
	}

	
	public void testInvokeNonStaticSuite() {
		BaseTestRunner runner= new MockRunner();
		runner.getTest("junit.tests.runner.BaseTestRunnerTest$NonStatic"); // Used to throw NullPointerException
	}
}
