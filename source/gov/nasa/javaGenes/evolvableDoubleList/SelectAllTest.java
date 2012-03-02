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
import gov.nasa.alsUtility.IntegerInterval;

public class SelectAllTest extends TestCase {

public SelectAllTest(String name) {super(name);}

public void test() {
    RandomNumber.setSeed(990739400906L);   // to get deterministic results
    final IntegerInterval interval = new IntegerInterval(1,30);
    for(int i = 0; i < 20; i++) {
        SelectAll selector = new SelectAll();
        EvolvableDoubleList list = new EvolvableDoubleList(interval.random());
        int[] indices = selector.getIndicesArray(list.getSize());
        assertTrue(i+" length", indices.length == list.getSize());
        for(int j = 0; j < indices.length; j++)
            assertTrue(i+" "+j, indices[j] == j);
    }
}
public int[] testSelectByProbability(double probability, int minimumNumberToSelect, int length) {
    SelectByProbability selector = new SelectByProbability(probability,minimumNumberToSelect);
    return selector.getIndicesArray(length);
}
}