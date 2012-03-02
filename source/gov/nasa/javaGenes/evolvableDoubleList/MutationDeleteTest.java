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
import gov.nasa.alsUtility.IntegerInterval;

public class MutationDeleteTest extends TestCase {

public MutationDeleteTest(String name) {super(name);}

public void testMutationDelete2() {
    int[] indicesToDelete = {2,5};
    int length = 8;
    int[] equivalent = {0,1,3,4,6,7};
    testMutationDelete2("1",indicesToDelete,length,equivalent);
    length = 4;
    int[] equivalent2 = {0,1,3};
    testMutationDelete2("2",indicesToDelete,length,equivalent2);
    int[] indicesToDelete1 = {0,7};
    length = 8;
    int[] equivalent3 = {1,2,3,4,5,6};
    testMutationDelete2("3",indicesToDelete1,length,equivalent3);
}
private void testMutationDelete2(String name, int[] indicesToDelete, int length, int[] equivalent) {
    MutationDelete mutator = new MutationDelete(new SelectFixedIndices(indicesToDelete));
    EvolvableDoubleList list = new EvolvableDoubleList(length);
    EvolvableDoubleList toMutate = (EvolvableDoubleList)list.copyForEvolution();
    mutator.mutate(toMutate);
    assertTrue(name + " length = " + toMutate.getSize(), equivalent.length == toMutate.getSize());
    for(int i = 0; i < equivalent.length; i++)
        assertTrue(name + i + "\nmutated  = " + toMutate.toString() + "\noriginal = " + list.toString(), 
            list.getDoubleValue(equivalent[i]) == toMutate.getDoubleValue(i));
}

public void testMutationDelete() {
    RandomNumber.setSeed(990639400906L);   // to get deterministic results
    int[] numberToDelete = {3};
    testMutationDelete("1", numberToDelete, 3, 6);
    int[] numberToDelete1 = {1,5,6};
    testMutationDelete("2", numberToDelete1, 3, 6);
    int[] numberToDelete2 = {7,4};
    testMutationDelete("3", numberToDelete2, 3, 6);
}
public void testMutationDelete(String name, int[] numberToDelete, int minimumSize, int length) {
    MutationDelete mutator = new MutationDelete(new SelectChunk(numberToDelete),minimumSize);
    for(int i = 0; i < 100; i++) {
        EvolvableDoubleList list = new EvolvableDoubleList(length);
        mutator.mutate(list);
        int sizeDifference = length - list.getSize();
        boolean correct = false;
        for(int j = 0; j < numberToDelete.length; j++) {
            if (length >= minimumSize || numberToDelete[j] == sizeDifference)
                correct = true;
        }
        assertTrue(name, correct);
    }
}
public void testRandom() {
    RandomNumber.setSeed(990637400906L);   // to get deterministic results
    for(int i = 0; i < 1000; i++) {
        int[] toDelete = new int[new IntegerInterval(1,5).random()];
        for(int j = 0; j < toDelete.length; j++)
            toDelete[j] = new IntegerInterval(1,10).random();
        MutationDelete mutator = new MutationDelete(new SelectChunk(toDelete),new IntegerInterval(1,4).random());
        EvolvableDoubleList list = new EvolvableDoubleList(new IntegerInterval(3,15).random());
        int originalSize = list.getSize();
        mutator.mutate(list);
        assertTrue(i + " " + originalSize + " " + list.getSize() + " " + mutator, 
            (originalSize <= mutator.getMinumumEvolvableSize() && originalSize == list.getSize()) || list.getSize() <= originalSize);
    }
}
}