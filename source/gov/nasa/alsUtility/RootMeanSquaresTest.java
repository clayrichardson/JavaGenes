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

import junit.framework.TestCase;
import gov.nasa.alsUtility.Utility;

public class RootMeanSquaresTest extends TestCase {

public RootMeanSquaresTest(String name) {super(name);}

private double get(double[] array) {
  RootMeanSquares rms = new RootMeanSquares();
  for(int i = 0; i < array.length; i++)
    rms.addDatum(array[i]);
  return rms.rms();
}
public void test() {
  double[] a = {1,1,1};
  assertTrue(Utility.nearlyEqual(1,get(a)));
  double[] b = {0.5,1,2};
  assertTrue(1 < get(b));;
  RootMeanSquares rms = new RootMeanSquares();
  assertTrue(rms.rms() == 0);
}
}