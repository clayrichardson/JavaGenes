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
//  Created by Al Globus on Thu Jan 16 2003.
package gov.nasa.javaGenes.permutation;

import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.Utility;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.alsUtility.RandomNumber;

/**
Think of a permutation as a graph where the nodes are the integers and neighboring
nodes share an edge.  Generate a child by cutting the graph in k places then
putting the pieces together in some other order.
*/
public class PermutationKCutMutation extends PermutationChildMaker {
protected int numberOfSegments = 1;

public PermutationKCutMutation(int numberOfCuts) {
    Error.assertTrue(numberOfCuts > 0);
    numberOfSegments = numberOfCuts+1;
}
public int numberOfParents() {return 1;}
public Evolvable[] makeChildren(Evolvable[] parents) {
    Error.assertTrue(parents.length == 1);
    PermutationEvolvable mom = (PermutationEvolvable)parents[0];
    int size = mom.getSize();
    Error.assertTrue(numberOfSegments < size); // otherwise this number of cuts impossible

    // create array of random segmentsand sort it in accending order
    // each ajacent pair of numbers defines a segment
    // first is 1 less than the start of the segment, second is the end of the segment
    int[] segments = new int[numberOfSegments+1];
    segments[0] = -1; // must be before start
    segments[segments.length-1] = size-1;
    for(int i = 1; i < segments.length-1; i++)
        do
            segments[i] = RandomNumber.getIndex(size-1); // cut is between here and next index
        while (indexInPrevious(i,segments));
    Utility.sort(segments);
    // find the order to put the permutation back together in
    PermutationEvolvable order = null;
    do
        order = new PermutationEvolvable(numberOfSegments);
    while (order.isInAscendingOrder()); // if in this order there is no change
     return makeChildren(parents,segments,order);
}
protected Evolvable[] makeChildren(Evolvable[] parents, int[] segments, PermutationEvolvable order) {
    PermutationEvolvable mom = (PermutationEvolvable)parents[0];
    int size = mom.getSize();
    PermutationEvolvable[] p = new PermutationEvolvable[1];
    p[0] = mom.deepCopyPermutationEvolvable();
    
   int childIndex = 0;
    for(int i = 0; i < numberOfSegments; i++) {
        int whichSegment = order.getIndexAt(i)+1;
        int from = segments[whichSegment-1]+1;
        int to = segments[whichSegment];
        Error.assertTrue(from <= to);
        for(int j = from; j <= to; j++)
            copyIndex(mom,j,p[0],childIndex++);
    }
    Error.assertTrue(childIndex == size);
    return p;
}
protected boolean indexInPrevious(int index, int[] array) {
    Error.assertTrue(index < array.length);
    for(int i = 0; i < index; i++) 
        if (array[i] == array[index])
            return true;
    return false;
}
public String toString() {
	return "KCutMutation cuts= " + (numberOfSegments-1);
}
}

