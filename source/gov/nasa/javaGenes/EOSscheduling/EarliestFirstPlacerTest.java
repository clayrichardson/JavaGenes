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
//  Created by Al Globus on Mon Sep 30 2002.
package gov.nasa.javaGenes.EOSscheduling;

import junit.framework.TestCase;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.DoubleInterval;

public class EarliestFirstPlacerTest extends TestCase {

public EarliestFirstPlacerTest(String name) {super(name);}
private String time[] = {
    "1 Jan 2002 00:00:00.00",
    "1 Jan 2002 00:00:20.00",
    "2 Jan 2002 00:00:00.00",
    "3 Jan 2002 00:00:00.00"
};
private Sensor sensor;
private SensorType sensorType;
private EarliestFirstPlacer placer;
private TaskPlacementData pd;
private Horizon horizon = new Horizon(time[0],time[1]);
private final int typicalTimeBewteenTasks = 2;

protected void setUp() {
    sensorType = new SensorType("st");
    sensor = new Sensor(new Satellite("sat"),sensorType,horizon,typicalTimeBewteenTasks);
    sensor.setSlewMotor(new SlewMotor(1,-50,50,horizon,typicalTimeBewteenTasks));
    placer = new EarliestFirstPlacer();
    pd = new TaskPlacementData();
}
private Task createTask(int start,int end,int duration,double slew) {
    return createTask(start,end,duration,slew,0);
}
private Task createTask(int start,int end,int duration,double slew,int SSRuse) {
    AccessWindow w = new AccessWindow(start,end,sensor);
    w.setSlewRequirement(new CrossTrackSlew(slew));
    Task task = new TakeImage(duration,sensorType,SSRuse);
    task.addAccessWindow(w);
    task.hasAllAccessWindowsNow();
    return task;
}
public void testPlaceInTimelinesSSR() {
    Satellite sat = sensor.getSatellite();
    int SSRcapacity = 10;
    sat.setSSR(SSRcapacity,horizon,typicalTimeBewteenTasks);
    Task task = createTask(0,10,3,5.0,1);
    assertTrue("1",placer.placeInTimelines(pd,task));
    assertTrue("1p",pd.getStartTime() == 0);
    assertTrue("2",placer.placeInTimelines(pd,task));
    assertTrue("2p",pd.getStartTime() == 3);
    task = createTask(5,10,5,5.0,1);
    assertTrue("3",!placer.placeInTimelines(pd,task));
    task = createTask(5,20,3,7.0,14);
    assertTrue("4",!placer.placeInTimelines(pd,task));
    task = createTask(5,20,3,7.0,7);
    assertTrue("5",placer.placeInTimelines(pd,task));
    assertTrue("5p",pd.getStartTime() == 8);
}
 public void testPlaceInTimelines() {
    Task task = createTask(0,10,3,5.0);
    assertTrue("1",placer.placeInTimelines(pd,task));
    assertTrue("1p",pd.getStartTime() == 0);
    assertTrue("2",placer.placeInTimelines(pd,task));
    assertTrue("2p",pd.getStartTime() == 3);
    task = createTask(5,10,5,5.0);
    assertTrue("3",!placer.placeInTimelines(pd,task));
    task = createTask(5,20,3,7.0);
    assertTrue("4",placer.placeInTimelines(pd,task));
    assertTrue("4p",pd.getStartTime() == 8);
    
    AccessWindow w = new AccessWindow(6,12,sensor);
    w.setSlewRequirement(new CrossTrackSlew(7.0));
    task = new Task(2);
    task.addAccessWindow(w);
    w = new AccessWindow(18,20,sensor);
    w.setSlewRequirement(new CrossTrackSlew(7.0));
    task.addAccessWindow(w);
    task.hasAllAccessWindowsNow();
    assertTrue("5",placer.placeInTimelines(pd,task));
    assertTrue("5p",pd.getStartTime() == 18);
    
    task = createTask(0,20,2,10);
    assertTrue("6",!placer.placeInTimelines(pd,task));
    
    task = createTask(0,20,1,10);
    assertTrue("7",placer.placeInTimelines(pd,task));
    assertTrue("7p",pd.getStartTime() == 14); 
}
public void testPlaceInTimelinesBig() {
    Horizon h = new Horizon(time[0],time[3]);
    int typicalTime = 200;
    sensor = new Sensor(new Satellite("sat"),new SensorType("st"),h,typicalTime);
    sensor.setSlewMotor(new SlewMotor(1,-60,60,h,typicalTime));
    int count = 50;
    final int maximum = 2 * 23 * 60 * 60; // a few less seconds than in two days
    for(int i = 0; i < count; i++) {
        int start = 0;
        int end = maximum;
        int duration = 100;
        double slew = -50;
        if (i%2 == 0)
            slew = 50;
        Task task = createTask(start,end,duration,slew);
        assertTrue(i+"",placer.placeInTimelines(pd,task));
        assertTrue(i + "p", pd.getStartTime() == i * typicalTime);
    }
    sensor.assertOk();
}
public void testPlaceInTimelinesRandomized() {
    randomized(true);
    randomized(false);
}
protected void randomized(boolean checkSensorEveryTime) {
    Horizon h = new Horizon(time[0],time[3]);
    int typicalTime = 24;
    sensor = new Sensor(new Satellite("sat"),new SensorType("st"),h,typicalTime);
    sensor.setSlewMotor(new SlewMotor(1,-50,50,h,typicalTime));
    int count = checkSensorEveryTime ? 10 : 10000;
    final int maximum = 2 * 23 * 60 * 60; // a few less seconds than in two days
    for(int i = 0; i < count; i++) {
        int start = RandomNumber.getIndex(maximum-200);
        int end = start + RandomNumber.getIndex(maximum-start);
        int duration = Math.max(1,RandomNumber.getIndex(Math.max(1,end - start)));
        double slew = new DoubleInterval(-49,49).random();
        Task task = createTask(start,end,duration,slew);
        placer.placeInTimelines(pd,task);
        if (checkSensorEveryTime)
            sensor.assertOk();
    }
    sensor.assertOk();
}
}
