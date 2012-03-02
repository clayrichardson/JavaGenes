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

import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.Error;

/**
selects a fixed number of indices, or all of them if there aren't enough */
public class SelectFixedNumber extends Selector {
protected int numberToSelect;

public SelectFixedNumber(int numberToSelect) {
    Error.assertTrue(numberToSelect > 0);
    this.numberToSelect = numberToSelect;
}
public Indices getIndices(int size) {
    if (size <= numberToSelect) {
        Indices selection = new Indices();
        selection.addAll(size);
        return selection;
    }
    double probability = (double)numberToSelect/(double)size;
    SelectByProbability selectByProbability = new SelectByProbability(probability, numberToSelect);
    Indices selection = selectByProbability.getIndices(size,MAXIMUM_TRIES);
    while (selection.size() > numberToSelect)
        selection.remove(RandomNumber.getIndex(selection.size()));
    return selection;
}
public String toString() {
    return "SelectFixedNumber numberToSelect = " + numberToSelect;
}

}