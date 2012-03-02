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
import gov.nasa.alsUtility.Vector3d;

public class Brick3dTest extends TestCase {
public Brick3dTest(String name) {super(name);}
public void testConstructor() {
	Brick3d brick = new Brick3d(0,1, 3,2, 5,6);
	Error.assertTrue("1", brick.getInterval(0).nearlyEqual(new DoubleInterval(0,1)));
	Error.assertTrue("2", brick.getInterval(1).nearlyEqual(new DoubleInterval(2,3)));
	Error.assertTrue("3", brick.getInterval(2).nearlyEqual(new DoubleInterval(5,6)));
	
	brick = new Brick3d(new Vector3d(0,2,6), new Vector3d(1,3,5));
	Error.assertTrue("1.1", brick.getInterval(0).nearlyEqual(new DoubleInterval(0,1)));
	Error.assertTrue("2.1", brick.getInterval(1).nearlyEqual(new DoubleInterval(2,3)));
	Error.assertTrue("3.1", brick.getInterval(2).nearlyEqual(new DoubleInterval(5,6)));
	
	Brick3d b2 = new Brick3d(brick);
	Error.assertTrue("1.2", brick.getInterval(0).nearlyEqual(new DoubleInterval(0,1)));
	Error.assertTrue("2.2", brick.getInterval(1).nearlyEqual(new DoubleInterval(2,3)));
	Error.assertTrue("3.2", brick.getInterval(2).nearlyEqual(new DoubleInterval(5,6)));
}
public void testExpandToInclude() {
	Brick3d brick = new Brick3d(0,1, 3,2, 5,6);
	brick.expandToInclude(new Vector3d(0,2.5,5.1));
	Error.assertTrue("1", brick.nearlyEqual(new Brick3d(0,1, 3,2, 5,6)));
	brick.expandToInclude(new Vector3d(-1,2.5,5.1));
	Error.assertTrue("2", brick.nearlyEqual(new Brick3d(-1,1, 3,2, 5,6)));
	brick.expandToInclude(new Vector3d(-1,2.5,6.5));
	Error.assertTrue("3", brick.nearlyEqual(new Brick3d(-1,1, 3,2, 5,6.5)));
	brick.expandToInclude(new Vector3d(0,-4,6.5));
	Error.assertTrue("4", brick.nearlyEqual(new Brick3d(-1,1, 3,-4, 5,6.5)));
}
public void testExpandByAddition() {
	Brick3d brick = new Brick3d(0,1, 3,2, 5,6);
	brick.expandByAddition(0.1);
	Error.assertTrue("1", brick.getInterval(0).nearlyEqual(new DoubleInterval(-0.1,1.1)));
	Error.assertTrue("2", brick.getInterval(1).nearlyEqual(new DoubleInterval(1.9,3.1)));
	Error.assertTrue("3", brick.getInterval(2).nearlyEqual(new DoubleInterval(4.9,6.1)));

	brick = new Brick3d(0,1, 3,2, 5,6);
	brick.expandByAddition(-0.1);
	Error.assertTrue("1.1", brick.getInterval(0).nearlyEqual(new DoubleInterval(-0.1,1.1)));
	Error.assertTrue("2.1", brick.getInterval(1).nearlyEqual(new DoubleInterval(1.9,3.1)));
	Error.assertTrue("3.1", brick.getInterval(2).nearlyEqual(new DoubleInterval(4.9,6.1)));

	brick = new Brick3d(0,1, 3,2, 5,6);
	brick.expandByAddition(0);
	Error.assertTrue("1.2", brick.getInterval(0).nearlyEqual(new DoubleInterval(0,1)));
	Error.assertTrue("2.2", brick.getInterval(1).nearlyEqual(new DoubleInterval(2,3)));
	Error.assertTrue("3.2", brick.getInterval(2).nearlyEqual(new DoubleInterval(5,6)));
}
public void testIsInside() {
	Brick3d brick = new Brick3d(0,1, 2,3, 5,6);
	Error.assertTrue("1", brick.isInside(new Vector3d(0.5, 2.5, 5.5)));
	Error.assertTrue("2", brick.isInside(new Vector3d(0, 2.5, 5.5)));
	Error.assertTrue("3", brick.isInside(new Vector3d(0.5, 3, 5.5)));
	Error.assertTrue("4", brick.isInside(new Vector3d(0.5, 2.5, 5)));
	Error.assertTrue("5", brick.isInside(new Vector3d(0,2,5)));
	Error.assertTrue("6", brick.isInside(new Vector3d(1,3,6)));
	
	Error.assertFalse("7", brick.isInside(new Vector3d(-1, 2.5, 5.5)));
	Error.assertFalse("8", brick.isInside(new Vector3d(0.5, 3.1, 5.5)));
	Error.assertFalse("9", brick.isInside(new Vector3d(0.5, 2.5, 6.000000000001)));
	Error.assertFalse("10", brick.isInside(new Vector3d(-1,-2,-3)));

}
public void testSideLength() {
	Brick3d brick = new Brick3d(0,4, 2,9, 5,5);
	Error.assertTrue("1", Utility.nearlyEqual(4,brick.getSideLength(0)));
	Error.assertTrue("2", Utility.nearlyEqual(7,brick.getSideLength(1)));
	Error.assertTrue("3", Utility.nearlyEqual(0,brick.getSideLength(2)));
}
}
