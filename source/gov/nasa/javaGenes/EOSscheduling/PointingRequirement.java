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
package gov.nasa.javaGenes.EOSscheduling;

import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.Utility;

public class PointingRequirement implements java.io.Serializable {
protected int time;
protected double azimuth;
protected double elevation;
protected double range;

public PointingRequirement(int inTime, double azimuthInDegrees, double elevationInDegrees, double inRange) {
    time = inTime;
    azimuth = azimuthInDegrees;
    elevation = elevationInDegrees;
    range = inRange;
}
public double getTime() {return time;}
public double getAzimuth() {return azimuth;}
public double getElevation() {return elevation;}
public double getRange() {return range;}
public boolean equivalent(PointingRequirement p) {
     return time == p.time 
            && Utility.nearlyEqual(azimuth,p.getAzimuth()) 
            && Utility.nearlyEqual(elevation,p.getElevation())
            && Utility.nearlyEqual(range,p.getRange());
}
}
