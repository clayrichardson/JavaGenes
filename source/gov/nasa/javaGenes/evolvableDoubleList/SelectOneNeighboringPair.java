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

import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.RandomNumber;

public class SelectOneNeighboringPair extends NeighboringPairsSelector {

public SelectOneNeighboringPair(){this(1,0);}
public SelectOneNeighboringPair(int firstModulo){this(firstModulo,0);}
public SelectOneNeighboringPair(int firstModulo, int firstOffset) {
	super(new SelectOne(), firstModulo, firstOffset);
}
public Indices getIndices(int size) {
	Indices selection = new Indices();
	if (size < 2)
		return selection;
	int[] theFirstIndex = simpleSelector.getIndicesArray(size - 1);
	Error.assertTrue(theFirstIndex.length <= 1);
	if (theFirstIndex.length == 0)
		return selection;
    selection.addIndex(theFirstIndex[0]);
    selection.addIndex(theFirstIndex[0]+1);
	return selection;
}
public String toString() {
    return "SelectOneNeighboringPair modulo=" + simpleSelector.modulo + " offset=" + simpleSelector.offset;
}

}