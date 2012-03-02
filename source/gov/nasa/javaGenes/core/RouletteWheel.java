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
//  Created by Al Globus on Wed May 14 2003.
package gov.nasa.javaGenes.core;

import gov.nasa.alsUtility.Error;
import java.util.Vector;
import java.util.Collections;
import gov.nasa.alsUtility.RandomNumber;
import java.util.Comparator;
import java.util.Collections;

/**
Implements a weighted roulette wheel with changable weights
*/
public class RouletteWheel implements java.io.Serializable {
protected Vector weights = new Vector();
protected Comparator comparator = new DescendingWeightsComparator();
protected double totalWeight = 0;
protected boolean prepareForSpinCalled = false;

public void add(ChangingWeightsObject w) {
    Error.assertTrue(w != null);
    Error.assertTrue(w.getObject() != null);
    weights.addElement(w);
}
public ChangingWeightsObject get(int i) {
    return (ChangingWeightsObject)weights.elementAt(i);
}
public Object spinWheel(int changingWeightsParameter) {
    Error.assertTrue(weights.size() > 0);
    prepareForSpin(changingWeightsParameter);
    return spinWheel();
}
public Object spinWheel() {
    Error.assertTrue(prepareForSpinCalled);
    return findObject(RandomNumber.getDouble(totalWeight));
}
// broken out for testing
protected Object findObject(double random) {
    double sum = 0;
    for(int i = 0; i < weights.size(); i++) {
        sum += get(i).getWeight();
        if (random <= sum)
            return get(i).getObject();
    }
    // can go off end due to floating point numerical error
    return get(weights.size()-1).getObject();
}
public void prepareForSpin(double changingWeightsParameter) {
    prepareForSpinCalled = true;
    for(int i = 0; i < weights.size(); i++)
        get(i).calculateWeight(changingWeightsParameter);
    // Collections.sort(weights,comparator);  // will make things faster under some circumstances

    totalWeight = 0;
    for(int i = 0; i < weights.size(); i++)
        totalWeight += get(i).getWeight();
}
public String toString() {
    String s = "RouletteWheel\n";
    for(int i = 0; i < weights.size(); i++){
        s += get(i).toString() + "\n";
    }
	s += "end RouletteWheel\n";
    return s;
}
}
