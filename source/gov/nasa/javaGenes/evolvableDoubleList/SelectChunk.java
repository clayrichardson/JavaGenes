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
import gov.nasa.alsUtility.ExtendedVector;
import gov.nasa.alsUtility.RandomNumber;

public class SelectChunk extends Selector {
protected SelectModulo simpleSelector;
protected int[] numberToChoose;

public SelectChunk(int[] numberToChoose) {this(numberToChoose,1,0);}
public SelectChunk(int[] numberToChoose, int modulo) {this(numberToChoose,modulo,0);}
public SelectChunk(int[] numberToChoose,int modulo, int offset) {this(new SelectOne(), numberToChoose, modulo, offset);}
public SelectChunk(Selector selector, int[] numberToChoose,int modulo, int offset) {
	Error.assertNotNull(selector);
	simpleSelector = new SelectModulo(selector,modulo,offset);

    Error.assertTrue(numberToChoose.length > 0);
    this.numberToChoose = new int[numberToChoose.length];
    for(int i = 0; i < numberToChoose.length; i++) {
        Error.assertTrue(numberToChoose[i] > 0);
        this.numberToChoose[i] = numberToChoose[i];
    }
}
public Indices getIndices(int size) {
    Indices selection = new Indices();
    int maxWeCanChoose = size;
    ExtendedVector canChoose = new ExtendedVector();
    for(int i = 0; i < numberToChoose.length; i++)
        if (numberToChoose[i] <= maxWeCanChoose)
            canChoose.add(new Integer(numberToChoose[i]));
    int howManyToChoose = (canChoose.size() == 0) ? 0 : ((Integer)canChoose.getRandomElement()).intValue();
    if (howManyToChoose <= 0)
        return selection;

    int maxIndexPlusOneToChooseFrom = size - howManyToChoose + 1;
	int[] where = simpleSelector.getIndicesArray(maxIndexPlusOneToChooseFrom);
	for(int j = 0; j < where.length; j++) {
		int indexToChooseFrom = RandomNumber.getIntFromArray(where);
		for(int i = 0; i < howManyToChoose; i++) {
			selection.addIndex(indexToChooseFrom+i);
		}
	}
    return selection;
}
public String toString() {
    String s = "SelectChunk numberToChoose = ";
    for(int i = 0; i < numberToChoose.length; i++)
        s += " " + numberToChoose[i];
	s +=  " modulo=" + simpleSelector.modulo + " offset=" + simpleSelector.offset + " selector=" + simpleSelector;
    return s;
}
}
