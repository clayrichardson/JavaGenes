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
//  Created by Al Globus on Wed Jul 03 2002.
package gov.nasa.javaGenes.EOSscheduling;

import gov.nasa.javaGenes.permutation.PermutationEvolvable;
import gov.nasa.alsUtility.Error;

public class EOSschedulingEvolvable extends PermutationEvolvable {
protected boolean hasBeenScheduledAlready = false; // used to avoid multiple scheduling during multi-object fitness generation
public static final int NOT_SCHEDULED = Scheduler.NOT_SCHEDULED;
public static final int NO_SENSOR = 0;
protected TaskPlacementData[] taskPlacements; // in numerical order, not permutation order!


public EOSschedulingEvolvable(int size) {
    super(size);
    makeTaskPlacements(size);
}
/**
@arg order must equal "ascending"
*/
public EOSschedulingEvolvable(int size, String order) {
    super(size,order);
    makeTaskPlacements(size);
}
public void prepareForEvaluator(gov.nasa.javaGenes.core.Parameters parameters) {
    EOSschedulingParameters p = (EOSschedulingParameters)parameters;
    p.scheduler.rescheduleFromPermutation(this);
}
/** used by HBSS.Scheduler to fill in tasks that contention found to be unschedulable */
public void indexOnUnschedulable(int index) {
    Error.assertTrue(index <= getSize());
    if (index == getSize())
        return;
    boolean[] examined = new boolean[getSize()]; // initialized to false!
    for(int i = 0; i < index; i++)
        examined[getIndexAt(i)] = true; // Scheduler explicitly tried to scheduled
    int notExaminedCount = 0;
    for(int i = 0; i < getSize(); i++)
        if (!examined[i]) {
            taskPlacements[i].setScheduled(false);
            setIndexAt(index+notExaminedCount,i);
            notExaminedCount++;
        }
    Error.assertTrue(index+notExaminedCount == getSize());
}
private void makeTaskPlacements(int size) {
    taskPlacements = new TaskPlacementData[size];
    for(int i = 0; i < taskPlacements.length; i++)
        taskPlacements[i] = new TaskPlacementData();
}
public int getScheduledTaskCount() {
    int count = 0;
    for(int i = 0; i < getSize(); i++) 
        if (isTaskScheduled(i))
            count++;
    return count;
}
public double getScheduledPrioritySum(EOSModel model) {
    double prioritySum = 0;
    for(int i = 0; i < getSize(); i++)
        if (isTaskScheduled(i))
            prioritySum += model.getTask(i).getPriority();
    return prioritySum;
}
/*
@arg index the task number of the data to be replaced (NOT the permutation index!)
*/
public void setTaskPlacement(int index, TaskPlacementData data) {taskPlacements[index].setFrom(data);}
public TaskPlacementData getTaskPlacement(int index) {return taskPlacements[index];}
public int getStartTime(int index) {return taskPlacements[index].getStartTime();}
public void setStartTime(int index, int time) {taskPlacements[index].setStartTime(time);}
public int getSensorNumber(int index) {return taskPlacements[index].getSensorNumber();}
public void setSensorNumber(int index, int sensor) {taskPlacements[index].setSensorNumber(sensor);}
public SlewRequirement getSlewRequirement(int index) {return taskPlacements[index].getSlewRequirement();}
public void setSlewRequirement(int index, SlewRequirement inSlewRequirement) {taskPlacements[index].setSlewRequirement(inSlewRequirement);}
public boolean hasBeenScheduled() {return hasBeenScheduledAlready;}
public void setHasBeenScheduled(boolean value){hasBeenScheduledAlready = value;}
public boolean isTaskScheduled(int index) {return taskPlacements[index].isScheduled();}

/**
@arg myIndex the permutation index of the task that get new data
@arg fromIndex the permutation index of the task the data comes from
*/
public void setTaskPlacement(int myIndex,EOSschedulingEvolvable from,int fromIndex) {
    int myTaskNumber = getIndexAt(myIndex);
    int fromTaskNumber = from.getIndexAt(fromIndex);
    setTaskPlacement(myTaskNumber,from.getTaskPlacement(fromTaskNumber));
}
public PermutationEvolvable deepCopyPermutationEvolvable() {
  try {
    return (EOSschedulingEvolvable)clone(); // not a true deep copy because of the way makeChildren work combined with doing the actual scheduling in the fitness function
  } catch (CloneNotSupportedException e) {
    Error.fatal("can't clone object: " + e);
    return null;
  }
}

/**
deep copy the permutation, but set up as if never scheduled
@exception CloneNotSupportedException
*/
public Object clone() throws CloneNotSupportedException {
    EOSschedulingEvolvable e = (EOSschedulingEvolvable)super.clone();
    e.hasBeenScheduledAlready = false;
    e.makeTaskPlacements(taskPlacements.length);
    for(int i = 0; i < taskPlacements.length; i++)
        e.taskPlacements[i].setFrom(taskPlacements[i]);

    return e;
}
public boolean isSame(PermutationEvolvable other) {
    // implemented in super, but extra data needs to be checked
    Error.notImplemented();
    return false;
}
public String toString() {
    StringBuffer s = new StringBuffer();
    for(int i = 0; i < permutation.length; i++) {
        int task = permutation[i];
        s.append(permutation[i] 
            + "," + (taskPlacements[task].isScheduled() ? 1 : 0)
            + "," + taskPlacements[task].getStartTime() 
            + "," + taskPlacements[task].getWindowNumber() 
            + "," + taskPlacements[task].getSensorNumber() + "\t");
    }
    return s.toString();
}
}