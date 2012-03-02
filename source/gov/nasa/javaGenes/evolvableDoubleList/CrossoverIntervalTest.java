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
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.alsUtility.IntegerInterval;
import gov.nasa.alsUtility.RandomNumber;

public class CrossoverIntervalTest extends TestCase {

public CrossoverIntervalTest(String name) {super(name);}

public void testCrossoverIntervalTest() {
    RandomNumber.setSeed(990639400906L);   // to get deterministic results
    CrossoverInterval xover = new CrossoverInterval(new SelectAll(),0.8);
    for(int i = 0; i < 100; i++) {
        EvolvableDoubleList mom = new EvolvableDoubleList(new IntegerInterval(1,20).random());
        EvolvableDoubleList dad = new EvolvableDoubleList(new IntegerInterval(1,30).random());
        EvolvableDoubleList[] parents = {mom,dad};
        EvolvableDoubleList child = (EvolvableDoubleList)xover.makeChildren(parents)[0];
        for(int j = 0; j < child.getSize(); j++) {
            double momValue = mom.getDoubleValue(j);
            double dadValue = dad.getDoubleValue(j);
            assertTrue(j+"", new DoubleInterval(momValue,dadValue).isBetween(child.getDoubleValue(j)));
        }
    }
}
}