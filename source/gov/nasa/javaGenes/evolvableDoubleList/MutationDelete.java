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
import gov.nasa.alsUtility.ExtendedVector;
import gov.nasa.alsUtility.Utility;

public class MutationDelete extends ChildMaker {
protected int minumumEvolvableSize;

public MutationDelete(Selector selector) {this(selector,0);}
public MutationDelete(Selector selector, int minumumEvolvableSize) {
    super(selector);
    Error.assertTrue(minumumEvolvableSize >= 0);
    this.minumumEvolvableSize = minumumEvolvableSize;
}
/** @return the input Evolvable in mutated condition */
public Evolvable mutate(Evolvable child) {
    EvolvableDoubleList c = (EvolvableDoubleList)child;
    int[] indices = selector.getIndicesArray(c);
    Utility.randomize(indices);
    ExtendedVector toDelete = new ExtendedVector();
    for(int i = 0; i < indices.length; i++)
        toDelete.add(c.getDouble(indices[i]));
    for(int i = 0; i < toDelete.size(); i++) {
        if (c.getSize() <= minumumEvolvableSize)
            break;
        c.remove((EvolvableDouble)toDelete.get(i));
    }
    return child;
}
public int getMinumumEvolvableSize() {return minumumEvolvableSize;}
public String toString() {
    return "MutationDelete selector = " + getSelector() + " minumumEvolvableSize = " + minumumEvolvableSize;
}
}
