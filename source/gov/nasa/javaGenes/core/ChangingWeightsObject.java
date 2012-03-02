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


/**
returns weights for an object.  These weights vary linearly with a 'distance' parameter.
*/
public class ChangingWeightsObject implements java.io.Serializable {
protected double start = 1;
protected double slope = 0;
double currentWeight = 1;
protected Object weightedObject;

public ChangingWeightsObject(Object inWeightedObject) {weightedObject = inWeightedObject;}
public ChangingWeightsObject(Object inWeightedObject, double inStart, double inSlope) {
    this(inWeightedObject);
    start = inStart;
    slope = inSlope;
    currentWeight = start;
}
public ChangingWeightsObject(Object inWeightedObject, double inStart, double end, double maxDistance ) {
    this(inWeightedObject,inStart,(end - inStart)/maxDistance);
}

public double getWeight() {return currentWeight;}
public void calculateWeight(double distance) {
    currentWeight = start + distance*slope;
}
public Object getObject() {return weightedObject;}
public String toString() {
    return "ChangingWeightsObject start " + start + " slope " + slope + " object " + weightedObject;
}
}
