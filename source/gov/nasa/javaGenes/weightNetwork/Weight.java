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
package gov.nasa.javaGenes.weightNetwork;

import gov.nasa.alsUtility.Error;

// NOTE: unit test is in WeightListTest.java
public class Weight extends UpdateAndPropagatable {
private static final boolean debug = gov.nasa.javaGenes.EOSscheduling.Debug.debug;

protected float weight = 1;

protected WeightList weightList; // one 'this' is in

// current Weights in a WeightList are in a double linked list for speedy removal
protected Weight next;
protected Weight previous;
public void setNext(Weight n) {next = n;}
public Weight getNext() {return next;}
public void setPrevious(Weight p) {previous = p;}
public Weight getPrevious() {return previous;}

public float getWeight() {return weight;} // always use even internally in case subclass redefines
protected void setWeight(float f) {
    if (debug)
        Error.assertTrue(f > 0);
    weight = f;
}
public void setWeightAndPropagate(float f) {
    float oldWeight = getWeight();
    setWeight(f);
    weightChanged(oldWeight,getWeight());
}
public void reinitialize() {}

public void weightChanged(float oldWeight, float newWeight) {
    if (debug) {
        Error.assertTrue(gov.nasa.alsUtility.Utility.nearlyEqual(newWeight,getWeight()));
        Error.assertTrue(oldWeight > 0);
        Error.assertTrue(newWeight > 0);
    }
    weightList.weightChanged(oldWeight,newWeight);
}
public void removeFromWeightList() {
    weightList.removeFromWeightList(this);
}
public void setWeightList(WeightList inWeightList) {
    Error.assertTrue(inWeightList != null);
    weightList = inWeightList;
}
public WeightList getWeightList() {return weightList;}
}
