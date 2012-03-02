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


public class EvolvableDoubleTest extends TestCase {

public EvolvableDoubleTest(String name) {super(name);}

public void testIsEqual() {
    assertTrue("1", new EvolvableDouble(0.5).isEqual(new EvolvableDouble(0.5)));
    assertFalse("2", new EvolvableDouble(1).isEqual(new EvolvableDouble(0.7)));
    assertFalse("3", new EvolvableDouble(1).isEqual(new EvolvableDouble(0.2)));
}
public void testCopy() {
    EvolvableDouble d = new EvolvableDouble();
    assertTrue("1", d.isEqual(d.copy()));
    assertFalse("2", new EvolvableDouble(0.5).isEqual(new EvolvableDouble(0.3).copy()));
}
public void testInterpolateInto() {
    interplateIntoTest("1", 0.25, 0, 1, 0.25);
    interplateIntoTest("2", 0.25, 0, 2, 0.5);
    interplateIntoTest("3", 0.25, -1, 1, -0.5);
    interplateIntoTest("4", 0.0, 1, 11, 1);
    interplateIntoTest("5", 1, 1, 11, 11);
    interplateIntoTest("6", 0.8, 0, 100, 80);
    interplateIntoTest("7", 0.8, -200, -100, -120);
}
protected void interplateIntoTest(String name, double value, double low, double high, double answer) {
    EvolvableDouble d = new EvolvableDouble(value);
    DoubleInterval interval = new DoubleInterval(low,high);
    double result = d.interpolateInto(interval);
    assertTrue(name+" "+result, Utility.nearlyEqual(result,answer));
}
}