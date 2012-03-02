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
//  Created by Al Globus on Fri Feb 07 2003.
package gov.nasa.javaGenes.EOSscheduling;

import junit.framework.TestCase;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.javaGenes.core.Evolvable;

public class SqueakyWheelPlacedTSMTest extends TestCase {
private EOSModel model;
private EOSschedulingEvolvable permutation;
private SqueakyWheelPlacedTSM maker;
private double moveForwardFraction = 0.5;
private double moveBackFraction = 0.3;
    
public SqueakyWheelPlacedTSMTest(String name) {super(name);}

public void setUp() {
    model = new EOSModel();
    int numberOfTasks = 100;
    permutation = new EOSschedulingEvolvable(numberOfTasks); 
    DoubleInterval priorityRange = new DoubleInterval(1,5);
    for(int i = 0; i < numberOfTasks; i++)
        addATask(i,RandomNumber.getBoolean(),priorityRange.random());
    maker = new SqueakyWheelPlacedTSM(model,1,10,moveForwardFraction,moveBackFraction);
         
}
private void addATask(int taskNumber,boolean scheduled, double priority) {
    Task task = new Task(5);
    task.setPriority(priority);
    model.addTask(task);
    TaskPlacementData data = new TaskPlacementData();
    data.setScheduled(scheduled);
    permutation.setTaskPlacement(taskNumber,data);
}

public void testSetUpIndices() {
    for(int i = 0; i < 200; i++) 
        check(i);
}
private void check(int name) {
    Evolvable p[] = new Evolvable[1];
    p[0] = permutation;
    maker.makeChildren(p);
    assertTrue(name+"-1",maker.moveForwardIndex > maker.moveBackIndex);
    assertTrue(name+"-2",maker.moveForwardIndex >= permutation.getSize() * moveForwardFraction - 1);
    assertTrue(name+"-3",maker.moveBackIndex <= maker.moveForwardIndex * moveBackFraction + 1);
}
}


