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
import gov.nasa.alsUtility.RandomNumber;
import java.lang.Math;

/**
Mutates selected doubles with a random number chosen
from a Gaussian distribution with the standard deviation
taken by the difference in two of the children
*/
public class Mutation3parents extends ChildMaker {

public Mutation3parents(Selector selector) {
    super(selector);
}
public gov.nasa.javaGenes.core.Evolvable[] makeChildren(gov.nasa.javaGenes.core.Evolvable[] parents) {
    Error.assertTrue(parents.length == 3);
    for(int i = 0; i < parents.length; i++)
        Error.assertTrue(parents[i].getSize() > 0);
    EvolvableDoubleList parent = (EvolvableDoubleList)parents[0];
    EvolvableDoubleList limit1 = (EvolvableDoubleList)parents[1];
    EvolvableDoubleList limit2 = (EvolvableDoubleList)parents[2];
    
    EvolvableDoubleList child = (EvolvableDoubleList)parent.copyForEvolution();
    int[] indices = getSelector().getIndicesArray(child);
    
    for(int i = 0; i < indices.length; i++) {
        double standardDeviation = Math.abs(limit1.getDoubleValueModulo(i)-limit2.getDoubleValueModulo(i));
        child.getDouble(i).mutateByStandardDeviation(standardDeviation);
    }

    EvolvableDoubleList[] children = new EvolvableDoubleList[1];
    children[0] = child;
    return children;
}
public int numberOfParents() {return 3;}
public String toString() {return "Mutation3Parents selector = " + getSelector();}
}


