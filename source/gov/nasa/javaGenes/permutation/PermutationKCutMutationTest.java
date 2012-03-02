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
//
//  Created by Al Globus on Thu Jan 16 2003.
package gov.nasa.javaGenes.permutation;

import junit.framework.TestCase;
import gov.nasa.javaGenes.core.Evolvable;

public class PermutationKCutMutationTest extends TestCase {

public PermutationKCutMutationTest(String name) {super(name);}

public void testIndexInPrevious() {
    PermutationKCutMutation p = new PermutationKCutMutation(2);
    int[] array = {0,4,3,9,3};
    assertTrue("1", !p.indexInPrevious(0,array));
    assertTrue("2", !p.indexInPrevious(1,array));
    assertTrue("3", !p.indexInPrevious(2,array));
    assertTrue("4", !p.indexInPrevious(3,array));
    assertTrue("5", p.indexInPrevious(4,array));
}
public void testMakeChildren() {
    final int tests = 20;
    PermutationEvolvable dad = new PermutationEvolvable(4);
    PermutationEvolvable[] parents = {dad};
    PermutationKCutMutation p = new PermutationKCutMutation(2);
    for(int i = 0; i < tests; i++) {
        Evolvable[] kids = p.makeChildren(parents);
        PermutationEvolvable kid = (PermutationEvolvable)kids[0];
        assertTrue(i + " 1",kids.length == 1);
        assertTrue(i + " 2",!kid.isSame(dad));
        assertTrue(i + " 3",kid.isPermutation());
    }
    
    dad.setToAscending();
    int[] segments = {-1,0,2,3};
    PermutationEvolvable order = new PermutationEvolvable(3);
    order.setIndexAt(0,1);
    order.setIndexAt(1,2);
    order.setIndexAt(2,0);
    PermutationEvolvable kid = (PermutationEvolvable)p.makeChildren(parents,segments,order)[0];
    assertTrue("1", kid.getIndexAt(0) == 1);
    assertTrue("2", kid.getIndexAt(1) == 2);
    assertTrue("3", kid.getIndexAt(2) == 3);
    assertTrue("4", kid.getIndexAt(3) == 0);
    order.setIndexAt(0,0);
    order.setIndexAt(1,2);
    order.setIndexAt(2,1);
    kid = (PermutationEvolvable)p.makeChildren(parents,segments,order)[0];
    assertTrue("1", kid.getIndexAt(0) == 0);
    assertTrue("2", kid.getIndexAt(1) == 3);
    assertTrue("3", kid.getIndexAt(2) == 1);
    assertTrue("4", kid.getIndexAt(3) == 2);
}
}

