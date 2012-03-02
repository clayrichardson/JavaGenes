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
//  Created by Al Globus on Wed Jul 03 2002.
package gov.nasa.javaGenes.EOSscheduling;

import java.util.Vector;
import gov.nasa.alsUtility.Error;
import java.io.PrintWriter;

public class AccessWindow implements java.io.Serializable {
protected Sensor sensor;
private static int DEFAULT_START = -1;
protected int start = DEFAULT_START;
protected int end = -2;
protected int duration; // calculated from start and end. Cached here for speed
protected Vector pointingRequirements = new Vector();
private boolean debug = Debug.debug;
private SlewRequirement slewRequirement = new SlewNone();
protected int number = -1;

public AccessWindow() {
    this(DEFAULT_START,-2,null);
}
public AccessWindow(int inStart, int inEnd) {
    this(inStart,inEnd,null);
}
public AccessWindow(int inStart, int inEnd, Sensor inSensor) {
    if (debug)
        Error.assertTrue(inStart == DEFAULT_START || inStart <= inEnd);
    start = inStart;
    end = inEnd;
    sensor = inSensor;
    calculateDuration();
}
public int getNumber() {return number;}
public void setNumber(int inNumber) {number = inNumber;}
/**
test for sensor, slewing and time compatibility. Assumes that
the task duration is equal to the duration of the AccessWindow
*/
public boolean couldBeScheduledWith(AccessWindow other) {
    Error.assertTrue(this != other);
    if (getSensor() != other.getSensor())
        return true;
    if (overlapInTimeWith(other))
        return false;
    if (other.getStart() >= getEnd()
        && !getSensor().slewTimeAdequate(other.getStart() - getEnd(),other.getSlewRequirement(), getSlewRequirement()))
        return false;
    if (other.getEnd() <= getStart()
        && !getSensor().slewTimeAdequate(getStart() - other.getEnd(),other.getSlewRequirement(), getSlewRequirement()))
        return false;
    return true;
}
public boolean overlapInTimeWith(AccessWindow other) {
    return
        ((getStart() <= other.getStart() && other.getStart() < getEnd()) 
        || (getStart() < other.getEnd() && other.getEnd() <= getEnd())
        || (other.getStart() <= getStart() && getEnd() <= other.getEnd()) 
        || (getStart() <= other.getStart() && other.getEnd() <= getEnd()))
        && other.getDuration() > 0 && getDuration() > 0;
}
public static String getReportHeader() {return "Task\tStart\tEnd\tDuration\tsensor\tslew";}
public void reportTo(int taskNumber,PrintWriter p) {
    p.print(taskNumber+"\t"+start+"\t"+end+"\t"+duration+"\t"+sensor.getNumber());
    if (slewRequirement != null)
        for(int i = 0; i < slewRequirement.numberOfParameters(); i++)
            p.print("\t" + slewRequirement.getParameter(i));
    p.println();
}
public Sensor getSensor() {return sensor;}
public int getStart() {return start;}
public int getEnd() {return end;}
public int getDuration() {return duration;}
public Satellite getSatellite() {
    return sensor.getSatellite();
}

public void setSensor(Sensor inSensor) {sensor = inSensor;}
public void setStart(int time) {
    start = time;
    calculateDuration();
}
public void setEnd(int time) {
    end = time;
    calculateDuration();
}
public int getSSRtime() {
    return getStart();
}
protected void calculateDuration() {duration = end - start;}
public void addPointingRequirement(PointingRequirement p ) { 
    pointingRequirements.addElement(p);
}
/**
@arg windowDuration shrink window be of exactly this length.  
Windows with a duration shorter than windowDuration will be unmodified and
Longer durations will be shrunk to this length with the center staying the same (or as close as possible). 
*/
public void shrinkAroundMiddle(int windowDuration) {
    if (duration <= windowDuration)
        return;
    int shrinkBy = (duration - windowDuration) / 2;
    start += shrinkBy;
    end -= shrinkBy;
    calculateDuration();
    if (duration < windowDuration && shrinkBy > 0) { // can happen due to integer arithmetic
        end++;
        calculateDuration();
    }
    if (duration > windowDuration) { // can happen due to integer artihmetic
        start++;
        calculateDuration();
    }
    if (debug) Error.assertTrue(getDuration() == windowDuration);
}
public void setSlewingToLargestPointing(Sensor sensor) {
    Error.assertTrue(pointingRequirements != null && pointingRequirements.size() > 0);
    PointingRequirement minElevationPointing = null;
    double minElevation = 1; // elevations are always < 0
    double minElevationAzimuth = 0;
    for(int i = 0; i < pointingRequirements.size(); i++) {
        PointingRequirement p = (PointingRequirement)pointingRequirements.elementAt(i);
        if (p.getElevation() < minElevation) {
            minElevation = p.getElevation();
            minElevationPointing = p;
        }
    }
    Error.assertTrue(minElevationPointing != null);
    slewRequirement = new CrossTrackSlew(minElevationPointing);
}
public SlewRequirement getSlewRequirement() {return slewRequirement;}
public void setSlewRequirement(SlewRequirement inSlewRequirement) {slewRequirement = inSlewRequirement;}
public int getTimeAtMiddle() {return (int)Math.round(start + ((end - start)/2.0));}

public boolean isWithin(int time) {
    return start <= time && time <= end;
}
public void deletePointingData() {pointingRequirements = null;} // optimizing space
}
