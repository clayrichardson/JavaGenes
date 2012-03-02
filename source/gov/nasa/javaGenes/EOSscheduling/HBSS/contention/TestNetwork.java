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
import gov.nasa.alsUtility.Utility;
import gov.nasa.javaGenes.EOSscheduling.*;
import gov.nasa.alsUtility.Error;

public class TestNetwork extends TestCase {
public static String time[] = {
    "1 Jan 2002 00:00:00.00",
    "1 Jan 2002 00:00:10.00",
    "2 Jan 2002 00:00:00.00",
    "3 Jan 2002 00:00:00.00"
};
public static final int SSR_SIZE = 10;
public static final int TAKEIMAGE_TIME = 100;
public static final int SLEW_RATE = 1;
public static final int SLEW_LIMIT = 24;
public static final int SSR_USE3 = 3;
public static final int SSR_USE2 = 2;

public static final int UNSCHEDULABLE = Constants.UNSCHEDULABLE;
public static final int SCHEDULED = Constants.SCHEDULED;
public static final int UNDECIDED = Constants.UNDECIDED;

private static SensorType sensorType1;
private static Sensor sensor1;
private static Satellite satellite;
private static Horizon horizon;

public TestNetwork(String name) {super(name);}

static public TaskList buildContentionNetwork(EOSModel model) {
    return buildContentionNetwork(model,1,1,1);
}
static public TaskList buildContentionNetwork(EOSModel model,float priority,float sensorAvail, float SSR) {
    TaskList taskList = new TaskList(model,priority,sensorAvail,SSR);
    model.beginScheduling();
    taskList.reinitialize();
    return taskList;
}    
static public EOSModel oneSatModel() {
    horizon = new Horizon(time[0],time[3]);
    satellite = new Satellite("satellite");
    satellite.setSSR(SSR_SIZE,horizon,1000000000);
    sensorType1 = new SensorType("sensor");
    sensor1 = new Sensor(satellite,sensorType1,horizon,TAKEIMAGE_TIME);
    satellite.addSensor(sensor1);
    SlewMotor slewMotor = new SlewMotor(SLEW_RATE,-SLEW_LIMIT,SLEW_LIMIT,horizon,TAKEIMAGE_TIME);
    sensor1.setSlewMotor(slewMotor);
    satellite.addSlewMotor(slewMotor);
    slewMotor.addSensor(sensor1);

    EOSModel model = new EOSModel();
    model.setHorizon(horizon);
    model.addSatellite(satellite);
    return model;
}

//......................................................................................................
/** one task, one access window. Static so can be used by other test classes */
static public EOSModel buildModel1() {
    EOSModel model = oneSatModel();
    TakeImage t = new TakeImage(TAKEIMAGE_TIME,sensorType1,SSR_USE2);
    AccessWindow aw = new AccessWindow(10,10+TAKEIMAGE_TIME,sensor1);
    aw.setSlewRequirement(new CrossTrackSlew(10));
    t.addAccessWindow(aw);
    t.hasAllAccessWindowsNow();
    t.setPriority(2);

    model.addTask(t);
    return model;
}
public void checkInitialModel1(String name, TaskList taskList) {
    assertTrue(name + "1", taskList.currentSize() == 1);
    checkTask(name + "2",taskList.getTaskWeight(0),Constants.UNDECIDED,1,2.0,4.0,2.0,0.4);
    checkAccessWindow(name+"3", taskList.getAccessWindowWeight(0,0),Constants.UNDECIDED,0,2.0,0.4);
    checkSSRcontenders(name+"6", taskList.getSSRcontenders(0,0),1,4,0.4,SSR_SIZE);
    taskList.assertCorrect();
}
public void testModel1() {
    TaskList taskList = buildContentionNetwork(buildModel1());
    checkInitialModel1("initial",taskList);

    taskList.getTaskWeight(0).unschedulable(taskList.getAccessWindowWeight(0,0));
    checkTask("1",taskList.getTaskWeight(0),Constants.UNSCHEDULABLE);
    checkAccessWindow("1", taskList.getAccessWindowWeight(0,0),Constants.UNSCHEDULABLE);
    checkSSRcontenders("1", taskList.getSSRcontenders(0,0),0,0,0,SSR_SIZE);
    assertTrue("2", Utility.nearlyEqual(taskList.getWeightSum(),0));
    assertTrue("2.5", taskList.currentSize() == 0);
    taskList.assertCorrect();

    reinitialize(taskList);
    checkInitialModel1("reinitialized-1-",taskList);    

    makeScheduled(taskList.getTaskWeight(0),taskList.getAccessWindowWeight(0,0));
    checkTask("3",taskList.getTaskWeight(0),Constants.SCHEDULED);
    checkAccessWindow("3", taskList.getAccessWindowWeight(0,0),Constants.SCHEDULED);
    checkSSRcontenders("3", taskList.getSSRcontenders(0,0),0,0,0,SSR_SIZE - SSR_USE2);
    assertTrue("4", Utility.nearlyEqual(taskList.getWeightSum(),0));
    assertTrue("5", taskList.currentSize() == 0);
    taskList.assertCorrect();

    reinitialize(taskList);
    checkInitialModel1("reinitialized-2-",taskList);    
}

//......................................................................................................
// two tasks with one accessWindow incompatible in SSR
static public EOSModel buildModel2() {
    EOSModel model = buildModel1();

    TakeImage t = new TakeImage(TAKEIMAGE_TIME,sensorType1,SSR_USE2);
    AccessWindow aw = new AccessWindow(1000,1000+TAKEIMAGE_TIME,sensor1);
    aw.setSlewRequirement(new CrossTrackSlew(10));
    t.addAccessWindow(aw);
    t.hasAllAccessWindowsNow();
    t.setPriority(2);
    model.addTask(t);
    return model;
}
public void checkInitialModel2(String name, TaskList taskList) {
    assertTrue(name + "1", taskList.currentSize() == 2);
    checkTask(name + "2",taskList.getTaskWeight(0),Constants.UNDECIDED,1,2.0,4.0,2.0,0.8);
    checkAccessWindow(name+"3", taskList.getAccessWindowWeight(0,0),Constants.UNDECIDED,0,2.0,0.8);
    checkTask(name + "4",taskList.getTaskWeight(1),Constants.UNDECIDED,1,2.0,4.0,2.0,0.8);
    checkAccessWindow(name+"5", taskList.getAccessWindowWeight(1,0),Constants.UNDECIDED,0,2.0,0.8);
    checkSSRcontenders(name+"6", taskList.getSSRcontenders(0,0),2,8,0.8,SSR_SIZE);
    taskList.assertCorrect();
}
public void testModel2Build() {
    TaskList taskList = buildContentionNetwork(buildModel2());
    checkInitialModel2("initial",taskList);
        
    // make only one task unschedulable
    taskList.getTaskWeight(0).unschedulable(taskList.getAccessWindowWeight(0,0));
    checkTask("1",taskList.getTaskWeight(0),Constants.UNSCHEDULABLE);
    checkAccessWindow("1", taskList.getAccessWindowWeight(0,0),Constants.UNSCHEDULABLE);
    checkTask("2",taskList.getTaskWeight(1),Constants.UNDECIDED,1,2.0,4.0,2.0,0.4);
    checkAccessWindow("2", taskList.getAccessWindowWeight(1,0),Constants.UNDECIDED,0,2.0,0.4);
    checkSSRcontenders("2", taskList.getSSRcontenders(0,0),1,4,0.4,SSR_SIZE);
    assertTrue("2", Utility.nearlyEqual(taskList.getWeightSum(),4.4));
    assertTrue("3", taskList.currentSize() == 1);
    taskList.assertCorrect();

    reinitialize(taskList);
    checkInitialModel2("reinitialized-1-",taskList);
    
    // schedule one task 
    makeScheduled(taskList.getTaskWeight(0),taskList.getAccessWindowWeight(0,0));
    checkTask("4",taskList.getTaskWeight(0),Constants.SCHEDULED);
    checkAccessWindow("4", taskList.getAccessWindowWeight(0,0),Constants.SCHEDULED);
    checkTask("5",taskList.getTaskWeight(1),Constants.UNDECIDED,1,2.0,4.0,2.0,0.5);
    checkAccessWindow("5", taskList.getAccessWindowWeight(1,0),Constants.UNDECIDED,0,2.0,0.5);
    checkSSRcontenders("6", taskList.getSSRcontenders(0,0),1,4,0.5,SSR_SIZE - SSR_USE2);
    assertTrue("7", Utility.nearlyEqual(taskList.getWeightSum(),4.5));
    assertTrue("8", taskList.currentSize() == 1);
    taskList.assertCorrect();

    reinitialize(taskList);
    checkInitialModel2("reinitialized-2-",taskList);

    // with insufficient SSR, scheduling one task makes other unschedulable
    taskList.getSSRcontenders(0,0).getSSRnode().removeCapacity(SSR_SIZE - SSR_USE2);
    makeScheduled(taskList.getTaskWeight(0),taskList.getAccessWindowWeight(0,0));
    checkTask("9",taskList.getTaskWeight(0),Constants.SCHEDULED);
    checkAccessWindow("10", taskList.getAccessWindowWeight(0,0),Constants.SCHEDULED);
    checkTask("11",taskList.getTaskWeight(1),Constants.UNSCHEDULABLE);
    checkAccessWindow("12", taskList.getAccessWindowWeight(1,0),Constants.UNSCHEDULABLE);
    checkSSRcontenders("13", taskList.getSSRcontenders(0,0),0,0,SSRcontenders.CONTENTION_WHEN_CAPACITY_IS_0,0);
    assertTrue("14", Utility.nearlyEqual(taskList.getWeightSum(),0));
    assertTrue("15", taskList.currentSize() == 0);
    taskList.assertCorrect();

    reinitialize(taskList);
    checkInitialModel2("reinitialized-3-",taskList);

    // schedule both tasks
    makeScheduled(taskList.getTaskWeight(0),taskList.getAccessWindowWeight(0,0));
    makeScheduled(taskList.getTaskWeight(1),taskList.getAccessWindowWeight(1,0));
    checkTask("16",taskList.getTaskWeight(0),Constants.SCHEDULED);
    checkAccessWindow("17", taskList.getAccessWindowWeight(0,0),Constants.SCHEDULED);
    checkTask("18",taskList.getTaskWeight(1),Constants.SCHEDULED);
    checkAccessWindow("19", taskList.getAccessWindowWeight(1,0),Constants.SCHEDULED);
    checkSSRcontenders("20", taskList.getSSRcontenders(0,0),0,0,0,SSR_SIZE - 2*SSR_USE2);
    assertTrue("21", Utility.nearlyEqual(taskList.getWeightSum(),0));
    assertTrue("22", taskList.currentSize() == 0);
    taskList.assertCorrect();

    reinitialize(taskList);
    checkInitialModel2("reinitialized-4-",taskList);

    // make both tasks unschedulable
    taskList.getTaskWeight(1).unschedulable(taskList.getAccessWindowWeight(1,0));
    taskList.getTaskWeight(0).unschedulable(taskList.getAccessWindowWeight(0,0));
    checkTask("23",taskList.getTaskWeight(0),Constants.UNSCHEDULABLE);
    checkAccessWindow("24", taskList.getAccessWindowWeight(0,0),Constants.UNSCHEDULABLE);
    checkTask("25",taskList.getTaskWeight(1),Constants.UNSCHEDULABLE);
    checkAccessWindow("26", taskList.getAccessWindowWeight(1,0),Constants.UNSCHEDULABLE);
    checkSSRcontenders("27", taskList.getSSRcontenders(0,0),0,0,0,SSR_SIZE);
    assertTrue("28", Utility.nearlyEqual(taskList.getWeightSum(),0));
    assertTrue("29", taskList.currentSize() == 0);
    taskList.assertCorrect();

    reinitialize(taskList);
    checkInitialModel2("reinitialized-5-",taskList);
}

//......................................................................................................
// two tasks with one access window that are incompatible in sensor use.  The second access window has lower priority
static public EOSModel buildModel3() {
    EOSModel model = buildModel1();

    TakeImage t = new TakeImage(TAKEIMAGE_TIME,sensorType1,SSR_USE2);
    AccessWindow aw = new AccessWindow(10,10+TAKEIMAGE_TIME,sensor1);
    aw.setSlewRequirement(new CrossTrackSlew(10));
    t.addAccessWindow(aw);
    t.hasAllAccessWindowsNow();
    t.setPriority(1);
    model.addTask(t);
    return model;
}
public void checkInitialModel3(String name, TaskList taskList) {
    assertTrue(name + "1", taskList.currentSize() == 2);
    checkTask(name + "2",taskList.getTaskWeight(0),Constants.UNDECIDED,1,2.0,4.0,3.0,0.6);
    checkAccessWindow(name+"3", taskList.getAccessWindowWeight(0,0),Constants.UNDECIDED,1,3.0,0.6);
    checkTask(name + "4",taskList.getTaskWeight(1),Constants.UNDECIDED,1,1.0,2.0,3.0,0.6);
    checkAccessWindow(name+"5", taskList.getAccessWindowWeight(1,0),Constants.UNDECIDED,1,3.0,0.6);
    checkSSRcontenders(name+"6", taskList.getSSRcontenders(0,0),2,6,0.6,SSR_SIZE);
    taskList.assertCorrect();
}
public void testModel3Build() {
    TaskList taskList = buildContentionNetwork(buildModel3());
    checkInitialModel3("initial",taskList);

    // make only one task unschedulable
    taskList.getTaskWeight(0).unschedulable(taskList.getAccessWindowWeight(0,0));
    checkTask("1",taskList.getTaskWeight(0),Constants.UNSCHEDULABLE);
    checkAccessWindow("1", taskList.getAccessWindowWeight(0,0),Constants.UNSCHEDULABLE);
    checkTask("2",taskList.getTaskWeight(1),Constants.UNDECIDED,1,1.0,2.0,1.0,0.2);
    checkAccessWindow("2", taskList.getAccessWindowWeight(1,0),Constants.UNDECIDED,0,1.0,0.2);
    checkSSRcontenders("2", taskList.getSSRcontenders(0,0),1,2,0.2,SSR_SIZE);
    assertTrue("2", Utility.nearlyEqual(taskList.getWeightSum(),2.2));
    assertTrue("3", taskList.currentSize() == 1);
    taskList.assertCorrect();

    reinitialize(taskList);
    checkInitialModel3("reinitialized-1-",taskList);

    // schedule one task
    makeScheduled(taskList.getTaskWeight(0),taskList.getAccessWindowWeight(0,0));
    checkTask("11",taskList.getTaskWeight(0),Constants.SCHEDULED);
    checkAccessWindow("11", taskList.getAccessWindowWeight(0,0),Constants.SCHEDULED);
    checkTask("12",taskList.getTaskWeight(1),Constants.UNSCHEDULABLE);
    checkAccessWindow("13", taskList.getAccessWindowWeight(1,0),Constants.UNSCHEDULABLE);
    checkSSRcontenders("14", taskList.getSSRcontenders(0,0),0,0,0,SSR_SIZE - SSR_USE2);
    assertTrue("15", Utility.nearlyEqual(taskList.getWeightSum(),0));
    assertTrue("16", taskList.currentSize() == 0);
    taskList.assertCorrect();

    reinitialize(taskList);
    checkInitialModel3("reinitialized-2-",taskList);
}

//......................................................................................................

// two tasks that not incompatible at all
static public EOSModel buildModel4() {
    EOSModel model = buildModel2();
    AccessWindow[] groundDump = {new AccessWindow(480,520)};
    model.getSatellite(0).setGroundStationAccessWindows(groundDump);
    return model;
}
public void checkInitialModel4(String name, TaskList taskList) {
    assertTrue(name + "1", taskList.currentSize() == 2);
    checkTask(name + "2",taskList.getTaskWeight(0),Constants.UNDECIDED,1,2.0,4.0,2.0,0.4);
    checkAccessWindow(name+"3", taskList.getAccessWindowWeight(0,0),Constants.UNDECIDED,0,2.0,0.4);
    checkTask(name + "4",taskList.getTaskWeight(1),Constants.UNDECIDED,1,2.0,4.0,2.0,0.4);
    checkAccessWindow(name+"5", taskList.getAccessWindowWeight(1,0),Constants.UNDECIDED,0,2.0,0.4);
    checkSSRcontenders(name+"6", taskList.getSSRcontenders(0,0),1,4,0.4,SSR_SIZE);
    checkSSRcontenders(name+"7", taskList.getSSRcontenders(0,1),1,4,0.4,SSR_SIZE);
    taskList.assertCorrect();
}
public void testModel4Build() {
    TaskList taskList = buildContentionNetwork(buildModel4());
    checkInitialModel4("initial",taskList);

    taskList.getTaskWeight(1).unschedulable(taskList.getAccessWindowWeight(1,0));
    assertTrue("1", taskList.currentSize() == 1);
    checkTask("2",taskList.getTaskWeight(0),Constants.UNDECIDED,1,2.0,4.0,2.0,0.4);
    checkAccessWindow("3", taskList.getAccessWindowWeight(0,0),Constants.UNDECIDED,0,2.0,0.4);
    checkTask("4",taskList.getTaskWeight(1),Constants.UNSCHEDULABLE);
    checkAccessWindow("5", taskList.getAccessWindowWeight(1,0),Constants.UNSCHEDULABLE);
    checkSSRcontenders("6", taskList.getSSRcontenders(0,0),1,4,0.4,SSR_SIZE);
    checkSSRcontenders("7", taskList.getSSRcontenders(0,1),0,0,0,SSR_SIZE);
    taskList.assertCorrect();

    reinitialize(taskList);
    checkInitialModel4("reinitialized-1-",taskList);

    makeScheduled(taskList.getTaskWeight(1),taskList.getAccessWindowWeight(1,0));
    assertTrue("11", taskList.currentSize() == 1);
    checkTask("12",taskList.getTaskWeight(0),Constants.UNDECIDED,1,2.0,4.0,2.0,0.4);
    checkAccessWindow("13", taskList.getAccessWindowWeight(0,0),Constants.UNDECIDED,0,2.0,0.4);
    checkTask("14",taskList.getTaskWeight(1),Constants.SCHEDULED);
    checkAccessWindow("15", taskList.getAccessWindowWeight(1,0),Constants.SCHEDULED);
    checkSSRcontenders("16", taskList.getSSRcontenders(0,0),1,4,0.4,SSR_SIZE);
    checkSSRcontenders("17", taskList.getSSRcontenders(0,1),0,0,0,SSR_SIZE - SSR_USE2);
    taskList.assertCorrect();

    reinitialize(taskList);
    checkInitialModel4("reinitialized-2-",taskList);

    makeScheduled(taskList.getTaskWeight(0),taskList.getAccessWindowWeight(0,0));
    makeScheduled(taskList.getTaskWeight(1),taskList.getAccessWindowWeight(1,0));
    assertTrue("21", taskList.currentSize() == 0);
    checkTask("22",taskList.getTaskWeight(0),Constants.SCHEDULED);
    checkAccessWindow("23", taskList.getAccessWindowWeight(0,0),Constants.SCHEDULED);
    checkTask("24",taskList.getTaskWeight(1),Constants.SCHEDULED);
    checkAccessWindow("25", taskList.getAccessWindowWeight(1,0),Constants.SCHEDULED);
    checkSSRcontenders("26", taskList.getSSRcontenders(0,0),0,0,0,SSR_SIZE - SSR_USE2);
    checkSSRcontenders("27", taskList.getSSRcontenders(0,1),0,0,0,SSR_SIZE - SSR_USE2);
    taskList.assertCorrect();

    reinitialize(taskList);
    checkInitialModel4("reinitialized-3-",taskList);
}

//......................................................................................................
// one task with three accessWindows incompatible in SSR
static public EOSModel buildModel5() {
    EOSModel model = oneSatModel();
    TakeImage t = new TakeImage(TAKEIMAGE_TIME,sensorType1,SSR_USE3);

    AccessWindow aw = new AccessWindow(10,10+TAKEIMAGE_TIME,sensor1);
    aw.setSlewRequirement(new CrossTrackSlew(10));
    t.addAccessWindow(aw);

    aw = new AccessWindow(1000,1000+TAKEIMAGE_TIME,sensor1);
    aw.setSlewRequirement(new CrossTrackSlew(10));
    t.addAccessWindow(aw);

    // another one incompatible in time with second, but shouldn't be on each other's sensorAvailContention lists
    aw = new AccessWindow(1000,1000+TAKEIMAGE_TIME,sensor1);
    aw.setSlewRequirement(new CrossTrackSlew(10));
    t.addAccessWindow(aw);

    t.setPriority(9);
    t.hasAllAccessWindowsNow();
    model.addTask(t);
    return model;
}
public void checkInitialModel5(String name, TaskList taskList) {
    assertTrue(name + "1", taskList.currentSize() == 1);
    checkTask(name + "2",taskList.getTaskWeight(0),Constants.UNDECIDED,3,3,9,3,0.9);
    checkAccessWindow(name+"3", taskList.getAccessWindowWeight(0,0),Constants.UNDECIDED,0,3,0.9);
    checkAccessWindow(name+"5", taskList.getAccessWindowWeight(0,1),Constants.UNDECIDED,0,3,0.9);
    checkAccessWindow(name+"6", taskList.getAccessWindowWeight(0,2),Constants.UNDECIDED,0,3,0.9);
    checkSSRcontenders(name+"7", taskList.getSSRcontenders(0,0),3,9,0.9,SSR_SIZE);
    taskList.assertCorrect();
}
public void testModel5Build() {
    TaskList taskList = buildContentionNetwork(buildModel5());
    checkInitialModel5("initial",taskList);

    taskList.getTaskWeight(0).unschedulable(taskList.getAccessWindowWeight(0,1));
    assertTrue("1", taskList.currentSize() == 1);
    checkTask("2",taskList.getTaskWeight(0),Constants.UNDECIDED,2,4.5,13.5,4.5,1.35);
    checkAccessWindow("3", taskList.getAccessWindowWeight(0,0),Constants.UNDECIDED,0,4.5,1.35);
    checkAccessWindow("5", taskList.getAccessWindowWeight(0,1),Constants.UNSCHEDULABLE);
    checkAccessWindow("6", taskList.getAccessWindowWeight(0,2),Constants.UNDECIDED,0,4.5,1.35);
    checkSSRcontenders("7", taskList.getSSRcontenders(0,0),2,13.5,1.35,SSR_SIZE);
    taskList.assertCorrect();

    reinitialize(taskList);
    checkInitialModel5("reinitialized-1-",taskList);

    makeScheduled(taskList.getTaskWeight(0),taskList.getAccessWindowWeight(0,1));
    assertTrue("11", taskList.currentSize() == 0);
    checkTask("12",taskList.getTaskWeight(0),Constants.SCHEDULED);
    checkAccessWindow("13", taskList.getAccessWindowWeight(0,0),Constants.UNSCHEDULABLE);
    checkAccessWindow("15", taskList.getAccessWindowWeight(0,1),Constants.SCHEDULED);
    checkAccessWindow("16", taskList.getAccessWindowWeight(0,2),Constants.UNSCHEDULABLE);
    checkSSRcontenders("17", taskList.getSSRcontenders(0,0),0,0,0,SSR_SIZE - SSR_USE3);
    taskList.assertCorrect();

    reinitialize(taskList);
    checkInitialModel5("reinitialized-2-",taskList);

    taskList.getTaskWeight(0).unschedulable(taskList.getAccessWindowWeight(0,1));
    taskList.getTaskWeight(0).unschedulable(taskList.getAccessWindowWeight(0,2));
    assertTrue("21", taskList.currentSize() == 1);
    checkTask("22",taskList.getTaskWeight(0),Constants.UNDECIDED,1,9,27,9,2.7);
    checkAccessWindow("23", taskList.getAccessWindowWeight(0,0),Constants.UNDECIDED,0,9,2.7);
    checkAccessWindow("25", taskList.getAccessWindowWeight(0,1),Constants.UNSCHEDULABLE);
    checkAccessWindow("26", taskList.getAccessWindowWeight(0,2),Constants.UNSCHEDULABLE);
    checkSSRcontenders("27", taskList.getSSRcontenders(0,0),1,27,2.7,SSR_SIZE);
    taskList.assertCorrect();

    reinitialize(taskList);
    checkInitialModel5("reinitialized-3-",taskList);

    taskList.getTaskWeight(0).unschedulable(taskList.getAccessWindowWeight(0,0));
    makeScheduled(taskList.getTaskWeight(0),taskList.getAccessWindowWeight(0,2));
    assertTrue("31", taskList.currentSize() == 0);
    checkTask("32",taskList.getTaskWeight(0),Constants.SCHEDULED);
    checkAccessWindow("33", taskList.getAccessWindowWeight(0,0),Constants.UNSCHEDULABLE);
    checkAccessWindow("35", taskList.getAccessWindowWeight(0,1),Constants.UNSCHEDULABLE);
    checkAccessWindow("36", taskList.getAccessWindowWeight(0,2),Constants.SCHEDULED);
    checkSSRcontenders("37", taskList.getSSRcontenders(0,0),0,0,0,SSR_SIZE - SSR_USE3);
    taskList.assertCorrect();

    reinitialize(taskList);
    checkInitialModel5("reinitialized-4-",taskList);

    taskList.getTaskWeight(0).unschedulable(taskList.getAccessWindowWeight(0,1));
    taskList.getTaskWeight(0).unschedulable(taskList.getAccessWindowWeight(0,2));
    taskList.getTaskWeight(0).unschedulable(taskList.getAccessWindowWeight(0,0));
    assertTrue("41", taskList.currentSize() == 0);
    checkTask("42",taskList.getTaskWeight(0),Constants.UNSCHEDULABLE);
    checkAccessWindow("43", taskList.getAccessWindowWeight(0,0),Constants.UNSCHEDULABLE);
    checkAccessWindow("45", taskList.getAccessWindowWeight(0,1),Constants.UNSCHEDULABLE);
    checkAccessWindow("46", taskList.getAccessWindowWeight(0,2),Constants.UNSCHEDULABLE);
    checkSSRcontenders("47", taskList.getSSRcontenders(0,0),0,0,0,SSR_SIZE);
    taskList.assertCorrect();

    reinitialize(taskList);
    checkInitialModel5("reinitialized-5-",taskList);
}

//......................................................................................................
// two tasks with 3 access windows each that interact.  A very common situation.
static public EOSModel buildModel6() {
    EOSModel model = oneSatModel();
    TakeImage t1 = new TakeImage(TAKEIMAGE_TIME,sensorType1,SSR_USE3);
    TakeImage t2 = new TakeImage(TAKEIMAGE_TIME,sensorType1,SSR_USE2);

    // AWs connected both ways
    AccessWindow aw = new AccessWindow(10,10+TAKEIMAGE_TIME,sensor1);
    aw.setSlewRequirement(new CrossTrackSlew(10));
    t1.addAccessWindow(aw);
    aw = new AccessWindow(10,10+TAKEIMAGE_TIME,sensor1);
    aw.setSlewRequirement(new CrossTrackSlew(10));
    t2.addAccessWindow(aw);

    // AWs connected by SSR only
    aw = new AccessWindow(1000,1000+TAKEIMAGE_TIME,sensor1);
    aw.setSlewRequirement(new CrossTrackSlew(10));
    t1.addAccessWindow(aw);
    aw = new AccessWindow(1200,1200+TAKEIMAGE_TIME,sensor1);
    aw.setSlewRequirement(new CrossTrackSlew(10));
    t2.addAccessWindow(aw);

    // AWs connected by Sensor Avail only
    aw = new AccessWindow(2450,2600,sensor1);
    aw.setSlewRequirement(new CrossTrackSlew(10));
    t1.addAccessWindow(aw);
    aw = new AccessWindow(2550,2650,sensor1);
    aw.setSlewRequirement(new CrossTrackSlew(10));
    t2.addAccessWindow(aw);

    t1.setPriority(9);
    t1.hasAllAccessWindowsNow();
    model.addTask(t1);
    t2.setPriority(12);
    t2.hasAllAccessWindowsNow();
    model.addTask(t2);

    AccessWindow[] groundDump = {
        new AccessWindow(480,520), 
        new AccessWindow(1480,1520), 
        new AccessWindow(2499,2501)
    };
    model.getSatellite(0).setGroundStationAccessWindows(groundDump);
    return model;
}
public void checkInitialModel6(String name, TaskList taskList) {
    assertTrue(name+"1", taskList.currentSize() == 2);

    checkAccessWindow(name+"2", taskList.getAccessWindowWeight(0,0),Constants.UNDECIDED,1, 7, 1.7);
    checkAccessWindow(name+"3", taskList.getAccessWindowWeight(0,1),Constants.UNDECIDED,0, 3, 1.7);
    checkAccessWindow(name+"4", taskList.getAccessWindowWeight(0,2),Constants.UNDECIDED,1, 7, 0.9);
    checkTask(name+"5",taskList.getTaskWeight(0),Constants.UNDECIDED,3, 3, 9, 3, 1.7);

    checkTask(name+"6",taskList.getTaskWeight(1),Constants.UNDECIDED,3, 4, 8, 4, 1.7);
    checkAccessWindow(name+"7", taskList.getAccessWindowWeight(1,0),Constants.UNDECIDED,1, 7, 1.7);
    checkAccessWindow(name+"8", taskList.getAccessWindowWeight(1,1),Constants.UNDECIDED,0, 4, 1.7);
    checkAccessWindow(name+"9", taskList.getAccessWindowWeight(1,2),Constants.UNDECIDED,1, 7, 0.8);

    checkSSRcontenders(name+"10", taskList.getSSRcontenders(0,0),2, 17, 1.7, SSR_SIZE);
    checkSSRcontenders(name+"11", taskList.getSSRcontenders(0,1),2, 17, 1.7, SSR_SIZE);
    checkSSRcontenders(name+"12", taskList.getSSRcontenders(0,2),1,  9, 0.9, SSR_SIZE);
    checkSSRcontenders(name+"13", taskList.getSSRcontenders(0,3),1,  8, 0.8, SSR_SIZE);
    
    taskList.assertCorrect();
}
public void testModel6() {
    TaskList taskList = buildContentionNetwork(buildModel6());
    checkInitialModel6("initial",taskList);

    taskList.getTaskWeight(0).unschedulable(taskList.getAccessWindowWeight(0,0));
    assertTrue("1", taskList.currentSize() == 2);

    checkAccessWindow("2", taskList.getAccessWindowWeight(0,0),Constants.UNSCHEDULABLE);
    checkAccessWindow("3", taskList.getAccessWindowWeight(0,1),Constants.UNDECIDED,0, 4.5, 2.15);
    checkAccessWindow("4", taskList.getAccessWindowWeight(0,2),Constants.UNDECIDED,1, 8.5, 1.35);
    checkTask("5",taskList.getTaskWeight(0),Constants.UNDECIDED,2, 4.5, 13.5, 4.5, 2.15);

    checkTask("6",taskList.getTaskWeight(1),Constants.UNDECIDED,3, 4, 8, 4, 0.8);
    checkAccessWindow("7", taskList.getAccessWindowWeight(1,0),Constants.UNDECIDED,0,  4  , 0.8);
    checkAccessWindow("8", taskList.getAccessWindowWeight(1,1),Constants.UNDECIDED,0,  4  , 2.15);
    checkAccessWindow("9", taskList.getAccessWindowWeight(1,2),Constants.UNDECIDED,1,  8.5, 0.8);

    checkSSRcontenders("10", taskList.getSSRcontenders(0,0),1,  8  , 0.8,  SSR_SIZE);
    checkSSRcontenders("11", taskList.getSSRcontenders(0,1),2, 21.5, 2.15, SSR_SIZE);
    checkSSRcontenders("12", taskList.getSSRcontenders(0,2),1, 13.5, 1.35,  SSR_SIZE);
    checkSSRcontenders("13", taskList.getSSRcontenders(0,3),1,  8  , 0.8,  SSR_SIZE);

    taskList.assertCorrect();
    reinitialize(taskList);
    checkInitialModel6("reinitialized-1-",taskList);

    makeScheduled(taskList.getTaskWeight(0),taskList.getAccessWindowWeight(0,0));
    checkModel6("schedule00", taskList, 1, 
        SCHEDULED, SCHEDULED,     UNSCHEDULABLE, UNSCHEDULABLE,
        UNDECIDED, UNSCHEDULABLE, UNDECIDED,    UNDECIDED);

    makeScheduled(taskList.getTaskWeight(1),taskList.getAccessWindowWeight(1,0));
    checkModel6("schedule10", taskList, 1, 
        UNDECIDED, UNSCHEDULABLE, UNDECIDED,     UNDECIDED,
        SCHEDULED, SCHEDULED,     UNSCHEDULABLE, UNSCHEDULABLE);

    makeScheduled(taskList.getTaskWeight(1),taskList.getAccessWindowWeight(1,1));
    assertTrue("schedule11-ssr", taskList.getSSRcontenders(0,1).getSSRcapacity() == SSR_SIZE - SSR_USE2);
    checkModel6("schedule11", taskList, 1, 
        UNDECIDED, UNDECIDED,     UNDECIDED, UNDECIDED,
        SCHEDULED, UNSCHEDULABLE, SCHEDULED, UNSCHEDULABLE);

    makeScheduled(taskList.getTaskWeight(0),taskList.getAccessWindowWeight(0,1));
    makeScheduled(taskList.getTaskWeight(1),taskList.getAccessWindowWeight(1,1));
    assertTrue("schedule 01 11-ssr", taskList.getSSRcontenders(0,1).getSSRcapacity() == SSR_SIZE-SSR_USE2-SSR_USE3);
    checkModel6("schedule 01 11", taskList, 0, 
        SCHEDULED, UNSCHEDULABLE, SCHEDULED, UNSCHEDULABLE,
        SCHEDULED, UNSCHEDULABLE, SCHEDULED, UNSCHEDULABLE);

    makeScheduled(taskList.getTaskWeight(0),taskList.getAccessWindowWeight(0,2));
    checkModel6("schedule 02", taskList, 1, 
        SCHEDULED, UNSCHEDULABLE, UNSCHEDULABLE, SCHEDULED,
        UNDECIDED, UNDECIDED,     UNDECIDED,     UNSCHEDULABLE);

    makeScheduled(taskList.getTaskWeight(0),taskList.getAccessWindowWeight(0,2));
    makeScheduled(taskList.getTaskWeight(1),taskList.getAccessWindowWeight(1,1));
    checkModel6("schedule 02 11", taskList, 0, 
        SCHEDULED, UNSCHEDULABLE, UNSCHEDULABLE, SCHEDULED,
        SCHEDULED, UNSCHEDULABLE, SCHEDULED,     UNSCHEDULABLE);

    taskList.getTaskWeight(0).unschedulable(taskList.getAccessWindowWeight(0,2));
    taskList.getTaskWeight(0).unschedulable(taskList.getAccessWindowWeight(0,0));
    checkModel6("unschedulable 02 00", taskList, 2, 
        UNDECIDED, UNSCHEDULABLE, UNDECIDED, UNSCHEDULABLE,
        UNDECIDED, UNDECIDED,     UNDECIDED, UNDECIDED);

    taskList.getTaskWeight(0).unschedulable(taskList.getAccessWindowWeight(0,2));
    taskList.getTaskWeight(0).unschedulable(taskList.getAccessWindowWeight(0,0));
    taskList.getTaskWeight(1).unschedulable(taskList.getAccessWindowWeight(1,0));
    checkModel6("unschedulable 02 00 10", taskList, 2, 
        UNDECIDED, UNSCHEDULABLE, UNDECIDED, UNSCHEDULABLE,
        UNDECIDED, UNSCHEDULABLE, UNDECIDED, UNDECIDED);
}
public void checkModel6(String name, TaskList taskList, int tlsize, 
        int t0,int aw00,int aw01,int aw02, 
        int t1,int aw10,int aw11,int aw12) 
    {
    assertTrue(name+"1", taskList.currentSize() == tlsize);
    assertTrue(name+"2", taskList.getTaskWeight(0).status == t0);
    assertTrue(name+"3", taskList.getAccessWindowWeight(0,0).status == aw00);
    assertTrue(name+"4", taskList.getAccessWindowWeight(0,1).status == aw01);
    assertTrue(name+"5",taskList.getAccessWindowWeight(0,2).status == aw02);

    assertTrue(name+"6", taskList.getTaskWeight(1).status == t1);
    assertTrue(name+"7", taskList.getAccessWindowWeight(1,0).status == aw10);
    assertTrue(name+"8", taskList.getAccessWindowWeight(1,1).status == aw11);
    assertTrue(name+"9", taskList.getAccessWindowWeight(1,2).status == aw12);
    taskList.assertCorrect();
    reinitialize(taskList);
    checkInitialModel6(name+"reinitialize",taskList);
}
//......................................................................................................
// three tasks with 3 access windows each that interact. 
static public EOSModel buildModel7() {
    EOSModel model = buildModel6();
    TakeImage t = new TakeImage(TAKEIMAGE_TIME,sensorType1,SSR_SIZE-1);

    // all AWs connected both ways
    AccessWindow aw = new AccessWindow(10,10+TAKEIMAGE_TIME,sensor1);
    aw.setSlewRequirement(new CrossTrackSlew(10));
    t.addAccessWindow(aw);

    // all AWs connected by SSR only
    aw = new AccessWindow(1000+TAKEIMAGE_TIME+10,1000+2*TAKEIMAGE_TIME+10,sensor1);
    aw.setSlewRequirement(new CrossTrackSlew(10));
    t.addAccessWindow(aw);

    aw = new AccessWindow(2450,2450+TAKEIMAGE_TIME,sensor1);
    aw.setSlewRequirement(new CrossTrackSlew(10));
    t.addAccessWindow(aw);

    t.setPriority(7);
    t.hasAllAccessWindowsNow();
    model.addTask(t);

    return model;
}
public void checkInitialModel7(String name, TaskList taskList) {
    assertTrue(name+"1", taskList.currentSize() == 3);    
    taskList.assertCorrect();
}
public void checkModel7(String name, TaskList taskList, int tlsize, 
        int t0,int aw00,int aw01,int aw02, 
        int t1,int aw10,int aw11,int aw12, 
        int t2,int aw20,int aw21,int aw22) 
    {
    assertTrue(name+"1", taskList.currentSize() == tlsize);
    assertTrue(name+"2", taskList.getTaskWeight(0).status == t0);
    assertTrue(name+"3" + taskList.getAccessWindowWeight(0,0).status, taskList.getAccessWindowWeight(0,0).status == aw00);
    assertTrue(name+"4", taskList.getAccessWindowWeight(0,1).status == aw01);
    assertTrue(name+"5",taskList.getAccessWindowWeight(0,2).status == aw02);

    assertTrue(name+"6", taskList.getTaskWeight(1).status == t1);
    assertTrue(name+"7", taskList.getAccessWindowWeight(1,0).status == aw10);
    assertTrue(name+"8", taskList.getAccessWindowWeight(1,1).status == aw11);
    assertTrue(name+"9 " + taskList.getAccessWindowWeight(1,2).status, taskList.getAccessWindowWeight(1,2).status == aw12);

    assertTrue(name+"10", taskList.getTaskWeight(2).status == t2);
    assertTrue(name+"11", taskList.getAccessWindowWeight(2,0).status == aw20);
    assertTrue(name+"12", taskList.getAccessWindowWeight(2,1).status == aw21);
    assertTrue(name+"13", taskList.getAccessWindowWeight(2,2).status == aw22);

    taskList.assertCorrect();
    reinitialize(taskList);
    checkInitialModel7(name+"reinitialize",taskList);
}
public void testModel7() {
    TaskList taskList = buildContentionNetwork(buildModel7());
    checkInitialModel7("initial",taskList);
    makeScheduled(taskList.getTaskWeight(2),taskList.getAccessWindowWeight(2,1));
    checkModel7("schedule 21 ", taskList, 2, 
        UNDECIDED, UNDECIDED,     UNSCHEDULABLE, UNDECIDED,
        UNDECIDED, UNDECIDED,     UNSCHEDULABLE, UNDECIDED,
        SCHEDULED, UNSCHEDULABLE, SCHEDULED,     UNSCHEDULABLE);
    makeScheduled(taskList.getTaskWeight(0),taskList.getAccessWindowWeight(0,0));
    makeScheduled(taskList.getTaskWeight(2),taskList.getAccessWindowWeight(2,2));
    checkModel7("schedule 00 02 ", taskList, 1, 
        SCHEDULED, SCHEDULED,     UNSCHEDULABLE, UNSCHEDULABLE,
        UNDECIDED, UNSCHEDULABLE, UNDECIDED,     UNDECIDED,
        SCHEDULED, UNSCHEDULABLE, UNSCHEDULABLE, SCHEDULED);
}

//......................................................................................................
// like model 7 except third task is on a different sensor with a different sensor type (same satellite)
static public EOSModel buildModel8() {
    EOSModel model = buildModel6();
    SensorType st = new SensorType("fee");
    Sensor s = new Sensor(satellite,st,horizon,TAKEIMAGE_TIME);
    satellite.addSensor(s);
    SlewMotor slewMotor = new SlewMotor(SLEW_RATE,-SLEW_LIMIT,SLEW_LIMIT,horizon,TAKEIMAGE_TIME);
    s.setSlewMotor(slewMotor);
    satellite.addSlewMotor(slewMotor);
    slewMotor.addSensor(s);

    TakeImage t = new TakeImage(TAKEIMAGE_TIME,st,SSR_SIZE-SSR_USE2);

    // all AWs connected both ways
    AccessWindow aw = new AccessWindow(10,10+TAKEIMAGE_TIME,s);
    aw.setSlewRequirement(new CrossTrackSlew(10));
    t.addAccessWindow(aw);

    // all AWs connected by SSR only
    aw = new AccessWindow(1000+TAKEIMAGE_TIME+10,1000+2*TAKEIMAGE_TIME,s);
    aw.setSlewRequirement(new CrossTrackSlew(10));
    t.addAccessWindow(aw);

    aw = new AccessWindow(2450,2600,s);
    aw.setSlewRequirement(new CrossTrackSlew(10));
    t.addAccessWindow(aw);

    t.setPriority(17);
    t.hasAllAccessWindowsNow();
    model.addTask(t);

    return model;
}
// checks for model 7 work for model 8
public void testModel8() {
    TaskList taskList = buildContentionNetwork(buildModel8());
    checkInitialModel7("initial",taskList);

    makeScheduled(taskList.getTaskWeight(2),taskList.getAccessWindowWeight(2,0));
    checkModel7("schedule 20 ", taskList, 2, 
        UNDECIDED, UNSCHEDULABLE, UNDECIDED,     UNDECIDED,
        UNDECIDED, UNDECIDED,     UNDECIDED,     UNDECIDED,
        SCHEDULED, SCHEDULED,     UNSCHEDULABLE, UNSCHEDULABLE);
    makeScheduled(taskList.getTaskWeight(2),taskList.getAccessWindowWeight(2,1));
    checkModel7("schedule 21 ", taskList, 2, 
        UNDECIDED, UNDECIDED,     UNSCHEDULABLE, UNDECIDED,
        UNDECIDED, UNDECIDED,     UNDECIDED,     UNDECIDED,
        SCHEDULED, UNSCHEDULABLE, SCHEDULED,     UNSCHEDULABLE);
    makeScheduled(taskList.getTaskWeight(1),taskList.getAccessWindowWeight(1,0));
    makeScheduled(taskList.getTaskWeight(2),taskList.getAccessWindowWeight(2,2));
    checkModel7("schedule 10 02 ", taskList, 1, 
        UNDECIDED, UNSCHEDULABLE, UNDECIDED,     UNSCHEDULABLE,
        SCHEDULED, SCHEDULED,     UNSCHEDULABLE, UNSCHEDULABLE,
        SCHEDULED, UNSCHEDULABLE, UNSCHEDULABLE, SCHEDULED);
}
//......................................................................................................
// same as model 7 except third task on another satellite (no interaction with the others) 
static public EOSModel buildModel9() {
    EOSModel model = buildModel6();

    Satellite sat = new Satellite("satellite2");
    sat.setSSR(SSR_SIZE,horizon,1000000000);
    SensorType st = new SensorType("st");
    Sensor s  = new Sensor(sat,st,horizon,TAKEIMAGE_TIME);
    sat.addSensor(s);
    SlewMotor slewMotor = new SlewMotor(SLEW_RATE,-SLEW_LIMIT,SLEW_LIMIT,horizon,TAKEIMAGE_TIME);
    s.setSlewMotor(slewMotor);
    sat.addSlewMotor(slewMotor);
    slewMotor.addSensor(s);
    model.addSatellite(sat);

    TakeImage t = new TakeImage(TAKEIMAGE_TIME,st,SSR_SIZE);

    AccessWindow aw = new AccessWindow(10,10+TAKEIMAGE_TIME,s);
    aw.setSlewRequirement(new CrossTrackSlew(10));
    t.addAccessWindow(aw);

    aw = new AccessWindow(1000+TAKEIMAGE_TIME+10,1000+2*TAKEIMAGE_TIME+10,s);
    aw.setSlewRequirement(new CrossTrackSlew(10));
    t.addAccessWindow(aw);

    aw = new AccessWindow(2450,2600,s);
    aw.setSlewRequirement(new CrossTrackSlew(10));
    t.addAccessWindow(aw);

    t.setPriority(4.2);
    t.hasAllAccessWindowsNow();
    model.addTask(t);

    return model;
}
// checks for model 7 work for model 9
public void testModel9() {
    TaskList taskList = buildContentionNetwork(buildModel9());
    checkInitialModel7("initial",taskList);

    makeScheduled(taskList.getTaskWeight(2),taskList.getAccessWindowWeight(2,0));
    checkModel7("schedule 20 ", taskList, 2, 
        UNDECIDED, UNDECIDED,     UNDECIDED,     UNDECIDED,
        UNDECIDED, UNDECIDED,     UNDECIDED,     UNDECIDED,
        SCHEDULED, SCHEDULED,     UNSCHEDULABLE, UNSCHEDULABLE);
    makeScheduled(taskList.getTaskWeight(2),taskList.getAccessWindowWeight(2,1));
    checkModel7("schedule 21 ", taskList, 2, 
        UNDECIDED, UNDECIDED,     UNDECIDED, UNDECIDED,
        UNDECIDED, UNDECIDED,     UNDECIDED,     UNDECIDED,
        SCHEDULED, UNSCHEDULABLE, SCHEDULED,     UNSCHEDULABLE);
    makeScheduled(taskList.getTaskWeight(1),taskList.getAccessWindowWeight(1,0));
    makeScheduled(taskList.getTaskWeight(2),taskList.getAccessWindowWeight(2,2));
    checkModel7("schedule 10 02 ", taskList, 1, 
        UNDECIDED, UNSCHEDULABLE, UNDECIDED,     UNDECIDED,
        SCHEDULED, SCHEDULED,     UNSCHEDULABLE, UNSCHEDULABLE,
        SCHEDULED, UNSCHEDULABLE, UNSCHEDULABLE, SCHEDULED);
}

//......................................................................................................
public void reinitialize(TaskList taskList) {
    taskList.model.endScheduling();
    taskList.model.beginScheduling();
    taskList.reinitialize();
}

public void checkTask(
        String name, 
        gov.nasa.javaGenes.EOSscheduling.HBSS.contention.TaskWeight t, 
        int status,
        int numberOfUndecidedAccessWindows,
        double sensorNeed,
        double SSRneed,
        double sensorDifficulty,
        double SSRdifficulty) 
        {
    Error.assertTrue(t.status == status, name+"task-status");
    Error.assertTrue(t.getNumberOfUndecidedAccessWindows() == numberOfUndecidedAccessWindows, name+"task-#AW");
    Error.assertTrue(Utility.nearlyEqual(t.getSensorAvailNeed(),sensorNeed), name+"-task-SAneed");
    Error.assertTrue(Utility.nearlyEqual(t.getSSRneed(),SSRneed), name+"-task-SSRneed");
    Error.assertTrue(Utility.nearlyEqual(t.getSensorAvailDifficulty(),sensorDifficulty), name+"-task-SAdifficulty" );
    Error.assertTrue(Utility.nearlyEqual(t.getSSRdifficulty(),SSRdifficulty), name+"-task-SSRdifficulty");
}
public void checkTask(String name, gov.nasa.javaGenes.EOSscheduling.HBSS.contention.TaskWeight t, int status) {
    Error.assertTrue(t.status == status,name+"task-status");
}
public void checkAccessWindow(String name, AccessWindowWeight aw, int status, int numberSAcontenders, double sensorContention, double SSRcontention) {
    assertTrue(name+"-accessWindow-status",status == aw.status);
    assertTrue(name+"-accessWindow-numberSAcontenders",numberSAcontenders == aw.getNumberOfSensorAvailContenders());
    assertTrue(name+"-accessWindow-SAcontention", Utility.nearlyEqual(aw.getSensorAvailContention(),sensorContention));
    assertTrue(name+"-accessWindow-SSRcontention", Utility.nearlyEqual(aw.getSSRcontention(),SSRcontention));
}
public void checkSSRcontenders(String name, SSRcontenders ssr, int numberOfAccessWindows, double needSum, double contention, int capacity) {
    assertTrue(name+"-SSRcontenders-size",numberOfAccessWindows == ssr.currentSize());
    assertTrue(name+"-SSRcontenders-needSum",Utility.nearlyEqual(needSum,ssr.getSSRneedSum()));
    assertTrue(name+"-SSRcontenders-contention",Utility.nearlyEqual(contention,ssr.getSSRcontention()));
    assertTrue(name+"-SSRcontenders-capacity",capacity == ssr.getSSRcapacity());
}
public void checkAccessWindow(String name, AccessWindowWeight aw, int status) {
    assertTrue(name+"-accessWindow-statuss",status == aw.status);
}
public void makeScheduled(TaskWeight tw, AccessWindowWeight aw) {
    aw.getSSRcontenders().getSSRnode().removeCapacity(tw.getSSRuse()); // simulated SSRTimeline.insertAt()
    tw.scheduled(aw);
}
}

