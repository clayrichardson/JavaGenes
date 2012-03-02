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

/** meant to replace a minimal subset of javax.vecmath.Matrix3d to eliminate JavaGenes dependence on Java3d */
public class Matrix3d implements java.io.Serializable {
public double m00;
public double m01;
public double m02;
public double m10;
public double m11;
public double m12;
public double m20;
public double m21;
public double m22;

public Matrix3d() {}

/** set this matrix to the equivalent of quaternion q */
public void set(Quat4d q) {
	double qxx = 2*q.x*q.x;
	double qyy = 2*q.y*q.y;
	double qzz = 2*q.z*q.z;
	double qxy = 2*q.x*q.y;
	double qxz = 2*q.x*q.z;
	double qyz = 2*q.y*q.z;
	double qwx = 2*q.w*q.x;
	double qwy = 2*q.w*q.y;
	double qwz = 2*q.w*q.z;
	m00 = 1 - qyy - qzz;
	m01 = qxy - qwz;
	m02 = qxz + qwy;
	m10 = qxy + qwz;
	m11 = 1 - qxx - qzz;
	m12 = qyz - qwx;
	m20 = qxz - qwy;
	m21 = qyz + qwx;
	m22 = 1 - qxx - qyy;
}
public void transform(Vector3d v) {
	double xx = v.x;
	double yy = v.y;
	double zz = v.z;
	v.x = m00*xx + m01*yy + m02*zz;
	v.y = m10*xx + m11*yy + m12*zz;
	v.z = m20*xx + m21*yy + m22*zz;
}
}