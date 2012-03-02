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
/**
puts tasks in available AccessWindow that starts earliest.  This may not be the
earliest possible start time since another window may allow somewhat earlier scheduling
*/ 

public class EarliestFirstPlacer extends Placer {

protected final int numberOfTimelines = 3;
protected Timeline[] timelines = new Timeline[numberOfTimelines];
protected final int AvailableIndex = 2;
protected final int SlewIndex = 1;
protected final int SSRindex = 0;
protected TaskPlacementData placementData; // used to avoid passing parameters (for speed, but might not actually speed things)
protected SchedulingData schedulingData = new SchedulingData();

/**
@arg inPlacementData modify this with the results of scheduling
*/
public boolean placeInTimelines(TaskPlacementData inPlacementData, Task task) {
    Error.assertTrue(task.areAccessWindowsInEarliestFirstOrder());
    schedulingData.setDuration(task.getDuration());
    if (task instanceof TakeImage) // only an issue for testing as of Nov 2002
        schedulingData.setSSRuse(((TakeImage)task).getSSRuse());
    else
       schedulingData.setSSRuse(0);
        
    placementData = inPlacementData;
    placementData.setScheduled(false);
    AccessWindow[] accessWindows = task.getAccessWindows();
    if (accessWindows.length <= 0)
        return false;
    int[] times = new int[numberOfTimelines];

    int firstWindow = getFirstWindow(placementData,accessWindows.length);
    boolean firstTime = true;
    for(int currentWindow = firstWindow; 
        currentWindow != firstWindow || firstTime; 
        currentWindow = increment(currentWindow,accessWindows.length)) {

        firstTime = false;
        AccessWindow w = accessWindows[currentWindow];
        if (schedulingData.getDuration() > w.getDuration())
            continue;
    	schedulingData.setSensor(w.getSensor());
        schedulingData.setSlewable(schedulingData.getSensor().getSlewMotor());
        schedulingData.setSlewRequirement(w.getSlewRequirement());
        int end = w.getEnd();

        int lastTime = w.getStart();
        timelines[AvailableIndex] = schedulingData.getSensor().getAvailabilityTimeline();
        timelines[SlewIndex] = schedulingData.getSensor().getSlewTimeline();
        timelines[SSRindex] = schedulingData.getSensor().getSatellite().getSSRtimeline();
        for(int i = 0; i < times.length; i++) times[i] = NOT_SCHEDULED;
        while(lastTime != NOT_SCHEDULED) {
            for(int i = 0; i < timelines.length; i++) {
                lastTime = timelines[i].findEarliest(lastTime,end,schedulingData);
                if (lastTime == NOT_SCHEDULED)
                    break;
                times[i] = lastTime;
                if (allEqual(lastTime,times))
                    return scheduledIt(lastTime,currentWindow);
            }
        }
    }
    return false;
}
private boolean allEqual(int lastTime, int[] times) {
    for(int i = 0; i < times.length; i++)
        if (lastTime != times[i])
            return false;
    return true;
}
protected int getFirstWindow(TaskPlacementData placementData,int numberOfWindows) {
    return 0; // forces earliest first placement
}
private int increment(int current,int length) {
    int c = current+1;
    if (c >= length)
        return 0;
    return c;
}
private boolean scheduledIt(int start, int window) {
    for(int i = 0; i < timelines.length; i++)
        timelines[i].insertAt(start,schedulingData);
    placementData.setScheduled(true);
    placementData.setStartTime(start);
    placementData.setWindowNumber(window);
    placementData.setSensorNumber(schedulingData.getSensor().getNumber());
    placementData.setSlewRequirement(schedulingData.getSlewRequirement());
    return true;
}
public String toString() {return "EOSEarlistFirstPlacer";}
}


