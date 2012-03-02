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
import java.lang.ArithmeticException;

public class MathUtilityTest extends TestCase {
public MathUtilityTest(String name) {super(name);}

public void testGetQuadraticFactorsFailure() {
	testGetQuadraticFactorsFailure(1,2,2);
	testGetQuadraticFactorsFailure(1,1,2);
	testGetQuadraticFactorsFailure(1,2,1);
}
private void testGetQuadraticFactorsFailure(double x1, double x2, double x3) {
	try {
		MathUtility.getQuadraticFactors(x1,1,x2,2,x3,3);
		fail("should never get here");
	} catch (ArithmeticException e) {}
}
public void testGetQuadraticFactorsRandom() {
	testGetQuadraticFactorsRandom(1,2,3);
	testGetQuadraticFactorsRandom(1,-2,3);
	testGetQuadraticFactorsRandom(0,0,3);
	testGetQuadraticFactorsRandom(1,0,3);
	testGetQuadraticFactorsRandom(10,2,3);
	testGetQuadraticFactorsRandom(1,2,-33);
	testGetQuadraticFactorsRandom(11,22,-333);
}
private void testGetQuadraticFactorsRandom(double a, double b, double c) {
	final int NUMBER_OF_TRIES = 100;
	final DoubleInterval interval = new DoubleInterval(-100,100);
	double[][] xyz = new double[3][3];
	for(int i = 0; i < NUMBER_OF_TRIES; i++) {
		double[] x = new double[3];
		double[] y = new double[3];
		for(int j = 0; j < x.length; j++) {
			x[j] = interval.random();
			y[j] = a*x[j]*x[j] + b*x[j] + c;
		}
		try {
			double[] abc = MathUtility.getQuadraticFactors(x[0],y[0],x[1],y[1],x[2],y[2]);
			Error.assertTrue(Utility.nearlyEqual(a,abc[0]));
			Error.assertTrue(Utility.nearlyEqual(b,abc[1]));
			Error.assertTrue(Utility.nearlyEqual(c,abc[2]));
		} catch (ArithmeticException e) {
			Error.assertTrue(Utility.twoNearlyEqual(x));
		}
	}
}
public void testDistance() {
	testDistance("1", 0,0,0, 1,0,0, 1);
	testDistance("2", -1,0,0, 1,0,0, 2);
	testDistance("3", 0,0,0, 0,3,4, 5);
}
private void testDistance(String name, double x1, double x2, double x3, double y1, double y2, double y3, double distance) {
	assertTrue(name, Utility.nearlyEqual(MathUtility.distance(new Vector3d(x1,x2,x3),new Vector3d(y1,y2,y3)), distance));
}
public void testMinus() {
	testMinus("1", 0,0,0, 1,2,3, -1,-2,-3);
}
private void testMinus(String name, double x1, double x2, double x3, double y1, double y2, double y3, double r1, double r2, double r3) {
	assertTrue(name, Utility.nearlyEqual(MathUtility.minus(new Vector3d(),new Vector3d(x1,x2,x3),(new Vector3d(y1,y2,y3))), new Vector3d(r1,r2,r3)));
}
public void testClosestLineSegmentPoints() {
	testClosestLineSegmentPoints("1", 0,0,0, 1,1,1, 2,2,2, 3,3,3, 1,1,1, 2,2,2);
	testClosestLineSegmentPoints("2", 0,0,0, 2,0,0, 1,1,1, 1,-1,1, 1,0,0, 1,0,1);
	testClosestLineSegmentPoints("3", 0,0,0, 1,1,1, 1,1,1, 3,3,3, 1,1,1, 1,1,1);
	testClosestLineSegmentPoints("4", 0,0,0, 2,0,0, 1,2,0, 1,4,0, 1,0,0, 1,2,0);
	testClosestLineSegmentPoints("5", 1,2,0, 1,4,0, 0,0,0, 2,0,0, 1,2,0, 1,0,0);
}
private void testClosestLineSegmentPoints(String name, 
	double a1x, double a1y, double a1z, 
	double a2x, double a2y, double a2z, 
	double b1x, double b1y, double b1z, 
	double b2x, double b2y, double b2z, 
	double r1x, double r1y, double r1z, 
	double r2x, double r2y, double r2z) { 

	Vector3d a1 = new Vector3d(a1x,a1y,a1z);
	Vector3d a2 = new Vector3d(a2x,a2y,a2z);
	Vector3d b1 = new Vector3d(b1x,b1y,b1z);
	Vector3d b2 = new Vector3d(b2x,b2y,b2z);
	Vector3d r1 = new Vector3d(r1x,r1y,r1z);
	Vector3d r2 = new Vector3d(r2x,r2y,r2z);
	
	testClosestLineSegmentPoints(name + "- 1",MathUtility.closestLineSegmentPoints(a1,a2,b1,b2),r1,r2);
	testClosestLineSegmentPoints(name + "- 2",MathUtility.closestLineSegmentPoints(a1,a2,b2,b1),r1,r2);
	testClosestLineSegmentPoints(name + "- 3",MathUtility.closestLineSegmentPoints(a2,a1,b1,b2),r1,r2);
	testClosestLineSegmentPoints(name + "- 4",MathUtility.closestLineSegmentPoints(a2,a1,b2,b1),r1,r2);
}
private void testClosestLineSegmentPoints(String name, Vector3d[] array, Vector3d r1, Vector3d r2) {
	assertTrue(name + array[0] + " " + array[1], Utility.nearlyEqual(array[0],r1) && Utility.nearlyEqual(array[1],r2));
}
}
