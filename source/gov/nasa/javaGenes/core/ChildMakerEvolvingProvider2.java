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

import java.util.Collections;
import gov.nasa.alsUtility.Error;

/**
starts with a set of random ChildMakers.  Every n get()s, throws out the worst ones and creates new ones to take their place
*/
public class ChildMakerEvolvingProvider2 extends ChildMakerProvider {
protected int numberOfGetsPerGeneration;
protected double numberToKill;
protected ChildMakerRandomCreator childMakerCreator;
protected int numberOfGetsSoFar;
protected double evolutionScaleFactor; // used to reduce the influence of old data on the evolutionary comparison
protected ChildMakerDownFractionComparator downFractionComparator = new ChildMakerDownFractionComparator();

public ChildMakerEvolvingProvider2(int numberOfGetsPerGeneration, int numberOfChildMakers, int numberToKill, double evolutionScaleFactor, ChildMakerRandomCreator childMakerCreator) {
	Error.assertTrue(numberOfChildMakers > 0);
	Error.assertTrue(numberOfGetsPerGeneration > 0);
	Error.assertTrue(numberToKill > 0);
	Error.assertTrue(evolutionScaleFactor >= 0);
	Error.assertNotNull(childMakerCreator);
	this.numberOfGetsPerGeneration = numberOfGetsPerGeneration;
	this.numberToKill = numberToKill;
	this.evolutionScaleFactor = evolutionScaleFactor;
	this.childMakerCreator = childMakerCreator;
	for(int i = 0; i < numberOfChildMakers; i++)
		add(childMakerCreator.create());
}
public ChildMaker get() {
	if (numberOfGetsSoFar >= numberOfGetsPerGeneration) {
		evolve();
		numberOfGetsSoFar = 0;
		for(int i = 0; i < childMakers.size(); i++)
			get(i).forEvolution.scaleBy(evolutionScaleFactor);
	}
	numberOfGetsSoFar++;
	return super.get();
}
protected void evolve() {
	Error.assertTrue(size() > 0);
	String[] names = get(0).getFitnessFunctionNames();
	Collections.sort(childMakers,downFractionComparator);
	for(int i = 0; i < numberToKill; i++)
		removeLastChildMaker();
	for(int i = 0; i < numberToKill; i++) {
		ChildMaker cm = childMakerCreator.create();
		cm.setFitnessFunctionNames(names);
		add(cm);
	}
}

public String toString() {return "ChildMakerEvolvingProvider2 numberOfGetsPerGeneration=" + numberOfGetsPerGeneration +
		" numberToKill= " + numberToKill + " childMakerCreator=" + childMakerCreator + " " + super.toString();}
}
