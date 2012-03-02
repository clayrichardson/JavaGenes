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
package gov.nasa.javaGenes.evolvableDoubleList;

import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.Utility;

/**
keeps its internal value between 0 and 1. Will silently enforce this is you try to get outside the range (see setValue)
*/
public class EvolvableDouble implements java.io.Serializable {
protected static final DoubleInterval universalRange = new DoubleInterval(0,1);
protected static final double universalRangeSize = 1;
protected double value;

public EvolvableDouble() {
    this(RandomNumber.getDouble());
    Error.assertTrue(universalRange.isBetween(getValue()));
}
public EvolvableDouble(double value) {
    setValue(value);
}
public EvolvableDouble copy() {
    return new EvolvableDouble(value);
}
public double interpolateInto(DoubleInterval range) {
    double r = range.low() + value*range.interval();
    // deal with numerical issues
    if (r < range.low())
        return range.low();
    if (r > range.high())
        return range.high();
    return r;
}
static public double limitToLegalRange(double value) {return universalRange.limitTo(value);}
public void mutateByStandardDeviation(double standardDeviation) {
    setValue(universalRange.randomGaussian(value,standardDeviation));
    Error.assertTrue(universalRange.isBetween(getValue()));
}
static public boolean isWithinRange(double value) {return universalRange.isBetween(value);}
public void setValue(double value) {this.value = universalRange.limitTo(value);}
public double getValue() {return value;}
public boolean isEqual(EvolvableDouble other) {return Utility.nearlyEqual(getValue(),other.getValue());}
public String toString() {return getValue() + "";}
}

