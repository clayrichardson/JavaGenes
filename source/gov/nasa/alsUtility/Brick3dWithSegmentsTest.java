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
package gov.nasa.alsUtility;

import junit.framework.TestCase;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;

public class Brick3dWithSegmentsTest extends TestCase {
public Brick3dWithSegmentsTest(String name) {super(name);}
public void test() {
	Brick3d b = new Brick3d(0,10, 1,13, -1,59);
	int[] numberOfSegments1 = {1,4,5};
	Brick3dWithSegments brick = new Brick3dWithSegments(b, numberOfSegments1);
	double[][] sides1 = {
		{0,10},
		{1,4,7,10,13},
		{-1,11,23,35,47,59}
	};
	for(int i = 0; i < sides1.length; i++) 
		Error.assertTrue("1-"+i, Utility.nearlyEqual(brick.getSegmentCoordinates(i),sides1[i]));
}
}
