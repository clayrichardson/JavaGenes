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

public class CrossoverOnePointEachTest extends TestCase {

public CrossoverOnePointEachTest(String name) {super(name);}

public void test() {
    RandomNumber.setSeed(990639400906L);   // to get deterministic results
    CrossoverOnePointEach xover = new CrossoverOnePointEach();
	EvolvableDoubleList parents[] = {new EvolvableDoubleList(4), new EvolvableDoubleList(5)};
	EvolvableDoubleList child = ((EvolvableDoubleList[])xover.makeChildren(parents))[0];
	//System.out.println(parents[0]);
	//System.out.println(parents[1]);
	//System.out.println(child.toString());
    double[] values = {0.455328933156024,0.7749004050158379,0.8327765542950442,0.20479234542355457,0.1641624627782836};
    assertTrue("known", child.isEqual(new EvolvableDoubleList(values)));
}
public void testCrossover() {
	double[] frontValues = {1,2,3,4,5};
	double[] backValues = {4,5,6,7};
	double[] resultValues1 = {1,2,6,7};
	testCrossover("1 ", frontValues, backValues, 1, 2, resultValues1);
	double[] resultValues2 = {1,2,3,4,5,6,7};
	testCrossover("2 ", frontValues, backValues, 3, 1, resultValues2);
	double[] resultValues3 = {1,4,5,6,7};
	testCrossover("3 ", frontValues, backValues, 0, 0, resultValues3);
}
private void testCrossover(String name, double[] frontValues, double[] backValues, int frontIndex, int backIndex, double[] resultValues) {
	double divideBy = 10;
	EvolvableDoubleList frontList = new EvolvableDoubleList(frontValues,divideBy);
	EvolvableDoubleList endList = new EvolvableDoubleList(backValues,divideBy);
    CrossoverOnePointEach xover = new CrossoverOnePointEach();
	EvolvableDoubleList child = (EvolvableDoubleList)frontList.copyForEvolution();
	int[] front = {frontIndex, frontIndex+1};
	int[] back = {backIndex-1, backIndex};
	xover.crossover(child,frontList,endList,front,back);
	EvolvableDoubleList result = new EvolvableDoubleList(resultValues,divideBy);
	assertTrue(name + child.toString(), child.isEqual(result));
}
}