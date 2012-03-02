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

/**
 * Test class used in TestTestCaseClassLoader
 */
import junit.framework.*;

public class ClassLoaderTest extends Assert {
	public ClassLoaderTest() {
	}
	public void verify() {
		verifyApplicationClassLoadedByTestLoader();
		verifySystemClassNotLoadedByTestLoader();
	}
	private boolean isTestCaseClassLoader(ClassLoader cl) {
		return (cl != null && cl.getClass().getName().equals(junit.runner.TestCaseClassLoader.class.getName()));
	}
	private void verifyApplicationClassLoadedByTestLoader() {
		assertTrue(isTestCaseClassLoader(getClass().getClassLoader()));
	} 
	private void verifySystemClassNotLoadedByTestLoader() {
		assertTrue(!isTestCaseClassLoader(Object.class.getClassLoader()));
		assertTrue(!isTestCaseClassLoader(TestCase.class.getClassLoader()));
	}
}