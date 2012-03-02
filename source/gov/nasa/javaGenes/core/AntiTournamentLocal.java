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

public class AntiTournamentLocal extends AntiTournament {
protected int extendFromFirstParentIndices;

public AntiTournamentLocal(int size, int extendFromFirstParentIndices) {
	super(size);
	Error.assertTrue(extendFromFirstParentIndices >= 0);
	this.extendFromFirstParentIndices = extendFromFirstParentIndices;
}
public int getDeathRowIndex(int[] parentIndices, IntegerInterval range, Population population) {
	Error.assertTrue(parentIndices.length > 0);
	IntegerInterval newRange = new IntegerInterval();
	newRange.setToExtremes(parentIndices);
	newRange.addToLow(-extendFromFirstParentIndices);
	newRange.addToHigh(extendFromFirstParentIndices);
	newRange.limitTo(range);
	return getChosenIndex(newRange,population);
}
public String toString() {
	return "AntiTournamentLocal size=" + size + " extendFromFirstParentIndices=" + extendFromFirstParentIndices;
}
}