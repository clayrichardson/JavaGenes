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

import gov.nasa.alsUtility.Error;

public class MinMaxes {
protected DoubleInterval[] intervals;

public void add(double[] array) {
  if (intervals == null) {
    intervals = new DoubleInterval[array.length];
    for(int i = 0; i < array.length; i++)
      intervals[i] = new DoubleInterval(array[i],array[i]);
  }
  else {
    Error.assertTrue(array.length == intervals.length);
    for(int i = 0; i < array.length; i++)
      intervals[i].extendIfNecessary(array[i]);
  }
}
/**
do not modify return value
*/
public DoubleInterval getDoubleInterval(int i) {
  return intervals[i];
}
public int size() {return intervals.length;}
}
