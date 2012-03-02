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

import junit.framework.TestCase;
import gov.nasa.alsUtility.Utility;

public class SensorTest extends TestCase {
private Sensor sensor1;
private final static double SLEW_RATE = 0.5;
private String time[] = {
    "1 Jan 2002 00:00:00.00",
    "1 Jan 2002 00:00:10.00",
    "2 Jan 2002 00:00:00.00",
    "3 Jan 2002 00:00:00.00"
};
public SensorTest(String name) {super(name);}

public void setUp() {
    Horizon horizon = new Horizon(time[0],time[3]);
    Satellite satellite = new Satellite("satellite");
    SensorType sensorType1 = new SensorType("sensor");
    sensor1 = new Sensor(satellite,sensorType1,horizon,100);
    satellite.addSensor(sensor1);
    SlewMotor slewMotor = new SlewMotor(SLEW_RATE,-100,100,horizon,100);
    sensor1.setSlewMotor(slewMotor);
    satellite.addSlewMotor(slewMotor);
    slewMotor.addSensor(sensor1);
}

public void testSlewTimeAdequate() {
    SlewRequirement st1 = new CrossTrackSlew(0);
    SlewRequirement st2 = new CrossTrackSlew(10);
    assertTrue("1", sensor1.slewTimeAdequate(20,st1,st2));
    assertTrue("2", sensor1.slewTimeAdequate(20,st2,st1));
    assertTrue("3", sensor1.slewTimeAdequate(21,st1,st2));
    assertTrue("4",!sensor1.slewTimeAdequate(19,st1,st2));
    assertTrue("5",!sensor1.slewTimeAdequate(19,st2,st1));
}
}

