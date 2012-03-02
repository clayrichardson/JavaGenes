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
import gov.nasa.alsUtility.Error;

public class SelectTwoNeighboringPairs extends NeighboringPairsSelector {
public SelectTwoNeighboringPairs(){this(1,0);}
public SelectTwoNeighboringPairs(int firstModulo){this(firstModulo,0);}
public SelectTwoNeighboringPairs(int firstModulo, int firstOffset) {
	super(new SelectFixedNumber(2), firstModulo, firstOffset);
}

/** necessary because the pairs can overlap */
public int[] getIndicesArray(int size) {
    Indices indices = getIndices(size);
	int[] array = indices.getArray();
	if (array.length == 3) {
		int[] temp = array;
		array = new int[4];
		array[0] = temp[0];
		array[1] = temp[1];
		array[2] = temp[1];
		array[3] = temp[2];
	}
    return array;
}
public Indices getIndices(int size) {
	Indices selection = new Indices();
	if (size <= 1)
		return selection;
	int[] theFirstIndices = simpleSelector.getIndicesArray(size - 2);
	Error.assertTrue(theFirstIndices.length <= 2);
	if (theFirstIndices.length == 0)
		return selection;
	selection.addIndex(theFirstIndices[0]);
	selection.addIndex(theFirstIndices[0] + 1);
	if (theFirstIndices.length == 2) {
		selection.addIndex(theFirstIndices[1]);
		selection.addIndex(theFirstIndices[1] + 1);
	}
	return selection;
}
public String toString() {
    return "SelectTwoNeighboringPairs modulo=" + simpleSelector.modulo + " offset=" + simpleSelector.offset;
}

}