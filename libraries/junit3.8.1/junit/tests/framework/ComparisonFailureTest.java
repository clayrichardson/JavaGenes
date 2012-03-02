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

import junit.framework.ComparisonFailure;
import junit.framework.TestCase;

public class ComparisonFailureTest extends TestCase {

	public void testComparisonErrorMessage() {
		ComparisonFailure failure= new ComparisonFailure("a", "b", "c");
		assertEquals("a expected:<b> but was:<c>", failure.getMessage());
	}

	public void testComparisonErrorStartSame() {
		ComparisonFailure failure= new ComparisonFailure(null, "ba", "bc");
		assertEquals("expected:<...a> but was:<...c>", failure.getMessage());
	}

	public void testComparisonErrorEndSame() {
		ComparisonFailure failure= new ComparisonFailure(null, "ab", "cb");
		assertEquals("expected:<a...> but was:<c...>", failure.getMessage());
	}

	public void testComparisonErrorSame() {
		ComparisonFailure failure= new ComparisonFailure(null, "ab", "ab");
		assertEquals("expected:<ab> but was:<ab>", failure.getMessage());
	}

	public void testComparisonErrorStartAndEndSame() {
		ComparisonFailure failure= new ComparisonFailure(null, "abc", "adc");
		assertEquals("expected:<...b...> but was:<...d...>", failure.getMessage());
	}

	public void testComparisonErrorStartSameComplete() {
		ComparisonFailure failure= new ComparisonFailure(null, "ab", "abc");
		assertEquals("expected:<...> but was:<...c>", failure.getMessage());
	}

	public void testComparisonErrorEndSameComplete() {
		ComparisonFailure failure= new ComparisonFailure(null, "bc", "abc");
		assertEquals("expected:<...> but was:<a...>", failure.getMessage());
	}

	public void testComparisonErrorOverlapingMatches() {
		ComparisonFailure failure= new ComparisonFailure(null, "abc", "abbc");
		assertEquals("expected:<......> but was:<...b...>", failure.getMessage());
	}

	public void testComparisonErrorOverlapingMatches2() {
		ComparisonFailure failure= new ComparisonFailure(null, "abcdde", "abcde");
		assertEquals("expected:<...d...> but was:<......>", failure.getMessage());
	}

	public void testComparisonErrorWithActualNull() {
		ComparisonFailure failure= new ComparisonFailure(null, "a", null);
		assertEquals("expected:<a> but was:<null>", failure.getMessage());
	}
	
	public void testComparisonErrorWithExpectedNull() {
		ComparisonFailure failure= new ComparisonFailure(null, null, "a");
		assertEquals("expected:<null> but was:<a>", failure.getMessage());
	}
}
