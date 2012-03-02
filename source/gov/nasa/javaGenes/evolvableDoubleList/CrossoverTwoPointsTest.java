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

public class CrossoverTwoPointsTest extends TestCase {

public CrossoverTwoPointsTest(String name) {super(name);}

public void testCrossover() {
	double[] insertIntoValues = {1,2,3,4,5};
	double[] takeFromValues =   {4,5,6,7,8};
	int[] indices1 = {1,2,3,4};
	double[] resultValues1 = {1,2,6,7,5};
	testCrossover("1 ", insertIntoValues, takeFromValues, indices1, resultValues1);
	int[] indices2 = {2,3,3,4};
	double[] resultValues2 = {1,2,3,7,5};
	testCrossover("2 ", insertIntoValues, takeFromValues, indices2, resultValues2);
	int[] indices3 = {};
	double[] resultValues3 = {1,2,3,4,5};
	testCrossover("3 ", insertIntoValues, takeFromValues, indices3, resultValues3);
	int[] indices4 = {0,1,3,4};
	double[] resultValues4 = {1,5,6,7,5};
	testCrossover("4 ", insertIntoValues, takeFromValues, indices4, resultValues4);
}
private void testCrossover(String name, double[] insertIntoValues, double[] takeFromValues, int[] indices, double[] resultValues) {
	double divideBy = 10;
	EvolvableDoubleList insertIntoList = new EvolvableDoubleList(insertIntoValues,divideBy);
	EvolvableDoubleList takeFromList = new EvolvableDoubleList(takeFromValues,divideBy);
    CrossoverTwoPoints xover = new CrossoverTwoPoints();
	EvolvableDoubleList child = (EvolvableDoubleList)insertIntoList.copyForEvolution();
	xover.crossover(child,insertIntoList,takeFromList,indices,indices);
	EvolvableDoubleList result = new EvolvableDoubleList(resultValues,divideBy);
	assertTrue(name + child.toString(), child.isEqual(result));
}
}