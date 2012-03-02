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
//
//  DoublesListTest.java
//  JavaGenesProject
//
//  Created by Al Globus on Wed Jun 26 2002.
//  Copyright (c) 2001 __MyCompanyName__. All rights reserved.
//
package gov.nasa.alsUtility;
import junit.framework.TestCase;

public class DoublesListTest extends TestCase {

public DoublesListTest(String name) {super(name);}

public void testDoublesList() {
    DoublesList list = new DoublesList("");
    assertTrue("1",list.size() == 0);

    list = new DoublesList("2");
    assertTrue("2",list.size() == 1);
    assertTrue("3",list.get(0) == 2);

    list = new DoublesList("2,6");
    assertTrue("4",list.size() == 2);
    assertTrue("5",list.get(0) == 2);
    assertTrue("6",list.get(1) == 6);

    list = new DoublesList("-2,6.2,3");
    assertTrue("7",list.size() == 3);
    assertTrue("8",list.get(0) == -2);
    assertTrue("9",list.get(1) == 6.2);
    assertTrue("10",list.get(2) == 3);
}
}
