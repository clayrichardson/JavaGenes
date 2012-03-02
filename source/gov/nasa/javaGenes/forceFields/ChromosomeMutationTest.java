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
package gov.nasa.javaGenes.forceFields;

import junit.framework.TestCase;
import gov.nasa.alsUtility.DoubleInterval;

public class ChromosomeMutationTest extends TestCase {

public ChromosomeMutationTest(String name) {super(name);}

public void testForceInsideAlleleLimits() {
    DoubleInterval d = new DoubleInterval(1,10);
    Allele a = new Allele(d);
    ChromosomeMutation cm = new ChromosomeMutation();
    DoubleInterval value = new DoubleInterval(-100,100);
    final int tries = 1000;
    for(int i = 0; i < tries; i++) {
        assertTrue("inside"+i, d.isBetween(cm.forceInsideAlleleLimits(a,value.random(),d.random())));               	assertTrue("outside"+i,d.isBetween(cm.forceInsideAlleleLimits(a,value.random(),value.random()))); 
	assertTrue("reverse"+i,d.isBetween(cm.forceInsideAlleleLimits(a,d.random(),value.random()))); 
    }
}
}
