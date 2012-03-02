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
package gov.nasa.javaGenes.permutation;

import junit.framework.TestCase;
import gov.nasa.javaGenes.core.Evolvable;

public class PermutationPositionCrossoverTest extends TestCase {

public PermutationPositionCrossoverTest(String name) {super(name);}
public void testMakeChildren() {
  final int size = 10;
  PermutationPositionCrossover crossover = new PermutationPositionCrossover();
  for(int i =0; i < 250; i++) {
    Evolvable[] p = new PermutationEvolvable[2];
    p[0] = new PermutationEvolvable(size);
    p[1] = new PermutationEvolvable(size);
    PermutationEvolvable[] children = (PermutationEvolvable[])crossover.makeChildren(p);
    assertTrue("length correct",children.length == 2);
    assertTrue(i + "is permutation 0", children[0].isPermutation());
    assertTrue(i + "is permutation 1", children[1].isPermutation());
  }
}
public void testCrossover() {
  final int size = 5;
  PermutationEvolvable mom = new PermutationEvolvable(size);
  PermutationEvolvable dad = new PermutationEvolvable(size);
  PermutationPositionCrossover crossover = new PermutationPositionCrossover();

  for(int i = 0; i < size; i++) {
    mom.setIndexAt(i,i);
    dad.setIndexAt(size-i-1,i);
  }

  boolean[] positions = new boolean[size];
  positions[0] = true;
  positions[3] = true;
  PermutationEvolvable kid = crossover.crossover(mom,dad,positions);
  assertTrue("is permutation", kid.isPermutation());
  assertTrue("0",kid.getIndexAt(0) == mom.getIndexAt(0));
  assertTrue("1",kid.getIndexAt(1) == dad.getIndexAt(0));
  assertTrue("2",kid.getIndexAt(2) == dad.getIndexAt(2));
  assertTrue("3",kid.getIndexAt(3) == mom.getIndexAt(3));
  assertTrue("4",kid.getIndexAt(4) == dad.getIndexAt(3));

  positions = new boolean[size];
  positions[1] = true;
  positions[4] = true;
  kid = crossover.crossover(mom,dad,positions);
  assertTrue("is permutation", kid.isPermutation());
  assertTrue("2-0",kid.getIndexAt(0) == dad.getIndexAt(1));
  assertTrue("2-1",kid.getIndexAt(1) == mom.getIndexAt(1));
  assertTrue("2-2",kid.getIndexAt(2) == dad.getIndexAt(2));
  assertTrue("2-3",kid.getIndexAt(3) == dad.getIndexAt(4));
  assertTrue("2-4",kid.getIndexAt(4) == mom.getIndexAt(4));
}
}