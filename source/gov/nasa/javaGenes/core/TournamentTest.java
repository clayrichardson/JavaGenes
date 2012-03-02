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

public class TournamentTest extends TestCase {

public TournamentTest(String name) {super(name);}
private Population population = new Population(50);
private Tournament tournament = new Tournament(2);

public void setUp() {
	for(int i = 0; i < population.getSize(); i++)
		population.setIndividual(i, new Individual(new FitnessDouble(i)));
}
public void testTournament() {
	int[] array1 = {1,3,5};
	Error.assertTrue("1", tournament.tournament(array1,population) == 1);
	array1[0] = 6;
	Error.assertTrue("2", tournament.tournament(array1,population) == 3);
	array1[1] = 12;
	Error.assertTrue("3", tournament.tournament(array1,population) == 5);
}
public void testGetParentIndices() {
	RandomNumber.setSeed(990639400906L); // used to generate repeatable runs.
	for(int i = 0; i < 10000; i++) {
		tournament = new Tournament(new IntegerInterval(1,5).random());
		int number = new IntegerInterval(1,3).random();
		int bottom = new IntegerInterval(0,30).random();
		IntegerInterval range = new IntegerInterval(bottom, bottom+new IntegerInterval(6,19).random());
		int[] indices = tournament.getParentIndices(number,range,population);
		Error.assertTrue("1-"+i, Utility.areDifferent(indices));
		Error.assertTrue("2-"+i, indices.length == number);
	}
}
}