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
//  Created by Al Globus on Mon Jan 13 2003.
package gov.nasa.javaGenes.EOSscheduling;

import junit.framework.TestCase;
import gov.nasa.alsUtility.RandomNumber;

public class EOSModelTest extends TestCase {

public EOSModelTest(String name) {super(name);}

public void testSetTaskPriorities() {
    final int numberOfTasks = 100;
    EOSModel model = new EOSModel();
    for(int i = 0; i < numberOfTasks; i++)
        addATask(model,1);
    assertTrue("-1",model.getTaskPrioritySum() == numberOfTasks);
    model.setTaskPriorities(1.0, 1.0, 1);
    assertTrue("-2",model.getTaskPrioritySum() == 5050.0);
    for(int i = 0; i < model.getNumberOfTasks(); i++)
        assertTrue(i+"first", i+1.0 == model.getTask(i).getPriority());
    model.setTaskPriorities(1.0,2.0,12);
    assertTrue("1", model.getTask(2).getPriority() == 1.0);
    assertTrue("2", model.getTask(15).getPriority() == 3.0);
    assertTrue("3", model.getTask(90).getPriority() == 1.0 + 2*7);
}

public void testRemoveUnexecutableTasks() {
    EOSModel model = new EOSModel();
    assertTrue("1", model.getNumberOfTasks() == 0);

    addATask(model,0);
    assertTrue("2", model.getNumberOfTasks() == 1);
    model.removeUnexecutableTasks();
    assertTrue("3", model.getNumberOfTasks() == 0);

    addATask(model,0);
    addATask(model,1);
    assertTrue("4", model.getNumberOfTasks() == 2);
    model.removeUnexecutableTasks();
    assertTrue("5", model.getNumberOfTasks() == 1);

    addATask(model,0);
    addATask(model,0);
    assertTrue("6", model.getNumberOfTasks() == 3);
    model.removeUnexecutableTasks();
    assertTrue("7", model.getNumberOfTasks() == 1);

    addATask(model,0);
    addATask(model,0);
    addATask(model,1);
    assertTrue("8", model.getNumberOfTasks() == 4);
    model.removeUnexecutableTasks();
    assertTrue("9", model.getNumberOfTasks() == 2);

    model.removeUnexecutableTasks();
    assertTrue("10", model.getNumberOfTasks() == 2);
}
private void addATask(EOSModel model, int numberOfWindows) {
    Task task = new Task(5);

    for(int i = 0; i < numberOfWindows; i++) {
        int start = RandomNumber.getIndex(10);
        int end = RandomNumber.getIndex(100)+start;
        AccessWindow w = new AccessWindow(start,end);
        task.addAccessWindow(w);
    }
    task.hasAllAccessWindowsNow();
    model.addTask(task);
}
}

