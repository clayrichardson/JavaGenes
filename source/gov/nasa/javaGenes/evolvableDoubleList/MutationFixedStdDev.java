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

public class MutationFixedStdDev extends ChildMaker {
protected double standardDeviation;

public MutationFixedStdDev(Selector selector, double standardDeviation) {
    super(selector);
    this.standardDeviation = standardDeviation;
}
/** @return the input Evolvable in mutated condition */
public Evolvable mutate(Evolvable child) {
    EvolvableDoubleList c = (EvolvableDoubleList)child;
    int[] indices = getSelector().getIndicesArray(c);
    for(int i = 0; i < indices.length; i++)
        c.getDouble(indices[i]).mutateByStandardDeviation(standardDeviation);
    return child;
}
public String toString() {
    return "MutationFixedStdDev selector = " + selector + " standardDeviation = " + standardDeviation;
}
}
