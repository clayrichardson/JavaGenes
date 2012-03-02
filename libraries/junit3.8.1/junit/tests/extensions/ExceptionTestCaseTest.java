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

public class ExceptionTestCaseTest extends junit.framework.TestCase {

	static public class ThrowExceptionTestCase extends ExceptionTestCase {
		public ThrowExceptionTestCase(String name, Class exception) {
			super(name, exception);
		}
		public void test() {
			throw new IndexOutOfBoundsException();
		}
	}

	static public class ThrowRuntimeExceptionTestCase extends ExceptionTestCase {
		public ThrowRuntimeExceptionTestCase(String name, Class exception) {
			super(name, exception);
		}
		public void test() {
			throw new RuntimeException();
		}
	}

	static public class ThrowNoExceptionTestCase extends ExceptionTestCase {
		public ThrowNoExceptionTestCase(String name, Class exception) {
			super(name, exception);
		}
		public void test() {
		}
	}

	public void testExceptionSubclass() {
		ExceptionTestCase test= new ThrowExceptionTestCase("test", IndexOutOfBoundsException.class);
		TestResult result= test.run();
		assertEquals(1, result.runCount());
		assertTrue(result.wasSuccessful());
	}
	public void testExceptionTest() {
		ExceptionTestCase test= new ThrowExceptionTestCase("test", IndexOutOfBoundsException.class);
		TestResult result= test.run();
		assertEquals(1, result.runCount());
		assertTrue(result.wasSuccessful());
	}
	public void testFailure() {
		ExceptionTestCase test= new ThrowRuntimeExceptionTestCase("test", IndexOutOfBoundsException.class);
		TestResult result= test.run();
		assertEquals(1, result.runCount());
		assertEquals(1, result.errorCount());
	}
	public void testNoException() {
		ExceptionTestCase test= new ThrowNoExceptionTestCase("test", Exception.class);
		TestResult result= test.run();
		assertEquals(1, result.runCount());
		assertEquals(1, result.failureCount());
	}
	public void testWrongException() {
		ExceptionTestCase test= new ThrowRuntimeExceptionTestCase("test", IndexOutOfBoundsException.class);
		TestResult result= test.run();
		assertEquals(1, result.runCount());
		assertEquals(1, result.errorCount());
	}
}