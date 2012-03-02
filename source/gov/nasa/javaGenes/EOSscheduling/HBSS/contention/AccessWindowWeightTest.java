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
package gov.nasa.javaGenes.EOSscheduling.HBSS.contention;

import junit.framework.TestCase;
import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.EOSscheduling.*;
import java.util.Vector;

public class AccessWindowWeightTest extends TestCase {
private Sensor sensor;
private    AccessWindowWeight aw1;
private    AccessWindowWeight aw2;
private    AccessWindowWeight aw3;
private    AccessWindowWeight aw4;

private static final int TAKEIMAGE_TIME = 2;
private static final int SLEW_RATE = 1;

public static String time[] = {
    "1 Jan 2002 00:00:00.00",
    "1 Jan 2002 00:00:50.00",
};

public AccessWindowWeightTest(String name) {super(name);}

public void setUp() {
    Horizon horizon = new Horizon(time[0],time[1]);
    Satellite satellite = new Satellite("satellite");
    satellite.setSSR(10,horizon,1000000000);
    SensorType sensorType1 = new SensorType("sensorType");
    sensor = new Sensor(satellite,sensorType1,horizon,TAKEIMAGE_TIME);
    satellite.addSensor(sensor);
    SlewMotor slewMotor = new SlewMotor(SLEW_RATE,-100,100,horizon,TAKEIMAGE_TIME);
    sensor.setSlewMotor(slewMotor);
    satellite.addSlewMotor(slewMotor);
    slewMotor.addSensor(sensor);
    aw1 = createAW(9,0);
    aw2 = createAW(10,0);
    aw3 = createAW(11,0);
    aw4 = createAW(13,2);
}
public void testCreateSensorAvailContenders1() {
    AccessWindowWeight[] a = {aw1,aw2,aw3};
    makeContenders(a);
    check(aw1,1,aw2);
    check(aw2,2,aw1,aw3);
    check(aw3,1,aw2);
}
public void testCreateSensorAvailContenders2() {
    AccessWindowWeight[] a1 = {aw1,aw2,aw3,aw4};
    makeContenders(a1);
    check(aw1,1,aw2);
    check(aw2,3,aw1,aw3,aw4);
    check(aw3,2,aw2,aw4);
    check(aw4,2,aw2,aw3);
}
public void check(AccessWindowWeight me, int numberOfContenders, AccessWindowWeight aContender) {
    SensorAvailContenders contenders = me.getSensorAvailContenders();
    Error.assertEqual(numberOfContenders,contenders.getCurrentSize());
    Error.assertTrue(contenders.currentlyContains(aContender));
}
public void check(AccessWindowWeight me, int numberOfContenders, AccessWindowWeight aw1, AccessWindowWeight aw2) {
    check(me, numberOfContenders, aw1);
    SensorAvailContenders contenders = me.getSensorAvailContenders();
    Error.assertTrue(contenders.currentlyContains(aw2));    
}
public void check(AccessWindowWeight me, int numberOfContenders, AccessWindowWeight aw1, AccessWindowWeight aw2, AccessWindowWeight aw3) {
    check(me, numberOfContenders, aw1, aw3);
    SensorAvailContenders contenders = me.getSensorAvailContenders();
    Error.assertTrue(contenders.currentlyContains(aw3));    
}
public void makeContenders(AccessWindowWeight[] a) {
    Vector v = new Vector();
    for(int i = 0; i < a.length; i++)
        v.add(a[i]);
    for(int i = 0; i < a.length; i++)
        a[i].createSensorAvailContenders(v,i);
}
    
    
public AccessWindowWeight createAW(int startTime, double pointing) {
    AccessWindow aw = new AccessWindow(startTime,startTime+TAKEIMAGE_TIME,sensor);
    aw.setSlewRequirement(new CrossTrackSlew(pointing));
    return new AccessWindowWeight(new TaskWeight(0),aw,1,1);
}
}
