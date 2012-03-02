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
import java.util.Vector;
import java.util.LinkedList;
import java.lang.Integer;

public class UtilityTest extends TestCase {

public UtilityTest(String name) {super(name);}

public void testGetFirstLineOfFile() {
	testGetLastLineOfFile(1, "abc", "de");
	testGetLastLineOfFile(2, "abc\ndef", "a");
	testGetLastLineOfFile(3, "abc\ndef", "");
}
public void testGetFirstLineOfFile(int name, String body, String firstLine) {
	final String filename = "TTTEEEMMMPPP12345";
	String fileContents = firstLine + "\n" + body;
	Utility.makeFile(filename, fileContents);
	Error.assertTrue(name + "", Utility.getFirstLineOfFile(filename).equals(firstLine));
	Utility.removeFile(filename);
}

public void testGetLastLineOfFile() {
	testGetLastLineOfFile(1, "abc", "de");
	testGetLastLineOfFile(2, "abc\ndef", "a");
	testGetLastLineOfFile(3, "abc\ndef", "");
}
public void testGetLastLineOfFile(int name, String body, String lastLine) {
	final String filename = "TTTEEEMMMPPP12345";
	String fileContents = body + "\n" + lastLine;
	Utility.makeFile(filename, fileContents);
	Error.assertTrue(name + "", Utility.getLastLineOfFile(filename).equals(lastLine));
	Utility.removeFile(filename);
}
public void testConcatentateStringArrays() {
	String[] s1 = {"a","b"};
	String[] s2 = {"c","b","d"};
	String[] result = {"a","b","c","b","d"};
	Error.assertTrue(Utility.isEqual(result,Utility.concatenate(s1,s2)));
	Error.assertTrue(Utility.isEqual(s1,Utility.concatenate(s1,null)));
	Error.assertTrue(Utility.isEqual(s2,Utility.concatenate(null,s2)));
	Error.assertTrue(null == Utility.concatenate(null,null));
}
public void testAreDifferentIntArrayToLimit() {
	int[] array1 = {1,2,3,1};
	assertTrue("1", Utility.areDifferent(array1,1));
	assertTrue("2", Utility.areDifferent(array1,2));
	assertFalse("3", Utility.areDifferent(array1,3));
	array1[3] = 4;
	assertTrue("4", Utility.areDifferent(array1,3));
}
public void testIsPositiveDoubleArray() {
	double[] a1 = {1,2,3};
	assertTrue("1", Utility.isPositive(a1));
	a1[2] = -1;
	assertFalse("2", Utility.isPositive(a1));
	assertFalse("3", Utility.isPositive(null));
	double[] a2 = {};
	assertTrue("4", Utility.isPositive(a2));
}
public void testDoubleLessThanOrEqual() {
	assertTrue("1", Utility.lessThanOrEqual(-1,-1));
	assertTrue("2", Utility.lessThanOrEqual(0,4));
	assertTrue("3", Utility.lessThanOrEqual(-1,6));
	assertTrue("4", Utility.lessThanOrEqual(0,0));
	assertFalse("5", Utility.lessThanOrEqual(1,0));
	assertFalse("5", Utility.lessThanOrEqual(1,-3));
	assertFalse("5", Utility.lessThanOrEqual(-1,-1.01));
}
public void testList2String() {
	LinkedList list = new LinkedList();
	list.add("1");
	list.add("2");
	list.add("10");
	list.add("");
	list.add("abc");
	String s = Utility.list2String(list);
	Error.assertTrue(s.equals("1\n2\n10\n\nabc\n"));
}
public void testIndexofLeast() {
	double[] a1 = {1,2,3};
	Error.assertTrue(Utility.indexOfLeast(a1) == 0);
	a1[1] = -1;
	Error.assertTrue(Utility.indexOfLeast(a1) == 1);
	a1[2] = -4;
	Error.assertTrue(Utility.indexOfLeast(a1) == 2);
}
public void testIndexofGreatest() {
	double[] a1 = {1,2,3};
	Error.assertTrue(Utility.indexOfGreatest(a1) == 2);
	a1[2] = -1;
	Error.assertTrue(Utility.indexOfGreatest(a1) == 1);
	a1[1] = -4;
	Error.assertTrue(Utility.indexOfGreatest(a1) == 0);
}
public void testCopy() {
	double[] d1 = {1,2,3};
	double[] d2 = Utility.copy(d1);
	assertTrue("double[]", Utility.nearlyEqual(d1,d2));
	double[][][] d3 = {
		{{1,1},{2}},
		{{1,2,3},{3,4,5}}
	};
	double[][][] d4 = Utility.copy(d3);
	assertTrue("double[][][] copy", Utility.nearlyEqual(d3,d4));
	assertTrue("double[][][] sameDimensions", Utility.sameDimensions(d3,d4));
	String[] s1 = {"a", "b", "cd"};
	String[] s2 = Utility.copy(s1);
	assertTrue("String[]", Utility.isEqual(s1,s2));
}
public void testRandomizeIntArray() {
    RandomNumber.setSeed(990639400906L);   // to get deterministic results
    for(int i = 0; i < 100; i++) {
        int[] a = {1,2,3,4,5,6,7};
        int sum = Utility.arraySum(a);
        Utility.randomize(a);
        /*
        for(int j = 0; j < a.length; j++)
            System.out.print(a[j] + " ");
        System.out.println();
        */
        assertFalse(i + " 1", Utility.isAscending(a));
        assertTrue(i + " 2", sum == Utility.arraySum(a));
    }
}
public void testEqualsIntArray() {
    int[] a1 = {1,2,3};
    int[] b1 = {1,2,3};
    assertTrue("1", Utility.equals(a1,b1));
    int[] a2 = {1,2,3};
    int[] b2 = {1,3};
    assertFalse("2", Utility.equals(a2,b2));
    int[] a3 = {1,2,3};
    int[] b3 = {1,2,4};
    assertFalse("3", Utility.equals(a3,b3));
    int[] a4 = {1,2,3};
    int[] b4 = {3,2,1};
    assertFalse("4", Utility.equals(a4,b4));
}
public void testFile2StringArray() {
    String[] testArray = {"a","ab","abc", "", "xxx"};
    String filename = "TEMP.UtilityFile2StringArrayTest";
    //Utility.makeFile(filename,"");
    java.io.PrintWriter file = Utility.outputFile(filename);
    for(int i = 0; i < testArray.length; i++)
        file.println(testArray[i]);
    file.close();
    String[] array = Utility.file2StringArray(filename);
    assertTrue("length", array.length == testArray.length);
    for(int i = 0; i < array.length; i++)
        assertTrue("string " + i, array[i].equals(testArray[i]));
    Utility.deleteFile(filename);
}   
public void testMakeFile() {
    String[] a1 = {"abc", "a", "", "12334"};
    makeFile1test("1",a1);
    String[] a2 = {};
    makeFile1test("2",a2);
    String[] a3 = {"abc", "", "lskdjflskjdf", ""};
    makeFile1test("3",a3);
    String[] a4 = {""};
    makeFile1test("4",a4);
}
private void makeFile1test(String name, String[] array) {
    String filename = "TEMP.UtilitymakeFileTest";
    Utility.makeFile(filename,array);
    String[] fromFile = Utility.file2StringArray(filename);
    assertTrue(name + " equal", array.length == fromFile.length);
    for(int i = 0; i < array.length; i++)
        assertTrue(name + " " + i, array[i].equals(fromFile[i]));
    Utility.deleteFile(filename);
}
public void testSignOf() {
    assertTrue("1", Utility.signOf(0) == 0);
    assertTrue("2", Utility.signOf(50.0) == 1);
    assertTrue("3", Utility.signOf(-0.001) == -1);
}
public void testArraySum() {
    int[] i1 = {1,2,3,4,6};
    assertTrue("1",Utility.arraySum(i1) == 16);
    int[] i2 = {-1};
    assertTrue("2",Utility.arraySum(i2) == -1);
    int[] i3 = {-1,5,-2,10};
    assertTrue("3",Utility.arraySum(i3) == 12);
    int[] i4 = {};
    assertTrue("4",Utility.arraySum(i4) == 0);
}
public void testDoubleArraySum() {
    double[] i1 = {1,2,3,4,6};
    assertTrue("1",Utility.doubleArraySum(i1) == 16);
    double[] i2 = {-1};
    assertTrue("2",Utility.doubleArraySum(i2) == -1);
    double[] i3 = {-1,5,-2,10};
    assertTrue("3",Utility.doubleArraySum(i3) == 12);
    double[] i4 = {};
    assertTrue("4",Utility.doubleArraySum(i4) == 0);
}
public void testSortIntegerArrays() {
    int[] a1 = {3,6,2,7,9,0};
    assertTrue("1",!Utility.isAscending(a1));
    Utility.sort(a1);
    assertTrue("2",Utility.isAscending(a1));
    
    int[] a2 = {100, 5, -3, -200};
    assertTrue("3",!Utility.isAscending(a2));
    Utility.sort(a2);
    assertTrue("4",Utility.isAscending(a2));
}
public void testAreDifferent() {
    Integer s1 = new Integer(1);
    Integer s1copy = new Integer(1);
    assertTrue("same", s1.equals(s1copy));
    String s2 = "s2";
    String s3 = "s3";

    Object array1[] = {s1,s1copy,s2,s3};
    assertTrue("1",Utility.areDifferent(array1));

    Object array2[] = {s1,s1,s2,s3};
    assertTrue("2",!Utility.areDifferent(array2));

    Object array3[] = {s1,s2,s1};
    assertTrue("3",!Utility.areDifferent(array3));

    Object array4[] = {s2,s1,s3,s1};
    assertTrue("2",!Utility.areDifferent(array4));

    Object array5[] = {s1,s3,s2,s1};
    assertTrue("2",!Utility.areDifferent(array5));

    Object array6[] = {null,null,s3,s2,s1};
    assertTrue("2",Utility.areDifferent(array6,true));
}
  public void testString2Integer() {
    assertTrue(Utility.string2integer("255") == 255);
  }
  public void testRemoveDuplicates() {
    String[] original = {"a", "aa", "a", "bb","a", "bb"};
    String[] correct = {"a","aa","bb"};
    String[] after = Utility.removeDuplicates(original);
    assertTrue("length", correct.length == after.length);
    for(int i = 0; i < after.length; i++)
        assertTrue(""+ i, correct[i].equals(after[i]));
    }
  public void testNearlyEqual() {
    assertTrue("1",!Utility.nearlyEqual(0,0.001));
    assertTrue("2",Utility.nearlyEqual(0.1,0.100000001));
    assertTrue("3",Utility.nearlyEqual(0,0));
    assertTrue("4",!Utility.nearlyEqual(-0.0001,0));
    assertTrue("5",Utility.nearlyEqual(1e6,1e6+5,1e-4));
    assertTrue("6",!Utility.nearlyEqual(0,1));
    assertTrue("7",!Utility.nearlyEqual(10,17,0.1));
    assertTrue("8",Utility.nearlyEqual(10,17,0.5));
    assertTrue("9",!Utility.nearlyEqual(-0.1,0.1,0.4));
  }
public void testNearlyEqualVector3d() {
	testNearlyEqualVector3d("1", 0,0,0, 0,0,0, true);
	testNearlyEqualVector3d("2", 1,0,0, 0,0,0, false);
	testNearlyEqualVector3d("3", 1,0,0, 1,3,0, false);
	testNearlyEqualVector3d("4", 0,0,0, 0,0,2, false);
}
private void testNearlyEqualVector3d(String name, double x1, double x2, double x3, double y1, double y2, double y3, boolean result) {
	assertTrue(name, Utility.nearlyEqual(new Vector3d(x1,x2,x3),new Vector3d(y1,y2,y3)) == result);
}
public void testTwoNearlyEqual() {
	double[] a1 = {1,2,3};
	assertFalse("1", Utility.twoNearlyEqual(a1));
	double[] a2 = {2,2,3};
	assertTrue("2", Utility.twoNearlyEqual(a2));
	double[] a3 = {1,2,2.000000001};
	assertTrue("3", Utility.twoNearlyEqual(a3));
	double[] a4 = {1,2,3,2,7,3};
	assertTrue("4", Utility.twoNearlyEqual(a4));
	double[] a5 = {1,2,3,4,5,6,7,8,0.9999999999999};
	assertTrue("5", Utility.twoNearlyEqual(a5));
	double[] a6 = {1,2,3,3.01};
	assertFalse("6", Utility.twoNearlyEqual(a6));
}
public void testLessThanOrEqual() {
  double[] a = {1.0,2.0,3.0};
  double[] b = {2.0,3.0,4.0};
  assertTrue("1", Utility.lessThanOrEqual(a,b));
  assertTrue("2", !Utility.lessThanOrEqual(b,a));
  double[] c = {1.0,2.0,3.0};
  double[] d = {1.0,2.0,3.0};
  assertTrue("3", Utility.lessThanOrEqual(c,d));
  assertTrue("4", Utility.lessThanOrEqual(d,c));
  d[2] = 2.0;
  assertTrue("5", !Utility.lessThanOrEqual(c,d));
  assertTrue("6", Utility.lessThanOrEqual(d,c));
}

public void testArrayToTabSeparated() {
  String s[] = {"1", "2","3"};
  assertTrue("string3", Utility.arrayToTabSeparated(s).equals("1\t2\t3"));
  String ss[] = {};
  assertTrue("string0", Utility.arrayToTabSeparated(ss).equals(""));
  String sss[] = {"1"};
  assertTrue("string1", Utility.arrayToTabSeparated(sss).equals("1"));

  double d[] = {1.1,2.1,3.1};
  assertTrue("double3", Utility.arrayToTabSeparated(d).equals("1.1\t2.1\t3.1"));
  double dd[] = {};
  assertTrue("double0", Utility.arrayToTabSeparated(dd).equals(""));
  double ddd[] = {1.1};
  assertTrue("double1", Utility.arrayToTabSeparated(ddd).equals("1.1"));
}
public void testShortFileIO() {
  String filename = "file.UTILITYTEST.temporary";
  String string = "testing";
  Utility.makeFile(filename,string);
  String input = Utility.readOneLineFile(filename);
  assertTrue(input.equals(string));
  Utility.removeFile(filename);
}
public void testNumberOfThisCharInString() {
  assertTrue(Utility.numberOfThisCharInString("a abaa",'a') == 4);
  assertTrue(Utility.numberOfThisCharInString("a abaa",'c') == 0);
  assertTrue(Utility.numberOfThisCharInString("a abaa",'b') == 1);
  assertTrue(Utility.numberOfThisCharInString("a abaa ",' ') == 2);
}
public void testTanimotoDistance() {
  assertTrue(Utility.TanimotoDistance(0,0) == 0);
  assertTrue(Utility.TanimotoDistance(0.5,0.5) == 0);
  assertTrue(Utility.TanimotoDistance(1,3) == 0.5);
  assertTrue(Utility.TanimotoDistance(-1,-3) == 0.5);

  // weakness of the measure
  assertTrue(Utility.TanimotoDistance(0,1) == 1);
  assertTrue(Utility.TanimotoDistance(2,0) == 1);
  assertTrue(Utility.TanimotoDistance(-1,1) == 1);
  assertTrue(Utility.TanimotoDistance(-1,100) == 1);
}
public void testTanimotoDistanceOneSideOfZero() {
  // same as TanimotoDistance
  assertTrue(Utility.TanimotoDistanceOneSideOfZero(0,0) == 0);
  assertTrue(Utility.TanimotoDistanceOneSideOfZero(0.5,0.5) == 0);
  assertTrue(Utility.TanimotoDistanceOneSideOfZero(1,3) == 0.5);
  assertTrue(Utility.TanimotoDistanceOneSideOfZero(-1,-3) == 0.5);
  // fixes some problems
  assertTrue(Utility.nearlyEqual(Utility.TanimotoDistanceOneSideOfZero(0,1),0.33333333333333));
  assertTrue(Utility.TanimotoDistanceOneSideOfZero(2,0) == 0.5);
  assertTrue(Utility.TanimotoDistanceOneSideOfZero(-1,1) == 0.5);
  assertTrue(Utility.nearlyEqual(Utility.TanimotoDistanceOneSideOfZero(-1,100),0.9805825242718447));
  // but has other problems
  assertTrue(Utility.TanimotoDistanceOneSideOfZero(-2,2) == 0.5);
  assertTrue(Utility.TanimotoDistanceOneSideOfZero(-100,100) == 0.5);
  assertTrue(Utility.TanimotoDistanceOneSideOfZero(-1000,1000) == 0.5);
}
private void printIt(double x, double y) {
  System.out.println("("+x+","+y+") = "+Utility.TanimotoDistanceOneSideOfZero(x,y));
}
public void testSortVectorOfDouble() {
  double[] array = {1,0.5,2,0.25};
  Vector vector = Utility.makeVector(array);
  Utility.sortVectorOfDouble(vector);
  assertTrue(vector.size() == 4);
  assertTrue(((Double)vector.elementAt(0)).doubleValue() == 0.25);
  assertTrue(((Double)vector.elementAt(1)).doubleValue() == 0.5);
  assertTrue(((Double)vector.elementAt(2)).doubleValue() == 1);
  assertTrue(((Double)vector.elementAt(3)).doubleValue() == 2);
}
public void testMakeVector() {
  double[] array = {1.0, 2.0, 0.5};
  Vector vector = Utility.makeVector(array);
  assertTrue(vector.size() == 3);
  assertTrue(((Double)vector.elementAt(2)).doubleValue() == 0.5);
}
}

