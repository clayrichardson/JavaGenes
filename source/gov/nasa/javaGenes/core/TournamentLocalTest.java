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

import junit.framework.TestCase;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.IntegerInterval;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.Utility;

public class TournamentLocalTest extends TestCase {

public TournamentLocalTest(String name) {super(name);}
private Population population = new Population(50);

public void setUp() {
	for(int i = 0; i < population.getSize(); i++)
		population.setIndividual(i, new Individual(new FitnessDouble(i)));
}
public void testGetParentIndicesRandom() {
    //RandomNumber.setSeed(990639400906L); // used to generate repeatable runs.
	for(int i = 0; i < 10000; i++) {
		int numberOfParents = new IntegerInterval(1,4).random();
		int number = new IntegerInterval(1,4).random();
		int size = new IntegerInterval(1,4).random();
		int minimumSize = number+size+1;
		int extendFromFirstParentIndex = new IntegerInterval(minimumSize, number+size+10).random();
		int bottom = new IntegerInterval(minimumSize,population.getSize()/2).random();
		int top = new IntegerInterval(bottom + minimumSize,population.getLastIndex()).random();
		IntegerInterval range =  new IntegerInterval(bottom,top);
		testGetParentIndices(i+"", number, size, extendFromFirstParentIndex,range);
	}
}
private void testGetParentIndices(String name, int number, int size, int extendFromFirstParentIndex, IntegerInterval range) {
	TournamentLocal chooser = new TournamentLocal(size,extendFromFirstParentIndex);
	int[] indices = chooser.getParentIndices(number, range, population);
	IntegerInterval extremes = new IntegerInterval();
	extremes.setToExtremes(indices);
	Error.assertTrue(extremes.intervalInclusive() <= 1 + 2*extendFromFirstParentIndex);
	for(int i = 0; i < indices.length; i++)
		Error.assertTrue(range.isBetween(indices[i]));
	indices = chooser.getParentIndices(number, population);
	Error.assertTrue(extremes.intervalInclusive() <= 1 + 2*extendFromFirstParentIndex);
}
}