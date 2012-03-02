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

/** meant to replace a minimal subset of javax.vecmath.Quat4d to eliminate JavaGenes dependence on Java3d */
public class Quat4d implements java.io.Serializable {
public double x;
public double y;
public double z;
public double w;

public Quat4d() {}
public Quat4d(Quat4d q) {
	x = q.x;
	y = q.y;
	z = q.z;
	w = q.w;
}
public Quat4d(double x, double y, double z, double w) {
	this.x = x;
	this.y = y;
	this.z = z;
	this.w = w;
}
public void set(Quat4d q) {
	x = q.x;
	y = q.y;
	z = q.z;
	w = q.w;
}
public void mul(Quat4d q) {
	double xx = x;
	double yy = y;
	double zz = z;
	double ww = w;
	x = ww*q.x + xx*q.w + yy*q.z - zz*q.y;
	y = ww*q.y + yy*q.w + zz*q.x - xx*q.z;
	z = ww*q.z + zz*q.w + xx*q.y - yy*q.x;
	w = ww*q.w - xx*q.x - yy*q.y - zz*q.z;
}
}