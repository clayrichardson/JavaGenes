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

public class MinMaxesTest extends TestCase {
public MinMaxesTest(String name) {super(name);}
public void testAll() {
  final int largestAdd = 5;
  final double constant = 4.0;
  double[] a = {0.0,1.0,2.0,constant};
  MinMaxes mm = new MinMaxes();
  for(int i = 0; i <= largestAdd; i++) {
    for(int j = 0; j < a.length-1; j++)
      a[j] += i;
    mm.add(a);
  }
  for(int i = 0; i < mm.size()-1; i++) {
    DoubleInterval d = mm.getDoubleInterval(i);
    assertTrue(i+" low",d.low() == i);
    assertTrue(i+" high",d.high() == i+15);
  }
  DoubleInterval d = mm.getDoubleInterval(3);
  assertTrue(3+" low",d.low() == constant);
  assertTrue(3+" high",d.high() == constant);
}
}
