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

public class Brick3d implements java.io.Serializable {
protected DoubleInterval[] intervals = new DoubleInterval[3];

public Brick3d(Brick3d other) {
	this(other.getInterval(0),other.getInterval(1),other.getInterval(2));
}
public Brick3d(Vector3d first, Vector3d second) {
	this(first.x, second.x, first.y, second.y, first.z, second.z);
}
public Brick3d(double x1, double x2, double y1, double y2, double z1, double z2) {
	this(new DoubleInterval(x1,x2), new DoubleInterval(y1,y2), new DoubleInterval(z1,z2));
}
public Brick3d(DoubleInterval x, DoubleInterval y, DoubleInterval z) {
	intervals[0] = new DoubleInterval(x);
	intervals[1] = new DoubleInterval(y);
	intervals[2] = new DoubleInterval(z);
}
public DoubleInterval getInterval(int i ) {
	return intervals[i];
}
public Vector3d getBottomCornerPoint() {
	return new Vector3d(getInterval(0).low(), getInterval(1).low(),getInterval(2).low());
}
public Vector3d getTopCornerPoint() {
	return new Vector3d(getInterval(0).high(), getInterval(1).high(),getInterval(2).high());
}
public void expandByAddition(double expandBy) {
	for(int i = 0; i < intervals.length; i++) 
		intervals[i].expandByAddition(expandBy);
		
}
public void expandToInclude(Vector3d point) {
	intervals[0].extendIfNecessary(point.x);
	intervals[1].extendIfNecessary(point.y);
	intervals[2].extendIfNecessary(point.z);
}
public boolean isInside(Vector3d v) {
	return intervals[0].isBetween(v.x) && intervals[1].isBetween(v.y) && intervals[2].isBetween(v.z);
}
public double getSideLength(int i) {
	return intervals[i].interval();
}
public boolean nearlyEqual(Brick3d other) {
	return
		getInterval(0).nearlyEqual(other.getInterval(0)) &&
		getInterval(1).nearlyEqual(other.getInterval(1)) &&
		getInterval(2).nearlyEqual(other.getInterval(2));
}
/** in tsd (tab separated data) format */
public String toString() { 
	return 
		intervals[0].low() + "\t" + intervals[0].high() + "\t" +
		intervals[1].low() + "\t" + intervals[1].high() + "\t" +
		intervals[2].low() + "\t" + intervals[2].high();
}
}

