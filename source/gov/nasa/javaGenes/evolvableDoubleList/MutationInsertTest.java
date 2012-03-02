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

public class MutationInsertTest extends TestCase {

public MutationInsertTest(String name) {super(name);}

public void testMutationInsert() {
    RandomNumber.setSeed(990639400906L);   // to get deterministic results
    int[] numberToInsert = {3};
    testMutationInsert("1", numberToInsert, 6);
    int[] numberToInsert1 = {1,5,6};
    testMutationInsert("2", numberToInsert1, 6);
    int[] numberToInsert2 = {7,4};
    testMutationInsert("3", numberToInsert2, 6);

    int[] numberToInsert3 = {1,2};
    MutationInsert mutator = new MutationInsert(numberToInsert3);
    EvolvableDoubleList list = new EvolvableDoubleList(4);
    //System.out.println(list); // use to get data for next test when algorithm or random number generation changes
    double[] values = {0.7215372296032879,0.2819729062792321,0.6207022147661634,0.6618204753671451};
    assertTrue("4", list.isEqual(new EvolvableDoubleList(values)));
    mutator.mutate(list);
    //System.out.println(list); // use to get data for next test when algorithm or random number generation changes
    double[] mutatedValues = {0.1799525705164407,0.017786304179108592,0.7215372296032879,0.2819729062792321,0.6207022147661634,0.6618204753671451};
    assertTrue("5", list.isEqual(new EvolvableDoubleList(mutatedValues)));
    
}
public void testMutationInsert(String name, int[] numberToInsert, int length) {
    MutationInsert mutator = new MutationInsert(numberToInsert);
    for(int i = 0; i < 100; i++) {
        EvolvableDoubleList list = new EvolvableDoubleList(length);
        mutator.mutate(list);
        int sizeDifference = list.getSize() - length;
        boolean correct = false;
        for(int j = 0; j < numberToInsert.length; j++)
            if (numberToInsert[j] == sizeDifference)
                correct = true;
        assertTrue(name, correct);
    }
}
public void testMultipleInsertions() {
	int[] numberToInsert = {1};
	int[] insertionPoints = {2,4};
    MutationInsert mutator = new MutationInsert(new SelectFixedIndices(insertionPoints),numberToInsert);
	EvolvableDoubleList list = new EvolvableDoubleList(4);
	EvolvableDoubleList original = (EvolvableDoubleList)list.copyForEvolution();
	mutator.mutate(list);
	assertTrue("length", list.getSize()== original.getSize()+2);
	assertTrue("0", original.getDoubleValue(0) == list.getDoubleValue(0));
	assertTrue("1", original.getDoubleValue(1) == list.getDoubleValue(1));
	assertTrue("2", original.getDoubleValue(2) == list.getDoubleValue(3));
	assertTrue("3", original.getDoubleValue(3) == list.getDoubleValue(4));
}
}