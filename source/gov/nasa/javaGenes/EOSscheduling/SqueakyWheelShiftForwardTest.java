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
//  Created by Al Globus on Wed Feb 12 2003.
//  Copyright (c) 2003 __MyCompanyName__. All rights reserved.
//

//  Created by Al Globus on Fri Feb 07 2003.
package gov.nasa.javaGenes.EOSscheduling;

import junit.framework.TestCase;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.javaGenes.core.Evolvable;

public class SqueakyWheelShiftForwardTest extends TestCase {
private EOSModel model;
private EOSschedulingEvolvable permutation;
private SqueakyWheelPlacedTSM maker;
private double moveForwardFraction = 0.5;
private double shiftFraction = 0.3;
private DoubleInterval priorityRange;

public SqueakyWheelShiftForwardTest(String name) {super(name);}

public void setUp() {
    model = new EOSModel();
    int numberOfTasks = 5;
    permutation = new EOSschedulingEvolvable(numberOfTasks); 
    priorityRange = new DoubleInterval(1,5);
    for(int i = 0; i < numberOfTasks; i++)
        addATask(i,RandomNumber.getBoolean(),priorityRange.random());
    maker = new SqueakyWheelShiftForward(model,1,2,moveForwardFraction,shiftFraction);
         
}
private void addATask(int taskNumber,boolean scheduled, double priority) {
    Task task = new Task(5);
    task.setPriority(priority);
    model.addTask(task);
    TaskPlacementData data = new TaskPlacementData();
    data.setScheduled(scheduled);
    permutation.setTaskPlacement(taskNumber,data);
}
public void testInteractive() { // for use with debugger to check things hard to test for
    permutation.setToAscending();
    Evolvable parent = permutation;
    Evolvable p[] = new Evolvable[1];
    p[0] = parent;
    Evolvable kid;
    for(int i = 0; i < 10; i++)
        kid = maker.makeChildren(p)[0];
}
public void testMakeChildrenCreatesPermutation() {
    final int number = 100;
    final int repetitions = 110;
    model = new EOSModel();
    permutation = new EOSschedulingEvolvable(number); 
    for(int i = 0; i < number; i++)
        addATask(i,RandomNumber.getBoolean(),priorityRange.random());
    EOSschedulingEvolvable parent = permutation;
    Evolvable p[] = new Evolvable[1];
    p[0] = parent;
    EOSschedulingEvolvable kid;

    assertTrue("parent", parent.isPermutation());
    maker = new SqueakyWheelShiftForward(model,1,2,moveForwardFraction,shiftFraction);
    for(int i = 0; i < repetitions; i++) {
        kid = (EOSschedulingEvolvable)maker.makeChildren(p)[0];
        assertTrue("1-"+i,kid.isPermutation());
    }
    maker = new SqueakyWheelShiftForward(model,10,20,moveForwardFraction,shiftFraction);
    for(int i = 0; i < repetitions; i++) {
        kid = (EOSschedulingEvolvable)maker.makeChildren(p)[0];
        assertTrue("2-"+i,kid.isPermutation());
    }
}
public void testSetUpIndices() {
    for(int i = 0; i < 200; i++) 
        check(i);
}
private void check(int name) {
    int moveForwardIndex = maker.getFirstIndex(permutation);
    int shiftIndex = maker.getSecondIndex(permutation,moveForwardIndex);
    assertTrue(name+"-1",moveForwardIndex > shiftIndex);
    assertTrue(name+"-2",moveForwardIndex >= permutation.getSize() * moveForwardFraction - 1);
    assertTrue(name+"-3",shiftIndex <= moveForwardIndex * shiftFraction + 1);
}
public void testMutate() {
    int[] a1 = {3,0,1,2,4};
    check(1,3,0,a1);
    int[] a2 = {0,3,1,2,4};
    check(2,3,1,a2);
    int[] a3 = {0,4,1,2,3};
    check(3,4,1,a3);
    int[] a4 = {1,0,2,3,4};
    check(4,1,0,a4);
}
private void check(int name, int moveForward, int shiftBefore, int[] result) {
    permutation.setToAscending();
    maker.mutate(moveForward,shiftBefore,permutation);
    assertTrue(name+"",permutation.equals(result));
}

}



