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

public class TaskWeightTest extends TestCase {
public TaskWeightTest(String name) {super(name);}
public void testConstructor() {
    Task t = makeTask(2,1,5);
    assertTrue("1", Utility.nearlyEqual(8,new TaskWeight(t,1,1,1).getWeight()));
    assertTrue("2", Utility.nearlyEqual(10,new TaskWeight(t,2,1,1).getWeight()));
    assertTrue("3", Utility.nearlyEqual(9,new TaskWeight(t,1,2,1).getWeight()));
    assertTrue("4", Utility.nearlyEqual(13,new TaskWeight(t,1,1,2).getWeight()));
    
    t = makeTask(3,2,1);
    TaskWeight tw = new TaskWeight(t,1,1,1);
    assertTrue("5", Utility.nearlyEqual(4.5,tw.getWeight()));
    assertTrue("6", tw.getAccessWindowList().getInitialSize() == 2);
}

private Task t1;
private Task t2;
private Task t3;
private TaskWeight tw1;
private TaskWeight tw2;
private TaskWeight tw3;
private EOSModel model;
private TaskList taskList;

public void setUp() {
    t1 = makeTask(1,1,1);
    t2 = makeTask(1,2,1);
    t3 = makeTask(1,3,1);
    model = new EOSModel();
    model.addTask(t1);
    model.addTask(t2);
    model.addTask(t3);
    taskList = new TaskList(model,1,1,1);
    tw1 = (TaskWeight)taskList.getWeight(0);
    tw2 = (TaskWeight)taskList.getWeight(1);
    tw3 = (TaskWeight)taskList.getWeight(2);
}
public void testUnscheduling() {
    assertTrue("1", taskList.currentSize() == 3);
    assertTrue("2", taskList.currentlyContains(tw1));
    assertTrue("3", taskList.currentlyContains(tw2));
    assertTrue("4", taskList.currentlyContains(tw3));
    
    tw2.unschedulable(getAW(tw2,0));
    assertTrue("11", taskList.currentSize() == 3);
    assertTrue("12", taskList.currentlyContains(tw1));
    assertTrue("13", taskList.currentlyContains(tw2));
    assertTrue("14", taskList.currentlyContains(tw3));
    
    tw2.unschedulable(getAW(tw2,1));
    assertTrue("21", taskList.currentSize() == 2);
    assertTrue("22", taskList.currentlyContains(tw1));
    assertTrue("23", !taskList.currentlyContains(tw2));
    assertTrue("24", taskList.currentlyContains(tw3));
    
    tw1.unschedulable(getAW(tw1,0));
    assertTrue("31", taskList.currentSize() == 1);
    assertTrue("32", !taskList.currentlyContains(tw1));
    assertTrue("33", !taskList.currentlyContains(tw2));
    assertTrue("34", taskList.currentlyContains(tw3));
}
public void testScheduling() {
    assertTrue("1", taskList.currentSize() == 3);
    assertTrue("2", taskList.currentlyContains(tw1));
    assertTrue("3", taskList.currentlyContains(tw2));
    assertTrue("4", taskList.currentlyContains(tw3));
    
    tw1.scheduled(getAW(tw1,0));
    assertTrue("11", taskList.currentSize() == 2);
    assertTrue("12", !taskList.currentlyContains(tw1));
    assertTrue("13", taskList.currentlyContains(tw2));
    assertTrue("14", taskList.currentlyContains(tw3));
    
    tw3.scheduled(getAW(tw3,1));
    assertTrue("21", taskList.currentSize() == 1);
    assertTrue("22", !taskList.currentlyContains(tw1));
    assertTrue("23", taskList.currentlyContains(tw2));
    assertTrue("24", !taskList.currentlyContains(tw3));
    
    tw2.scheduled(getAW(tw2,0));
    assertTrue("31", taskList.currentSize() == 0);
    assertTrue("32", !taskList.more());
}
public AccessWindowWeight getAW(TaskWeight tw, int index) {
    return (AccessWindowWeight)tw.getAccessWindowList().getWeight(index);
}
public Task makeTask(double priority,int numberOfAccessWindows, int SSRuse) {
    SensorType sensorType = new SensorType("foo");
    final int duration = 10;
    Task task = new TakeImage(duration, sensorType, SSRuse);
    task.setPriority(priority);
    for(int i = 0; i < numberOfAccessWindows; i++) {
        AccessWindow a = new AccessWindow(i, i+10);
        a.setSlewRequirement(new CrossTrackSlew(i));
        task.addAccessWindow(a);
    }
    task.hasAllAccessWindowsNow();
    return task;
}

}
