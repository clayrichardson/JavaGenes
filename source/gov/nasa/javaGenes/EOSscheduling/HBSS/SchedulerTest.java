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
package gov.nasa.javaGenes.EOSscheduling.HBSS;

import junit.framework.TestCase;
import gov.nasa.alsUtility.Utility;
import gov.nasa.javaGenes.EOSscheduling.Task;
import gov.nasa.javaGenes.EOSscheduling.SensorType;
import gov.nasa.javaGenes.EOSscheduling.TakeImage;
import gov.nasa.javaGenes.EOSscheduling.AccessWindow;
import gov.nasa.javaGenes.EOSscheduling.CrossTrackSlew;
import gov.nasa.javaGenes.EOSscheduling.EOSModel;
import gov.nasa.javaGenes.EOSscheduling.Satellite;
import gov.nasa.javaGenes.EOSscheduling.Sensor;
import gov.nasa.javaGenes.EOSscheduling.SlewMotor;
import gov.nasa.javaGenes.EOSscheduling.Horizon;
import gov.nasa.javaGenes.EOSscheduling.EOSschedulingEvolvable;

public class SchedulerTest extends TestCase {
private String time[] = {
    "1 Jan 2002 00:00:00.00",
    "1 Jan 2002 00:00:10.00",
    "2 Jan 2002 00:00:00.00",
    "3 Jan 2002 00:00:00.00"
};
private final static int observationTime = 10;
private final static int SSRsize = 2;

private Task t1;
private Task t2;
private Task t3;
private TaskWeight tw1;
private TaskWeight tw2;
private TaskWeight tw3;
private EOSModel model;
private TaskList taskList;
private Satellite satellite;
private SensorType sensorType;
private Sensor sensor;
private SlewMotor slewMotor;
private Scheduler scheduler;
private EOSschedulingEvolvable evolvable;

public SchedulerTest(String name) {super(name);}

public void testInsertIntoTimelines() {
    assertTrue("1",scheduler.insertIntoTimelines(t1.getAccessWindow(0),t1));
    assertTrue("2",!scheduler.insertIntoTimelines(t2.getAccessWindow(0),t2));
    assertTrue("3",scheduler.insertIntoTimelines(t2.getAccessWindow(1),t2));
    assertTrue("4",!scheduler.insertIntoTimelines(t3.getAccessWindow(2),t3));
}
public void testScheduleTask() {
    assertTrue("1", taskList.currentSize() == 3);

    scheduler.scheduleTask(evolvable,tw1);
    assertTrue("2", taskList.currentSize() == 2);
    assertTrue("3", !taskList.currentlyContains(tw1));
    assertTrue("4", taskList.currentlyContains(tw2));
    assertTrue("5", taskList.currentlyContains(tw3));

    scheduler.scheduleTask(evolvable,tw3);
    assertTrue("12", taskList.currentSize() == 1);
    assertTrue("13", !taskList.currentlyContains(tw1));
    assertTrue("14", taskList.currentlyContains(tw2));
    assertTrue("15", !taskList.currentlyContains(tw3));

    scheduler.scheduleTask(evolvable,tw2);
    assertTrue("22", taskList.currentSize() == 0);
    assertTrue("23", !taskList.currentlyContains(tw1));
    assertTrue("24", !taskList.currentlyContains(tw2));
    assertTrue("25", !taskList.currentlyContains(tw3));
    
    assertTrue("31", evolvable.isTaskScheduled(0));
    assertTrue("41", !evolvable.isTaskScheduled(1));
    assertTrue("51", evolvable.isTaskScheduled(2));
    
    assertTrue("61", evolvable.getStartTime(0) == 100);
    assertTrue("62", evolvable.getSensorNumber(0) == sensor.getNumber());
    assertTrue("63", ((CrossTrackSlew)evolvable.getSlewRequirement(0)).getSlewPoint() == 0);
}
public void testCreateSchedule() {
    scheduler.createSchedule(evolvable);
    assertTrue("22", taskList.currentSize() == 0);
    assertTrue("23", !taskList.currentlyContains(tw1));
    assertTrue("24", !taskList.currentlyContains(tw2));
    assertTrue("25", !taskList.currentlyContains(tw3));  
}
public void setUp() {
    Horizon horizon = new Horizon(time[0],time[3]);
    model = new EOSModel(horizon);
    satellite = new Satellite("S");
    satellite.setSSR(SSRsize,horizon,observationTime);
    model.addSatellite(satellite);
    sensorType = new SensorType("foo");
    sensor = new Sensor(satellite,sensorType,horizon,observationTime);
    slewMotor = new SlewMotor(1, -50, 50, horizon,observationTime);
    sensor.setSlewMotor(slewMotor);
    satellite.addSensor(sensor);
    t1 = makeTask(1,1,1);
    t2 = makeTask(1,2,1);
    t3 = makeTask(1,3,1);
    model.addTask(t1);
    model.addTask(t2);
    model.addTask(t3);
    scheduler = new Scheduler(model,1,1,1);
    taskList = scheduler.getTaskList();
    tw1 = (TaskWeight)taskList.getWeight(0);
    tw2 = (TaskWeight)taskList.getWeight(1);
    tw3 = (TaskWeight)taskList.getWeight(2);
    
    evolvable = new EOSschedulingEvolvable(3);
}
public Task makeTask(double priority,int numberOfAccessWindows, int SSRuse) {
    Task task = new TakeImage(observationTime, sensorType, SSRuse);
    task.setPriority(priority);
    for(int i = 0; i < numberOfAccessWindows; i++) {
        AccessWindow a = new AccessWindow((i+1)*100, (i+1)*100+observationTime);
        a.setSlewRequirement(new CrossTrackSlew(i));
        a.setSensor(sensor);
        task.addAccessWindow(a);
    }
    task.hasAllAccessWindowsNow();
    return task;
}

}

