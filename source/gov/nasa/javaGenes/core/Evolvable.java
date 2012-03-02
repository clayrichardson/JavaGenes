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

import java.lang.Cloneable;
import java.io.Serializable;
import gov.nasa.alsUtility.KeyCounter;

/**
objects that can evolve in a genetic software system.
*/
public class Evolvable implements Cloneable, Serializable {
protected KeyCounter childMakersUsed = new KeyCounter(); // keep track of the variation operators used to create this evolvable

/** for testing only */
protected Evolvable() {} 
public KeyCounter getChildMakersUsed() {return childMakersUsed;} // do not modify!
public void addChildMakers(ChildMaker madeMe,Individual[] parents) {
	childMakersUsed = new KeyCounter();
	childMakersUsed.add(madeMe.toString());
	int index = Individual.bestIndividualIndex(parents);
	Evolvable evolvable = parents[index].getEvolvable();
	KeyCounter parentsUsed = evolvable.getChildMakersUsed();
	if (parentsUsed.size() > 0)
		childMakersUsed.addKeyCounter(parentsUsed);
}			
	
public void stateSave(TokenizeOutput tokenizer) {Error.notImplemented();} // obsolete
/** 
should be implemented by all subclasses, but isn't for all of the older code.  The functionality
is there but the naming is different.
*/
protected Evolvable copyForEvolution() {
    Error.notImplemented();
    return null;
}
/**
@return the size of the object
*/
public int getSize() {return 0;}
/**
Subclasses will almost always override this.

@return the distance from Evolvable e. 
*/
public double distanceFrom(Evolvable e) {return -1;}
/** 
Used when special setup is needed to calculate the evaluation fitness
Reporter.java does when each generation is complete.  Originally
implemented for EOSscheduling.HBSS since any rescheduling will
generate a new permutation.
*/
public void prepareForEvaluator(Parameters parameters) {}
static public Evolvable getSmallest(Evolvable[] array) {
	Error.assertNotNull(array);
	Error.assertFalse(array.length < 1);
	Evolvable smallest = array[0];
	for(int i = 1; i < array.length; i++) {
		Error.assertNotNull(array[i]);
		if (array[i].getSize() < smallest.getSize())
			smallest = array[i];
	}
	return smallest;
}
}
