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
import java.lang.Float;
import java.lang.Math;
import gov.nasa.alsUtility.RandomNumber;

public class RouletteWheelChooseLowWeight extends WeightList {
private static final boolean debug = gov.nasa.javaGenes.EOSscheduling.Debug.debug;
protected float subtractWeightFrom;
protected boolean mustCalculateSubtractWeightFrom = true;
protected int numberOfWeightsRemoved = 0;

public void reinitialize() {
    numberOfWeightsRemoved = 0;
    super.reinitialize();
}
public void add(Weight w) {
    mustCalculateSubtractWeightFrom = true;
    super.add(w);
}
public void removeFromWeightList(Weight w) {
    numberOfWeightsRemoved++;
    super.removeFromWeightList(w);
}
public Weight spinWheel() {
    if (debug)
        Error.assertTrue(more());
    if (mustCalculateSubtractWeightFrom) {
        float max = getMaxCurrentWeight();
        float min = getMinCurrentWeight();
        subtractWeightFrom = max + min;
        mustCalculateSubtractWeightFrom = false;
    }
    float random = (float)RandomNumber.getDouble((getInitialSize()-numberOfWeightsRemoved)*subtractWeightFrom - weightSum.getCurrent());
    return spinWheel(random);
}
/**
only for testing.  Do not call in operation!
*/
protected Weight spinWheel(float random) {
    float sum = 0;
    for(Weight w = currentWeights; w != null; w = w.getNext()) {
        sum += subtractWeightFrom - w.getWeight();
        if (random <= sum)
            return w;
    }
    // can go off end due to floating point numerical error
    return currentWeights;
}
}
