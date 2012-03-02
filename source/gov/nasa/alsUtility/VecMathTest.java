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

// everything commented out so that JavaGenes won't be dependent on Java3d (javax.vecmath).  Uncomment to verify JavaGenes versions of vecmath classes.
//import javax.vecmath.*;

/** compare Vector3d, Matrix3d, and Quat4d with Java3d's javax.vecmath to make sure they give similar results */
public class VecMathTest extends junit.framework.TestCase {
public VecMathTest(String name) {super(name);}
final public DoubleInterval interval = new DoubleInterval(-10,10);
final int trials = 10000;
/*
public class BothVector3d {
public javax.vecmath.Vector3d vecmath = new javax.vecmath.Vector3d();
public gov.nasa.alsUtility.Vector3d als = new gov.nasa.alsUtility.Vector3d();

public void randomize() {
	vecmath.x = interval.random();
	vecmath.y = interval.random();
	vecmath.z = interval.random();
	setAlsToVecMath();
}
public void setAlsToVecMath() {
	als.x = vecmath.x;
	als.y = vecmath.y;
	als.z = vecmath.z;
}
public boolean nearlyEqual() {
	return Utility.nearlyEqual(als.x,vecmath.x) && Utility.nearlyEqual(als.y,vecmath.y) && Utility.nearlyEqual(als.z,vecmath.z);
}
}	
public class BothQuat4d {
public javax.vecmath.Quat4d vecmath = new javax.vecmath.Quat4d();
public gov.nasa.alsUtility.Quat4d als = new gov.nasa.alsUtility.Quat4d();

public void randomize() {
	vecmath.x = interval.random();
	vecmath.y = interval.random();
	vecmath.z = interval.random();
	vecmath.w = interval.random();
	setAlsToVecMath();
}
public void setAlsToVecMath() {
	als.x = vecmath.x;
	als.y = vecmath.y;
	als.z = vecmath.z;
	als.w = vecmath.w;
}
public boolean nearlyEqual() {
	return Utility.nearlyEqual(als.x,vecmath.x) && Utility.nearlyEqual(als.y,vecmath.y) && Utility.nearlyEqual(als.z,vecmath.z) && Utility.nearlyEqual(als.w,vecmath.w);
}
}
public class BothMatrix3d {
public javax.vecmath.Matrix3d vecmath = new javax.vecmath.Matrix3d();
public gov.nasa.alsUtility.Matrix3d als = new gov.nasa.alsUtility.Matrix3d();

public void randomize() {
	vecmath.m00 = interval.random();
	vecmath.m01 = interval.random();
	vecmath.m02 = interval.random();
	vecmath.m10 = interval.random();
	vecmath.m11 = interval.random();
	vecmath.m12 = interval.random();
	vecmath.m20 = interval.random();
	vecmath.m21 = interval.random();
	vecmath.m22 = interval.random();
	setAlsToVecMath();
}
public void setAlsToVecMath() {
	als.m00 = vecmath.m00;
	als.m01 = vecmath.m01;
	als.m02 = vecmath.m02;
	als.m10 = vecmath.m10;
	als.m11 = vecmath.m11;
	als.m12 = vecmath.m12;
	als.m20 = vecmath.m20;
	als.m21 = vecmath.m21;
	als.m22 = vecmath.m22;
}

public boolean nearlyEqual() {
	return
		Utility.nearlyEqual(als.m00,vecmath.m00) &&
		Utility.nearlyEqual(als.m01,vecmath.m01) &&
		Utility.nearlyEqual(als.m02,vecmath.m02) &&
		Utility.nearlyEqual(als.m10,vecmath.m10) &&
		Utility.nearlyEqual(als.m11,vecmath.m11) &&
		Utility.nearlyEqual(als.m12,vecmath.m12) &&
		Utility.nearlyEqual(als.m20,vecmath.m20) &&
		Utility.nearlyEqual(als.m21,vecmath.m21) &&
		Utility.nearlyEqual(als.m22,vecmath.m22);
}
}
public void testMatrix3dd() {
	for(int i = 0; i < trials; i++) {
		BothMatrix3d m = new BothMatrix3d();
		m.randomize();
		BothVector3d v = new BothVector3d();
		v.randomize();
		m.als.transform(v.als);
		m.vecmath.transform(v.vecmath);
		Error.assertTrue(v.nearlyEqual());

		BothQuat4d q = new BothQuat4d();
		q.randomize();
		m.als.set(q.als);
		m.vecmath.set(q.vecmath);
		Error.assertTrue(m.nearlyEqual());
	}
}

public void testQuat4d() {
	for(int i = 0; i < trials; i++) {
		BothQuat4d q1 = new BothQuat4d();
		q1.randomize();
		BothQuat4d q2 = new BothQuat4d();
		q2.randomize();
		q1.als.mul(q2.als);
		q1.vecmath.mul(q2.vecmath);
		Error.assertTrue(q1.nearlyEqual());
	}
}
		
public void testVector3d() {
	for(int i = 0; i < trials; i++) {
		BothVector3d v1 = new BothVector3d();
		v1.randomize();
		BothVector3d v2 = new BothVector3d();
		v2.randomize();
		BothVector3d v3 = new BothVector3d();
		v3.randomize();
		testDot(v1,v2);
		testCross(v1,v2);
		testAngle(v1,v2);
		testNegate(v1,v2);
		testScale(v1,v2,interval.random());
		testAdd(v1,v2,v3);
		testSub(v1,v2,v3);
		testLength(v1,v2);
	}
}
private void testDot(BothVector3d v1, BothVector3d v2) {
	Error.assertTrue(Utility.nearlyEqual(v1.als.dot(v2.als),v1.vecmath.dot(v2.vecmath)));
}
private void testCross(BothVector3d v1, BothVector3d v2) {
	BothVector3d b = new BothVector3d();
	b.randomize();
	b.als.cross(v1.als,v2.als);
	b.vecmath.cross(v1.vecmath,v2.vecmath);
	Error.assertTrue(b.nearlyEqual());
}
private void testAngle(BothVector3d v1, BothVector3d v2) {
	Error.assertTrue(Utility.nearlyEqual(v1.als.angle(v2.als),v1.vecmath.angle(v2.vecmath)));
}
private void testNegate(BothVector3d v1, BothVector3d v2) {
	v1.als.negate();
	v1.vecmath.negate();
	Error.assertTrue(v1.nearlyEqual());
	v1.als.negate(v2.als);
	v1.vecmath.negate(v2.vecmath);
	Error.assertTrue(v1.nearlyEqual());
}
private void testScale(BothVector3d v1, BothVector3d v2, double s) {
	v1.als.scale(s);
	v1.vecmath.scale(s);
	Error.assertTrue(v1.nearlyEqual());
	v1.als.scale(s,v2.als);
	v1.vecmath.scale(s,v2.vecmath);
	Error.assertTrue(v1.nearlyEqual());
}
private void testAdd(BothVector3d v1, BothVector3d v2, BothVector3d v3) {
	v1.als.add(v2.als);
	v1.vecmath.add(v2.vecmath);
	Error.assertTrue(v1.nearlyEqual());
	v1.als.add(v2.als,v3.als);
	v1.vecmath.add(v2.vecmath,v3.vecmath);
	Error.assertTrue(v1.nearlyEqual());
}	
private void testSub(BothVector3d v1, BothVector3d v2, BothVector3d v3) {
	v1.als.sub(v2.als,v3.als);
	v1.vecmath.sub(v2.vecmath,v3.vecmath);
	Error.assertTrue(v1.nearlyEqual());
}	
private void testLength(BothVector3d v1, BothVector3d v2) {
	Error.assertTrue(Utility.nearlyEqual(v1.als.length(),v1.vecmath.length()));
}
*/
// avoid complaints from junit about empty classes
public void test() {
	assertTrue(true);
}
}
