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

public class SelectModulo extends Selector {
protected Selector selector;
protected int modulo = 1;
protected int offset = 0;

public SelectModulo(Selector selector, int modulo) {this(selector,modulo,0);}
public SelectModulo(Selector selector, int modulo, int offset) {
	this.selector = selector;
	this.modulo = modulo;
	this.offset = offset;
	Error.assertFalse(selector instanceof NeighboringPairsSelector);
	Error.assertTrue(modulo > 0);
	Error.assertTrue(offset >= 0);
}

public Indices getIndices(int size) {
	int[] indicesFromSelector = selector.getIndicesArray(getDummySize(size));
    Indices selection = new Indices();
	for(int i = 0; i < indicesFromSelector.length; i++)
		selection.addIndex(convertBack(indicesFromSelector[i]));
	return selection;
}
protected int getDummySize(int oldSize) {
	int dummySize = oldSize - offset;
	dummySize = 1 + ((dummySize-1)/modulo);
	return dummySize;
}
protected int convertBack(int indexToDummy) {
	return (indexToDummy*modulo) + offset;
}
public String toString() {
    return "SelectModulo modulo=" + modulo + " offset=" + offset + " selector=" + selector.toString();
}
}