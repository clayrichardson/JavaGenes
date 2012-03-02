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
import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.EOSscheduling.AccessWindow;
import gov.nasa.javaGenes.EOSscheduling.Sensor;
import gov.nasa.javaGenes.EOSscheduling.SlewRequirement;
import gov.nasa.javaGenes.EOSscheduling.Task;
import gov.nasa.javaGenes.EOSscheduling.TakeImage;
import java.util.Vector;
import gov.nasa.alsUtility.ReinitializableFloat;

public class AccessWindowWeight extends gov.nasa.javaGenes.EOSscheduling.HBSS.AccessWindowWeight {
private final static boolean debug = gov.nasa.javaGenes.EOSscheduling.Debug.debug;

protected TaskWeight taskWeight;
protected SSRcontendersNode mySSRcontendersNode;

protected float sensorAvailWeight;
protected float SSRweight;
protected float nadirPointingContribution; // to the weight


protected int status = Constants.UNDECIDED;
protected SensorAvailContenders sensorAvailContenders = new SensorAvailContenders();
protected ReinitializableFloat sensorAvailContention = new ReinitializableFloat();
protected float sensorAvailContentionChange = 0;
protected ReinitializableFloat reinitializableWeight = new ReinitializableFloat();
public void reinitialize() {
    super.reinitialize();
    status = Constants.UNDECIDED;
    sensorAvailContenders.reinitialize();
    sensorAvailContention.reinitialize();
    sensorAvailContentionChange = 0;
    reinitializableWeight.reinitialize();
    // SSR lists are reinitialized in TaskList
}
public void assertCorrect() {
    Error.assertEqual(status,Constants.UNDECIDED);
    Error.assertEqual(getTaskWeight().status,Constants.UNDECIDED);
    Error.assertNearlyEqual(getSensorAvailContention(),getSensorAvailContentionFromCalculation());
    Error.assertNearlyEqual(sensorAvailContentionChange,0);
    //SSR done in AllSSRcontenders
    getSensorAvailContenders().assertAllContendWith(this);
    Error.assertNearlyEqual(getWeight(),getWeightFromCalculation());
}
public AccessWindowWeight(TaskWeight inTaskWeight,AccessWindow inAccessWindow,
                          float inSensorAvailWeight,float inSSRweight) {
    this(inTaskWeight,inAccessWindow,inSensorAvailWeight,inSSRweight,0);
}
public AccessWindowWeight(TaskWeight inTaskWeight,AccessWindow inAccessWindow,
                          float inSensorAvailWeight,float inSSRweight,float nadirPointingWeight) {
    taskWeight = inTaskWeight;
    accessWindow = inAccessWindow; 
    Error.assertTrue(accessWindow != null);

    sensorAvailWeight = inSensorAvailWeight;
    SSRweight = inSSRweight;
    SlewRequirement sr = accessWindow.getSlewRequirement(); 
    nadirPointingContribution = nadirPointingWeight * (float)java.lang.Math.abs(sr.getParameter(0)); //NOTE: only works for 1D slews
}
/**
Assumes that Task duration is equal to AccessWindow duration.
@arg accessWindowsWithSameSensor is AccessWindows using same sensor sorted by start times
*/
public void createSensorAvailContenders(Vector accessWindowsWithSameSensor,int myIndex) {
    Error.assertTrue(0 <= myIndex && myIndex < accessWindowsWithSameSensor.size());
    int maxSlewTime = getSensor().getMaxSlewTimeFrom(getSlew());
    int earliestTime = accessWindow.getStart() - maxSlewTime;
    int latestTime = accessWindow.getEnd() + maxSlewTime;
    // search in past
    for(int i = myIndex-1; i >= 0; i--) {
        AccessWindowWeight aw = (AccessWindowWeight)accessWindowsWithSameSensor.get(i);
        Error.assertTrue(getSensor() == aw.getSensor());
        AccessWindow a = aw.getAccessWindow();
        if (a.getEnd() < earliestTime)
            break;
        if (!accessWindow.couldBeScheduledWith(a) && getTaskWeight() != aw.getTaskWeight())
            sensorAvailContenders.add(aw);
    }
    // search in future
    for(int i = myIndex+1; i < accessWindowsWithSameSensor.size(); i++) {
        AccessWindowWeight aw = (AccessWindowWeight)accessWindowsWithSameSensor.get(i);
        AccessWindow a = aw.getAccessWindow();
        if (a.getStart() > latestTime)
            break;
        if (!accessWindow.couldBeScheduledWith(a) && getTaskWeight() != aw.getTaskWeight())
            sensorAvailContenders.add(aw);    
    }
}
public void calculateInitialSensorAvailContention() {
    float initialValue = 0;
    for(int i = 0; i < sensorAvailContenders.getInitialSize(); i++)
        initialValue += sensorAvailContenders.getAccessWindowWeight(i).getTaskWeight().getSensorAvailNeed();
    sensorAvailContention.setInitial(initialValue+getTaskWeight().getSensorAvailNeed());
}
public float getSensorAvailContentionFromCalculation() {
    return getTaskWeight().getSensorAvailNeed() 
            + getSensorAvailContenders().getSensorAvailNeedSumFromCalculation();
}
public void initializeWeight() {
    reinitializableWeight.setInitial(getWeightFromCalculation());
}

public void scheduled() {
    status = Constants.SCHEDULED;
    getSensorAvailContenders().unSchedulableSoRemoveFromContentionNetwork();
    getSSRcontenders().removeAccessWindowWeightAndCapacity(getSSRcontendersNode());
}
public void unschedulableSoRemoveFromContentionNetwork() {
    if (debug) Error.assertTrue(status == Constants.UNDECIDED);
    status = Constants.UNSCHEDULABLE;
    taskWeight.removeFromWeightList(this);
    mySSRcontendersNode.removeFromWeightList();
    sensorAvailContenders.sensorAvailNeedChangedBy(-taskWeight.getSensorAvailNeed());
}
public void sensorAvailNeedChangedBy(float amount) {
    sensorAvailContentionChange += amount;
    UpdateAndPropagateLists.AWcontentionChanged.add(this);
}
public void updateAndPropagate(int which) {
    if (status != Constants.UNDECIDED)
        return;
    float oldWeight = getWeight();
    float oldContention = getContention();
    sensorAvailContention.add(sensorAvailContentionChange);
    sensorAvailContentionChange = 0;
    taskWeight.contentionChanged(this, oldContention);
    reinitializableWeight.setCurrent(getWeightFromCalculation());
    weightChanged(oldWeight,getWeight());
}
public int getSSRuse() {return taskWeight.getSSRuse();}
public AccessWindowWeight getNextAW() {return (AccessWindowWeight)getNext();}

public SlewRequirement getSlew() {return getAccessWindow().getSlewRequirement();}
public Sensor getSensor() {return accessWindow.getSensor();}
public TaskWeight getTaskWeight() {return taskWeight;}

public float getContention() {
    return sensorAvailWeight*getSensorAvailContention() + SSRweight*getSSRcontention();
}
public SensorAvailContenders getSensorAvailContenders() {return sensorAvailContenders;}
public float getSensorAvailContention() {return sensorAvailContention.getCurrent();}

public float getWeight() {return reinitializableWeight.getCurrent();}
public float getWeightFromCalculation() {
    return nadirPointingContribution + getContention();
}

public float getSSRneed() {return taskWeight.getSSRneed();}
public int getSSRcapacity() {return getSSRcontenders().getSSRcapacity();}
public float getSSRcontention() {return getSSRcontenders().getSSRcontention();}
public float getSSRoldContention() {return getSSRcontenders().getSSRoldContention();}
public SSRcontenders getSSRcontenders() {return mySSRcontendersNode.getSSRcontenders();}
public SSRcontendersNode getSSRcontendersNode() {return mySSRcontendersNode;}
public void setSSRcontendersNode(SSRcontendersNode inSSRcontendersNode) {mySSRcontendersNode = inSSRcontendersNode;}
// for testing
public int getNumberOfSensorAvailContenders() {return getSensorAvailContenders().getCurrentSize();}
}
