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

public class Vector3dTest extends TestCase {
private Vector3d zero;
private Vector3d one;
private Vector3d two;
private Vector3d unit;
private Vector3d random;

public Vector3dTest(String name) {super(name);}
protected void setUp() {
  zero = new Vector3d(0,0,0);
  one = new Vector3d(1,1,1);
  two = new Vector3d(2,2,2);
  unit = new Vector3d(1,0,0);
  random = new Vector3d(2.5,-3,77);
}
public void testDistanceToSquared() {
	Error.assertTrue("0", Utility.nearlyEqual(0, new Vector3d(1,2,3).distanceToSquared(new Vector3d(1,2,3))));
	Error.assertTrue("1", Utility.nearlyEqual(1, new Vector3d(0,0,0).distanceToSquared(new Vector3d(1,0,0))));
	Error.assertTrue("2", Utility.nearlyEqual(4, new Vector3d(1,-1,0).distanceToSquared(new Vector3d(1,1,0))));
	Error.assertTrue("3", Utility.nearlyEqual(9, new Vector3d(1,1,-1).distanceToSquared(new Vector3d(1,1,-4))));
	Error.assertTrue("3", Utility.nearlyEqual(25, new Vector3d(3,0,0).distanceToSquared(new Vector3d(0,4,0))));
}
public void testDistanceTo() {
	Error.assertTrue("1", Utility.nearlyEqual(1, new Vector3d(0,0,0).distanceTo(new Vector3d(1,0,0))));
	Error.assertTrue("2", Utility.nearlyEqual(2, new Vector3d(1,-1,0).distanceTo(new Vector3d(1,1,0))));
	Error.assertTrue("3", Utility.nearlyEqual(3, new Vector3d(1,1,-1).distanceTo(new Vector3d(1,1,-4))));
	Error.assertTrue("3", Utility.nearlyEqual(5, new Vector3d(3,0,0).distanceTo(new Vector3d(0,4,0))));
}
	
public void testNormalize() {
	random.normalize();
	zero.normalize();
	assertTrue("random vector", Utility.nearlyEqual(1.0,random.length()));
	assertTrue("zero vector", 0 == zero.length());
}
public void testSubtract() {
  assertTrue(one.nearlyEqual(two.subtract(one)));
}
public void testAdd() {
  assertTrue(two.nearlyEqual(one.add(one)));
}
public void testMultiply() {
  assertTrue(two.nearlyEqual(one.multiply(one,2)));
}
public void testLength() {
  assertTrue(Utility.nearlyEqual(1,unit.length()));
}
public void testCalculateTranslations() {
  final double start = 0.5;
  final double end = 1.5;
  final int number = 4;
  final Vector3d[] a = Vector3d.calculateTranslations(two,zero,start,end,number);
  assertTrue(a.length == number);
  assertTrue("first", Utility.nearlyEqual(start,Vector3d.subtract(two,a[0]).length()));
  assertTrue("last", Utility.nearlyEqual(end,Vector3d.subtract(two,a[a.length-1]).length()));
  for(int i = 0; i < a.length; i++) {
    final double x= a[i].getX();
    final double y= a[i].getY();
    final double z= a[i].getZ();
    assertTrue(i+" positive",x > 0 && y > 0 && z > 0);
    assertTrue(i+" less than 2",x < 2 && y < 2 && z < 2);
    assertTrue(i+" equal",Utility.nearlyEqual(x,y) && Utility.nearlyEqual(y,z));
  }
}
}
