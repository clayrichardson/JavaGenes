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

import junit.framework.TestCase;
import gov.nasa.alsUtility.RandomNumber;

public class SelectByProbabilityTest extends TestCase {

public SelectByProbabilityTest(String name) {super(name);}

public void testSelectByProbability() {
    RandomNumber.setSeed(990739400906L);   // to get deterministic results
    assertTrue("1", testSelectByProbability(0.5, 3, 8).length >= 3);
    assertTrue("2", testSelectByProbability(0.001, 9, 8).length == 8);
    assertTrue("3", testSelectByProbability(1, 6, 8).length == 8);
}
public int[] testSelectByProbability(double probability, int minimumNumberToSelect, int length) {
    SelectByProbability selector = new SelectByProbability(probability,minimumNumberToSelect);
    return selector.getIndicesArray(length);
}
}