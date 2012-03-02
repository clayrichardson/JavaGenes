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

import java.util.Vector;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.alsUtility.RandomNumber;

/**
probabalistically mutates the children of a ChildMaker.  Can chain mutators for an unlimited length.
*/
public class ChildMakerChain extends ChildMaker {
protected ChildMaker initialChildMaker;
protected Vector chain = new Vector(); // of ChainElement
protected class ChainElement implements java.io.Serializable {
	ChildMaker childMaker;
	double probability;
	public ChainElement(ChildMaker childMaker, double probability) {
		Error.assertNotNull(childMaker);
		this.childMaker = childMaker;
		this.probability = probability;
	}
	public String toString() {return childMaker.toString() + " probability=" + probability;}
}

public ChildMakerChain(ChildMaker childMaker) {
	Error.assertNotNull(childMaker);
	initialChildMaker = childMaker;
}
/** mutator must take one parent only */
public void addMutator(ChildMaker mutator, double probability) {
	final DoubleInterval probabilityRange = new DoubleInterval(0,1);
	Error.assertNotNull(mutator);
	Error.assertTrue(mutator.numberOfParents() == 1);
	Error.assertTrue(probabilityRange.isBetween(probability));
	chain.add(new ChainElement(mutator,probability));
}
public int numberOfParents() {return initialChildMaker.numberOfParents();}
public Evolvable[] makeChildren(Evolvable[] parents) {
    Error.assertTrue(parents.length == numberOfParents());
	for(int i = 0; i < parents.length; i++)
		Error.assertNotNull(parents[i]);
    Evolvable[] children = initialChildMaker.makeChildren(parents);
	for(int child = 0; child < children.length; child++)
		for(int i = 0; i < chain.size(); i++) 
			if (RandomNumber.getProbability(getFromChain(i).probability))
				getFromChain(i).childMaker.mutate(children[child]);
			else
				break;
    return children;
}
protected ChainElement getFromChain(int index) {return (ChainElement)chain.get(index);}
public String toString() {
	String s = "ChildMakerChain " + initialChildMaker.toString();
	for(int i = 0; i < chain.size(); i++)
		s += " " + getFromChain(i);
	return s;
}
}