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

public class ReinitializableFloatWithFactorTest extends TestCase {

public ReinitializableFloatWithFactorTest(String name) {super(name);}

public void testReinitializableFloatWithFactor() {
    ReinitializableFloatWithFactor f = new ReinitializableFloatWithFactor();
    f.setInitial(3);
    assertTrue("-1", f.getCurrent() == 3);
    assertTrue("-2", f.getInitial() == 3);
    f.setFactor(2);
    assertTrue("1", f.getCurrent() == 6);
    assertTrue("2", f.getInitial() == 6);
    f.add(2);
    assertTrue("3", f.getCurrent() == 10);
    assertTrue("4", f.getInitial() == 6);
    f.reinitialize();
    assertTrue("5", f.getCurrent() == 6);
    assertTrue("6", f.getInitial() == 6);
}
}
