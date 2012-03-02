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

public class SSRcontendersNode extends Weight {
protected AccessWindowWeight accessWindowWeight;

public SSRcontendersNode(AccessWindowWeight in) {
    accessWindowWeight = in;
    accessWindowWeight.setSSRcontendersNode(this);
}

// these two ar meant to be noops
public float getWeight() {return 0;}
public void weightChanged(float oldWeight, float newWeight) {
    Error.assertTrue(newWeight == 0); // this is true when called from removeFromWeightList(), the only time this happens
}
public int getSSRuse() {
    return getTaskWeight().getSSRuse();
}
public SSRcontenders getSSRcontenders() {
    return (SSRcontenders)getWeightList();
}
public SSRcontendersNode getNextSCN() {
    return (SSRcontendersNode)getNext();
}
public AccessWindowWeight getAccessWindowWeight() {
    return accessWindowWeight;
}
public TaskWeight getTaskWeight() {
    return getAccessWindowWeight().getTaskWeight();
}
public float getSSRneed() {return accessWindowWeight.getSSRneed();}
}
