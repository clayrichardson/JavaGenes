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
import gov.nasa.alsUtility.DoubleInterval;


public class EvolvableDoubleListTest extends TestCase {

public EvolvableDoubleListTest(String name) {super(name);}

public void testGetDoubleModule() {
    EvolvableDoubleList list = new EvolvableDoubleList(10);
    assertTrue("1", list.getDoubleValue(1) == list.getDoubleValueModulo(1));
    assertTrue("2", list.getDoubleValue(1) == list.getDoubleValueModulo(11));
    assertTrue("3", list.getDoubleValue(9) == list.getDoubleValueModulo(9));
    assertTrue("4", list.getDoubleValue(9) == list.getDoubleValueModulo(59));
    assertTrue("5", list.getDoubleValue(0) == list.getDoubleValueModulo(0));
    assertTrue("6", list.getDoubleValue(0) == list.getDoubleValueModulo(100));
}
public void testIsEqual() {
    double[] d1 = {1,2,3};
    double[] d2 = {1,2,3};
    assertTrue("1", testIsEqual(d1,d2));
    double[] d3 = {3,2,1};
    assertFalse("2", testIsEqual(d1,d3));
    double[] d4 = {1,2};
    assertFalse("3", testIsEqual(d1,d4));
}
private boolean testIsEqual(double[] d1, double[] d2) {
    final double divideBy = 10;
    EvolvableDoubleList list1 = new EvolvableDoubleList();
    for(int i = 0; i < d1.length; i++)
        list1.addDoubleValue(d1[i]/divideBy);
    EvolvableDoubleList list2 = new EvolvableDoubleList();
    for(int i = 0; i < d2.length; i++)
        list2.addDoubleValue(d2[i]/divideBy);
    return list1.isEqual(list2);
}
   
public void testCopyForEvolution() {
    copyForEvolutionTest("1",7);
    copyForEvolutionTest("2",1);
    copyForEvolutionTest("3",4);
    copyForEvolutionTest("4",3);
}
protected void copyForEvolutionTest(String name, int length) {
    EvolvableDoubleList list = new EvolvableDoubleList(length);
    assertTrue(name+" length", list.getSize() == length);
    EvolvableDoubleList copy = (EvolvableDoubleList)list.copyForEvolution();
    assertTrue("isEqual", list.isEqual(copy));
}
public void testInsert() {
    double[] initial = {1,2,3};
    double[] insert = {4};
    int insertAt = 1;
    double[] insertBeforeResult = {1,4,2,3};
    double[] insertAfterResult = {1,2,4,3};
    testInsert("1", initial, insert, insertAt, insertBeforeResult, insertAfterResult);
}
protected void testInsert(String name, double[] initial, double[] insert, int insertAt, double[] insertBeforeResult, double[] insertAfterResult) {
    final double divideBy = 10;
    EvolvableDoubleList initialList = new EvolvableDoubleList(initial,divideBy);
    EvolvableDouble[] toInsert = new EvolvableDouble[insert.length];
    for(int i = 0; i < toInsert.length; i++)
        toInsert[i] = new EvolvableDouble(insert[i]/divideBy);
    EvolvableDoubleList copy = (EvolvableDoubleList)initialList.copyForEvolution();
    copy.insertBefore(insertAt, toInsert);
    assertTrue(name + " before", copy.isEqual(new EvolvableDoubleList(insertBeforeResult,divideBy)));
    initialList.insertAfter(insertAt, toInsert);
    assertTrue(name + " after", initialList.isEqual(new EvolvableDoubleList(insertAfterResult,divideBy)));
}
}
