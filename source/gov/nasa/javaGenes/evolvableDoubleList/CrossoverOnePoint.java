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

public class CrossoverOnePoint extends Crossover {

public CrossoverOnePoint() {this(new SelectOneNeighboringPair());}
public CrossoverOnePoint(int modulo) {this(new SelectOneNeighboringPair(modulo,0));}
public CrossoverOnePoint(int modulo, int offset) {this(new SelectOneNeighboringPair(modulo,offset));}
public CrossoverOnePoint(SelectOneNeighboringPair selector) {super(selector);}

protected void crossover(EvolvableDoubleList child, EvolvableDoubleList front, EvolvableDoubleList back, int[] frontIndices, int[] backIndices) {
	child.removeAll();
	int frontIndex = frontIndices.length >= 2 ? frontIndices[0] : front.getLastIndex();
	int backIndex = backIndices.length >= 2 ? backIndices[1] : 0;
    for(int i = 0; i <= frontIndex; i++)
        child.addDoubleValue(front.getDoubleValue(i));
    for(int i = backIndex; i < back.getSize(); i++)
        child.addDoubleValue(back.getDoubleValue(i));
}

public String toString() {return "CrossoverOnePoint selector = " + selector.toString();}
}


