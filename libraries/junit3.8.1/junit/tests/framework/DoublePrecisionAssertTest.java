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

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

public class DoublePrecisionAssertTest extends TestCase {

	/**
		 * Test for the special Double.NaN value.
		 */
	public void testAssertEqualsNaNFails() {
		try {
			assertEquals(1.234, Double.NaN, 0.0);
		} catch (AssertionFailedError e) {
			return;
		}
		fail();
	}

	public void testAssertNaNEqualsFails() {
		try {
			assertEquals(Double.NaN, 1.234, 0.0);
		} catch (AssertionFailedError e) {
			return;
		}
		fail();
	}

	public void testAssertNaNEqualsNaNFails() {
		try {
			assertEquals(Double.NaN, Double.NaN, 0.0);
		} catch (AssertionFailedError e) {
			return;
		}
		fail();
	}

	public void testAssertPosInfinityNotEqualsNegInfinity() {
		try {
			assertEquals(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 0.0);
		} catch (AssertionFailedError e) {
			return;
		}
		fail();
	}

	public void testAssertPosInfinityNotEquals() {
		try {
			assertEquals(Double.POSITIVE_INFINITY, 1.23, 0.0);
		} catch (AssertionFailedError e) {
			return;
		}
		fail();
	}

	public void testAssertPosInfinityEqualsInfinity() {
		assertEquals(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 0.0);
	}

	public void testAssertNegInfinityEqualsInfinity() {
		assertEquals(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 0.0);
	}

}
