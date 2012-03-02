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
package gov.nasa.javaGenes.core;

import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.IntegerInterval;
import gov.nasa.javaGenes.core.Population;

public class TournamentLocal extends Tournament {
protected int extendFromFirstParentIndex;

public TournamentLocal(int size, int extendFromFirstParentIndex) {
	super(size);
	this.extendFromFirstParentIndex = extendFromFirstParentIndex;
}
public int[] getParentIndices(int number, IntegerInterval range, Population population) {
	int firstParentIndex = getChosenIndex(range,population);
	IntegerInterval newRange = new IntegerInterval(firstParentIndex-extendFromFirstParentIndex,firstParentIndex+extendFromFirstParentIndex);
	newRange.limitTo(range);
	int[] alreadyChosen = {firstParentIndex};
	return getChosenIndices(alreadyChosen,number,newRange,population);
}
public String toString() {
	return "TournamentLocal size=" + size + " extendFromFirstParentIndex=" + extendFromFirstParentIndex;
}

}