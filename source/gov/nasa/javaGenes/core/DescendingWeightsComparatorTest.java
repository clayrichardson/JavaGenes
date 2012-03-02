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
import java.lang.Math;
import gov.nasa.alsUtility.Utility;

public class DescendingWeightsComparatorTest extends TestCase {

public DescendingWeightsComparatorTest(String name) {super(name);}

public void testCompare() {
    ChangingWeightsObject low = new ChangingWeightsObject(null,1,0);
    ChangingWeightsObject high = new ChangingWeightsObject(null,2,0);
    ChangingWeightsObject high2 = new ChangingWeightsObject(null,2,0);
    DescendingWeightsComparator c = new DescendingWeightsComparator();
    assertTrue("1", c.compare(low,high) > 0);
    assertTrue("2", c.compare(high,low) < 0);
    assertTrue("2", c.compare(high2,high) == 0);
}
}

