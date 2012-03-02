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
import gov.nasa.alsUtility.Utility;

public class ComparativeStatisticsTest extends TestCase {
public ComparativeStatisticsTest(String name) {super(name);}
public void testTtest() {
    double result;
    result = ComparativeStatistics.ttest(5,5, 3.0, 3.0, 0.1, 0.1);
    assertTrue("1 " + result, Utility.nearlyEqual(1.0,result));
    result = ComparativeStatistics.ttest(5,5, 3.0, 3.5, 0.1, 0.1);
    assertTrue("2 " + result, Utility.nearlyEqual(4.75547140867949E-5,result));
    result = ComparativeStatistics.ttest(3,3, 3.0, 3.1, 0.1, 0.1);
    assertTrue("3 " + result, Utility.nearlyEqual(0.28786413472669037,result));
    result = ComparativeStatistics.ttest(8,8, 3.0, 3.1, 0.1, 0.1);
    assertTrue("4 " + result, Utility.nearlyEqual(0.06528795288911149,result));
    result = ComparativeStatistics.ttest(50,50, 3.0, 3.1, 0.1, 0.1);
    assertTrue("5 " + result, Utility.nearlyEqual(2.5135780088447746E-6,result));
    result = ComparativeStatistics.ttest(0,50, 3.0, 3.1, 0.1, 0.1);
    assertTrue("6 " + result, Utility.nearlyEqual(-1,result));
    result = ComparativeStatistics.ttest(50,0, 3.0, 3.1, 0.1, 0.1);
    assertTrue("7 " + result, Utility.nearlyEqual(-1,result));
    result = ComparativeStatistics.ttest(50,50, 3.0, 3.1, 0, 0.1);
    assertTrue("8 " + result, Utility.nearlyEqual(-1,result));
    result = ComparativeStatistics.ttest(50,50, 3.0, 3.1, 0.1, 0);
    assertTrue("9 " + result, Utility.nearlyEqual(-1,result));
}
}
