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

import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.alsUtility.RandomNumber;

public class PermutationEvolvable extends Evolvable {
protected int[] permutation;  // of unique integers 0-(permutation.length-1)

public PermutationEvolvable(int size) {
    permutation = new int[size];
    boolean[] assigned = new boolean[size];  // defaults to false
    for(int i = 0; i < getSize(); i++) { // assign a random but unique index to each position in permutation
        int p = RandomNumber.getIndex(size);
        while(assigned[p]) {
            p++;
            if (p >= size)
            p = 0;
        }
        setIndexAt(i,p);
        assigned[p] = true;
    }
    Error.assertTrue(isPermutation());
}
/**
@arg order must equal "ascending".  Creates a permutation in ascending order
*/
public PermutationEvolvable(int size, String order) {
    Error.assertTrue(order.equals("ascending"));
    permutation = new int[size];
    setToAscending();
}
public boolean isPermutation() {
  boolean[] assigned = new boolean[getSize()];  // defaults to false
  for(int i = 0; i < permutation.length; i++) {
    int index = getIndexAt(i);
    if (assigned[index]) return false;
    assigned[index] = true;
  }
  return true;
}
public boolean isSame(PermutationEvolvable other) {
    if (getSize() != other.getSize())
        return false;
    for(int i = 0; i < getSize(); i++)
        if (getIndexAt(i) != other.getIndexAt(i))
            return false;
    return true;
}
/**
used for testing
*/
public boolean equals(int[] array) {
    Error.assertTrue(array.length == getSize());
    for(int i = 0; i < getSize(); i++)
        if (getIndexAt(i) != array[i])
            return false;
    return true;
}
/**
used for testing
*/
public void setToAscending() {
    for(int i = 0; i < permutation.length; i++) 
        setIndexAt(i,i);
}
public boolean isInAscendingOrder() {
    for(int i = 0; i < permutation.length; i++) 
        if (i != getIndexAt(i))
            return false;
    return true;
}
public PermutationEvolvable deepCopyPermutationEvolvable() {
  try {
    return (PermutationEvolvable)clone();
  } catch (CloneNotSupportedException e) {
    Error.fatal("can't clone object: " + e);
    return null;
  }
}
/**
deep copy
@exception CloneNotSupportedException
*/
public Object clone() throws CloneNotSupportedException {
	PermutationEvolvable p = (PermutationEvolvable)super.clone();
  p.permutation = new int[getSize()];
  for(int i = 0; i < getSize(); i++)
  	p.setIndexAt(i,getIndexAt(i));
  return p;
}
public int getSize(){
	return permutation.length;
}
public int getIndexAt(int i) {
  return permutation[i];
}
public void setIndexAt(int index, int setTo) {
  Error.assertTrue(0 <= setTo && setTo < getSize());
  // don't check for uniqueness since several setIndexAt may be required
  permutation[index] = setTo;
}
public double distanceFrom(Evolvable e) {
  Error.notImplemented();
  return 0;
}
public String toString() {
	StringBuffer s = new StringBuffer();
  for(int i = 0; i < permutation.length; i++)
    s.append(permutation[i] + "\t");
  return s.toString();
}
}