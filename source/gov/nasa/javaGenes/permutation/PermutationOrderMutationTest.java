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

public class PermutationOrderMutationTest extends TestCase {

public PermutationOrderMutationTest(String name) {super(name);}

public void testMakeChildren() {
  final int size = 5;
  PermutationEvolvable parent = new PermutationEvolvable(size);
  Evolvable[] p = new PermutationEvolvable[1];
  p[0] = parent;
  PermutationOrderMutation m = new PermutationOrderMutation();
  PermutationEvolvable[] childArray = (PermutationEvolvable[])m.makeChildren(p);
  assertTrue("length correct",childArray.length == 1);
  PermutationEvolvable child = childArray[0];
  assertTrue("is permutation", child.isPermutation());
  int count = 0;
  int lastParent = -1;
  int lastChild = -1;
  for(int i = 0; i < size; i++) {
    if (parent.getIndexAt(i) != child.getIndexAt(i)) {
      count++;
      if (lastParent == -1)
        lastParent = parent.getIndexAt(i);
      else
        assertTrue("was a swap", lastParent == child.getIndexAt(i));
      if (lastChild == -1)
        lastChild = child.getIndexAt(i);
      else
        assertTrue("was a swap", lastChild == parent.getIndexAt(i));
    }
  }
  assertTrue("two swaps only", count == 2);
}
}