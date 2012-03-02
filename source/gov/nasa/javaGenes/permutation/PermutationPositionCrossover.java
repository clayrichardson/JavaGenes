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
import java.lang.Math;
import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.alsUtility.RandomNumber;
/**
Gilbert Sysweda's position based crossover
*/
public class PermutationPositionCrossover extends PermutationChildMaker {
public int numberOfParents() {return 2;}
public Evolvable[] makeChildren(Evolvable[] parents) {
	Error.assertTrue(parents.length == 2);
	PermutationEvolvable dad = (PermutationEvolvable)parents[0];
	PermutationEvolvable mom = (PermutationEvolvable)parents[1];
  boolean[] positions = new boolean[mom.getSize()];

  do {
    for(int i = 0; i < positions.length; i++)
      positions[i] = RandomNumber.getBoolean();
  } while (isPure(positions));

  PermutationEvolvable[] kids = new PermutationEvolvable[2];
  kids[0] = crossover(mom,dad,positions);
  kids[1] = crossover(dad,mom,positions);

  return kids;
}
public PermutationEvolvable crossover(PermutationEvolvable keepPositions,PermutationEvolvable keepOrder, boolean[] positions) {
  Error.assertTrue(keepPositions.getSize() == keepOrder.getSize());
  Error.assertTrue(keepPositions.getSize() == positions.length);

  PermutationEvolvable kid = keepPositions.deepCopyPermutationEvolvable();
  int count = 0;
  for(int i = 0; i < positions.length; i++)
    if (positions[i]) {
        copyIndex(keepPositions,i,kid,i);
      //kid.setIndexAt(i,keepPositions.getIndexAt(i));
      count++;
    }
  int[] keepValues = new int[count];
  int j = 0;
  for(int i = 0; i < positions.length; i++)
    if (positions[i])
      keepValues[j++] = keepPositions.getIndexAt(i);

  int index = 0;
  for(int i = 0; i < positions.length; i++)
    if (!positions[i]) {
      int value;
      do
        value = keepOrder.getIndexAt(index++);
      while (isIn(value,keepValues));
      copyIndex(keepOrder,index-1,kid,i);
    }
  return kid;
}
protected boolean isIn(int value, int[] keepValues) {
  for(int i = 0; i < keepValues.length; i++)
   if (value == keepValues[i])
    return true;
  return false;
}
protected boolean isPure(boolean[] array) {
  for(int i = 1; i < array.length; i++)
    if (array[i] != array[i-1]) return false;
  return true;
}

public String toString() {
	return "PositionCrossover";
}
}