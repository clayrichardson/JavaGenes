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
import gov.nasa.alsUtility.Error;

// depends on AccessWindowWeight.status to determine what's in list and what's not
public class SensorAvailContenders extends Vector implements java.io.Serializable {

public void reinitialize() {} 
public void unSchedulableSoRemoveFromContentionNetwork() {
    for(int i = 0; i < getInitialSize(); i++) {
        AccessWindowWeight aw = getAccessWindowWeight(i);
        if (aw.status == Constants.UNDECIDED)
            aw.unschedulableSoRemoveFromContentionNetwork();
    }
}
public void sensorAvailNeedChangedBy(float amount) {
    for(int i = 0; i < getInitialSize(); i++) {
        AccessWindowWeight aw = getAccessWindowWeight(i);
        if (aw.status == Constants.UNDECIDED)
            aw.sensorAvailNeedChangedBy(amount);
    }
}
public AccessWindowWeight getAccessWindowWeight(int index) {return (AccessWindowWeight)get(index);}
public int getInitialSize() {return size();}
public int getCurrentSize() {
    int count = 0;
    for(int i = 0; i < getInitialSize(); i++) {
        AccessWindowWeight aw = getAccessWindowWeight(i);
        if (aw.status == Constants.UNDECIDED)
            count++;
    }
    return count;
}
public float getSensorAvailNeedSumFromCalculation() {
    float sum = 0;
    for(int i = 0; i < getInitialSize(); i++) {
        AccessWindowWeight aw = getAccessWindowWeight(i);
        if (aw.status == Constants.UNDECIDED)
            sum += aw.getTaskWeight().getSensorAvailNeed();
    }
    return sum;
}
/** used for mutual contention check*/
public void assertAllContendWith(AccessWindowWeight contendsWith) {
    for(int i = 0; i < getInitialSize(); i++) {
        AccessWindowWeight aw = getAccessWindowWeight(i);
        if (aw.status == Constants.UNDECIDED)
            Error.assertTrue(aw.getSensorAvailContenders().currentlyContains(contendsWith));
    }
}
public boolean currentlyContains(AccessWindowWeight isHere) {
    for(int i = 0; i < getInitialSize(); i++) {
        AccessWindowWeight aw = getAccessWindowWeight(i);
        if (aw.status == Constants.UNDECIDED && isHere == aw)
                return true;
    }
    return false;
}

}