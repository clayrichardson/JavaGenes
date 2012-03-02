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
import gov.nasa.alsUtility.Utility;

public class ChangingWeightsObjectTest extends TestCase {

public ChangingWeightsObjectTest(String name) {super(name);}

public void testConstructor() {
    ChangingWeightsObject c = new ChangingWeightsObject(null,1,6,2);
    assertTrue("1",Utility.nearlyEqual(c.slope,2.5));
}
public void testWeight() {
    ChangingWeightsObject c = new ChangingWeightsObject(null,1,6,2);
    c.calculateWeight(0);
    assertTrue("1", Utility.nearlyEqual(c.getWeight(),1));
    c.calculateWeight(2);
    assertTrue("2", Utility.nearlyEqual(c.getWeight(),6));
    c.calculateWeight(1);
    assertTrue("3", Utility.nearlyEqual(c.getWeight(),3.5));
}
}
