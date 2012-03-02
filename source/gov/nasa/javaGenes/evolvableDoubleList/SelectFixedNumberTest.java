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
import gov.nasa.alsUtility.Utility;

public class SelectFixedNumberTest extends TestCase {

public SelectFixedNumberTest(String name) {super(name);}

public void testSelectFixedNumber() {
    RandomNumber.setSeed(990739400906L);   // to get deterministic results
    assertTrue("1", testSelectFixedNumber(3, 8).length == 3);
    assertTrue("2", testSelectFixedNumber(9, 8).length == 8);
    assertTrue("3", testSelectFixedNumber(6, 8).length == 6);
    assertTrue("4", testSelectFixedNumber(1, 8).length == 1);
    assertTrue("5", testSelectFixedNumber(8, 8).length == 8);
    int[] correct = {2,4,7};
    assertTrue("6", Utility.equals(correct,testSelectFixedNumber(3,8))); // will fail if you change random number generation
}
public int[] testSelectFixedNumber(int numberToSelect, int evolvableLength) {
    SelectFixedNumber selector = new SelectFixedNumber(numberToSelect);
    int[] indices = selector.getIndicesArray(evolvableLength);
    //for(int i = 0; i < indices.length; i++)
        //System.out.print(indices[i] + " ");
    //System.out.println();
    return indices;
}
}