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
//  Created by Al Globus on Fri Jan 31 2003.
package gov.nasa.javaGenes.EOSscheduling;

import junit.framework.TestCase;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.javaGenes.core.Evolvable;

public class SqueakyWheelTournamentSwapMutationTest extends TestCase {
private EOSModel model;
private EOSschedulingEvolvable permutation;
private SqueakyWheelTournamentSwapMutation maker;
    
public SqueakyWheelTournamentSwapMutationTest(String name) {super(name);}

private void check(String name,int[] array, int moveForward, int moveBack) {
    maker.indices = array;
    maker.setUpIndicesFromArray(permutation);
    assertTrue(name + "-1", maker.moveForwardIndex == moveForward);
    assertTrue(name + "-2", maker.moveBackIndex == moveBack);
}

public void testSetUpIndicesFromArray() {
    int[] a1 = {0,1,2};
    check("1",a1,0,1);
    int[] a2 = {0,1,4,2};
    check("2",a2,0,1);
    int[] a3 = {6,5,4,2};
    check("3",a3,6,5);
    int[] a4 = {7,4,5,6,0,1,3,2};
    check("4",a4,3,1);
    int[] a5 = {7,4,5,6};
    check("5",a5,6,5);
}

public void setUp() {
    model = new EOSModel();
    permutation = new EOSschedulingEvolvable(8); 
    addATask(0,7,true,1);
    addATask(1,6,false,1);
    addATask(2,5,true,1);

    addATask(3,4,true,2);
    addATask(4,3,false,2);
    addATask(5,2,true,2);

    addATask(6,1,true,1);
    addATask(7,0,false,1);
    assertTrue("1",permutation.isPermutation());
    maker = new SqueakyWheelTournamentSwapMutation(model,1,2);
}
public void testMakeChildrenCreatesPermutation() {
    final int number = 100;
    final int repetitions = 110;
    model = new EOSModel();
    permutation = new EOSschedulingEvolvable(number); 
    DoubleInterval priorityRange = new DoubleInterval(1,6);
    for(int i = 0; i < number; i++)
        addATask(i,number-i-1,RandomNumber.getBoolean(),priorityRange.random());
    EOSschedulingEvolvable parent = permutation;
    Evolvable p[] = new Evolvable[1];
    p[0] = parent;
    EOSschedulingEvolvable kid;

    assertTrue("parent", parent.isPermutation());
    maker = new SqueakyWheelTournamentSwapMutation(model,1,2);
    for(int i = 0; i < repetitions; i++) {
        kid = (EOSschedulingEvolvable)maker.makeChildren(p)[0];
        assertTrue("1-"+i,kid.isPermutation());
    }
    maker = new SqueakyWheelTournamentSwapMutation(model,10,20);
    for(int i = 0; i < repetitions; i++) {
        kid = (EOSschedulingEvolvable)maker.makeChildren(p)[0];
        assertTrue("2-"+i,kid.isPermutation());
    }
}

public void testMoreDeserving() {
    check("1",0,1);
    check("2",2,1);
    check("2.5",0,2);
    check("3",3,0);
    check("4",3,1);
    check("5",3,4);
    check("6",3,5);
    check("7",4,5);
    check("8",2,7);
    check("9",4,7);
    check("10",6,2);
    check("11",6,5);
}
private void check(String name, int goForward, int goBack) {
    maker.moveForwardIndex = goBack;
    assertTrue(name+"-1-",maker.moreDeservingToMoveForward(permutation,goForward));
    maker.moveForwardIndex = goForward;
    assertTrue(name+"-2-",!maker.moreDeservingToMoveForward(permutation,goBack));
    maker.moveBackIndex = goBack;
    assertTrue(name+"-3-",!maker.moreDeservingToMoveBack(permutation,goForward));
    maker.moveBackIndex = goForward;
    assertTrue(name+"-4-", maker.moreDeservingToMoveBack(permutation,goBack));
}
private void addATask(int taskNumber,int index,boolean scheduled, double priority) {
    Task task = new Task(5);
    task.setPriority(priority);
    model.addTask(task);
    TaskPlacementData data = new TaskPlacementData();
    data.setScheduled(scheduled);
    permutation.setTaskPlacement(taskNumber,data);
    permutation.setIndexAt(index,taskNumber);
}
}

