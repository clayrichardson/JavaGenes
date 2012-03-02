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
//  Created by Al Globus on Wed Feb 12 2003.
package gov.nasa.javaGenes.EOSscheduling;

import gov.nasa.javaGenes.permutation.PermutationOrderMutation;
import gov.nasa.javaGenes.permutation.PermutationEvolvable;
import gov.nasa.alsUtility.IntegerInterval;
import gov.nasa.alsUtility.Error;
import java.lang.Math;
import gov.nasa.alsUtility.RandomNumber;


public class SqueakyWheelShiftForward extends SqueakyWheelPlacedTSM {
// NOTE: moveBackIndex and moveBackFraction are not used
protected double shiftFraction = 0.5; // moveBackIndex (place to shift before) must be at least half the permutation forward from moveForwardFraction

public SqueakyWheelShiftForward(EOSModel inModel, int inNumberOfSwaps, int tournamentSize) {
    this(inModel,inNumberOfSwaps,tournamentSize,0.5,0.5);
}
/**
@arg inMoveForwardFraction how much of the back of the permutation may be used to find a task to move forward
@arg inShiftFraction how much closer to the front of the permutation the the place to shift to must be
*/
public SqueakyWheelShiftForward(EOSModel inModel, int inNumberOfSwaps, int tournamentSize, double inMoveForwardFraction,double inShiftFraction) {
    super(inModel,inNumberOfSwaps,tournamentSize,inMoveForwardFraction,0);
    shiftFraction = inShiftFraction;
    Error.assertTrue(0 <= shiftFraction && shiftFraction <= 1);
}
protected void calculateOutputMeasures(EOSschedulingEvolvable kid) {
    // must be done in getSecondIndex
}
protected int getSecondIndex(PermutationEvolvable kid, int firstIndex) {
    int shiftCutoff = (int)Math.floor(firstIndex * shiftFraction);
    if (shiftCutoff >= firstIndex) shiftCutoff = firstIndex-1;
    if (shiftCutoff < 0) shiftCutoff = 0;
    IntegerInterval range = new IntegerInterval(0,shiftCutoff);
    int shiftBeforeIndex = range.random();
    indexDifferenceSum = firstIndex - shiftBeforeIndex;
    return shiftBeforeIndex;
}
/**
shifts shift to in front of shiftBeforeMe
*/
public void mutate(int shift, int shiftBeforeMe, PermutationEvolvable kid) {
    Error.assertTrue(shift > shiftBeforeMe);
    Error.assertTrue(shift < kid.getSize());
    Error.assertTrue(shiftBeforeMe >= 0);
    int temp = kid.getIndexAt(shift);
    for(int i = shift; i > shiftBeforeMe; i--)
        kid.setIndexAt(i,kid.getIndexAt(i-1));
    kid.setIndexAt(shiftBeforeMe,temp);
}
public String subClassMeasures(){
    String s = "\t";
    s += indexDifferenceSum + "";
    return s;
}
public String toString() {
    return "SqueakyWheelShiftForward " 
        + "numberOfSwaps = " + numberOfSwaps
        + " tournamentSize = " + indices.length
        + " moveForwardFraction = " + moveForwardFraction
        + " shiftFraction = " + shiftFraction;
}
}



