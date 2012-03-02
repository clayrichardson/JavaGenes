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

import java.util.Iterator;
import java.util.HashMap;
import gov.nasa.javaGenes.weightNetwork.WeightList;
import gov.nasa.javaGenes.weightNetwork.Weight;
import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.EOSscheduling.SSRNode;
import gov.nasa.javaGenes.EOSscheduling.Node;
import gov.nasa.javaGenes.EOSscheduling.Satellite;
import gov.nasa.javaGenes.EOSscheduling.AccessWindow;
import gov.nasa.alsUtility.ReinitializableFloat;
import gov.nasa.alsUtility.ReinitializableInt;
import gov.nasa.alsUtility.Utility;

public class SSRcontenders extends WeightList {
private static final boolean debug = gov.nasa.javaGenes.EOSscheduling.Debug.debug;
protected static float CONTENTION_WHEN_CAPACITY_IS_0 = 10000000;
protected SSRNode theSSRNode;

/** used in SSRneed propagation to avoid updating twice for same task
 marking can fail is used more than 4 billion times per process due to integer overflow */
private static int markValue = java.lang.Integer.MIN_VALUE; 
private int myMark = markValue;

protected ReinitializableFloat SSRneedSum = new ReinitializableFloat();
protected float SSRneedSumChange = 0;
protected ReinitializableFloat SSRcontention = new ReinitializableFloat();
protected float SSRoldContention = -1; // should never stay like this
protected HashMap numberOfAccessWindowsPerTask = new HashMap();
public void reinitialize() {
    super.reinitialize();
    SSRneedSum.reinitialize();
    SSRneedSumChange = 0;
    SSRcontention.reinitialize();
    SSRoldContention = -1;
    for(Iterator it = numberOfAccessWindowsPerTask.values().iterator(); it.hasNext();)
        ((ReinitializableInt)it.next()).reinitialize();
}
public void assertCorrect() {
    Error.assertNearlyEqual(getSSRneedSum(),getSSRneedSumFromCalculation(),1e-3);
    Error.assertNearlyEqual(SSRneedSumChange,0);
    // for completeness, not really a good check since SSRneedSum isn't recalculated
    // must stay this way because getSSRcontentionSumFromCalculation() used in performance
    // required situations
    Error.assertNearlyEqual(getSSRcontention(),getSSRcontentionFromCalculation());
    Error.assertEqual(currentSize(),numberOfUndecidedAccessWindowWeights());
    Error.assertTrue(getSSRcapacity() >= 0);
    if (getSSRcapacity() == 0)
        Error.assertTrue(isCurrentlyEmpty()); // assumes every task requires SSR
    int count = 0;
    for(Iterator it = numberOfAccessWindowsPerTask.values().iterator(); it.hasNext();)
        count += ((ReinitializableInt)it.next()).getCurrent();
    Error.assertEqual(numberOfUndecidedAccessWindowWeights(),count);
}
public int numberOfUndecidedAccessWindowWeights() {
    int count = 0;
    for(int i = 0; i < getInitialSize(); i++) 
        if (getAccessWindowWeight(i).status == Constants.UNDECIDED)
            count++;
    return count;
}
public void setInitialSSRneedSumAndContention() {
    float sum = 0;
    for(Iterator it = numberOfAccessWindowsPerTask.keySet().iterator(); it.hasNext();) {
        final TaskWeight t = (TaskWeight)it.next();
        if (debug) Error.assertTrue(t.status == Constants.UNDECIDED);
        sum += t.getSSRneed();
    }
    SSRneedSum.setInitial(sum); 
    SSRcontention.setInitial(getSSRcontentionFromCalculation());
}
public float getSSRneedSumFromCalculation() {
    float sum = 0;
    for(Iterator it = numberOfAccessWindowsPerTask.keySet().iterator(); it.hasNext();) {
        final TaskWeight t = (TaskWeight)it.next();
        if (t.status == Constants.UNDECIDED &&  getTaskWeightCount(t) > 0)
            sum += t.getSSRneed();
    }
    return sum;
}
/** number of UNDECIDED AccessWindows 't' has contending for this SSR node */
public int getTaskWeightCount(TaskWeight t) {
    return ((ReinitializableInt)numberOfAccessWindowsPerTask.get(t)).getCurrent();
}
/** number of tasks contending for this SSR node */
public int getCurrentTaskWeightCount() {
    return numberOfAccessWindowsPerTask.keySet().size();
}
public int getCurrentAccessWeightCount() {
    return currentSize();
}
public void removeAccessWindowWeightAndCapacity(SSRcontendersNode node) {
    removeFromWeightList(node);
    // actual capacity removal from SSRNode done by SSRTimeline.insertAt();
    if (debug) Error.assertTrue(getSSRcapacity() >= 0);
    for(SSRcontendersNode n = getFirstSCN(); n != null; n = n.getNextSCN()) {
        AccessWindowWeight aw = n.getAccessWindowWeight();
        if (debug) Error.assertTrue(aw != node.getAccessWindowWeight());
        if (aw.getSSRuse() > getSSRcapacity() && node.getTaskWeight() != aw.getTaskWeight())
            aw.unschedulableSoRemoveFromContentionNetwork();
    }
}
public void removeFromWeightList(Weight weight) {
    if (debug) Error.assertTrue(weight instanceof SSRcontendersNode);
    super.removeFromWeightList(weight);
    SSRcontendersNode node = (SSRcontendersNode)weight;
    ReinitializableInt value = (ReinitializableInt)numberOfAccessWindowsPerTask.get(node.getTaskWeight());
    if (debug) {
        Error.assertTrue(value != null);
        Error.assertTrue(value.getCurrent() >= 1);
    }
    value.add(-1);
    if (value.getCurrent() == 0)
        SSRneedChangedBy(-node.getSSRneed());
}
public void SSRneedChangedBy(float amount) {
    SSRneedSumChange += amount;
    if (debug) 
        Error.assertTrue(SSRneedSumChange >= -SSRneedSum.getCurrent() 
                        || Utility.nearlyEqual(SSRneedSumChange,-SSRneedSum.getCurrent(),1e-3));
    UpdateAndPropagateLists.SSRneedChanged.add(this);
}
public void updateAndPropagate(int which) {
    if (debug) 
        Error.assertTrue(SSRneedSumChange >= -SSRneedSum.getCurrent() 
                        || Utility.nearlyEqual(SSRneedSumChange,-SSRneedSum.getCurrent(),1e-3));
    SSRneedSum.add(SSRneedSumChange);
    if (SSRneedSum.getCurrent() < 0) // can happen due to numerical issues
        SSRneedSum.setCurrent(0);
    SSRneedSumChange = 0;
    SSRoldContention = SSRcontention.getCurrent(); // needed in AW updateAndPropagate(int)
    SSRcontention.setCurrent(getSSRcontentionFromCalculation());
    for(SSRcontendersNode n = getFirstSCN(); n != null; n = n.getNextSCN()) {
        AccessWindowWeight aw = n.getAccessWindowWeight();
        if (debug) Error.assertTrue(aw.status == Constants.UNDECIDED);
        UpdateAndPropagateLists.AWcontentionChanged.add(aw);
    }
}

public void addAccessWindowWeight(AccessWindowWeight aw) {add(new SSRcontendersNode(aw));}
public void add(Weight w) {
    SSRcontendersNode node = (SSRcontendersNode)w;
    super.add(node);
    TaskWeight t = node.getTaskWeight();
    ReinitializableInt value = (ReinitializableInt)numberOfAccessWindowsPerTask.get(t);
    if (value != null)
        value.addToInitial(1);
    else
        numberOfAccessWindowsPerTask.put(t,new ReinitializableInt(1));
}
protected void setSSRnode(Node node) {theSSRNode = (SSRNode)node;}
protected SSRNode getSSRnode() {return theSSRNode;}
public int getSSRcapacity() {return theSSRNode.getRemainingCapacity();}
// of AccessWindowWeights -- all the same
public float getSSRcontention() {return SSRcontention.getCurrent();}
public float getSSRcontentionFromCalculation() {
    if (getSSRcapacity() == 0)
        return CONTENTION_WHEN_CAPACITY_IS_0;
    return getSSRneedSum() / (float)getSSRcapacity();
}
public SSRcontendersNode getFirstSCN() {return (SSRcontendersNode)getFirst();}

/** make sure 'this' is consistent with the Satellite's SSRtimeline */
public void assertIsValid(Satellite satellite) {
    for(SSRcontendersNode n = getFirstSCN(); n != null; n = n.getNextSCN()) {
        AccessWindowWeight aw = n.getAccessWindowWeight();
        AccessWindow a = aw.getAccessWindow();
        Error.assertTrue(satellite == a.getSatellite());
        Error.assertTrue(theSSRNode.includes(a.getSSRtime()));
    }
}
public AccessWindowWeight getAccessWindowWeight(int index) {
    return ((SSRcontendersNode)getWeight(index)).getAccessWindowWeight();
}
public float getSSRneedSum() {return SSRneedSum.getCurrent();}
public float getSSRoldContention() {return SSRoldContention;}
// marking is implenented by using a current integer (markValue) to indicate what has been marked
static public void beginMarking() {markValue++;}
public boolean isMarked() {return myMark == markValue;}
public void setMark() {myMark = markValue;}
}
