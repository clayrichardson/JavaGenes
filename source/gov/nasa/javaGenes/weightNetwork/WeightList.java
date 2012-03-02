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

import java.util.Vector;
import gov.nasa.alsUtility.ReinitializableFloat;
import java.lang.Float;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.RandomNumber;

public class WeightList  extends UpdateAndPropagatable {
private static final boolean debug = gov.nasa.javaGenes.EOSscheduling.Debug.debug;
protected Vector initialWeights = new Vector(); // of class Weight
protected Weight currentWeights;
protected ReinitializableFloat weightSum = new ReinitializableFloat();

public void weightChanged(float oldValue, float newValue) {
    weightSum.add(newValue - oldValue);
}
public void initializeWeightSum() {
    float sum = 0;
    for(int i = 0; i < getInitialSize(); i++)
        sum += getWeight(i).getWeight();
    weightSum.setInitial(sum);
}
public void removeFromWeightList(Weight w) {
    if (debug)
        Error.assertTrue(currentlyContains(w));
    weightChanged(w.getWeight(),0);
    if (w.getNext() != null)
        w.getNext().setPrevious(w.getPrevious());
    if (w.getPrevious() != null)
        w.getPrevious().setNext(w.getNext());
    else {
        if (debug) Error.assertTrue(w == currentWeights);
        currentWeights = w.getNext();
    }
    // NOTE: next and previous are NOT set to null as a loop may need to use w to get the next node!
    // This happens when a weight must be removed from a list as the list is being processed.
    // NOTE: this is pretty dangerous.  Might be a good idea to redesign, but must avoid memory deallocation
    // because this must be fast (is in inner loop)!
}
public float getMinCurrentWeight() {
    float minimum = Float.MAX_VALUE;
    for(Weight w = currentWeights; w != null; w = w.getNext())
        minimum = (float)Math.min(minimum,w.getWeight());
    return minimum;
}
public float getMaxCurrentWeight() {
    float maximum = -Float.MAX_VALUE;
    for(Weight w = currentWeights; w != null; w = w.getNext())
        maximum = (float)Math.max(maximum,w.getWeight());
    return maximum;
}
public float getCurrentWeightSumFromCalculation() {
    float sum = 0;
    for(Weight w = currentWeights; w != null; w = w.getNext())
        sum += w.getWeight();
    return sum;
}
public boolean currentlyContains(Weight isInNow) {
    for(Weight w = currentWeights; w != null; w = w.getNext())
        if (w == isInNow)
            return true;
    return false;
}
public boolean everContains(Weight isIn) {
    for(int i = 0; i < initialWeights.size(); i++) {
        Weight w = (Weight)initialWeights.get(i);
        if (w == isIn)
            return true;
    }
    return false;
}
public boolean more() {return currentWeights != null;}
/**
turn intialWeights into a doubly linked list for easy deletion
*/
public void reinitialize() {
    weightSum.reinitialize();
    currentWeights = null;
    Weight previous = null;
    if (initialWeights.size() > 0)
        currentWeights = (Weight)initialWeights.get(0);
    else if (debug)
        Error.assertTrue(currentWeights == null);
    for(int i = 0; i < initialWeights.size(); i++) {
        Weight w = (Weight)initialWeights.get(i);
        w.reinitialize();
        w.setPrevious(previous);
        if (i == initialWeights.size()-1)
            w.setNext(null);
        else
            w.setNext((Weight)initialWeights.get(i+1));
        previous = w;
    }
}
public void add(Weight w) {
    if (debug) 
        Error.assertTrue(!everContains(w));
    initialWeights.add(w);
    w.setWeightList(this);
}
public int currentSize() {
    int size = 0;
    for(Weight w = currentWeights; w != null; w = w.getNext())
        size++;
    return size;
}
public int getInitialSize() {return initialWeights.size();}
public Weight getWeight(int index) {return (Weight)initialWeights.get(index);}
public Weight getFirst() {return currentWeights;}
public void setFirst(Weight w) {currentWeights = w;}
public float getWeightSum() {return weightSum.getCurrent();}
public boolean isCurrentlyEmpty() {return getFirst() == null;}
}

