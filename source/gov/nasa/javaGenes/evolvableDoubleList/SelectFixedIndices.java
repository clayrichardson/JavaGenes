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

public class SelectFixedIndices extends Selector {
protected int[] indicesToSelect;

public SelectFixedIndices(int[] indicesToSelect) {
    Error.assertTrue(indicesToSelect.length > 0);
    this.indicesToSelect = new int[indicesToSelect.length];
    for(int i = 0; i < this.indicesToSelect.length; i++) {
        Error.assertTrue(indicesToSelect[i] >= 0);
        this.indicesToSelect[i] = indicesToSelect[i];
    }
}
public Indices getIndices(int size) {
    Indices selection = new Indices();
    for(int i = 0; i < indicesToSelect.length; i++)
        if (indicesToSelect[i] < size)
            selection.addIndex(indicesToSelect[i]);
    return selection;
}
public String toString() {
    String s = "SelectFixedIndices indicesToSelect = ";
    for(int i = 0; i < indicesToSelect.length; i++)
        s += " " + indicesToSelect[i];
    return s;
}
}