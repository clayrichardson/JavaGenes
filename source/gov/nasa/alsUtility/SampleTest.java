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
//  Created by Al Globus on Mon Jan 27 2003.
package gov.nasa.alsUtility;

import junit.framework.TestCase;
import gov.nasa.alsUtility.Utility;

public class SampleTest extends TestCase {
private Sample sample;
public SampleTest(String name) {super(name);}
public void setUp() {
    sample = new Sample();
}
public void testMeanAndN() {
    check(1,java.lang.Double.NaN,0); 
    sample.addDatum(1);
    check(2,1,1);
    sample.addDatum(2);
    check(3,1.5,2);
    sample.addDatum(0);
    check(4,1,3);
}
public void testSD() {
    for(int i = 0; i < 100; i++)
        sample.addDatum(1);
    check(100,1,100);
    assertTrue("sd", sample.getStandardDeviation() == 0);
}
private void check(int test, double mean, double N) {
    if (Utility.normalNumber(sample.getMean()))
        assertTrue(test+"Mean", sample.getMean() == mean);
    assertTrue(test+"N", sample.getN() == N);
}

}
