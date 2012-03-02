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
//  Created by Al Globus on Fri Jul 05 2002.

package gov.nasa.javaGenes.EOSscheduling;

import gov.nasa.alsUtility.Error;

public class Sensor implements java.io.Serializable {

/**
starts at 1 so arrays of sensor numbers automatically initialized to 0 will mean no sensor allocated.
*/
static private int nextNumber = 1; 
protected Satellite satellite;
protected SensorType sensorType;
protected int number;
protected AvailabilityTimeline availableTimeline;
protected SlewMotor slewMotor = new SlewMotorNone();
protected Horizon horizon;
protected int typicalTakeImageTime;

/**
must be called by all Sensor and subclass constructors to assign numbers
and maintain allSensor's list
*/
public Sensor(Satellite inSatellite,SensorType inSensorType,Horizon inHorizon,int inTypicalTakeImageTime) {
    satellite = inSatellite;
    sensorType = inSensorType;
    horizon = inHorizon.copy();
    typicalTakeImageTime = inTypicalTakeImageTime;
     
    availableTimeline = new AvailabilityTimeline(horizon,typicalTakeImageTime);    
    
    number = nextNumber;
    nextNumber++;
    setupOk();
}
public void setDutyCycles(DutyCycleConstraint[] dutyCycles) {availableTimeline.setDutyCycles(dutyCycles);}
public Satellite getSatellite() {return satellite;}
public void setSlewMotor(SlewMotor inSlewMotor) {
    slewMotor = inSlewMotor;
}
public double getMaxAbsoluteSlew() {
    return slewMotor.getMaxAbsoluteSlew();
}
public SlewMotor getSlewMotor() {return slewMotor;}
public int getMaxSlewTimeFrom(SlewRequirement slew) {
    if (slewMotor == null) return 0;
    return slewMotor.getMaxSlewTimeFrom(slew);
}
public boolean slewTimeAdequate(int time,SlewRequirement s1, SlewRequirement s2) {
    if (slewMotor == null) return true;
    return slewMotor.slewTime(s1,s2) <= time;
}
public int getNumber() {return number;}
public SensorType getSensorType() {return sensorType;}
public void assertOk() {
    setupOk();
    timelinesOk();
}
public void setupOk() {
    Error.assertNotNull(satellite);
    Error.assertNotNull(sensorType);
    Error.assertNotNull(availableTimeline);
    Error.assertNotNull(slewMotor);
    Error.assertTrue(number > 0);
}
public void timelinesOk() {
    availableTimeline.assertIsValid();
    getSlewTimeline().assertIsValid();
}
public AvailabilityTimeline getAvailabilityTimeline() {return availableTimeline;}
public SlewTimeline getSlewTimeline() {return slewMotor.getSlewTimeline();}
public SlewRequirement getSlewRequirement(PointingRequirement pointingRequirement) {
    return new CrossTrackSlew(pointingRequirement);
}
}
