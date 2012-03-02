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
package gov.nasa.javaGenes.permutation;

import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.alsUtility.RandomNumber;

/**
Gilbert Sysweda's order based mutation
*/
public class PermutationOrderMutation extends PermutationChildMaker {
protected int numberOfSwaps = 1;

public PermutationOrderMutation() {this(1);}
public PermutationOrderMutation(int inNumberOfSwaps) {
    numberOfSwaps = inNumberOfSwaps;
    Error.assertTrue(numberOfSwaps > 0);
}
public int numberOfParents() {return 1;}
public Evolvable[] makeChildren(Evolvable[] parents) {
    Error.assertTrue(parents.length == 1);
    PermutationEvolvable kid = ((PermutationEvolvable)parents[0]).deepCopyPermutationEvolvable();
    for(int i = 0; i < numberOfSwaps; i++) {
        if (Debug.debug)
            Error.assertTrue(kid.isPermutation());
        int index1 = getFirstIndex(kid);
        int index2 = getSecondIndex(kid,index1);
        mutate(index1,index2,kid);
    }
    if (Debug.debug)
        Error.assertTrue(kid.isPermutation());
    PermutationEvolvable[] p = new PermutationEvolvable[1];
    p[0] = kid;
    return p;
}
/**
made public only for testing
*/
public void mutate(int index1, int index2, PermutationEvolvable kid) {
    int temp = kid.getIndexAt(index1);
    kid.setIndexAt(index1,kid.getIndexAt(index2));
    kid.setIndexAt(index2,temp);
}
protected int getFirstIndex(PermutationEvolvable kid) {
    return RandomNumber.getIndex(kid.getSize());
}
protected int getSecondIndex(PermutationEvolvable kid, int firstIndex) {
    return RandomNumber.getUniqueIndex(kid.getSize(),firstIndex);
}
public String toString() {
	return "OrderMutation swaps = " + numberOfSwaps;
}
}

