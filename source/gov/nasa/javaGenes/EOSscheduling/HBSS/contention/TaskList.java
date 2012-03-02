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
package gov.nasa.javaGenes.EOSscheduling.HBSS.contention;

import java.util.Vector;
import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import gov.nasa.javaGenes.EOSscheduling.EOSModel;
import gov.nasa.javaGenes.EOSscheduling.AccessWindow;
import gov.nasa.javaGenes.weightNetwork.RouletteWheel;
import gov.nasa.javaGenes.weightNetwork.Weight;
import gov.nasa.javaGenes.EOSscheduling.Task;
import gov.nasa.alsUtility.Error;

public class TaskList extends gov.nasa.javaGenes.EOSscheduling.HBSS.TaskList {
protected EOSModel model;
protected AllSSRcontenders allSSRcontenders;

public TaskList(EOSModel inModel,float priorityWeight,float availableWeight,float SSRweight) {
    this(inModel,priorityWeight,availableWeight,SSRweight,0);
}
public TaskList(EOSModel inModel,float priorityWeight,float availableWeight,float SSRweight,float nadirPointingWeight) {
    Error.assertTrue(inModel != null);
    model = inModel;
    model.numberTasksAndAccessWindows();
    for(int i = 0; i < model.getNumberOfTasks(); i++)
        add(new TaskWeight(model.getTask(i),priorityWeight,availableWeight,SSRweight,nadirPointingWeight));
        // NOTE: initial need values are calculated in the constructor
    createAccessWindowSensorAvailIncompatibilityNetwork(model);
    for(int i = 0; i < getInitialSize(); i++)
        getTaskWeight(i).calculateInitialAccessWindowSensorAvailContention();
    // SSR need and contention must be set up when first schedule created; i.e., in reinitialize()
    // because SSR data not available yet.  Both difficulties and the weights must be set up at this time too.
}
/**
First time through sets up SSR contention lists.  Done here because SSR timelines are not set up until
just before a schedule is generated
*/
protected boolean firstTime = true;
public void reinitialize() {
    if (firstTime)
        allSSRcontenders = new AllSSRcontenders(model,this);
    allSSRcontenders.reinitializeAndAttachContendersToTimelineNodes(); // probably unnecessary first time
    if (firstTime) {
        for(int i = 0; i < getInitialSize(); i++)
            getTaskWeight(i).calculateInitialSensorAvailDifficulty();
        for(int i = 0; i < getInitialSize(); i++)
            getTaskWeight(i).calculateInitialSSRdifficulty();
        for(int i = 0; i < getInitialSize(); i++)
            getTaskWeight(i).initializeWeights();
        initializeWeightSum();
    }
    firstTime = false;
    for(int i = 0; i < getInitialSize(); i++)
        getTaskWeight(i).reinitialize();
    super.reinitialize(); // must be here because calls AccessWindowList.reinitialize() and SSR must be set up
}
public void createAccessWindowSensorAvailIncompatibilityNetwork(EOSModel model) {
    // create Vector of accessWindows for each sensor
    HashMap sensorToAccessWindowMap = new HashMap();
    Vector allSensors = model.getSensors();
    for(int i = 0; i < allSensors.size(); i++) 
        sensorToAccessWindowMap.put(allSensors.get(i),new Vector());
    for(int i = 0; i < getInitialSize(); i++) {
        AccessWindowsList accessWindows = getTaskWeight(i).getContentionAWlist();
        for(int j = 0; j < accessWindows.getInitialSize(); j++) {
            AccessWindowWeight aw = (AccessWindowWeight)accessWindows.getAccessWindowWeight(j);
            Vector v = (Vector)sensorToAccessWindowMap.get(aw.getSensor());
            Error.assertTrue(v != null);
            v.add(aw);
        }
    }
    // sort the Vectors and calculate the accessWindow incompatibilities
    Collection accessWindows = sensorToAccessWindowMap.values();
    for(Iterator it = accessWindows.iterator(); it.hasNext();) {
        Vector v = (Vector)it.next();
        Collections.sort(v,new CompareAccessWindowStartTimes());
        for(int i = 0; i < v.size(); i++)
            ((AccessWindowWeight)v.get(i)).createSensorAvailContenders(v,i);
    }
}
/** to order AccessWindowWeights earliest to latest start times */
protected class CompareAccessWindowStartTimes implements java.util.Comparator {
    public int compare(Object o1, Object o2) {
        int t1 = ((AccessWindowWeight)o1).getAccessWindow().getStart();
        int t2 = ((AccessWindowWeight)o2).getAccessWindow().getStart();
        if (t1 < t2) return -1;
        if (t1 > t2) return 1;
        return 0;
        }
}
public TaskWeight getTaskWeight(int index) {return (TaskWeight)getWeight(index);}
public AccessWindowWeight getAccessWindowWeight(int task, int accessWindow) {
    return getTaskWeight(task).getContentionAW(accessWindow);
}
public SSRcontenders getSSRcontenders(int satellite, int node) {
    return allSSRcontenders.getSSRcontenders(satellite,node);
}
/**
For all task and access window weights currently in the contention network,
check status is UNDECIDED, and needs, contentions, difficulties, and weights are consistent with
the current values they depend on.  This makes sure the incremental
updates necessary for speed worked right.  Also check that all AccessWindowWeights
in SensorAvailContention are actually pairs (mutually in contention).
<p>
By convension the first number in the error mesasges will be the actual values, the second the correct value.
<p>
Only used for testing so performance is not an issue (good thing too).
*/
public void assertCorrect() {
    for(Weight w = getFirst(); w != null; w = w.getNext())
        Error.assertEqual(((TaskWeight)w).status,Constants.UNDECIDED);
    for(int i = 0; i < getInitialSize(); i++)
        getTaskWeight(i).assertCorrect();
    allSSRcontenders.assertCorrect();
    Error.assertNearlyEqual(getWeightSum(),getCurrentWeightSumFromCalculation());
    Error.assertEqual(currentSize(), numberOfUndecidedTasks());
}
public int numberOfUndecidedTasks() {
    int count = 0;
    for(int i = 0; i < getInitialSize(); i++)
        if (getTaskWeight(i).status == Constants.UNDECIDED)
            count++;
    return count;
}
}
