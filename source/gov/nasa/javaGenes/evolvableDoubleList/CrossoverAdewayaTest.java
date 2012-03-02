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
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.Utility;

public class CrossoverAdewayaTest extends TestCase {

public CrossoverAdewayaTest(String name) {super(name);}

public void testGetLinearValue() {
	CrossoverAdewaya crossover = new CrossoverAdewaya();
	Error.assertTrue(Utility.nearlyEqual(0.5,crossover.getLinearValue(0.5,1,2)));
	Error.assertTrue(Utility.nearlyEqual(2.5,crossover.getLinearValue(0.5,2,1)));
	Error.assertTrue(Utility.nearlyEqual(2.5,crossover.getLinearValue(0.5,2.5,2.5)));
	Error.assertTrue(Utility.nearlyEqual(-1,crossover.getLinearValue(0.5,0,2)));
	Error.assertTrue(Utility.nearlyEqual(-2.1,crossover.getLinearValue(0.1,-2,-1)));
	Error.assertTrue(Utility.nearlyEqual(-5.8,crossover.getLinearValue(0.1,-6,-8)));
}
public void testQuadraticPart() {
	testQuadraticPart(1,false);
	testQuadraticPart(-0.5,true);
	testQuadraticPart(-10,false);
}
private void testQuadraticPart(double b, boolean succeed) {
    RandomNumber.setSeed(990737400906L);   // to get deterministic results
	int[] indicesToCheck = {1,3,5,7,9,11};
	CrossoverAdewaya crossover = new CrossoverAdewaya(new SelectFixedIndices(indicesToCheck));
	final int length = 12;
	final double childValue = 1;
	EvolvableDoubleList child = new EvolvableDoubleList(length,childValue);
	EvolvableDoubleList[] parents = new EvolvableDoubleList[3];
	final double[] xValues = {0.1,0.5,0.9};
	final double[] fitness = new double[3];
	for(int i = 0; i < parents.length; i++) {
		double x = xValues[i];
		double y = x*x + b*x + 5;
		parents[i] = new EvolvableDoubleList(length,x);
		fitness[i] = y;
	}
	Indices quadraticFailed = crossover.quadraticPart(child,fitness,parents);
	if (succeed)
		Error.assertTrue(quadraticFailed.size() == 0);
	else
		Error.assertTrue(Utility.isEqual(quadraticFailed.getArray(),indicesToCheck));
	for(int i = 0; i < length; i++) {
		double value = child.getDoubleValue(i);
		if (i%2 == 0 || !succeed)
			Error.assertTrue(value == childValue);
		else 
			Error.assertFalse(value == childValue);
	}
}

public void testLinearPart() {
    RandomNumber.setSeed(990737400906L);   // to get deterministic results
	CrossoverAdewaya crossover = new CrossoverAdewaya();
	final int length = 12;
	final double childValue = 0;
	final double[] parentValues = {1,0.5,0.6};
	final double[] fitness = {1.5,1,2};
	EvolvableDoubleList child = new EvolvableDoubleList(length,childValue);
	EvolvableDoubleList[] parents = {
		new EvolvableDoubleList(length,parentValues[0]),
		new EvolvableDoubleList(length,parentValues[1]),
		new EvolvableDoubleList(length,parentValues[2])};
	for(int i = 0; i < length; i++)
		if (i%4 == 0) {
			parents[1].setDoubleValue(i,parentValues[2]);
			parents[2].setDoubleValue(i,parentValues[1]);
		}
	Indices quadraticFailed = new Indices(length-4,2);
	
	// test success
	boolean success = crossover.linearPart(child,fitness,parents,quadraticFailed);
	Error.assertTrue(success);
	for(int i = 0; i < length; i++) {
		double value = child.getDoubleValue(i);
		if (i%2 == 1 || i >= length-4)
			Error.assertTrue(value == childValue);
		else if (i%4 == 0) {
			if (i < length-4)
				Error.assertTrue(value > parentValues[2]);
			else
				Error.assertTrue(value == childValue);
		} else if (i%2 == 0)  {
			if (i < length-4)
				Error.assertTrue(value < parentValues[1]);
			else
				Error.assertTrue(value == childValue);
		} else
			Error.assertTrue(value == childValue);
	}
	
	// test failure
	parents[1].setDoubleValue(4,0);
	parents[2].setDoubleValue(4,1);
	child = new EvolvableDoubleList(length,0);
	EvolvableDoubleList childCopy = (EvolvableDoubleList)child.copyForEvolution();
	Error.assertFalse(crossover.linearPart(child,fitness,parents,quadraticFailed));
	Error.assertTrue(child.isEqual(childCopy));
}
public void testPickOnePart() {
    RandomNumber.setSeed(990737400906L);   // to get deterministic results
	CrossoverAdewaya crossover = new CrossoverAdewaya();
	final int length = 100;
	final double childValue = 0;
	final double[] parentValues = {0.25, 0.5, 0.75};
	EvolvableDoubleList child = new EvolvableDoubleList(length,childValue);
	EvolvableDoubleList[] parents = {
		new EvolvableDoubleList(length,parentValues[0]),
		new EvolvableDoubleList(length,parentValues[1]),
		new EvolvableDoubleList(length,parentValues[2])};
	Indices quadraticFailed = new Indices(length,2);
	crossover.pickOnePart(child,parents,quadraticFailed);
	boolean[] hasParent = {false,false,false};
	for(int i = 0; i < length; i++) {
		double value = child.getDoubleValue(i);
		if (i%2 == 1)
			Error.assertTrue(value == childValue);
		else {
			boolean ok = false;
			for(int j = 0; j < parentValues.length; j++)
				if (value == parentValues[j]) {ok = true;hasParent[j] = true;}
			Error.assertTrue(ok);
		}
	}
	for(int i = 0; i < hasParent.length; i++)
		Error.assertTrue(hasParent[i]);
}
}
