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

import gov.nasa.alsUtility.IntegerInterval;

/** assumes that the tournament size is usually quite a bit smaller than the index interval from which the indices are chosen */
public class AntiTournament extends Tournament implements ChooseForDeath {

public AntiTournament(int size) {
	super(size);
}
public int getDeathRowIndex(int[] parentIndices, Population population) {
	return getDeathRowIndex(parentIndices, new IntegerInterval(0,population.getSize()-1), population);
}
public int getDeathRowIndex(int[] parentIndices, IntegerInterval range, Population population) {
	return getChosenIndex(range,population);
}

protected boolean shouldChooseSecondOne(Fitness first, Fitness second) {
	if (first == null)
		return true;
	return first.fitterThan(second);
}
public String toString() {
	return "AntiTournament size=" + size;
}

}
