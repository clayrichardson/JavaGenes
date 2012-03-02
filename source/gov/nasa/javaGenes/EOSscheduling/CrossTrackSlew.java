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
//  Created by Al Globus on Fri Sep 06 2002.
package gov.nasa.javaGenes.EOSscheduling;

import gov.nasa.alsUtility.Utility;
import java.lang.Math;
import gov.nasa.alsUtility.Error;

public class CrossTrackSlew extends SlewRequirement {
protected double angleInDegrees = 0;

public CrossTrackSlew(double inAngleInDegrees) {
    angleInDegrees = inAngleInDegrees;
}
public CrossTrackSlew(PointingRequirement p) {
    angleInDegrees = calculateCrossTrackSlew(p);
}
/**
When the azimuth is far from normal to the satellite track this gives somewhat inaccurate.
See crossTrackPointingDrawing.ppt (MS PowerPoint) for the math behind this calculation.
@p is at some time close to when the target is on the normal to the satellite track
@return the cross track pointing angle in degrees at
the point when the target is on the normal to the satellite track
*/
public static double calculateCrossTrackSlew(PointingRequirement p) { 
    Error.assertTrue(p.getAzimuth() >= 0);
    final double a = Math.sin(Math.toRadians(Math.abs(p.getElevation()))) * p.getRange();
    final double f = Math.sqrt(p.getRange()*p.getRange() - a*a);
    final double b = Math.sin(Math.toRadians(p.getAzimuth())) * f;
    final double c = Math.sqrt(a*a + b*b);
    double aSLASHc = a/c;
    if (aSLASHc > 1)
        aSLASHc = 1;
    if (aSLASHc < -1)
        aSLASHc = -1;
    final double crossTrackPoint = Math.asin(aSLASHc);
    final double sign = p.getAzimuth() >= 180 ? -1 : 1; // direction is arbitrary
    return sign * (90 - Math.abs(Math.toDegrees(crossTrackPoint)));
}


public double getSlewPoint() {return angleInDegrees;}
public int numberOfParameters() {return 1;};
public double getParameter(int index) {return angleInDegrees;}
/**
slow
*/
public double[] getParameters() {
    double[] r = {angleInDegrees};
    return r;
}
    
}
