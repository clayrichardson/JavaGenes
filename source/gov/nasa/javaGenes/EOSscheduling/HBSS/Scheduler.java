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

import gov.nasa.javaGenes.EOSscheduling.EOSschedulingEvolvable;
import gov.nasa.javaGenes.weightNetwork.*;
import gov.nasa.javaGenes.EOSscheduling.SchedulingData;
import gov.nasa.javaGenes.EOSscheduling.EOSModel;
import gov.nasa.javaGenes.EOSscheduling.AccessWindow;
import gov.nasa.javaGenes.EOSscheduling.Task;
import gov.nasa.javaGenes.EOSscheduling.SchedulingData;
import gov.nasa.javaGenes.EOSscheduling.AccessWindow;
import gov.nasa.javaGenes.EOSscheduling.TaskPlacementData;
import gov.nasa.javaGenes.EOSscheduling.TakeImage;
import gov.nasa.javaGenes.EOSscheduling.AvailabilityTimeline;
import gov.nasa.javaGenes.EOSscheduling.SlewTimeline;
import gov.nasa.javaGenes.EOSscheduling.SSRTimeline;
import gov.nasa.alsUtility.Error;

/** Assumes that AccessWindow.getDuration() == Task.getDuration() */
public class Scheduler extends gov.nasa.javaGenes.EOSscheduling.Scheduler {
static final boolean debug = gov.nasa.javaGenes.EOSscheduling.Debug.debug;
protected TaskList taskList;
private SchedulingData schedulingData = new SchedulingData(); // avoids memory allocation/deallocation

/**
only for use of subclasses
*/
protected Scheduler(EOSModel model) {
    super(model);
}
/**
NOTE: requires that there are no tasks with 0 accessWindows

BUG: in the evaluation function used by JavaGenes.Reporter.java the best schedule cannot
be regenerated, so some of the objective values (e.g., MinimizeSlewingFitness and SmallSlewsFitness)
will have values form a new HBSS run, not the actual best run!  Need to figure out how to fix this.
*/
public Scheduler(EOSModel model,float priorityWeight,float numberOfAccessWindowsWeight,float SSRweight) {
    super(model);
    model.numberTasksAndAccessWindows();
    taskList = new TaskList(model,priorityWeight,numberOfAccessWindowsWeight,SSRweight);
}
public void createSchedule(EOSschedulingEvolvable evolvable) {
    if (!beginScheduling(evolvable))
        return;
    taskList.reinitialize();
    int index = 0;
    while (taskList.more()) {
        TaskWeight tw = (TaskWeight)taskList.spinWheel();
        evolvable.setIndexAt(index, tw.getTask().getNumber()); // allows checking the order HBSS chose
        index++;
        scheduleTask(evolvable,tw);
        if (debug)
            taskList.assertCorrect();
    }
    if (index < evolvable.getSize())
        evolvable.indexOnUnschedulable(index);
    if (debug) 
        Error.assertTrue(evolvable.isPermutation());
    endScheduling(evolvable);
}
protected void scheduleTask(EOSschedulingEvolvable evolvable,TaskWeight t) {
    if (debug) Error.assertTrue(t != null);
    while(t.getAccessWindowList().more()) {
        AccessWindowWeight aw = (AccessWindowWeight)t.getAccessWindowList().spinWheel();
        if (debug) Error.assertTrue(aw != null);
        AccessWindow a = aw.getAccessWindow();

        TaskPlacementData placementData = evolvable.getTaskPlacement(t.getTask().getNumber());
        placementData.setScheduled(false);
        if (insertIntoTimelines(a,t.getTask())) {
            placementData.setScheduledAtStartOf(a);
            t.scheduled(aw);
            return;
        } else
            t.unschedulable(aw); // will remove from TaskList if aw is last AccessWindow
    }
}
/**
Try to insert Task into location suggested by AccessWindow starting at begining of AccessWindow.
If sucessful, return true.
*/
protected boolean insertIntoTimelines(AccessWindow a,Task t) {
    if (debug)
        Error.assertTrue(a.getDuration() >= t.getDuration());
    
    schedulingData.setDuration(t.getDuration());
    schedulingData.setSlewRequirement(a.getSlewRequirement());
    schedulingData.setSlewable(a.getSensor().getSlewMotor());
    schedulingData.setSensor(a.getSensor());

    if (debug) Error.assertTrue(t instanceof TakeImage);
    schedulingData.setSSRuse(((TakeImage)t).getSSRuse());
 
    AvailabilityTimeline availableTimeline = a.getSensor().getAvailabilityTimeline();
    SlewTimeline         slewTimeline =      a.getSensor().getSlewTimeline();
    SSRTimeline          SSRtimeline =       a.getSensor().getSatellite().getSSRtimeline();

    final int start = a.getStart();
    if (availableTimeline.fits(start,schedulingData)
        &&   slewTimeline.fits(start,schedulingData)
        &&   SSRtimeline.fits(start,schedulingData)) {

        availableTimeline.insertAt(start,schedulingData);
        slewTimeline.insertAt(start,schedulingData);
        SSRtimeline.insertAt(start,schedulingData);
        return true;
    }
    return false;
}
public void setUpForRescheduleFromPermutation() {
    super.setUpForRescheduleFromPermutation();
    taskList.reinitialize();
}
protected TaskList getTaskList() {return taskList;} // for testing
//protected SchedulingData getSchedulingData() {return schedulingData;} // for testing
}
