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
package gov.nasa.alsUtility;


public class CircularIntQueue {
protected int pushIndex;
protected int popIndex;
protected int[] buffer;

public CircularIntQueue(int length) {
	buffer = new int[length];
  reset();
}
public void reset() {
	pushIndex = 0;
  popIndex = 0;
}
public int length() {return buffer.length;}
public void push(int value) {
	buffer[pushIndex] = value;
  pushIndex = increment(pushIndex);
}
public int pop() {
 	int value = buffer[popIndex];
  popIndex = increment(popIndex);
  return value;
}
private int increment(int index) {
	return (index + 1) % buffer.length;
}
}