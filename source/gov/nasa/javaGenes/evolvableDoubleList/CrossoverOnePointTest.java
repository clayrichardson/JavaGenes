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

public class CrossoverOnePointTest extends TestCase {

public CrossoverOnePointTest(String name) {super(name);}

public void testCrossover() {
	double[] frontValues = {1,2,3,4,5};
	double[] backValues = {4,5,6,7};
	double[] resultValues1 = {1,2,6,7};
	testCrossover("1 ", frontValues, backValues, 1, resultValues1);
	double[] resultValues2 = {1,2,3,7};
	testCrossover("2 ", frontValues, backValues, 2,  resultValues2);
	double[] resultValues3 = {1,5,6,7};
	testCrossover("3 ", frontValues, backValues, 0, resultValues3);

	double divideBy = 10;
	int[] indices = {1};
	double[] resultValues = {1,2,3,4,5,4,5,6,7};
	EvolvableDoubleList frontList = new EvolvableDoubleList(frontValues,divideBy);
	EvolvableDoubleList endList = new EvolvableDoubleList(backValues,divideBy);
    CrossoverOnePoint xover = new CrossoverOnePoint();
	EvolvableDoubleList child = (EvolvableDoubleList)frontList.copyForEvolution();
	xover.crossover(child,frontList,endList,indices,indices);
	EvolvableDoubleList result = new EvolvableDoubleList(resultValues,divideBy);
	assertTrue("4" + child.toString(), child.isEqual(result));
}
private void testCrossover(String name, double[] frontValues, double[] backValues, int index, double[] resultValues) {
	double divideBy = 10;
	EvolvableDoubleList frontList = new EvolvableDoubleList(frontValues,divideBy);
	EvolvableDoubleList endList = new EvolvableDoubleList(backValues,divideBy);
    CrossoverOnePoint xover = new CrossoverOnePoint();
	EvolvableDoubleList child = (EvolvableDoubleList)frontList.copyForEvolution();
	int[] indices = {index, index+1};
	xover.crossover(child,frontList,endList,indices,indices);
	EvolvableDoubleList result = new EvolvableDoubleList(resultValues,divideBy);
	assertTrue(name + child.toString(), child.isEqual(result));
}
}
