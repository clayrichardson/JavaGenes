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
package gov.nasa.javaGenes.weightNetwork;

import junit.framework.TestCase;

import java.lang.Integer;

public class RouletteWheelTest extends TestCase {
private RouletteWheel wheel;
private Weight w1;
private Weight w2;
private Weight w3;
private Weight w4;

public RouletteWheelTest(String name) {super(name);}
public void setUp() {
    wheel = new RouletteWheel();
    w1 = new Weight();
    w1.setWeight(4);
    w2 = new Weight();
    w2.setWeight(3);
    w3 = new Weight();
    w3.setWeight(2);
    w4 = new Weight();
    w4.setWeight(1);
    wheel.add(w1);
    wheel.add(w2);
    wheel.add(w3);
    wheel.add(w4);
    wheel.initializeWeightSum();
    wheel.reinitialize();
}
public void testSpinWheel() {
    wheel.spinWheel();
    check(1,3.5,w1);
    check(2,4.5,w2);
    check(3,7.5,w3);
    check(4,9.5,w4);
    check(5,0,w1);
    check(6,10,w4);
    check(7,4.0001,w2);
    check(8,3.999,w1);
    for(int i = 0; i < 100; i++) {
        Weight w = wheel.spinWheel();
        assertTrue("r",w == w1 || w == w2 || w == w3 || w == w4);
    }
}
public void testRemoval() {
    w2.removeFromWeightList();
    for(int i = 0; i < 1000; i++) {
        Weight w = wheel.spinWheel();
        assertTrue("r",w == w1 || w == w3 || w == w4);
    }
}
private void check(int name, double number, Weight w) {
    assertTrue(name+"",wheel.spinWheel((float)number) == w);
}
}

