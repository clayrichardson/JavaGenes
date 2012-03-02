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
package gov.nasa.alsUtility;

import junit.framework.TestCase;
import java.lang.Integer;
import java.util.Vector;

public class ExtendedVectorTest extends TestCase {

public ExtendedVectorTest(String name) {super(name);}

public void testIsEqual() {
    int[] a = {1,2,3};
    int[] b = {1,2,4};
    int[] c = {1,2};
    int[] d = {3,2,1};
    assertTrue("1", new ExtendedVector(a).isEqual(new ExtendedVector(a)));
    assertFalse("2", new ExtendedVector(a).isEqual(new ExtendedVector(b)));
    assertFalse("3", new ExtendedVector(a).isEqual(new ExtendedVector(c)));
    assertFalse("4", new ExtendedVector(a).isEqual(new ExtendedVector(d)));
}
public void testInsert() {
    int[] initial = {1,2,3};
    int[] insert = {4,-1};
    int insertAt = 1;
    int[] insertBeforeResult = {1,4,-1,2,3};
    int[] insertAfterResult = {1,2,4,-1,3};
    testInsert("1", initial, insert, insertAt, insertBeforeResult, insertAfterResult);
    insertAt = 0;
    int[] insertBeforeResult1 = {4,-1,1,2,3};
    int[] insertAfterResult1 = {1,4,-1,2,3};
    testInsert("2", initial, insert, insertAt, insertBeforeResult1, insertAfterResult1);
    insertAt = 2;
    int[] insertBeforeResult2 = {1,2,4,-1,3};
    int[] insertAfterResult2 = {1,2,3,4,-1};
    testInsert("3", initial, insert, insertAt, insertBeforeResult2, insertAfterResult2);
    int[] initial4 = {1,2,3};
    int[] insert4 = {4};
    insertAt = 1;
    int[] insertBeforeResult4 = {1,4,2,3};
    int[] insertAfterResult4 = {1,2,4,3};
    testInsert("4", initial4, insert4, insertAt, insertBeforeResult4, insertAfterResult4);
     
}
protected void testInsert(String name, int[] initial, int[] insert, int insertAt, int[] insertBeforeResult, int[] insertAfterResult) {
    ExtendedVector initialList = new ExtendedVector(initial);
    ExtendedVector toInsert = new ExtendedVector(insert);
    ExtendedVector copy = initialList.copy();
    copy.insertBefore(insertAt, toInsert);
    assertTrue(name + " before", copy.isEqual(new ExtendedVector(insertBeforeResult)));
    initialList.insertAfter(insertAt, toInsert);
    assertTrue(name + " after", initialList.isEqual(new ExtendedVector(insertAfterResult)));
}
}
