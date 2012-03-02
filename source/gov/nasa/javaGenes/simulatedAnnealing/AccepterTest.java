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
//  Created by Al Globus on Thu Dec 05 2002.

package gov.nasa.javaGenes.simulatedAnnealing;

import junit.framework.TestCase;
import gov.nasa.javaGenes.core.FitnessDouble;

public class AccepterTest extends TestCase {

public AccepterTest(String name) {super(name);}

public void testDelta() {
    FitnessDouble f1 = new FitnessDouble(1);
    FitnessDouble f2 = new FitnessDouble(2);

    Accepter a = new Accepter(100, 10, 2, Accepter.DELTA_REDUCE);
    double probabilty = a.probabilityToBeat(1.0);
    assertTrue("1", a.getCurrentTemperature() == 100);
    a.accept(f1,f2);
    assertTrue("2", a.getCurrentTemperature() == 100);
    assertTrue("3", a.probabilityToBeat(1.0) == probabilty);
    a.accept(f1,f2);
    assertTrue("2.1", a.getCurrentTemperature() == 100);
    assertTrue("3.1", a.probabilityToBeat(1.0) == probabilty);

    a.accept(f1,f2);
    assertTrue("4", a.getCurrentTemperature() == 90);
    assertTrue("5", a.probabilityToBeat(1.0) < probabilty);
    probabilty = a.probabilityToBeat(1.0);
    a.accept(f1,f2);
    assertTrue("6", a.getCurrentTemperature() == 90);
    assertTrue("7", a.probabilityToBeat(1.0) == probabilty);

    a.accept(f1,f2);
    assertTrue("8", a.getCurrentTemperature() == 80);
    assertTrue("9", a.probabilityToBeat(1.0) < probabilty);
    probabilty = a.probabilityToBeat(1.0);
    a.accept(f1,f2);
    assertTrue("10", a.getCurrentTemperature() == 80);
    assertTrue("11", a.probabilityToBeat(1.0) == probabilty);
}
public void testFactor() {
    FitnessDouble f1 = new FitnessDouble(1);
    FitnessDouble f2 = new FitnessDouble(2);

    Accepter a = new Accepter(100, 0.8, 2, Accepter.FACTOR_REDUCE);
    double probabilty = a.probabilityToBeat(1.0);
    assertTrue("1", a.getCurrentTemperature() == 100);
    a.accept(f1,f2);
    assertTrue("2", a.getCurrentTemperature() == 100);
    assertTrue("3", a.probabilityToBeat(1.0) == probabilty);
    a.accept(f1,f2);
    assertTrue("2.1", a.getCurrentTemperature() == 100);
    assertTrue("3.1", a.probabilityToBeat(1.0) == probabilty);

    a.accept(f1,f2);
    assertTrue("4", a.getCurrentTemperature() == 80);
    assertTrue("5", a.probabilityToBeat(1.0) < probabilty);
    probabilty = a.probabilityToBeat(1.0);
}
public void testProbabilityToBeat() {
    FitnessDouble f1 = new FitnessDouble(2);
    FitnessDouble f2 = new FitnessDouble(1);

    Accepter a = new Accepter(1000, 10, 9, Accepter.DELTA_REDUCE);
    for(int i = 0; i < 899; i++) {
        a.accept(f1,f2);
        assertTrue(i+"-1.0", a.probabilityToBeat(1.0) <= 1.0);
        assertTrue(i+"-0.0", a.probabilityToBeat(1.0) >= 0);
        assertTrue(i+"-level", a.probabilityToBeat(1.0) > a.probabilityToBeat(10.0));
    }
}
}

