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
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.Utility;

public class Mutation3parentsTest extends TestCase {

public Mutation3parentsTest(String name) {super(name);}

public void testMutation3parents() {
    RandomNumber.setSeed(990639400906L);   // to get deterministic results
    assertTrue("1", testMutation3parents(0.5, 3, 8) >= 3); 
    assertTrue("2", testMutation3parents(0.001, 9, 8) == 8);
    assertTrue("3", testMutation3parents(1, 6, 8) == 8);
}
public int testMutation3parents(double probability, int minimumNumberToSelect, int length) {
    SelectByProbability selector = new SelectByProbability(probability,minimumNumberToSelect);
    EvolvableDoubleList[] lists = {
        new EvolvableDoubleList(length),
        new EvolvableDoubleList(length-1),
        new EvolvableDoubleList(length+1)
    };
    double[] initialValues = new double[lists[0].getSize()];
    for(int i = 0; i < lists[0].getSize(); i++)
        initialValues[i] = lists[0].getDoubleValue(i);
    Mutation3parents mutator = new Mutation3parents(selector);
    EvolvableDoubleList[] children = (EvolvableDoubleList[])mutator.makeChildren(lists);
    assertTrue("length", children.length == 1);
    EvolvableDoubleList child = children[0];
    int changes = 0;
    for(int i = 0; i < child.getSize(); i++)
        if (!Utility.nearlyEqual(initialValues[i],child.getDoubleValue(i)))
            changes++;
    return changes;
}
}