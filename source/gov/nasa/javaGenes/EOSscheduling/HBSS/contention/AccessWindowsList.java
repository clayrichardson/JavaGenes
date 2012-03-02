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

import gov.nasa.alsUtility.ReinitializableInt;
import gov.nasa.javaGenes.weightNetwork.Weight;
import gov.nasa.alsUtility.Error;

public class AccessWindowsList extends  gov.nasa.javaGenes.EOSscheduling.HBSS.AccessWindowsList {
private final static boolean debug = gov.nasa.javaGenes.EOSscheduling.Debug.debug;

protected ReinitializableInt numberOfUndecidedAccessWindows;
public void reinitialize() {
    super.reinitialize();
    numberOfUndecidedAccessWindows.reinitialize();
}
public void assertCorrect() {
    Error.assertEqual(numberOfUndecidedAccessWindows.getCurrent(),currentSize());
    Error.assertEqual(countUndecidedAccessWindows(),currentSize());
    for(AccessWindowWeight aw = getFirstAW(); aw != null; aw = aw.getNextAW())
        aw.assertCorrect();
}
public void assertIsScheduled() {
    boolean foundOneScheduled = false;
    for(int i = 0; i < getInitialSize(); i++) {
        final AccessWindowWeight aw = getAccessWindowWeight(i);
        Error.assertTrue(aw.status != Constants.UNDECIDED);
        Error.assertTrue(!aw.getSSRcontenders().currentlyContains(aw));
        if (aw.status == Constants.SCHEDULED) {
            Error.assertTrue(!foundOneScheduled);
            foundOneScheduled = true;
        }
    }
}
public void assertAllUnSchedulable() {
    for(int i = 0; i < getInitialSize(); i++) {
        final AccessWindowWeight aw = getAccessWindowWeight(i);
        Error.assertTrue(aw.status == Constants.UNSCHEDULABLE);
        Error.assertTrue(!aw.getSSRcontenders().currentlyContains(aw));
    }
}
public int countUndecidedAccessWindows() {
    int count = 0;
    for(int i = 0; i < getInitialSize(); i++) {
        final AccessWindowWeight aw = getAccessWindowWeight(i);
        if (aw.status == Constants.UNDECIDED)
            count++;
    }
    return count;
}
public AccessWindowsList(int numberOfAccessWindows) {
    numberOfUndecidedAccessWindows = new ReinitializableInt(numberOfAccessWindows);
}
public void unschedulableSoRemoveFromContentionNetwork() {
    for(int i = 0; i < getInitialSize(); i++) { // linked list not used to avoid possible removal problems
        final AccessWindowWeight aw = getAccessWindowWeight(i);
        if (aw.status == Constants.UNDECIDED)
            aw.unschedulableSoRemoveFromContentionNetwork();
    }
}
public void fromMyTaskNeedChangedBy(float sensorAvailChange, float SSRchange) {
    SSRcontenders.beginMarking();
    for(AccessWindowWeight aw = getFirstAW(); aw != null; aw = aw.getNextAW()) {
        if (debug) Error.assertTrue(aw.status == Constants.UNDECIDED);
        aw.sensorAvailNeedChangedBy(sensorAvailChange);
        aw.getSensorAvailContenders().sensorAvailNeedChangedBy(sensorAvailChange);
        if (!aw.getSSRcontenders().isMarked()) {
            aw.getSSRcontenders().SSRneedChangedBy(SSRchange);
            aw.getSSRcontenders().setMark();
        }
    }
}
public AccessWindowWeight getCurrentMinContender() {
    AccessWindowWeight min = getFirstAW();
    if (min == null)
        return null;
    for(AccessWindowWeight aw = min.getNextAW(); aw != null; aw = aw.getNextAW())
        if (aw.status == Constants.UNDECIDED)
            if (min.getContention() > aw.getContention())
                min = aw;
    return min;
}
public AccessWindowWeight getInitialMinContender() {
    if (getInitialSize() == 0)	
        return null;
    AccessWindowWeight min = getAccessWindowWeight(0);
    for(int i = 1; i < getInitialSize(); i++)
        if (min.getContention() > getAccessWindowWeight(i).getContention())
            min = getAccessWindowWeight(i);
    return min;
}

public int getNumberOfUndecidedAccessWindows() {return numberOfUndecidedAccessWindows.getCurrent();}
public void removeFromWeightList(Weight aw) {
    super.removeFromWeightList(aw);
    numberOfUndecidedAccessWindows.add(-1);
}

public AccessWindowWeight getAccessWindowWeight(int index) {return (AccessWindowWeight)getWeight(index);}
public AccessWindowWeight getFirstAW() {return (AccessWindowWeight)getFirst();}
}
