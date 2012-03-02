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

import java.lang.ArithmeticException;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.MathUtility;
import gov.nasa.alsUtility.RandomNumber;

/** Implementation of Adewaya's MIT Master's Thesis "New Methods in Genetic Search with Real-Valued Chromosomes" 1996.
Required three parents.  One difference, we are looking for minimal fitness (Adewaya looked for maxima) */
public class CrossoverAdewaya extends ChildMaker {
protected final double BAD_VALUE = -1000;
protected int maxLinearTries = 5;

public CrossoverAdewaya() {this(new SelectAll());}
public CrossoverAdewaya(Selector selector) {super(selector);}
public CrossoverAdewaya(Selector selector, int maxLinearTries) {
	this(selector);
	this.maxLinearTries = maxLinearTries;
}
public int numberOfParents() {return 3;}

public gov.nasa.javaGenes.core.Evolvable[] makeChildren(gov.nasa.javaGenes.core.Evolvable[] parents) {Error.notImplemented(); return null;} // requires fitness
public gov.nasa.javaGenes.core.Evolvable[] makeChildren(gov.nasa.javaGenes.core.Individual[] parents) {
    Error.assertTrue(parents.length == numberOfParents());
    for(int i = 0; i < parents.length; i++) {
        Error.assertNotNull(parents[i]);
		Error.assertNotNull(parents[i].getFitness());
		Error.assertNotNull(parents[i].getEvolvable());
	}
	
	EvolvableDoubleList[] evolvables = new EvolvableDoubleList[parents.length];
	double[] fitness = new double[parents.length];
	for(int i = 0; i < parents.length; i++) {
		evolvables[i] = (EvolvableDoubleList)parents[i].getEvolvable();
		fitness[i] = parents[i].getFitness().asDouble();
	}
	EvolvableDoubleList child = (EvolvableDoubleList)evolvables[0].copyForEvolution();
	Indices quadraticFailed = quadraticPart(child, fitness, evolvables);
	if (!linearPart(child, fitness, evolvables, quadraticFailed))
		pickOnePart(child, evolvables, quadraticFailed);
    EvolvableDoubleList[] children = {child};
    return children;
}
protected Indices quadraticPart(EvolvableDoubleList child, double[] fitness, EvolvableDoubleList[] evolvables) {
	Indices quadraticFailed = new Indices();
	int[] indices = selector.getIndicesArray((EvolvableDoubleList)gov.nasa.javaGenes.core.Evolvable.getSmallest(evolvables));

	for(int i = 0; i < indices.length; i++) {
		int index = indices[i];
		double value = getQuadraticMinimum(fitness,evolvables,index);
		double legalValue = EvolvableDouble.limitToLegalRange(value);
		if (!EvolvableDouble.isWithinRange(value) && !Utility.nearlyEqual(value,legalValue))
			quadraticFailed.addIndex(index);
		else
			child.setDoubleValue(index,legalValue);
	}
	return quadraticFailed;
}

protected double getQuadraticMinimum(double[] fitness, EvolvableDoubleList[] evolvables, int index) {
	double f1 = fitness[0];
	double f2 = fitness[1];
	double f3 = fitness[2];
	double v1 = evolvables[0].getDoubleValue(index);
	double v2 = evolvables[1].getDoubleValue(index);
	double v3 = evolvables[2].getDoubleValue(index);
	
	try {
		double[] abc = MathUtility.getQuadraticFactors(v1,f1,v2,f2,v3,f3);
		if (abc[0] < 0 || Utility.nearlyEqual(abc[0],0))
			return BAD_VALUE;	
		return MathUtility.getQuadraticExtremumX(abc);
	} catch (ArithmeticException e) {
		return BAD_VALUE;
	}
}
/**
@param child is modified if successful
@return true if linear assignment succeeds 
*/
protected boolean linearPart(EvolvableDoubleList child, double[] fitness, EvolvableDoubleList[] evolvables, Indices quadraticFailed) {
	if (quadraticFailed.size() == 0)
		return true;
	int[] indices = quadraticFailed.getArray();

	int bestIndex = Utility.indexOfLeast(fitness);
	int worstIndex = Utility.indexOfGreatest(fitness);
	double r = RandomNumber.getDouble();
	boolean feasible = true;
	double[] results = new double[indices.length];
	for(int t = 0; t < maxLinearTries; t++) {
		feasible = true;
		for(int i = 0; i < indices.length; i++) {
			int index = indices[i];
			double value = getLinearValue(r,evolvables[bestIndex].getDoubleValue(index),evolvables[worstIndex].getDoubleValue(index));
			double legalValue = EvolvableDouble.limitToLegalRange(value);
			if (!EvolvableDouble.isWithinRange(value) && !Utility.nearlyEqual(value,legalValue)) {
				feasible = false;
				break;
			} else
				results[i] = legalValue;
		}
		r /= 2.0;
	}
	if (feasible)
		for(int i = 0; i < indices.length; i++)
			child.setDoubleValue(indices[i],results[i]);

	return feasible;
}
/** best <= worst */
protected double getLinearValue(double r, double best, double worst) {
	return best + r*(best-worst);
}
/**
@param child is modified
*/
protected void pickOnePart(EvolvableDoubleList child, EvolvableDoubleList[] evolvables, Indices quadraticFailed) {
	int[] indices = quadraticFailed.getArray();
	for(int i = 0; i < indices.length; i++)
			child.setDoubleValue(indices[i],evolvables[RandomNumber.getIndex(evolvables.length)].getDoubleValue(i));
}
public String toString() {return "CrossoverAdewaya selector = " + getSelector();}
}


