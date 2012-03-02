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

import java.lang.ArithmeticException;

public class MathUtility {

/** @return a, b, c in ax^2 + bx + c = y given three sets of x,y */
static public double[] getQuadraticFactors(double x1, double y1, double x2, double y2, double x3, double y3) throws  ArithmeticException {
	if (Utility.nearlyEqual(x1,x2) || Utility.nearlyEqual(x2,x3) || Utility.nearlyEqual(x1,x3))
		throw new ArithmeticException("attempted divide by zero");
	double a = (1/(x3 - x2)) * (((y3-y1)/(x3-x1)) - ((y2-y1)/(x2-x1)));
	double b = ((y2-y1)/(x2-x1)) - (a * (x2+x1));
	double c = y1 - (a*x1*x1) - (b*x1); 
	
	double[] r = {a,b,c};
	return r;
}
static public double getQuadraticExtremumX(double[] abc)  throws  ArithmeticException {
	Error.assertTrue(abc.length >= 2); // only actually need a and b
	if (Utility.nearlyEqual(abc[0],0))
		throw new ArithmeticException("attempted divide by zero");
	return -abc[1]/(2*abc[0]);
}

static public double distance(Vector3d a, Vector3d b) {
	return Math.sqrt((a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y) + (a.z-b.z)*(a.z-b.z));
}
/** @arg result must not be either of the other two */
static public Vector3d minus(Vector3d result, Vector3d first, Vector3d second) {
	Error.assertTrue(result != first);
	Error.assertTrue(result != second);
	result.negate(second);
	result.add(first);
	return result;
}
static protected Vector3d[] makeArray(Vector3d a, Vector3d b) {
	Vector3d[] array = new Vector3d[2];
	array[0] = new Vector3d(a);
	array[1] = new Vector3d(b);
	return array;
}
static public double distanceBetweenLineSegments (Vector3d a1,Vector3d a2,Vector3d b1, Vector3d b2) {
	Vector3d[] point = closestLineSegmentPoints(a1,a2,b1,b2);
	return distance(point[0],point[1]);
}

// Taken from ODE (many of the comments in the code are the original ODE source)
// given two line segments A and B with endpoints a1-a2 and b1-b2, return the
// points on A and B that are closest to each other (in cp1 and cp2).
// in the case of parallel lines where there are multiple solutions, a
// solution involving the endpoint of at least one line will be returned.
// this will work correctly for zero length lines, e.g. if a1==a2 and/or
// b1==b2.
//
// the algorithm works by applying the voronoi clipping rule to the features
// of the line segments. the three features of each line segment are the two
// endpoints and the line between them. the voronoi clipping rule states that,
// for feature X on line A and feature Y on line B, the closest points PA and
// PB between X and Y are globally the closest points if PA is in V(Y) and
// PB is in V(X), where V(X) is the voronoi region of X.
static public Vector3d[] closestLineSegmentPoints (Vector3d a1,Vector3d a2,Vector3d b1, Vector3d b2) {
	Vector3d cp1 = new Vector3d();
	Vector3d cp2 = new Vector3d();
	Vector3d a1a2 = new Vector3d(); 
	Vector3d b1b2 = new Vector3d(); 
	Vector3d a1b1 = new Vector3d(); 
	Vector3d a1b2 = new Vector3d(); 
	Vector3d a2b1 = new Vector3d(); 
	Vector3d a2b2 = new Vector3d(); 
	Vector3d n = new Vector3d(); 
	Vector3d scaled = new Vector3d(); 
	Vector3d negative = new Vector3d(); 

	double la;
	double lb;
	double k;
	double da1;
	double da2;
	double da3;
	double da4;
	double db1;
	double db2;
	double db3;
	double db4;
	double det;

	//#define SET2(a,b) a[0]=b[0]; a[1]=b[1]; a[2]=b[2];
	//#define SET3(a,b,op,c) a[0]=b[0] op c[0]; a[1]=b[1] op c[1]; a[2]=b[2] op c[2];

	// check vertex-vertex features
	minus(a1a2,a2,a1); //SET3 (a1a2,a2,-,a1);
	minus(b1b2,b2,b1); //SET3 (b1b2,b2,-,b1);
	minus(a1b1,b1,a1); //SET3 (a1b1,b1,-,a1);
	da1 = a1a2.dot(a1b1); //dDOT(a1a2,a1b1);
	db1 = b1b2.dot(a1b1); //db1 = dDOT(b1b2,a1b1);
	if (da1 <= 0 && db1 >= 0)
		return makeArray(a1,b1);

	minus(a1b2,b2,a1); //SET3 (a1b2,b2,-,a1);
	da2 = a1a2.dot(a1b2); //da2 = dDOT(a1a2,a1b2);
	db2 = b1b2.dot(a1b2); //db2 = dDOT(b1b2,a1b2);
	if (da2 <= 0 && db2 <= 0)
		return makeArray(a1,b2);

	minus(a2b1,b1,a2); //SET3 (a2b1,b1,-,a2);
	da3 = a1a2.dot(a2b1); //da3 = dDOT(a1a2,a2b1);
	db3 = b1b2.dot(a2b1); //db3 = dDOT(b1b2,a2b1);
	if (da3 >= 0 && db3 >= 0) 
		return makeArray(a2,b1);

	minus(a2b2,b2,a2); //SET3 (a2b2,b2,-,a2);
	da4 = a1a2.dot(a2b2); //da4 = dDOT(a1a2,a2b2);
	db4 = b1b2.dot(a2b2); //db4 = dDOT(b1b2,a2b2);
	if (da4 >= 0 && db4 <= 0)
		return makeArray(a2,b2);

	// check edge-vertex features.
	// if one or both of the lines has zero length, we will never get to here,
	// so we do not have to worry about the following divisions by zero.

	la = a1a2.dot(a1a2); //la = dDOT(a1a2,a1a2);
	if (da1 >= 0 && da3 <= 0) {
		k = da1 / la;
		scaled.scale(k,a1a2);
		minus(n,a1b1,scaled); //SET3 (n,a1b1,-,k*a1a2);
		if (b1b2.dot(n) >= 0) {// if (dDOT(b1b2,n) >= 0) {
			cp1.add(a1,scaled); //SET3 (cp1,a1,+,k*a1a2);
			cp2.set(b1); //SET2 (cp2,b1);
			return makeArray(cp1,cp2);
		}
	}

	if (da2 >= 0 && da4 <= 0) {
		k = da2 / la;
		scaled.scale(k,a1a2);
		minus(n,a1b2,scaled); //SET3 (n,a1b2,-,k*a1a2);
		if (b1b2.dot(n) <= 0) { //if (dDOT(b1b2,n) <= 0) {
			cp1.add(a1,scaled); //SET3 (cp1,a1,+,k*a1a2);
			cp2.set(b2); //SET2 (cp2,b2);
			return makeArray(cp1,cp2);
			}
	}

	lb = b1b2.dot(b1b2); //lb = dDOT(b1b2,b1b2);
	if (db1 <= 0 && db2 >= 0) {
		k = -db1 / lb;
		scaled.scale(k,b1b2);
		negative.negate(a1b1);
		minus(n,negative,scaled); //SET3 (n,-a1b1,-,k*b1b2);
		if (a1a2.dot(n) >= 0) { //if (dDOT(a1a2,n) >= 0) {
			cp1.set(a1); //SET2 (cp1,a1);
			cp2.add(b1,scaled); //SET3 (cp2,b1,+,k*b1b2);
			return makeArray(cp1,cp2);
		}
	}

	if (db3 <= 0 && db4 >= 0) {
		k = -db3 / lb;
		scaled.scale(k,b1b2);
		negative.negate(a2b1);
		minus(n,negative,scaled); //SET3 (n,-a2b1,-,k*b1b2);
		if (a1a2.dot(n) <= 0) { //if (dDOT(a1a2,n) <= 0) {
			cp1.set(a2); //SET2 (cp1,a2);
			cp2.add(b1,scaled); //SET3 (cp2,b1,+,k*b1b2);
			return makeArray(cp1,cp2);
		}
	}

	// it must be edge-edge

	k = a1a2.dot(b1b2); //k = dDOT(a1a2,b1b2);
	det = la*lb - k*k;
	if (det <= 0) // this should never happen, but just in case...
		makeArray(a1,b1);
	det = 1.0/det; //det = dRecip (det);
	double alpha = (lb*da1 -  k*db1) * det;
	double beta  = ( k*da1 - la*db1) * det;
	//SET3 (cp1,a1,+,alpha*a1a2);
	scaled.scale(alpha,a1a2);
	cp1.add(a1,scaled);
	//SET3 (cp2,b1,+,beta*b1b2);
	scaled.scale(beta,b1b2);
	cp2.add(b1,scaled);
	return makeArray(cp1,cp2);
}

}