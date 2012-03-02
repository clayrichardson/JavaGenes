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
package gov.nasa.javaGenes.forceFields;
import java.util.Hashtable;
import java.io.Serializable;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.DoubleInterval;

public class AlleleSetup implements Serializable {
protected Hashtable intervals = new Hashtable();
protected Hashtable indices = new Hashtable();
public static final int NOT_HERE = -1;
void add(String speciesNames, int index) {
  indices.put(speciesNames, new Integer(index));
  Error.assertTrue(intervals.get(speciesNames) == null);
}
void add(String parameterName, int index, DoubleInterval interval) {
  indices.put(parameterName, new Integer(index));
  intervals.put(parameterName, interval);
}
public DoubleInterval getInterval(String parameterName) {
  DoubleInterval interval = (DoubleInterval)intervals.get(parameterName);
  Error.assertTrue(interval != null);
  return interval;
}
public int getIndex(String parameterName) {
  Integer integer = (Integer)indices.get(parameterName);
  if (integer == null)
    return NOT_HERE;
  return integer.intValue();
}
}