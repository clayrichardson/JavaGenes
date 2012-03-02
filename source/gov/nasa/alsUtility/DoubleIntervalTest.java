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

public class DoubleIntervalTest extends TestCase {

public DoubleIntervalTest(String name) {super(name);}
private DoubleInterval a;
public void setUp() {
  a = new DoubleInterval(0,1);
}
public void testConstructor() {
	DoubleInterval interval = new DoubleInterval(1,50);
	Error.assertTrue("1", interval.low() == 1);
	Error.assertTrue("2", interval.high() == 50);
	interval = new DoubleInterval(50,1);
	Error.assertTrue("3", interval.low() == 1);
	Error.assertTrue("4", interval.high() == 50);
}
public void testNearlyEqual() {
	Error.assertTrue("1", new DoubleInterval(0,1).nearlyEqual(new DoubleInterval(0,1)));
	Error.assertTrue("2", new DoubleInterval(0,1).nearlyEqual(new DoubleInterval(0,1.000000000000001)));
	Error.assertFalse("3", new DoubleInterval(0,1).nearlyEqual(new DoubleInterval(0,1.1)));
	Error.assertFalse("4", new DoubleInterval(0,false,1,true).nearlyEqual(new DoubleInterval(0,1)));
	Error.assertFalse("5", new DoubleInterval(0,true,1,false).nearlyEqual(new DoubleInterval(0,1)));
}
public void testMidPoint() {
	Error.assertTrue("1", Utility.nearlyEqual(0.5, new DoubleInterval(0,1).midPoint()));
	Error.assertTrue("2", Utility.nearlyEqual(2.0, new DoubleInterval(1,3).midPoint()));
	Error.assertTrue("3", Utility.nearlyEqual(-4.5, new DoubleInterval(-3,-6).midPoint()));
	Error.assertTrue("4", Utility.nearlyEqual(1.0, new DoubleInterval(1,1).midPoint()));
}
public void testExpandByAddition() {
	DoubleInterval interval = new DoubleInterval(0,2);
	interval.expandByAddition(0.1);
	Error.assertTrue("1", new DoubleInterval(-0.1,2.1).nearlyEqual(interval));
	interval = new DoubleInterval(0,1);
	interval.expandByAddition(-0.1);
	Error.assertTrue("2", new DoubleInterval(-0.1,1.1).nearlyEqual(interval));
}
public void testExpand() {
	DoubleInterval interval = new DoubleInterval(0,1);
	interval.expand(1.2);
	Error.assertTrue("1", new DoubleInterval(-0.1,1.1).nearlyEqual(interval));
	interval = new DoubleInterval(0,1);
	interval.expand(0.2);
	Error.assertTrue("2", new DoubleInterval(0.4,0.6).nearlyEqual(interval));
}
public void testGet() {
	DoubleInterval interval = new DoubleInterval(50,1);
	Error.assertTrue("1", interval.low() == interval.get(0));
	Error.assertTrue("2", interval.high() == interval.get(1));
}
	
public void testTerpolate0to1() {
	DoubleInterval interval = new DoubleInterval(0,10);
	Error.assertTrue("1", Utility.nearlyEqual(0.5,interval.terpolate0to1(5)));
	Error.assertTrue("2", Utility.nearlyEqual(1.5,interval.terpolate0to1(15)));
	Error.assertTrue("3", Utility.nearlyEqual(-0.5,interval.terpolate0to1(-5)));
	Error.assertTrue("4", Utility.nearlyEqual(1,interval.terpolate0to1(10)));
	Error.assertTrue("5", Utility.nearlyEqual(0,interval.terpolate0to1(0)));

	interval = new DoubleInterval(-1,1);
	Error.assertTrue("6", Utility.nearlyEqual(0,interval.terpolate0to1(-1)));
	Error.assertTrue("7", Utility.nearlyEqual(2,interval.terpolate0to1(3)));
	Error.assertTrue("8", Utility.nearlyEqual(-0.25,interval.terpolate0to1(-1.5)));
	Error.assertTrue("9", Utility.nearlyEqual(0.5,interval.terpolate0to1(0)));

	interval = new DoubleInterval(-20,-10);
	Error.assertTrue("10", Utility.nearlyEqual(0.8,interval.terpolate0to1(-12)));
	Error.assertTrue("11", Utility.nearlyEqual(2,interval.terpolate0to1(0)));
	Error.assertTrue("10", Utility.nearlyEqual(-0.1,interval.terpolate0to1(-21)));
}
public void testDistanceFrom() {
	Error.assertTrue("1", Utility.nearlyEqual(new DoubleInterval(0,1).distanceFrom(0.5),0));
	Error.assertTrue("2", Utility.nearlyEqual(new DoubleInterval(0,1).distanceFrom(0.0),0));
	Error.assertTrue("3", Utility.nearlyEqual(new DoubleInterval(0,1).distanceFrom(1),0));
	Error.assertTrue("4", Utility.nearlyEqual(new DoubleInterval(0,1).distanceFrom(1.5),0.5));
	Error.assertTrue("5", Utility.nearlyEqual(new DoubleInterval(0,1).distanceFrom(-0.5),-0.5));
	Error.assertTrue("6", Utility.nearlyEqual(new DoubleInterval(-1,1).distanceFrom(-0.5),0));
	Error.assertTrue("7", Utility.nearlyEqual(new DoubleInterval(-2,1).distanceFrom(-3.5),-1.5));
	Error.assertTrue("8", Utility.nearlyEqual(new DoubleInterval(-2,1).distanceFrom(3.5),2.5));
}
public void testLimitToDouble() {
    DoubleInterval d = new DoubleInterval(-1,1);
    assertTrue("1", d.limitTo(0) == 0);
    assertTrue("2", d.limitTo(-10) == -1);
    assertTrue("3", d.limitTo(1) == 1);
    assertTrue("4", d.limitTo(0.5) == 0.5);
    assertTrue("5", d.limitTo(4) == 1);
    assertTrue("6", d.limitTo(-1) == -1);
}
public void testRandomGaussian() {
    final int tries = 100;
    DoubleInterval d = new DoubleInterval(-1,1);
    for(int i = 0; i < 100; i++)
        assertTrue(i + "", d.isBetween(d.randomGaussian(0,10.0)));
}
public void testExtendIfNecessary() {
    a.extendIfNecessary(0.5);
    assertTrue(a.high() == 1);
    assertTrue(a.low() == 0);
    a.extendIfNecessary(1.1);
    assertTrue(a.high() == 1.1);
    assertTrue(a.low() == 0);
    a.extendIfNecessary(-1);
    assertTrue(a.high() == 1.1);
    assertTrue(a.low() == -1);
}
public void testSetToExtremes() {
  double array[] = {-1.9,0,0.5,1.3};
  a.setToExtremes(array);
  assertTrue(a.high() == 1.3);
  assertTrue(a.low() == -1.9);
}
public void testIsPositive() {
  assertTrue(new DoubleInterval(1.0,2.0).isPositive());
  assertTrue(!new DoubleInterval(-1.0,2.0).isPositive());
  assertTrue(!new DoubleInterval(1.0,0.0).isPositive());
}
}