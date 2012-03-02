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

public class GrowOnlyArrayTest extends TestCase {

public GrowOnlyArrayTest(String name) {super(name);}

public void testArrayLength() {
    GrowOnlyArray a = new GrowOnlyArray(3,2);
    assertTrue("1",a.array.length == 3);
    assertTrue("1.5",a.size() == 0);
    a.add(null);
    assertTrue("2",a.array.length == 3);
    assertTrue("2.5",a.size() == 1);
    a.add(null);
    assertTrue("3",a.array.length == 3);
    assertTrue("3.5",a.size() == 2);
    a.add(null);
    assertTrue("4",a.array.length == 3);
    a.add(null);
    assertTrue("5",a.array.length == 5);
    assertTrue("5.5",a.size() == 4);
    a.add(null);
    assertTrue("6",a.array.length == 5);
    a.add(null);
    assertTrue("7",a.array.length == 7);
    a.reinitialize();
    assertTrue("8",a.array.length == 7);
    assertTrue("8.5",a.size() == 0);
    for(int i = 0; i < 7; i++) {
        a.add(null);
        assertTrue("loop"+i,a.array.length == 7);
        assertTrue("loop-s"+i,a.size() == i+1);
    }
    a.add(null);
    assertTrue("9",a.array.length == 9);
}
public void testArrayValues() {
    final int howMany = 1000;
    final int factor = 11;
    GrowOnlyArray a = new GrowOnlyArray();
    for(int i = 0; i < howMany; i++)
        a.add(new Integer(i*factor));
    for(int i = howMany - 1; i >= 0; i--)
        assertTrue(i+"", ((Integer)a.get(i)).intValue() == i*factor);
}
}
