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

import gov.nasa.alsUtility.Error;

public class CrossoverTwoPoints extends Crossover {

public CrossoverTwoPoints() {this(new SelectTwoNeighboringPairs());}
public CrossoverTwoPoints(int modulo) {this(new SelectTwoNeighboringPairs(modulo,0));}
public CrossoverTwoPoints(int modulo, int offset) {this(new SelectTwoNeighboringPairs(modulo,offset));}
public CrossoverTwoPoints(SelectTwoNeighboringPairs selector) {super(selector);}

/** 
insertInto (the first parent) chooses the elements between the indices to replace, takeFrom (the second parent) insertInto.
This implies that the first and last value in the first parent will always be in the child
*/
protected void crossover(EvolvableDoubleList child, EvolvableDoubleList insertIntoList, EvolvableDoubleList takeFromList, int[] insertInto, int[] takeFrom) {
	if (!isValid(insertInto) || !isValid(takeFrom))
		return;
	int removeIndex = insertInto[1];
	for(int i = removeIndex; i <= insertInto[2]; i++)
		child.remove(removeIndex);
			
	EvolvableDouble[] insert = new EvolvableDouble[takeFrom[2] - takeFrom[1] + 1];
	for(int i = 0; i < insert.length; i++)
		insert[i] = takeFromList.getDouble(i+takeFrom[1]);
	child.insertAfter(insertInto[0],insert);
}
protected boolean isValid(int[] array) {
	if (array.length < 4)   
		return false;
	if (array[1] - array[0] != 1)
		return false;
	if (array[3] - array[2] != 1)
		return false;
	if (array[3] <= array[0])
		return false;
	return true;
}

public String toString() {return "CrossoverTwoPoints selector = " + selector.toString();}
}


