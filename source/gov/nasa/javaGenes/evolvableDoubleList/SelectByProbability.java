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

public class SelectByProbability extends Selector {
protected double probability;
protected int minimumNumberToSelect;
public final int DEFAULT_MAXIMUM_TRIES = 5;

public SelectByProbability(double probability, int minimumNumberToSelect) {
    this.probability = probability;
    this.minimumNumberToSelect = minimumNumberToSelect;
}
public Indices getIndices(int size) {return getIndices(size, DEFAULT_MAXIMUM_TRIES);}
/**
@arg maxTries number of times to go through the list trying to select at least the minimum number
*/
public Indices getIndices(int size, int maxTries) {
    Indices selection = new Indices();
    if (size <= minimumNumberToSelect) {
        selection.addAll(size);
        return selection;
    }
    for(int tries = 0; tries < maxTries; tries++) {
        for(int i = 0; i < size; i++)
            if (RandomNumber.getProbability(probability))
                selection.addIndex(i);
    	if (selection.size() >= minimumNumberToSelect)
            return selection;
    }
    return selection;
}
public String toString() {
    return "SelectByProbability probability = " + probability + " minimumNumberToSelect = " + minimumNumberToSelect;
}

}