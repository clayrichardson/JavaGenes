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

public class Brick3dWithSegments extends Brick3d {
double[][] segmentCoordinates = new double[3][];

/** NOTE: the segments are created here and never updated, even if you change the brick */
public Brick3dWithSegments(Brick3d corners, int[] numberOfSegments) {
	super(corners);
	Error.assertTrue(numberOfSegments.length == 3);
	for(int i = 0; i < numberOfSegments.length; i++)
		segmentCoordinates[i] = createSegmentCoordinates(corners.getInterval(i),numberOfSegments[i]);
}
protected double[] createSegmentCoordinates(DoubleInterval interval, int numberOfSegments) {
	Error.assertTrue(numberOfSegments > 0);
	double[] coordinates = new double[numberOfSegments+1];
	double segmentLength = interval.interval() / numberOfSegments;
	coordinates[0] = interval.low(); // to avoid any possibility of numerical problems
	for(int i = 1; i < coordinates.length-1; i++)
		coordinates[i] = interval.low() + i*segmentLength;
	coordinates[coordinates.length-1] = interval.high(); // to avoid any possibility of numerical problems
	return coordinates; 
}
public double[] getSegmentCoordinates(int i) {
	return segmentCoordinates[i];
}
public void expandByAddition(double expandBy) {
	Error.fatal("segments coordinates are never updated");
}
}
	
