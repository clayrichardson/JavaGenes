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

public class CrossoverPickOne extends ChildMaker {

public CrossoverPickOne() {this(new SelectByProbability(0.5,1));}
public CrossoverPickOne(Selector selector) {super(selector);}
public gov.nasa.javaGenes.core.Evolvable[] makeChildren(gov.nasa.javaGenes.core.Evolvable[] parents) {
    Error.assertTrue(parents.length == 2);
    for(int i = 0; i < parents.length; i++)
        Error.assertTrue(parents[i].getSize() > 0);
    EvolvableDoubleList dad = null;
    EvolvableDoubleList mom = null;

	if (RandomNumber.getBoolean()) {
		dad = (EvolvableDoubleList)parents[0];
		mom = (EvolvableDoubleList)parents[1];
	} else {
		dad = (EvolvableDoubleList)parents[1];
		mom = (EvolvableDoubleList)parents[0];
	}
    EvolvableDoubleList child = (EvolvableDoubleList)dad.copyForEvolution();
    int[] indices = getSelector().getIndicesArray(Math.min(dad.getSize(),mom.getSize()));
    for(int i = 0; i < indices.length; i++)
		child.setDoubleValue(indices[i], mom.getDoubleValue(indices[i]));

    EvolvableDoubleList[] children = new EvolvableDoubleList[1];
    children[0] = child;
    return children;
}
public int numberOfParents() {return 2;}
public String toString() {return "CrossoverPickOne selector = " + getSelector();}
}


