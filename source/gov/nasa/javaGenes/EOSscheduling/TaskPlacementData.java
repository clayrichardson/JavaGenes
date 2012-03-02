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
//  Created by Al Globus on Thu Jul 18 2002.
package gov.nasa.javaGenes.EOSscheduling;

/**
just holds data to simplify returning it.
*/
public class TaskPlacementData implements java.io.Serializable {
public static final int NO_WINDOW = -1;

private boolean scheduled = false;
private int startTime = -1;
protected int windowNumber = NO_WINDOW;
private int sensorNumber = -1;
private SlewRequirement slewRequirement;

public TaskPlacementData(){}
public int getStartTime() {return startTime;}
public void setStartTime(int inStartTime) {startTime = inStartTime;}
public int getSensorNumber() {return sensorNumber;}
public void setSensorNumber(int inSensorNumber) {sensorNumber = inSensorNumber;}
public SlewRequirement getSlewRequirement() {return slewRequirement;}
public void setSlewRequirement(SlewRequirement inSlewRequirement) {slewRequirement = inSlewRequirement;}
public int getWindowNumber() {return windowNumber;}
public void setWindowNumber(int index) {windowNumber = index;}
public boolean isScheduled() {return scheduled;}
public void setScheduled(boolean isScheduled) {scheduled = isScheduled;}
public void setFrom(TaskPlacementData other) {
    startTime = other.startTime;
    sensorNumber = other.sensorNumber;
    windowNumber = other.windowNumber;
    scheduled = other.scheduled;
}
public void setScheduledAtStartOf(AccessWindow a) {
    scheduled = true;
    startTime = a.getStart();
    windowNumber = a.getNumber();
    sensorNumber = a.getSensor().getNumber();
    slewRequirement = a.getSlewRequirement();
}

public boolean theSameAs(TaskPlacementData other) {
    return startTime == other.startTime
        && sensorNumber == other.sensorNumber
        && windowNumber == other.windowNumber;
}
public String toString() {
    return "scheduled= " + scheduled
        + " startTime= " + startTime
        + " windowNumber= " + windowNumber
        + " sensorNumber= " + sensorNumber
        + " slewRequirement= " + slewRequirement.getParameter(0); // BUG: only works for 1d slew
}
}
