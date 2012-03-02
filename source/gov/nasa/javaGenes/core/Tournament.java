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

/** assumes that the tournament size is usually quite a bit smaller than the index interval from which the indices are chosen */
public class Tournament implements ChooseParents {
protected int size;

public Tournament(int size) {
	this.size = size;
}
public int[] getParentIndices(int number, Population population) {return getParentIndices(number,population.getIndexRange(),population);}
public int[] getParentIndices(int number, IntegerInterval range, Population population) {return getChosenIndices(number,range,population);}

public int getChosenIndex(Population population) {
	return getChosenIndices(1,population)[0];
}
public int getChosenIndex(IntegerInterval range, Population population) {
	return getChosenIndices(1,range,population)[0];
}
public int[] getChosenIndices(int number, Population population) {
	Error.assertNotNull(population);
	return getChosenIndices(number,new IntegerInterval(0,population.getSize()-1),population);
}
public int[] getChosenIndices(int number, IntegerInterval range, Population population) {
	return getChosenIndices(new int[0], number, range,population);
}
public int[] getChosenIndices(int[] alreadyChosen, int number, IntegerInterval range, Population population) {
	Error.assertTrue("too many needed for range to support", (number-1)+size <= range.intervalInclusive());
	Error.assertTrue(alreadyChosen.length <= number);
	Error.assertTrue(Utility.areDifferent(alreadyChosen));
	if (number == range.intervalInclusive()) // special case, Note: can only happen if size == 1
		return range.getArrayOfAllValues();
	int[] indices = new int[number];
	for(int i = 0; i < alreadyChosen.length; i++) 
		indices[i] = alreadyChosen[i];
	for(int i = alreadyChosen.length; i < indices.length; i++){
		indices[i] = tournament(range.getRandomUniqueIndices(size),population); 
		if (!Utility.areDifferent(indices,i))
			i--; // try again
	}
	return indices;
}
protected int tournament(int[] indices, Population population) {
	Fitness chosenSoFarFitness = null;
	int chosenSoFarIndex = -1;
	for(int i = 0; i < indices.length; i++) {
		int index = indices[i];
		Individual individual = population.getIndividual(index);
		Error.assertNotNull(individual);
		if (shouldChooseSecondOne(chosenSoFarFitness,individual.getFitness())) {
			chosenSoFarFitness = individual.getFitness();
			chosenSoFarIndex = index;
		}
	}
	return chosenSoFarIndex;
}
protected boolean shouldChooseSecondOne(Fitness first, Fitness second) {
	if (first == null)
		return true;
	return second.fitterThan(first);
}
public String toString() {
	return "Tournament size=" + size;
}

}