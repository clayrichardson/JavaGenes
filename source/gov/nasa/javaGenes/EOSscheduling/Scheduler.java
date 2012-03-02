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

import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.ExtendedVector;
import gov.nasa.javaGenes.core.Evolvable;

public class Scheduler implements java.io.Serializable {
/**
Must be less than the earliest legal timestep
*/
static final public int NOT_SCHEDULED = -1;

protected EOSModel model;
protected ExtendedVector placers = new ExtendedVector();

public String toString() {return "Scheduler";}

public Scheduler(EOSModel inModel) {
    model = inModel;
    Error.assertNotNull(model);
}
public void addPlacer(Placer placer) {
    placers.addElement(placer);
}
protected boolean beginScheduling(EOSschedulingEvolvable evolvable) {
    if (evolvable.hasBeenScheduled())
        return false;
    model.beginScheduling();
    return true;
}
protected void endScheduling(EOSschedulingEvolvable evolvable) {
    model.endScheduling();
    evolvable.setHasBeenScheduled(true);
}
public void createSchedule(EOSschedulingEvolvable evolvable) {
    if (!beginScheduling(evolvable))
        return;
    //model.beginScheduling();
    for(int i = 0; i < evolvable.getSize(); i++)  {
        int taskNumber = evolvable.getIndexAt(i);
        scheduleTask(evolvable.getTaskPlacement(taskNumber),taskNumber);
    }
    endScheduling(evolvable);
}

public void rescheduleFromPermutation(EOSschedulingEvolvable evolvable) {
    Error.assertTrue(evolvable.hasBeenScheduled());
    setUpForRescheduleFromPermutation();
    for(int i = 0; i < evolvable.getSize(); i++)  {
        int taskNumber = evolvable.getIndexAt(i);
        scheduleTaskFromEvolvableData(model.getTask(taskNumber),evolvable.getTaskPlacement(taskNumber));
    }
    rescheduleFromPermutationOver();
}
public void setUpForRescheduleFromPermutation() {
    model.beginScheduling();
}
public void rescheduleFromPermutationOver(){
    model.endScheduling();
}
public void scheduleTaskFromEvolvableData(Task t, TaskPlacementData tpd) {
    if (Debug.debug) Error.assertTrue(t instanceof TakeImage);
    if (!tpd.isScheduled())
        return;
    AccessWindow a = t.getAccessWindow(tpd.getWindowNumber());

    SchedulingData schedulingData = new SchedulingData();
    schedulingData.setDuration(t.getDuration());
    schedulingData.setSlewRequirement(a.getSlewRequirement());
    schedulingData.setSlewable(a.getSensor().getSlewMotor());
    schedulingData.setSensor(a.getSensor());
    schedulingData.setSSRuse(((TakeImage)t).getSSRuse());
 
    AvailabilityTimeline availableTimeline = a.getSensor().getAvailabilityTimeline();
    SlewTimeline         slewTimeline =      a.getSensor().getSlewTimeline();
    SSRTimeline          SSRtimeline =       a.getSensor().getSatellite().getSSRtimeline();

    final int start = a.getStart();
    Error.assertTrue(availableTimeline.fits(start,schedulingData));
    Error.assertTrue(slewTimeline.fits(start,schedulingData));
    Error.assertTrue(SSRtimeline.fits(start,schedulingData));

    availableTimeline.insertAt(start,schedulingData);
    slewTimeline.insertAt(start,schedulingData);
    SSRtimeline.insertAt(start,schedulingData);
}


/**
@arg placementData will be set to the proper values during scheduling
*/
public boolean scheduleTask(TaskPlacementData placementData,int taskIndex) {
    Placer placer = (Placer)placers.getRandomElement();
    return placer.placeInTimelines(placementData,model.getTask(taskIndex));
}
}
