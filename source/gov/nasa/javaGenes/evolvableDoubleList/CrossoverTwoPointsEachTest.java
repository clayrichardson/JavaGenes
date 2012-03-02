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

import junit.framework.TestCase;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.alsUtility.IntegerInterval;
import gov.nasa.alsUtility.RandomNumber;

public class CrossoverTwoPointsEachTest extends TestCase {

public CrossoverTwoPointsEachTest(String name) {super(name);}

public void testCrossover() {
	double[] insertIntoValues = {1,2,3,4,5};
	double[] takeFromValues = {4,5,6,7};
	int[] insertInto1 = {1,2,3,4};
	int[] takeFrom1 = {0,1,2,3};
	double[] resultValues1 = {1,2,5,6,5};
	testCrossover("1 ", insertIntoValues, takeFromValues, insertInto1, takeFrom1, resultValues1);
	int[] insertInto2 = {2,3,3,4};
	int[] takeFrom2 = {1,2,2,3};
	double[] resultValues2 = {1,2,3,6,5};
	testCrossover("2 ", insertIntoValues, takeFromValues, insertInto2, takeFrom2, resultValues2);
	int[] insertInto3 = {};
	int[] takeFrom3 = {};
	double[] resultValues3 = {1,2,3,4,5};
	testCrossover("3 ", insertIntoValues, takeFromValues, insertInto3, takeFrom3, resultValues3);
	int[] insertInto4 = {2,3,3,4};
	int[] takeFrom4 = {0,1,2,3};
	double[] resultValues4 = {1,2,3,5,6,5};
	testCrossover("4 ", insertIntoValues, takeFromValues, insertInto4, takeFrom4, resultValues4);
}
private void testCrossover(String name, double[] insertIntoValues, double[] takeFromValues, int[] insertInto, int[] takeFrom, double[] resultValues) {
	double divideBy = 10;
	EvolvableDoubleList insertIntoList = new EvolvableDoubleList(insertIntoValues,divideBy);
	EvolvableDoubleList takeFromList = new EvolvableDoubleList(takeFromValues,divideBy);
    CrossoverTwoPointsEach xover = new CrossoverTwoPointsEach();
	EvolvableDoubleList child = (EvolvableDoubleList)insertIntoList.copyForEvolution();
	xover.crossover(child,insertIntoList,takeFromList,insertInto,takeFrom);
	EvolvableDoubleList result = new EvolvableDoubleList(resultValues,divideBy);
	assertTrue(name + child.toString(), child.isEqual(result));
}
}