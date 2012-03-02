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

import java.lang.Math;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.DoubleInterval;

public class CrossoverInterval extends ChildMaker {
protected double expandIntervalsBy;

public CrossoverInterval(Selector selector, double expandIntervalsBy) {
    super(selector);
    this.expandIntervalsBy = expandIntervalsBy;
}
public gov.nasa.javaGenes.core.Evolvable[] makeChildren(gov.nasa.javaGenes.core.Evolvable[] parents) {
    Error.assertTrue(parents.length == 2);
    for(int i = 0; i < parents.length; i++)
        Error.assertTrue(parents[i].getSize() > 0);
    EvolvableDoubleList dad = (EvolvableDoubleList)parents[0];
    EvolvableDoubleList mom = (EvolvableDoubleList)parents[1];

    EvolvableDoubleList child = (EvolvableDoubleList)(dad.getSize() < mom.getSize() ? dad.copyForEvolution() : mom.copyForEvolution());
    int[] indices = getSelector().getIndicesArray(child);
    for(int i = 0; i < indices.length; i++) {
        int index = indices[i];
        DoubleInterval interval = new DoubleInterval(dad.getDoubleValue(index),mom.getDoubleValue(index));
        interval.expand(expandIntervalsBy);
        interval.limitTo(EvolvableDouble.universalRange);
        child.getDouble(index).setValue(interval.random());
    }

    EvolvableDoubleList[] children = new EvolvableDoubleList[1];
    children[0] = child;
    return children;
}
public int numberOfParents() {return 2;}
public String toString() {return "CrossoverInterval selector = " + getSelector() + " expandIntervalsBy = " + expandIntervalsBy;}
}


