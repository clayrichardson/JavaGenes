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

import java.lang.reflect.*;
import junit.framework.*;
import junit.runner.*;
import java.net.URL;

/**
 * A TestCase for testing the TestCaseClassLoader
 *
 */
public class TestCaseClassLoaderTest extends TestCase {

	public void testClassLoading() throws Exception {
		TestCaseClassLoader loader= new TestCaseClassLoader();
		Class loadedClass= loader.loadClass("junit.tests.runner.ClassLoaderTest", true);
		Object o= loadedClass.newInstance();
		//
		// Invoke the assertClassLoaders method via reflection.
		// We use reflection since the class is loaded by
		// another class loader and we can't do a successfull downcast to
		// ClassLoaderTestCase.
		//
		Method method= loadedClass.getDeclaredMethod("verify", new Class[0]);
		method.invoke(o, new Class[0]);
	}

	public void testJarClassLoading() throws Exception {
		URL url= getClass().getResource("test.jar");
		assertNotNull("Cannot find test.jar", url);
		String path= url.getFile();
		TestCaseClassLoader loader= new TestCaseClassLoader(path);
		Class loadedClass= loader.loadClass("junit.tests.runner.LoadedFromJar", true);
		Object o= loadedClass.newInstance();
		//
		// Invoke the assertClassLoaders method via reflection.
		// We use reflection since the class is loaded by
		// another class loader and we can't do a successfull downcast to
		// ClassLoaderTestCase.
		//
		Method method= loadedClass.getDeclaredMethod("verify", new Class[0]);
		method.invoke(o, new Class[0]);
	}
}