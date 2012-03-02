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
//  Created by Al Globus on Thu Jul 18 2002.

package gov.nasa.javaGenes.EOSscheduling;

import junit.framework.TestCase;
import gov.nasa.alsUtility.RandomNumber;

public class TaskTest extends TestCase {

public TaskTest(String name) {super(name);}

public void testSortingAccessWindows() {
    final int numberOfWindows = 50;
    Task task = new Task(5);
    for(int i = 0; i < numberOfWindows; i++) {
        int start = RandomNumber.getIndex(10000);
        int end = RandomNumber.getIndex(100)+start;
        AccessWindow w = new AccessWindow(start,end);
        task.addAccessWindow(w);
    }
    assertTrue("0",!task.areAccessWindowsInEarliestFirstOrder());
    task.hasAllAccessWindowsNow();
    assertTrue("1",task.areAccessWindowsInEarliestFirstOrder());
    AccessWindow[] array = task.getAccessWindows();
    assertTrue("2",array.length == numberOfWindows);
    int lastOne = -1;
    for(int i = 0; i < array.length; i++) {
        int thisOne = array[i].getStart();
        assertTrue("i = " + i, thisOne >= lastOne);
        lastOne = thisOne;
    }
}
public void testIsExecutable() {
    final int numberOfWindows = 50;
    Task task = new Task(5);
    assertTrue("-1",!task.isExecutable());
    for(int i = 0; i < numberOfWindows; i++) {
        int start = RandomNumber.getIndex(10000);
        int end = RandomNumber.getIndex(100)+start;
        AccessWindow w = new AccessWindow(start,end);
        task.addAccessWindow(w);
        assertTrue(i+"",task.isExecutable());
    }
    task.hasAllAccessWindowsNow();
    assertTrue((numberOfWindows+1)+"",task.isExecutable());
}
}
