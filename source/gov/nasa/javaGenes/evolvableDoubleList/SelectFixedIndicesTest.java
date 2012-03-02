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
import gov.nasa.alsUtility.Utility;

public class SelectFixedIndicesTest extends TestCase {

public SelectFixedIndicesTest(String name) {super(name);}

public void testSelectFixedIndices() {
    int[] a = {1,2,4};
    testSelectFixedIndices("1",5,a,a);
    int[] b = {1,2};
    testSelectFixedIndices("2",4,a,b);
    int[] c = {4,2,1};
    testSelectFixedIndices("3",6,c,a);    
    testSelectFixedIndices("4",3,c,b);    
}
public void testSelectFixedIndices(String name, int evolvableLength, int[] selections, int[] results) {
    SelectFixedIndices selector = new SelectFixedIndices(selections);
    int[] indices = selector.getIndicesArray(evolvableLength);
    assertTrue(name + Utility.toString(indices), Utility.equals(results,indices));
}
}