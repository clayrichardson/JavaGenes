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
//  Created by Al Globus on Wed Sep 11 2002.
package gov.nasa.javaGenes.EOSscheduling;

import gov.nasa.alsUtility.Error;
import java.util.Vector;

// cross track slewing
public class SlewMotor extends Slewable {
protected double rate;
protected double minimum;
protected double maximum;
protected CrossTrackSlew minimumSlew;
protected CrossTrackSlew maximumSlew;
protected double slop = 0.01;
protected Vector sensors = new Vector();

public SlewMotor() {super();}
/**
@arg inRate n degrees per second
*/
public SlewMotor(double inRate, double inMinimum, double inMaximum,Horizon horizon,int typicalTakeImageTime) {
    super();
    rate = inRate;
    Error.assertTrue(rate != 0);
    minimum = inMinimum;
    maximum = inMaximum;
    Error.assertTrue(minimum <= maximum);
    minimumSlew = new CrossTrackSlew(minimum);
    maximumSlew = new CrossTrackSlew(maximum);
    slewTimeline = new SlewTimeline(horizon,typicalTakeImageTime);
}
public void addSensor(Sensor s) {
    Error.assertTrue(s != null);
    sensors.addElement(s);
}
public Sensor[] getSensors() {return (Sensor[])sensors.toArray(new Sensor[sensors.size()]);}
public void setSlop(double inSlop) {
    slop = inSlop;
}
public boolean isWithinLimits(SlewRequirement s) {
    double f = ((CrossTrackSlew)s).getSlewPoint();
    return minimum <= f && f <= maximum;
}
public int slewTime(SlewRequirement from, SlewRequirement to) {
    if (from instanceof SlewNone || to instanceof SlewNone)
        return 0;
    double f = ((CrossTrackSlew)from).getSlewPoint();
    double t = ((CrossTrackSlew)to).getSlewPoint();
    if (f == t)
        return 0;
    return (int)Math.round(Math.abs(f-t)/rate);
}
public int getMaxSlewTimeFrom(SlewRequirement slew) {
    return Math.max(slewTime(slew,minimumSlew),slewTime(slew,maximumSlew));
}
public double getMaxAbsoluteSlew() {
    return Math.max(Math.abs(minimum),Math.abs(maximum));
}
public boolean equivalent(SlewRequirement one, SlewRequirement other) {
    if (one instanceof SlewNone || other instanceof SlewNone)
        return true;
    double oneValue = ((CrossTrackSlew)one).getParameter(0);
    double otherValue = ((CrossTrackSlew)other).getParameter(0);
    return slop >= Math.abs(oneValue - otherValue);
}
}
