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

public class RandomNumberTest extends TestCase {

public RandomNumberTest(String name) {super(name);}

public void testGetUniqueIndex() {
  for(int i = 0; i < 1000; i++) {
    assertTrue("1",RandomNumber.getUniqueIndex(3,1) != 1);
    assertTrue("2",RandomNumber.getUniqueIndex(3,0) != 0);
    assertTrue("3",RandomNumber.getUniqueIndex(3,2) != 2);
    assertTrue("4",RandomNumber.getUniqueIndex(2,0) != 0);
    assertTrue("5",RandomNumber.getUniqueIndex(20,8) != 8);
  }
}
public void testDouble() {
    for(int i = 0; i < 1000; i++) {
        assertTrue("1",RandomNumber.getDouble() <= 1);
        assertTrue("2",RandomNumber.getDouble() >= 0);
        assertTrue("3",RandomNumber.getDouble(4.4) <= 4.4);
        assertTrue("4",RandomNumber.getDouble(4.4) >= 0);
    }
}
public void testGetDoubleInInterval() {
    final double low = 1;
    final double high = 10;
    DoubleInterval interval = new DoubleInterval(low,high);
    for(int i = 0; i < 1000; i++) {
        double value = RandomNumber.getDouble(interval);
        assertTrue(low <= value && value <= high);
    }
}
public void testGetIntegerInInterval() {
    final int low = 1;
    final int high = 10;
    IntegerInterval interval = new IntegerInterval(low,high);
    for(int i = 0; i < 1000; i++) {
        int value = RandomNumber.getInteger(interval);
        assertTrue("1-"+i,low <= value && value <= high);
    }
    interval = new IntegerInterval(0,1);
    for(int i = 0; i < 1000; i++) {
        int value = RandomNumber.getInteger(interval);
        assertTrue("2-"+i,0 == value || value == 1);
    }
}
public void testFillRandomly() {
    final int low = 1;
    final int high = 10;
    IntegerInterval interval = new IntegerInterval(low,high);
    int[] array = new int[6];
    for(int i = 0; i < 100; i++) {
        RandomNumber.fillRandomly(interval,array);
        for(int j = 0; j < array.length; j++) 
            assertTrue(i+" "+j+" "+array[j],low <= array[j] && array[j] <= high);
    }
}
public void testGetProbability() {
  assertTrue(RandomNumber.getProbability(1));
  assertTrue(!RandomNumber.getProbability(0));
  assertTrue(RandomNumber.getProbability(1.5));
  assertTrue(!RandomNumber.getProbability(-0.2));
  boolean wasTrue = false;
  boolean wasFalse = false;
  for(int i = 0; i < 500; i++)
    if (RandomNumber.getProbability(0.5)) // could fail but unlikely
      wasTrue = true;
    else
      wasFalse = true;
  assertTrue(wasTrue);
  assertTrue(wasFalse);
}
}