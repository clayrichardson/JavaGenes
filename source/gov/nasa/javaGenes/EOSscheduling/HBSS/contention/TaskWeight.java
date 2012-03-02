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

import gov.nasa.javaGenes.weightNetwork.Weight;
import java.lang.Math;
import gov.nasa.javaGenes.EOSscheduling.Task;
import gov.nasa.javaGenes.EOSscheduling.TakeImage;
import gov.nasa.alsUtility.ReinitializableFloat;
import gov.nasa.alsUtility.ReinitializableInt;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.Utility;

public class TaskWeight extends gov.nasa.javaGenes.EOSscheduling.HBSS.TaskWeight {
private final static boolean debug = gov.nasa.javaGenes.EOSscheduling.Debug.debug;
public static final int NEED = 1;
public static final int DIFFICULTY = 2;

protected float priorityTimesWeightFactor;
protected float priority;
protected float SSRuseTimesPriority;
protected float sensorAvailDifficultyWeightFactor;
protected float SSRdifficultyWeightFactor;

protected int status = Constants.UNDECIDED;
protected ReinitializableFloat sensorAvailNeed = new ReinitializableFloat();
protected ReinitializableFloat SSRneed = new ReinitializableFloat();
protected ReinitializableFloat sensorAvailDifficulty = new ReinitializableFloat();
protected ReinitializableFloat SSRdifficulty = new ReinitializableFloat();
protected ReinitializableFloat reinitializableWeight = new ReinitializableFloat();
protected boolean recalculateSensorAvailDifficulty = false;
protected boolean recalculateSSRdifficulty = false;
protected AccessWindowWeight currentMinimumContender = null;

public void reinitialize() {
    super.reinitialize(); // reinitializes AccessWindowList [getContentionAWlist() return value]
    status = Constants.UNDECIDED;
    sensorAvailNeed.reinitialize();
    SSRneed.reinitialize();
    sensorAvailDifficulty.reinitialize();
    SSRdifficulty.reinitialize();
    reinitializableWeight.reinitialize();
    recalculateSensorAvailDifficulty = false;
    recalculateSSRdifficulty = false;
    currentMinimumContender = null;
}
public void assertCorrect() {
    switch (status) {
    case Constants.UNDECIDED:
        Error.assertEqual(status,Constants.UNDECIDED);
        Error.assertNearlyEqual(getSensorAvailNeed(),getSensorAvailNeedFromCalculation());
        Error.assertNearlyEqual(getSSRneed(),getSSRneedFromCalculation());
        getContentionAWlist().assertCorrect();
        Error.assertNearlyEqual(getSensorAvailDifficulty(),getSensorAvailDifficultyFromCalculation());
        Error.assertNearlyEqual(getSSRdifficulty(),getSSRdifficultyFromCalculation());
        Error.assertNearlyEqual(getWeight(),getWeightFromCalculation());
        return;
    case Constants.SCHEDULED:
        getContentionAWlist().assertIsScheduled();
        return;
    case Constants.UNSCHEDULABLE:
        getContentionAWlist().assertAllUnSchedulable();
        return;
    }
}
/** for testing only, to generate different, but non-functional, TaskWeights */
protected TaskWeight(int meaningless) {}
/**
assumes Task requires SSR.  Note that initial values for SensorAvail need calculations are set up here.
*/
public TaskWeight(Task inTask,float priorityWeightFactor,float sensorAvailWeightFactor,float SSRweightFactor) {
    this(inTask,priorityWeightFactor,sensorAvailWeightFactor,SSRweightFactor,0);
}
public TaskWeight(Task inTask,float priorityWeightFactor,float sensorAvailWeightFactor,float SSRweightFactor,float nadirPointingWeightFactor) {
    task = inTask;
    Error.assertTrue(task.numberOfAccessWindows() > 0);

    priority = (float)task.getPriority();
    priorityTimesWeightFactor = priority*priorityWeightFactor; // never changes
    SSRuseTimesPriority = getSSRuse() * priority; // never changes

    sensorAvailNeed.setInitial(priority/task.numberOfAccessWindows());
    SSRneed.setInitial(SSRuseTimesPriority/task.numberOfAccessWindows());
 
    sensorAvailDifficultyWeightFactor = sensorAvailWeightFactor;
    SSRdifficultyWeightFactor = SSRweightFactor;
    accessWindowsList = new AccessWindowsList(task.numberOfAccessWindows());
    for(int i = 0; i < task.numberOfAccessWindows(); i++)
        getContentionAWlist().add(new AccessWindowWeight(this,task.getAccessWindow(i),sensorAvailWeightFactor,SSRweightFactor,nadirPointingWeightFactor));
}
public void calculateInitialAccessWindowSensorAvailContention() {
    for(int i = 0; i < getContentionAWlist().getInitialSize(); i++)
        getContentionAW(i).calculateInitialSensorAvailContention();
}
public void initializeWeights() {
    for(int i = 0; i < getContentionAWlist().getInitialSize(); i++)
        getContentionAW(i).initializeWeight();
    reinitializableWeight.setInitial(getWeightFromCalculation());
    getContentionAWlist().initializeWeightSum();
}
public void calculateInitialSensorAvailDifficulty() {
    currentMinimumContender = getContentionAWlist().getInitialMinContender();
    if (currentMinimumContender == null)
        sensorAvailDifficulty.setInitial(0);
    else
        sensorAvailDifficulty.setInitial(currentMinimumContender.getSensorAvailContention());
}
public void calculateInitialSSRdifficulty() {
    currentMinimumContender = getContentionAWlist().getInitialMinContender();
    if (currentMinimumContender == null)
        SSRdifficulty.setInitial(0);
    else
        SSRdifficulty.setInitial(currentMinimumContender.getSSRcontention());
}

public void unschedulable(gov.nasa.javaGenes.EOSscheduling.HBSS.AccessWindowWeight inAw) {
    AccessWindowWeight aw = (AccessWindowWeight)inAw;
    if (debug) {
        Error.assertTrue(status == Constants.UNDECIDED);
        Error.assertTrue(aw.status == Constants.UNDECIDED);
        Error.assertTrue(aw.getTaskWeight() == this);
    }
    UpdateAndPropagateLists.reinitialize();
    aw.unschedulableSoRemoveFromContentionNetwork();    
    UpdateAndPropagateLists.updateAndPropagate();
}
/**
assumes that the Timelines have already been updated (by Timeline.insertAt())
*/
public void scheduled(gov.nasa.javaGenes.EOSscheduling.HBSS.AccessWindowWeight inAw) {
    AccessWindowWeight aw = (AccessWindowWeight)inAw;
    if (debug) {
        Error.assertTrue(status == Constants.UNDECIDED);
        Error.assertTrue(aw.status == Constants.UNDECIDED);
        Error.assertTrue(aw.getTaskWeight() == this);
    }
    UpdateAndPropagateLists.reinitialize();
    status = Constants.SCHEDULED;
    removeFromWeightList();
    aw.scheduled();
    getContentionAWlist().unschedulableSoRemoveFromContentionNetwork();
    UpdateAndPropagateLists.updateAndPropagate();
}
public void removeFromWeightList(AccessWindowWeight aw) {
    if (debug) {
        Error.assertTrue(getNumberOfUndecidedAccessWindows() > 0);
        Error.assertTrue(aw.status != Constants.UNDECIDED);
    }
    if (status != Constants.UNDECIDED)
        return;
    getContentionAWlist().removeFromWeightList(aw);
    if (getNumberOfUndecidedAccessWindows() == 0) {
        removeFromWeightList();
        status = Constants.UNSCHEDULABLE;
        // aw will take care of propagation since it must do so in all cases
    } else {
        UpdateAndPropagateLists.taskNeedChanged.add(this);
        if (aw == currentMinimumContender) {
            currentMinimumContender = getContentionAWlist().getCurrentMinContender();
            UpdateAndPropagateLists.taskDifficultyChanged.add(this);
        }
    }
}
public void contentionChanged(AccessWindowWeight whatChanged, float oldContention) {
    if (currentMinimumContender == null || whatChanged.getContention() < currentMinimumContender.getContention()) {
        currentMinimumContender = whatChanged;
        UpdateAndPropagateLists.taskDifficultyChanged.add(this);
    } else if (whatChanged == currentMinimumContender) {
        if (whatChanged.getContention() > oldContention) {
            currentMinimumContender = getContentionAWlist().getCurrentMinContender();
            Error.assertTrue(currentMinimumContender != null);
        }
        UpdateAndPropagateLists.taskDifficultyChanged.add(this);
    }
}
public void updateAndPropagate(int which) {
    if (status != Constants.UNDECIDED)
        return;
    switch(which) {
    case NEED: updateAndPropagateTaskNeed(); return;
    case DIFFICULTY: updateAndPropagateTaskDifficulty(); return;
    default: Error.fatal("bad case");
    }
}
// NOTE: I think either both needs change or neither, so just do both
protected void updateAndPropagateTaskNeed() {
    if (status != Constants.UNDECIDED)
        return;
    if (debug) Error.assertTrue(getNumberOfUndecidedAccessWindows() > 0);

    float oldNeed = sensorAvailNeed.getCurrent();
    sensorAvailNeed.setCurrent(priority / (float)getNumberOfUndecidedAccessWindows());
    float sensorAvailNeedChange = sensorAvailNeed.getCurrent() - oldNeed;

    float oldSSRneed = SSRneed.getCurrent();
    SSRneed.setCurrent(SSRuseTimesPriority / (float)getNumberOfUndecidedAccessWindows());
    float SSRneedChange = SSRneed.getCurrent() - oldSSRneed;

    getContentionAWlist().fromMyTaskNeedChangedBy(sensorAvailNeedChange,SSRneedChange);
}
protected void updateAndPropagateTaskDifficulty() {
    if (status != Constants.UNDECIDED)
        return;
    float oldWeight = getWeight();
    sensorAvailDifficulty.setCurrent(getSensorAvailDifficultyFromCalculation());
    SSRdifficulty.setCurrent(getSSRdifficultyFromCalculation());
    reinitializableWeight.setCurrent(getWeightFromCalculation());
    weightChanged(oldWeight,getWeight());
}

public float getWeight() {return reinitializableWeight.getCurrent();}
public float getWeightFromCalculation() {
    return  priorityTimesWeightFactor 
            + sensorAvailDifficultyWeightFactor * sensorAvailDifficulty.getCurrent() 
            + SSRdifficultyWeightFactor * SSRdifficulty.getCurrent();
}
public float getSensorAvailNeed() {return sensorAvailNeed.getCurrent();}
public float getSensorAvailNeedFromCalculation() {return priority / (float)getContentionAWlist().currentSize();}
public float getSSRneed() {return SSRneed.getCurrent();}
public float getSSRneedFromCalculation() {return SSRuseTimesPriority / (float)getContentionAWlist().currentSize();}

public float getSensorAvailDifficulty() {return sensorAvailDifficulty.getCurrent();}
public float getSSRdifficulty() {return SSRdifficulty.getCurrent();}

public float getSensorAvailDifficultyFromCalculation() {
    if (currentMinimumContender == null)
        currentMinimumContender = getContentionAWlist().getCurrentMinContender();
    if (currentMinimumContender == null)
        return 0;
    else
        return currentMinimumContender.getSensorAvailContention();
}
public float getSSRdifficultyFromCalculation() {
    if (currentMinimumContender == null)
        currentMinimumContender = getContentionAWlist().getCurrentMinContender();
    if (currentMinimumContender == null)
        return 0;
    else
        return currentMinimumContender.getSSRcontention();
}
public AccessWindowsList getContentionAWlist() {
    return (gov.nasa.javaGenes.EOSscheduling.HBSS.contention.AccessWindowsList)accessWindowsList;
} 
public AccessWindowWeight getContentionAW(int i) {
    return (gov.nasa.javaGenes.EOSscheduling.HBSS.contention.AccessWindowWeight)getContentionAWlist().getAccessWindowWeight(i);
}
public Task getTask() {return task;}
public int getSSRuse() {return ((TakeImage)task).getSSRuse();}
public int getNumberOfUndecidedAccessWindows() {return getContentionAWlist().getNumberOfUndecidedAccessWindows();}
public int getNumberOfAccessWindows() {return getContentionAWlist().getInitialSize();}
}
