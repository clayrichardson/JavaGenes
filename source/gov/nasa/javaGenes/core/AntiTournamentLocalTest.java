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

public class AntiTournamentLocalTest extends TestCase {

public AntiTournamentLocalTest(String name) {super(name);}
private Population population = new Population(50);

public void setUp() {
	for(int i = 0; i < population.getSize(); i++)
		population.setIndividual(i, new Individual(new FitnessDouble(i)));
}
public void testCoverage() {
	System.out.println("Should be even");
	testCoverage(1);
	System.out.println("Should be ascending");
	testCoverage(2);
}
private void testCoverage(int size) {
	IntegerInterval range = new IntegerInterval(4,9);
	int[] results = new int[range.intervalInclusive()];
	for(int i = 0; i < 100000; i++) {
		AntiTournamentLocal chooser = new AntiTournamentLocal(size,1);
		int[] parentIndices = {5,8};
		int index = chooser.getDeathRowIndex(parentIndices, range, population);
		results[index-4]++;
	}
	System.out.println();
	for(int i = 0; i < results.length; i++)
		System.out.println(i + " " + results[i]);
}
public void testGetDeathRowIndexRandom() {
    //RandomNumber.setSeed(990639400906L); // used to generate repeatable runs.
	for(int i = 0; i < 10000; i++) {
		int bottom = new IntegerInterval(0,population.getSize()/2).random();
		int top = new IntegerInterval(bottom + 3,population.getLastIndex()).random();
		IntegerInterval range =  new IntegerInterval(bottom,top);
		int numberOfParents = new IntegerInterval(1,4).random();
		int[] parentIndices = new int[numberOfParents];
		for(int j = 0; j < parentIndices.length; j++)
			parentIndices[j] = new IntegerInterval(range).random();
		int size = new IntegerInterval(1,4).random();
		int minimumSize = size+1;
		int extendFromFirstParentIndices = new IntegerInterval(minimumSize,minimumSize+3).random();
		testGetDeathRowIndex(i+"", size, extendFromFirstParentIndices, parentIndices,range);
	}
}
private void testGetDeathRowIndex(String name, int size, int extendFromFirstParentIndices, int[] parentIndices, IntegerInterval range) {
	AntiTournamentLocal chooser = new AntiTournamentLocal(size,extendFromFirstParentIndices);
	int index = chooser.getDeathRowIndex(parentIndices, range, population);
	Error.assertTrue(name + " isBetween " + index, range.isBetween(index));
	index = chooser.getDeathRowIndex(parentIndices, population);
	Error.assertTrue(name + " isBetween " + index, population.getIndexRange().isBetween(index));
}
}