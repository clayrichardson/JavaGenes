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

import java.util.Date;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.Utility;

/**
default timestep is seconds
*/
public class Horizon extends TimeInterval {
public static final int NOT_SCHEDULED = Scheduler.NOT_SCHEDULED;
protected Date startDate;
protected Date endDate;
protected long startJavaEpoch; // Java time conventions
protected long endJavaEpoch;
protected int start = defaultStart(); // most work done in int for speed
protected int end = start + 1; // because of int, no more than 2 billion seconds or so, about 63 years
protected int duration; // maintained for speed

static private int millisecondsPerTimestep = 1000;
/**
changes all time calculations.  Only use before creating any Horizons.
*/
static public void setMillisecondsPerTimestep(int inMillisecondsPerTimestep) {
    millisecondsPerTimestep = inMillisecondsPerTimestep;
}
static public int defaultStart() {return NOT_SCHEDULED + 1;}
/**
@return in seconds by default, use setMillisecondsPerTimestep to change
*/
static public int getDurationOf(String startAsStkTimeString, String endAsStkTimeString) {
    Date start = Utility.stkDateString2Date(startAsStkTimeString);
    Date end   = Utility.stkDateString2Date(endAsStkTimeString);
    return (int)((end.getTime() - start.getTime()) / millisecondsPerTimestep);
}
protected Horizon(){} // used for testing
public Horizon(String startAsStkTimeString, String endAsStkTimeString) {
    startDate = Utility.stkDateString2Date(startAsStkTimeString);
    endDate   = Utility.stkDateString2Date(endAsStkTimeString);
    Error.assertTrue(endDate.after(startDate), endAsStkTimeString + " not after " + startAsStkTimeString);
    setDerivedValues();
}
public boolean theSame(Horizon other) {return start == other.start && end == other.end;}
private long date2timesteps(Date date) {
    return date.getTime() / millisecondsPerTimestep;
}
protected void setDerivedValues() {
    startJavaEpoch = date2timesteps(startDate);
    endJavaEpoch = date2timesteps(endDate);
    Error.assertTrue(endJavaEpoch > startJavaEpoch);
    start = defaultStart();
    duration = (int)(endJavaEpoch - startJavaEpoch);
    end = start + duration;
}
public int getIntegerTimeAt(Date date) {
    long now = date2timesteps(date);
    Error.assertTrue(startJavaEpoch <= now && now <= endJavaEpoch);
    return (int)(start + (now - startJavaEpoch));
}
public boolean includes(AccessWindow window) {
    return start <= window.getStart() && window.getEnd() <= end;
}
public boolean includes(Horizon horizon) {
    return     (startDate.before(horizon.startDate) || startDate.equals(horizon.startDate))
            && (endDate.after(horizon.endDate)      || endDate.equals(horizon.endDate));
}
public int getStart() {return start;}
public int getEnd() {return end;}
public int getDuration() {return duration;}
public boolean isValid() {return end > start;}
public Horizon copy() {
    Horizon r = new Horizon();
    r.startDate = new Date(startDate.getTime());
    r.endDate = new Date(endDate.getTime());
    r.setDerivedValues();
    return r;
}
}
