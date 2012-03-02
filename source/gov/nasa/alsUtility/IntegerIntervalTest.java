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

public class IntegerIntervalTest extends TestCase {

public IntegerIntervalTest(String name) {super(name);}

public void testEquals() {
	assertTrue("1", new IntegerInterval(-2,4).equals(new IntegerInterval(-2,4)));
	assertFalse("2", new IntegerInterval(-2,3).equals(new IntegerInterval(-2,4)));
	assertFalse("3", new IntegerInterval(2,4).equals(new IntegerInterval(-2,4)));
	assertFalse("4", new IntegerInterval(2,5).equals(new IntegerInterval(-2,4)));
}
public void testLimitTo() {
	assertTrue("1", new IntegerInterval(-2,4).equals(new IntegerInterval(-100,100).limitTo(new IntegerInterval(-2,4))));
	assertTrue("2", new IntegerInterval(-2,4).equals(new IntegerInterval(-2,4).limitTo(new IntegerInterval(-2,4))));
	assertTrue("3", new IntegerInterval(-2,4).equals(new IntegerInterval(-2,4).limitTo(new IntegerInterval(-3,5))));
	assertTrue("4", new IntegerInterval(-2,4).equals(new IntegerInterval(-3,4).limitTo(new IntegerInterval(-2,5))));
	assertTrue("5", new IntegerInterval(-2,4).equals(new IntegerInterval(-2,5).limitTo(new IntegerInterval(-3,4))));
	assertFalse("6", new IntegerInterval(-2,4).equals(new IntegerInterval(-1,4).limitTo(new IntegerInterval(-2,4))));
	assertFalse("7", new IntegerInterval(-2,4).equals(new IntegerInterval(-2,3).limitTo(new IntegerInterval(-2,4))));
	assertFalse("8", new IntegerInterval(-2,4).equals(new IntegerInterval(-1,3).limitTo(new IntegerInterval(-2,4))));
}
public void testIsNonNegative() {
	assertTrue("1", new IntegerInterval(0,2).isNonNegative());
	assertTrue("2", new IntegerInterval(0,0).isNonNegative());
	assertFalse("3", new IntegerInterval(0,-2).isNonNegative());
	assertFalse("4", new IntegerInterval(-1,2).isNonNegative());
}
public void testGetRandomUniqueIndices() {
	testGetRandomUniqueIndices("1",5,1,10);
	testGetRandomUniqueIndices("2",2,1,10);
	testGetRandomUniqueIndices("3",3,-1,1);
}
private void testGetRandomUniqueIndices(String name,int number, int low, int high) {
	IntegerInterval interval = new IntegerInterval(low,high);
	int[] array = interval.getRandomUniqueIndices(number);
	Error.assertTrue(name,array.length == number);
	for(int i = 0; i < array.length; i++)
		Error.assertTrue(name+i, interval.isBetween(array[i]));
}
public void testGetArrayOfAllValues() {
	int[] array1 = {1,2,3};
	assertTrue("1", Utility.equals(array1,new IntegerInterval(1,3).getArrayOfAllValues()));
	assertFalse("2", Utility.equals(array1,new IntegerInterval(1,4).getArrayOfAllValues()));
	assertFalse("3", Utility.equals(array1,new IntegerInterval(0,2).getArrayOfAllValues()));
}
public void testIntervalInclusive() {
	assertTrue("1", new IntegerInterval(1,9).intervalInclusive() == 9);
	assertTrue("2", new IntegerInterval(-1,1).intervalInclusive() == 3);
	assertTrue("3", new IntegerInterval(1,1).intervalInclusive() == 1);
	assertTrue("4", new IntegerInterval(0,9).intervalInclusive() == 10);
}
public void testEqualsExtremes() {
	IntegerInterval interval = new IntegerInterval(-2,4);
	assertTrue("1", interval.equalsExtremes(-2));
	assertTrue("2", interval.equalsExtremes(4));
	assertFalse("3", interval.equalsExtremes(0));
	assertFalse("4", interval.equalsExtremes(1));
	assertFalse("5", interval.equalsExtremes(-4));
	assertFalse("6", interval.equalsExtremes(2));
	assertFalse("7", interval.equalsExtremes(5));
}
public void testDistanceFrom() {
	IntegerInterval interval = new IntegerInterval(1,3);
	assertTrue("1", interval.distanceFrom(2) == 0);
	assertTrue("2", interval.distanceFrom(1) == 0);
	assertTrue("3", interval.distanceFrom(3) == 0);
	assertTrue("4", interval.distanceFrom(0) == -1);
	assertTrue("5", interval.distanceFrom(4) == 1);
	assertTrue("5", interval.distanceFrom(10) == 7);
	assertTrue("6", interval.distanceFrom(-1) == -2);
	interval = new IntegerInterval(2,2);
	assertTrue("7", interval.distanceFrom(-1) == -3);
	assertTrue("8", interval.distanceFrom(2) == 0);
	assertTrue("9", interval.distanceFrom(3) == 1);
}
public void testRandom() {
  int low = 0;
  int high = 10;
  IntegerInterval interval = new IntegerInterval(low,high);
  boolean[] results = new boolean[high+1];
  for(int i = 0; i < results.length; i++)
    results[i] = false;
  for(int i = 0; i < 10000; i++)
    results[interval.random()] = true;
  for(int i = 0; i < results.length; i++)
    assertTrue(i + "", results[i]);
}
public void testIsBetween() {
	IntegerInterval interval = new IntegerInterval(-1,4);
	assertTrue("1", interval.isBetween(3));
	assertFalse("2", interval.isBetween(5));
	assertTrue("3", interval.isBetween(-1));
	assertTrue("4", interval.isBetween(4));
	assertFalse("5", interval.isBetween(-2));
	assertFalse("6", interval.isBetween(1000));
}
}