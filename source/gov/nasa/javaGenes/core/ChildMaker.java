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
import gov.nasa.alsUtility.LogComparisons;

/**
This is the superclass for all transmission operators.  All mutation and crossover
classes must be subclasses of ChildMaker.  Class is designed to default to mutation to
simplify mutation code.  See doc on individual methods for what needs to be over-ridden.
*/
public class ChildMaker implements java.lang.Cloneable, java.io.Serializable {
protected LogComparisons cumulative = new LogComparisons(new String[0]);
protected LogComparisons last = new LogComparisons(new String[0]);
protected LogComparisons forEvolution = new LogComparisons(new String[0]);

public boolean neverUsed() {
    return cumulative.getN() == 0;
}
public double proportionDown() {
    return cumulative.getDown();
}
/**
@return the number of parents makeChildren expects.  I.e., the length of the
array passed to makeChildren.  If not implemented by subclass, defaults to 1 (mutation).
*/
public int numberOfParents() {return 1;}
/**
Defaults to mutation, any subclass wanting more than one parent must over-ride.  Mutators can over-ride mutate(Evovlable)
@param parents the evolvables from which children will be created.  By convention, the parents are not modified.
@return the children created.  Subclass must implement copyForEvolution()
*/
public Evolvable[] makeChildren(Evolvable[] parents) {
    Error.assertTrue(parents.length == 1);
    Error.assertNotNull(parents[0]);
    Evolvable evolvable = mutate(parents[0].copyForEvolution());
    Evolvable[] child = {evolvable};
    return child;
}
/** only used when the variation operator need the fitness value */ 
public Evolvable[] makeChildren(Individual[] parents) {
	Evolvable[] p = new Evolvable[parents.length];
	for(int i = 0; i < p.length; i++)
		p[i] = parents[i].getEvolvable();
	return makeChildren(p);
}
/** 
mutators should over-ride, subclasses with multiple parents can ignore.  This method
should never be called from anywhere but makeChildren(Evolvable[]) in this class.
@return the input Evolvable in mutated condition
*/
protected Evolvable mutate(Evolvable chld) {
    Error.notImplemented();
    return null;
}
public void setFitnessFunction(FitnessFunction ff) {
    String[] fitnessNames = ff.getNameArray();
	setFitnessFunctionNames(fitnessNames);
}
public void setFitnessFunctionNames(String[] fitnessNames) {
	cumulative = new LogComparisons(fitnessNames);
	last = new LogComparisons(fitnessNames);
	forEvolution = new LogComparisons(fitnessNames);
}
public String[] getFitnessFunctionNames() {
	return cumulative.getNames();
}
/**
for testing only
*/
protected boolean checkResults(int index, int upv, int downv, int samev) {
    return cumulative.checkResults(index,upv,downv,samev);
}
protected boolean checkLastResults(int index, int upv, int downv, int samev) {
    return last.checkResults(index,upv,downv,samev);
}
public void results(Individual child, Individual[] parents) {
	child.getEvolvable().addChildMakers(this,parents);
    if (cumulative.getSize() == 0)
        return;
    double[] childFitness = child.getFitness().getFitnessArray();
    Error.assertTrue(cumulative.getSize() == childFitness.length);
	double[][] parentFitness = new double[childFitness.length][parents.length];
    for(int f = 0; f < parentFitness.length; f++) 
    for(int p = 0; p < parentFitness[f].length; p++) 
		parentFitness[f][p] = parents[p].getFitness().getFitnessArray()[f];
    cumulative.results(childFitness,parentFitness);
    last.results(childFitness,parentFitness);
    forEvolution.results(childFitness,parentFitness);
}
// coordinate with headerFragment()
public String tabSeparatedResults() {
	return cumulative.getTabSeparatedResults() + subClassMeasures();
}
// coordinate with headerFragment()
public String getAndClearLastTabSeparatedResults() {
	String r = getLastTabSeparatedResults();
	last.clear();
	return r;
}
public String getLastTabSeparatedResults() {
	return last.getTabSeparatedResults();
}
public void clearLast() {
	last.clear();
}
// coordinate with tabSeparatedResults()
public String headerFragment() {return cumulative.getHeaderFragment();}
public String subClassMeasures(){return "";}
public String toString() {return getClass().toString();}
}