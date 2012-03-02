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

import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.RandomNumber;

public class MutationInsert extends ChildMaker {
protected int[] numberToInsert;

public MutationInsert(int[] numberToInsert) {this(new SelectOne(), numberToInsert);}
public MutationInsert(Selector selector, int[] numberToInsert) {
	super(selector);
    Error.assertTrue(numberToInsert.length > 0);
    for(int i = 0; i < numberToInsert.length; i++)
        Error.assertTrue(numberToInsert[i] > 0);
    this.numberToInsert = numberToInsert;
}
public Evolvable mutate(Evolvable child) {
    EvolvableDoubleList c = (EvolvableDoubleList)child;
	int[] indices = getSelector().getIndicesArray(c.getSize()+1); // +1 necessary to make sure all insertion locations equally likely
	for(int indexToInsert = indices.length-1; indexToInsert >= 0; indexToInsert--) {
		EvolvableDouble[] toInsert = new EvolvableDouble[RandomNumber.getIntFromArray(numberToInsert)];
		for(int i = 0; i < toInsert.length; i++)
			toInsert[i] = new EvolvableDouble();
		if (indices[indexToInsert] == c.getSize())	
			c.insertAfter(c.getSize()-1,toInsert); // this is why the +1 above
		else
			c.insertBefore(indices[indexToInsert],toInsert);
	}
    return c;
}
public String toString() {
    String s = "MutationInsert selector = " + getSelector();
    for(int i = 0; i < numberToInsert.length; i++)
        s += " " + numberToInsert[i];
    return s;
}
}
