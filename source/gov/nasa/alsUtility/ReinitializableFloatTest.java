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
//  Created by Al Globus on Fri Jun 27 2003.
package gov.nasa.alsUtility;
import junit.framework.TestCase;

public class ReinitializableFloatTest extends TestCase {

public ReinitializableFloatTest(String name) {super(name);}

public void testReinitializableFloat() {
    ReinitializableFloat i = new ReinitializableFloat();
    i.setInitial(1);
    assertTrue("1", i.getCurrent() == 1);
    assertTrue("2", i.getInitial() == 1);
    i.addToInitial(-2);
    assertTrue("3", i.getCurrent() == -1);
    assertTrue("4", i.getInitial() == -1);
    i.add(5);
    assertTrue("5", i.getCurrent() == 4);
    assertTrue("6", i.getInitial() == -1);
    i.reinitialize();
    assertTrue("7", i.getCurrent() == -1);
    assertTrue("8", i.getInitial() == -1);
    i.multiply(3);
    assertTrue("9", i.getCurrent() == -3);
    assertTrue("10", i.getInitial() == -1);
    i.reinitialize();
    assertTrue("11", i.getCurrent() == -1);
    assertTrue("12", i.getInitial() == -1);

    i.setCurrent(3);
    assertTrue("13", i.getCurrent() == 3);
    assertTrue("14", i.getInitial() == -1);
    i.reinitialize();
    assertTrue("15", i.getCurrent() == -1);
    assertTrue("16", i.getInitial() == -1);
    
    i.setInitial(8);
    assertTrue("17", i.getCurrent() == 8);
    assertTrue("18", i.getInitial() == 8);
    i.reinitialize();
    assertTrue("19", i.getCurrent() == 8);
    assertTrue("10", i.getInitial() == 8);
}
}
