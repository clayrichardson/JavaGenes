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
//  Created by Al Globus on Thu Jan 30 2003.
package gov.nasa.javaGenes.EOSscheduling;

import gov.nasa.javaGenes.permutation.PermutationOrderMutation;
import gov.nasa.javaGenes.permutation.PermutationEvolvable;
import gov.nasa.alsUtility.IntegerInterval;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.RandomNumber;

public class SqueakyWheelTournamentSwapMutation extends PermutationOrderMutation {
protected int[] indices;
protected IntegerInterval legalRange;
protected int moveForwardIndex = -1;
protected int moveBackIndex = -1;
protected EOSModel model;
//keep track of how indexies are chosen
protected int indexDifferenceSum = 0;
protected int backSmallerThanForward = 0;
protected int backLargerThanForward = 0;
protected int backSameAsForward = 0;

/**

Do numberOfSwaps swaps.  For each swap

Take n random takeImages. n should generally be fairly large. Swap two of them. They are chosen thusly:

1. The one that most needs to move forward in the permutation, by these criteria (in order)
	- unscheduled rather than scheduled
	- higher priority
	- further back in the permutation
2. The one that can be moved backward in the permutation with maximum effect, by these criteria (in order)
	- scheduled rather than unscheduled
	- lower priority
	- further forward in the permutation
        
@arg inTournamentSize the number of randomly chosen tasks.  The one that most needs to move forward in the permutation is swapped with the one that least needs to move forward.
This argument should generally be quite large.
*/
public SqueakyWheelTournamentSwapMutation(EOSModel inModel, int inNumberOfSwaps, int tournamentSize) {
    super(inNumberOfSwaps);
    model = inModel;
    Error.assertTrue(tournamentSize >= 2);
    indices = new int[tournamentSize];
    legalRange = new IntegerInterval(0,0);
}
protected int getFirstIndex(PermutationEvolvable kid) {
    setUpIndices((EOSschedulingEvolvable)kid);
    return moveForwardIndex;
}
protected int getSecondIndex(PermutationEvolvable kid, int firstIndex) {
    return moveBackIndex;
}
protected void calculateOutputMeasures(EOSschedulingEvolvable kid) {
    // keep track of how the idexies move
    indexDifferenceSum += moveForwardIndex - moveBackIndex;
    if (moveBackIndex < moveForwardIndex)
        backSmallerThanForward++;
    else if (moveBackIndex > moveForwardIndex)
        backLargerThanForward++;
    else
        backSameAsForward++;
}

protected void setUpIndices(EOSschedulingEvolvable kid) {
    legalRange.setHigh(kid.getSize()-1);
    RandomNumber.fillRandomly(legalRange,indices); // may be repetition
    setUpIndicesFromArray(kid);
    calculateOutputMeasures(kid);
}
/**
 testing purposes only, has no randomization here!
*/
protected void setUpIndicesFromArray(EOSschedulingEvolvable kid) {
    moveForwardIndex = indices[0];
    if (moreDeservingToMoveForward(kid,indices[1])) {
        moveForwardIndex = indices[1];
        moveBackIndex = indices[0];
    } else
        moveBackIndex = indices[1];
    for(int i = 2; i < indices.length; i++) {
        if (moreDeservingToMoveForward(kid,indices[i]))
            moveForwardIndex = indices[i];
        if (moreDeservingToMoveBack(kid,indices[i]))
            moveBackIndex = indices[i];
    }
}
protected boolean moreDeservingToMoveForward(EOSschedulingEvolvable kid,int index) {
    return shouldSecondGoForward(kid,moveForwardIndex,index);
}
protected boolean moreDeservingToMoveBack(EOSschedulingEvolvable kid,int index) {
    return !shouldSecondGoForward(kid,moveBackIndex,index);
}
protected boolean shouldSecondGoForward(EOSschedulingEvolvable kid, int firstIndex, int secondIndex) {
    int firstTask = kid.getIndexAt(firstIndex);
    int secondTask = kid.getIndexAt(secondIndex);
    if (kid.isTaskScheduled(firstTask) && !kid.isTaskScheduled(secondTask))
            return true;
    if (kid.isTaskScheduled(firstTask) == kid.isTaskScheduled(secondTask)) {
        double firstPriority = model.getTask(firstTask).getPriority();
        double secondPriority = model.getTask(secondTask).getPriority();
        if (firstPriority < secondPriority)
            return true;
        if (firstPriority > secondPriority)
            return false;
        return firstIndex < secondIndex;
    }
    return false;
}
public String subClassMeasures(){
    String s = "\t";
    s += indexDifferenceSum + "\t";
    s += backSmallerThanForward + "\t";
    s += backLargerThanForward + "\t";
    s += backSameAsForward + "";
    return s;
}

public String toString() {
    return "SqueakyWheelTournamentSwapMutation " 
        + "numberOfSwaps = " + numberOfSwaps
        + " tournamentSize = " + indices.length;
}
}

