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
//  Created by Al Globus on Fri Feb 07 2003.
package gov.nasa.javaGenes.EOSscheduling;

import gov.nasa.javaGenes.permutation.PermutationOrderMutation;
import gov.nasa.javaGenes.permutation.PermutationEvolvable;
import gov.nasa.alsUtility.IntegerInterval;
import gov.nasa.alsUtility.Error;
import java.lang.Math;
import gov.nasa.alsUtility.RandomNumber;

/**
Same as SqueakyWheelTournamentSwapMutation except that moveForwardIndex is
forced to be near the end of the permutation and moveBackIndex is forced to be
closer to the front of the permutation.
*/
public class SqueakyWheelPlacedTSM extends SqueakyWheelTournamentSwapMutation {
protected double moveForwardFraction = 0.5; // moveForwardIndex must be in back half of permutation
protected double moveBackFraction = 0.5; // moveBackIndex must be at least half the permutation forward from moveForwardFraction

//keep track of how good the moves are
protected int goodMoves = 0;
protected int badMoves = 0;

public SqueakyWheelPlacedTSM(EOSModel inModel, int inNumberOfSwaps, int tournamentSize) {
    this(inModel,inNumberOfSwaps,tournamentSize,0.5,0.5);
}
/**
@arg inMoveForwardFraction how much of the back of the permutation may be used to find a task to move forward
@arg inMoveBackFraction how much closer to the front of the permutation the task to be moved backwards must be
*/
public SqueakyWheelPlacedTSM(EOSModel inModel, int inNumberOfSwaps, int tournamentSize,double inMoveForwardFraction,double inMoveBackFraction) {
    super(inModel,inNumberOfSwaps,tournamentSize);
    moveForwardFraction = inMoveForwardFraction;
    moveBackFraction = inMoveBackFraction;
    Error.assertTrue(0 <= moveForwardFraction && moveForwardFraction <= 1);
    Error.assertTrue(0 <= moveBackFraction && moveBackFraction <= 1);
}
protected void calculateOutputMeasures(EOSschedulingEvolvable kid) {
    // keep track of how the idexies move
    indexDifferenceSum += moveForwardIndex - moveBackIndex;
    if (shouldSecondGoForward(kid,moveForwardIndex,moveBackIndex))
        badMoves++;
    else
        goodMoves++;
}
protected void setUpIndices(EOSschedulingEvolvable kid) {
}
protected int getFirstIndex(PermutationEvolvable kid) {
    int maxKidIndex = kid.getSize()-1;
    
    int forwardCutoff = (int)Math.round(maxKidIndex * moveForwardFraction);
    if (forwardCutoff < 0) forwardCutoff = 0;
    if (forwardCutoff > maxKidIndex) forwardCutoff = maxKidIndex;

    legalRange.set(forwardCutoff,maxKidIndex);
    RandomNumber.fillRandomly(legalRange,indices); // may be repetition
    setUpIndicesFromArray((EOSschedulingEvolvable)kid);
    return moveForwardIndex;
}
protected int getSecondIndex(PermutationEvolvable kid, int firstIndex) {
    int backCutoff = (int)Math.floor(moveForwardIndex * moveBackFraction);
    if (backCutoff < 0) backCutoff = 0;
    if (backCutoff > moveForwardIndex) backCutoff = moveForwardIndex-1;

    int saveMoveForwardIndex = moveForwardIndex; // necessary for testing and output measures
    legalRange.set(0,backCutoff);
    RandomNumber.fillRandomly(legalRange,indices); // may be repetition
    setUpIndicesFromArray((EOSschedulingEvolvable)kid);
    moveForwardIndex = saveMoveForwardIndex;

    calculateOutputMeasures((EOSschedulingEvolvable)kid);
    return moveBackIndex;
}
public String subClassMeasures(){
    String s = "\t";
    s += indexDifferenceSum + "\t";
    s += goodMoves + "\t";
    s += badMoves + "";
    return s;
}

public String toString() {
    return "SqueakyWheelPlacedTSM " 
        + "numberOfSwaps = " + numberOfSwaps
        + " tournamentSize = " + indices.length
        + " moveForwardFraction = " + moveForwardFraction
        + " moveBackFraction = " + moveBackFraction;
}
}


