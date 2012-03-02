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
//  Created by Al Globus on Wed May 14 2003.
package gov.nasa.javaGenes.core;

import junit.framework.TestCase;

import java.lang.Integer;

public class RouletteWheelTest extends TestCase {
private RouletteWheel wheel;

public RouletteWheelTest(String name) {super(name);}
public void setUp() {
    wheel = new RouletteWheel();
    wheel.add(new ChangingWeightsObject(new Integer(50),  4,0));
    wheel.add(new ChangingWeightsObject(new Integer(300), 3,0));
    wheel.add(new ChangingWeightsObject(new Integer(2000),2,0));
    wheel.add(new ChangingWeightsObject(new Integer(10),  1,0));
}
public void testFindObject() {
    wheel.spinWheel(1);
    check(1,3.5,50);
    check(2,4.5,300);
    check(3,7.5,2000);
    check(4,9.5,10);
    check(5,0,50);
    check(6,10,10);
    check(7,4.0001,300);
    check(8,3.999,50);
}
private void check(int name, double number, int value) {
    assertTrue(name+"",((Integer)wheel.findObject(number)).intValue() == value);
}
}
