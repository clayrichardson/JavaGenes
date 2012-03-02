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
//  Created by Al Globus on Thu Dec 05 2002.
package gov.nasa.javaGenes.simulatedAnnealing;

import gov.nasa.javaGenes.core.Fitness;
import gov.nasa.alsUtility.RandomNumber;
import java.lang.Math;
import gov.nasa.alsUtility.Error;

public class Accepter extends gov.nasa.javaGenes.hillClimbing.Accepter {
protected double reduceBy = 1;
protected double currentTemperature = 100;
protected int callsPerStaticTemperature = 1;
protected int callsAtThisTemperature = 0;
static final public int DELTA_REDUCE = 0;
static final public int FACTOR_REDUCE = 1;
protected int whichReduction = DELTA_REDUCE;

public Accepter(double initialTemperature, double inReduceBy, int inCallsPerStaticTemperature) {
    this(initialTemperature,inReduceBy,inCallsPerStaticTemperature,DELTA_REDUCE);
}
/**
@arg inWhichReduction Accepter.DELTA_REDUCE means temperature goes down by inReduceBy each reduction (and inReduceBy must be positive), AcceptWorseFitness.FACTOR_REDUCE means temperature is multiplied by inReduceBY each reduction (and 0 < inReduceBy < 1)
@arg initialTemperature must be non-negative
@arg inCallsPerStaticTemperature must be > 0
*/
public Accepter(double initialTemperature, double inReduceBy, int inCallsPerStaticTemperature, int inWhichReduction) {
    currentTemperature = initialTemperature;
    Error.assertTrue(currentTemperature >= 0);
    callsPerStaticTemperature = inCallsPerStaticTemperature;
    Error.assertTrue(callsPerStaticTemperature > 0);

    whichReduction = inWhichReduction;
    Error.assertTrue(whichReduction == DELTA_REDUCE || whichReduction == FACTOR_REDUCE);
    reduceBy = inReduceBy;
    Error.assertTrue(0 < reduceBy);
    if (whichReduction == FACTOR_REDUCE)
        Error.assertTrue(reduceBy < 1);
}

public boolean accept(Fitness kid,Fitness parent) {
    if (callsAtThisTemperature >= callsPerStaticTemperature) {
        if (whichReduction == DELTA_REDUCE)
            currentTemperature -= reduceBy;
        else if (whichReduction == FACTOR_REDUCE)
            currentTemperature *= reduceBy;
        else
            Error.fatal("bad whichReduction = " + whichReduction);
        callsAtThisTemperature = 1;
    } else
        callsAtThisTemperature++;
    if (kid.fitterThan(parent))
        return true;
    double deltaFitness = kid.asDouble() - parent.asDouble();
    if (deltaFitness <= 0)
        return true; // the equal fitness case
    if (currentTemperature <= 0)
        return false;
    return RandomNumber.getDouble() <= probabilityToBeat(deltaFitness);
}
public double probabilityToBeat(double deltaFitness) {return Math.exp(-deltaFitness/currentTemperature);}
public double getCurrentTemperature() {return currentTemperature;}
public String toString() {
    return "Accepter: reduceBy = " + reduceBy
        + " currentTemperature = " + currentTemperature
        + " callsPerStaticTemperature = " + callsPerStaticTemperature
        + (whichReduction == DELTA_REDUCE ? " delta" : " factor");
}
}
