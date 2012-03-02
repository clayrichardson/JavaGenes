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

import java.lang.Math;
import java.lang.Double;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.alsUtility.ExtendedVector;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;

public class Vector3d implements java.io.Serializable {
public double x;
public double y;
public double z;

public Vector3d() {}
public Vector3d(Vector3d v) {
  x = v.x;
  y = v.y;
  z = v.z;
}
public Vector3d(double x, double y, double z) {
  this.x = x;
  this.y = y;
  this.z = z;
}
public Vector3d(double[] xyz) {
	Error.assertNotNull(xyz);
	Error.assertTrue(xyz.length == 3);
	this.x = xyz[0];
	this.y = xyz[1];
	this.z = xyz[2];
}
public double distanceTo(Vector3d other) {return Math.sqrt(distanceToSquared(other));}
public double distanceToSquared(Vector3d other) {
	return (x-other.x)*(x-other.x) + (y-other.y)*(y-other.y) + (z-other.z)*(z-other.z);
}
public double getX() {return x;}
public double getY() {return y;}
public double getZ() {return z;}
public void setX(double d) {x = d;}
public void setY(double d) {y = d;}
public void setZ(double d) {z = d;}
public boolean nearlyEqual(Vector3d v) {
	return Utility.nearlyEqual(x,v.x) && Utility.nearlyEqual(y,v.y) && Utility.nearlyEqual(z,v.z);
}
public static Vector3d factoryRandom(DoubleInterval interval) {
  Vector3d self = new Vector3d();
  self.x = RandomNumber.getDouble(interval);
  self.y = RandomNumber.getDouble(interval);
  self.z = RandomNumber.getDouble(interval);
  return self;
}
public static Vector3d subtract(Vector3d first, Vector3d second) {
  final Vector3d self = new Vector3d(first);
  self.subtract(second);
  return self;
}
public Vector3d subtract(Vector3d other) {
	x -= other.x;
	y -= other.y;
	z -= other.z;
  return this;
}
public Vector3d add(Vector3d other) {
	x += other.x;
	y += other.y;
	z += other.z;
	return this;
}
public static Vector3d multiply(Vector3d first, double d) {
  final Vector3d self = new Vector3d(first);
  self.multiply(d);
  return self;
}
public Vector3d multiply(double d) {
	x *= d;
	y *= d;
	z *= d;
  return this;
}
public void normalize() {
  final double length = length();
  if (length != 0)
    multiply(1/length);
}
public double length() {
  return Math.sqrt(x*x + y*y + z*z);
}
public static Vector3d[] calculateTranslations(Vector3d stay, Vector3d move, double start, double end, int number) {
  Error.assertTrue(number >= 2);
  Vector3d[] consecutiveTranslations = new Vector3d[number];
  Vector3d difference = Vector3d.subtract(move,stay);
  double length = difference.length();
  Vector3d unit = new Vector3d(difference);
  unit.normalize();
  double increment = (end - start)/(number-1);
  for(int i = 0; i < consecutiveTranslations.length; i++) {
    Vector3d next = new Vector3d();
	next.add(stay,Vector3d.multiply(unit,start+i*increment));
    consecutiveTranslations[i] = Vector3d.subtract(next,move);
  }
  return consecutiveTranslations;
}
public double dot(Vector3d other) {
	return x*other.x + y*other.y + z*other.z;
}
public void cross(Vector3d v1, Vector3d v2) {
	x = v1.y*v2.z - v1.z*v2.y;
	y = v1.z*v2.x - v1.x*v2.z;
	z = v1.x*v2.y - v1.y*v2.x;
}
public double angle(Vector3d v) {
	final DoubleInterval range = new DoubleInterval(-1,1);
	Vector3d aa = new Vector3d(this);
	Vector3d bb = new Vector3d(v);
	aa.normalize();
	bb.normalize();
	double dot = aa.dot(bb);
	if (Utility.normalNumber(dot))
		return Math.acos(range.limitTo(dot)); // limitTo() avoid numerical problems
	return 0;
}
public void negate() {
	x = -x;
	y = -y;
	z = -z;
}
public void negate(Vector3d v1) {
	set(v1);
	negate();
}
public void scale(double d) {
	multiply(d);
}
public void scale(double d, Vector3d v1) {
	set(v1);
	multiply(d);
}
public void set(Vector3d v1) {
	x = v1.x;
	y = v1.y;
	z = v1.z;	
}
public void add(Vector3d first, Vector3d second) {
  set(first);
  add(second);
}
public void sub(Vector3d first, Vector3d second) {
  set(first);
  subtract(second);
}
public boolean equals(Vector3d v) {
	return v != null && x == v.x && y == v.y && z == v.z;
}
public int hashCode() {
	long xbits = Double.doubleToLongBits(x);
	long ybits = Double.doubleToLongBits(y);
	long zbits = Double.doubleToLongBits(z);
	return (int)(xbits ^ (xbits >> 32) ^ ybits ^ (ybits >> 32) ^ zbits ^ (zbits >> 32));
}

}