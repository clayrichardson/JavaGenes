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

public class CrossoverTwoPointsEach extends CrossoverTwoPoints {

public CrossoverTwoPointsEach() {this(new SelectTwoNeighboringPairs());}
public CrossoverTwoPointsEach(int modulo) {this(new SelectTwoNeighboringPairs(modulo,0));}
public CrossoverTwoPointsEach(int modulo, int offset) {this(new SelectTwoNeighboringPairs(modulo,offset));}
public CrossoverTwoPointsEach(SelectTwoNeighboringPairs selector) {super(selector);}

protected int[] getFirstIndicesArray(gov.nasa.javaGenes.core.Evolvable[] parents) {
	return getSelector().getIndicesArray((EvolvableDoubleList)parents[0]);
}
protected int[] getSecondIndicesArray(gov.nasa.javaGenes.core.Evolvable[] parents) {
	return getSelector().getIndicesArray((EvolvableDoubleList)parents[1]);
}
public String toString() {return "CrossoverTwoPointsEach selector = " + selector.toString();}
}


