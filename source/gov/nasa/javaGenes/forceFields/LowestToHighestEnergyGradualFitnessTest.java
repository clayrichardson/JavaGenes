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
import java.io.PrintWriter;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.DoubleInterval;

public class LowestToHighestEnergyGradualFitnessTest extends TestCase {

public LowestToHighestEnergyGradualFitnessTest(String name) {super(name);}

public void testOutOfOrder() {
    LowestToHighestEnergyGradualFitness f = new LowestToHighestEnergyGradualFitness();
    double[] e1 = {1,2,3};
    assertTrue("1", f.outOfOrder(e1) == 0);
    double[] e2 = {2,1,3};
    assertTrue("2", Utility.nearlyEqual(f.outOfOrder(e2),2));
    double[] e3 = {3,2,1};
    assertTrue("3", Utility.nearlyEqual(f.outOfOrder(e3),3 + Math.sqrt(6/3)));
    double[] e4 = {3};
    assertTrue("4", f.outOfOrder(e4) == 0);
    double[] e5 = {3,3};
    assertTrue("4.5", f.outOfOrder(e5) == 1);
    f.setOutOfOrderPenalty(2);
    assertTrue("4.7", Utility.nearlyEqual(f.outOfOrder(e5),2));
    assertTrue("5", Utility.nearlyEqual(f.outOfOrder(e3),6 + Math.sqrt(6/3)));
    assertTrue("6", f.outOfOrder(e1) == 0);
}

}
