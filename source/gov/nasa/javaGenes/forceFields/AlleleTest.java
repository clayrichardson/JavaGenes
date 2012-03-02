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
//  Created by Al Globus on Thu Feb 13 2003.
package gov.nasa.javaGenes.forceFields;

import junit.framework.TestCase;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.javaGenes.core.Evolvable;

public class AlleleTest extends TestCase {

public AlleleTest(String name) {super(name);}
private final int repetitions = 100;
private Allele allele;

public void setUp() {
    DoubleInterval interval = new DoubleInterval(-1.0,1.0);
    allele = new Allele("zero",interval);
}
public void testRandom() {
    for(int i = 0; i < repetitions; i++) {
        double value = allele.getRandomValue();
        assertTrue("random"+i, -1 <= value && value <= 1);
    }
    for(int i = 0; i < repetitions; i++) {
        double value = allele.getRandomValueBelow(0.5);
        assertTrue("randomAbove"+i, -1 <= value && value <= 0.5);
    }
    for(int i = 0; i < repetitions; i++) {
        double value = allele.getRandomValueAbove(-0.5);
        assertTrue("randomBelow"+i, -0.5 <= value && value <= 1);
    }
}
}