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
//  Created by Al Globus on Tue Nov 12 2002.
package gov.nasa.javaGenes.EOSscheduling;

import junit.framework.TestCase;
import gov.nasa.alsUtility.Error;

public class EOSschedulingEvolvableTest extends TestCase {

public EOSschedulingEvolvableTest(String name) {super(name);}

public void testSetTaskPlacement() {
    final int size = 3;
    EOSschedulingEvolvable a = new EOSschedulingEvolvable(size);
    EOSschedulingEvolvable b = new EOSschedulingEvolvable(size);
    TaskPlacementData data = new TaskPlacementData();
    data.setStartTime(5);
    data.setSensorNumber(6);
    data.setWindowNumber(7);
    
    int aIndex = 1;
    int bIndex = 2;
    int aTask = a.getIndexAt(aIndex);
    int bTask = b.getIndexAt(bIndex);

    assertTrue("1", a.getTaskPlacement(aTask).theSameAs(b.getTaskPlacement(bTask)));
    a.setTaskPlacement(aTask,data);
    assertTrue("2", !a.getTaskPlacement(aTask).theSameAs(b.getTaskPlacement(bTask)));
    b.setTaskPlacement(bIndex,a,aIndex);
    assertTrue("3", a.getTaskPlacement(aTask).theSameAs(b.getTaskPlacement(bTask)));
}
public void testIndexOnUnschedulable() {
    int[] i1 = {1,3,5};
    int[] i2 = {};
    int[] i3 = {4,9,2,0};
    int[] i4 = {0,1,2,3};
    indexOnUnschedulableTest(6,i1);
    indexOnUnschedulableTest(20,i1);
    indexOnUnschedulableTest(20,i2);
    indexOnUnschedulableTest(20,i3);
    indexOnUnschedulableTest(10,i3);
    indexOnUnschedulableTest(4,i4);
}
public void indexOnUnschedulableTest(int size, int[] beginning) {
    Error.assertTrue(size >= beginning.length);
    EOSschedulingEvolvable a = new EOSschedulingEvolvable(size);
    for(int i = 0; i < beginning.length; i++)
        a.setIndexAt(i,beginning[i]);
    a.indexOnUnschedulable(beginning.length);
    Error.assertTrue(a.isPermutation());
    for(int i = 0; i < beginning.length; i++)
        Error.assertEqual(a.getIndexAt(i),beginning[i]);
}
}

