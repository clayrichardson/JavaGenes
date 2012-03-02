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

import java.io.File;
import java.io.PrintWriter;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URL;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import java.io.Serializable;
import java.io.IOException;
import java.util.Vector;
import java.util.Hashtable;
import gov.nasa.alsUtility.Error;
import java.lang.Cloneable;
import java.lang.reflect.Constructor;
import java.lang.CloneNotSupportedException;
import gov.nasa.alsUtility.IO;
import java.util.Date;
import java.util.List;

/**
Miscellaneous useful functions.
*/
public class Utility {

static public String[] concatenate(String[] array1, String[] array2) {
	if (array1 == null)
		return array2;
	if (array2 == null)
		return array1;
	if (array1 == null && array2 == null)
		return null;
	String[] array = new String[array1.length + array2.length];
	System.arraycopy(array1,0,array,0,array1.length);
	System.arraycopy(array2,0,array,array1.length,array2.length);
	return array;
}
static public int indexOfLeast(double[] a) {
	Error.assertTrue(a.length > 0);
	int leastIndex = 0;
	for(int i = 1; i < a.length; i++)
		if (a[i] < a[leastIndex])
			leastIndex = i;
	return leastIndex;
}
static public int indexOfGreatest(double[] a) {
	Error.assertTrue(a.length > 0);
	int greatestIndex = 0;
	for(int i = 1; i < a.length; i++)
		if (a[i] > a[greatestIndex])
			greatestIndex = i;
	return greatestIndex;
}
static public boolean sameDimensions(double[][][] a, double [][][] b) {
	if (a == b)
		return true;
	if (a.length != b.length)
		return false;
	for(int i = 0; i < a.length; i++)
		if (!sameDimensions(a[i],b[i]))
			return false;
	return true;
}
static public boolean sameDimensions(double[][] a, double [][] b) {
	if (a == b)
		return true;
	if (a.length != b.length)
		return false;
	for(int i = 0; i < a.length; i++)
		if (!sameDimensions(a[i],b[i]))
			return false;
	return true;
}
static public boolean sameDimensions(double[] a, double [] b) {
	if (a == b)
		return true;
	if (a.length != b.length)
		return false;
	return true;
}
static public void setToConstant(double[][][] array, double value) {
	for(int i = 0; i < array.length; i++)
		setToConstant(array[i],value);
}
static public void setToConstant(double[][] array, double value) {
	for(int i = 0; i < array.length; i++)
		setToConstant(array[i],value);
}
static public void setToConstant(double[] array, double value) {
	for(int i = 0; i < array.length; i++)
		array[i] = value;
}
static public void multiply(double[][][] array, double factor) {
	for(int i = 0; i < array.length; i++)
		multiply(array[i],factor);
}
static public void multiply(double[][] array, double factor) {
	for(int i = 0; i < array.length; i++)
		multiply(array[i],factor);
}
static public void multiply(double[] array, double factor) {
	for(int i = 0; i < array.length; i++)
		array[i] *= factor;
}

static public double[][][] copy(double[][][] in) {
	double[][][] out = new double[in.length][][];
	for(int i = 0; i < in.length; i++)
		out[i] = copy(in[i]);
	return out;
}
static public double[][] copy(double[][] in) {
	double[][] out = new double[in.length][];
	for(int i = 0; i < in.length; i++)
		out[i] = copy(in[i]);
	return out;
}
static public double[] copy(double[] in) {
	double[] out = new double[in.length];
	for(int i = 0; i < out.length; i++)
		out[i] = in[i];
	return out;
}
static public int[] copy(int[] in) {
	int[] out = new int[in.length];
	for(int i = 0; i < out.length; i++)
		out[i] = in[i];
	return out;
}
static public String[] copy(String[] in) {
	String[] out = new String[in.length];
	for(int i = 0; i < out.length; i++)
		out[i] = in[i];
	return out;
}
static public boolean[] copy(boolean[] in) {
	boolean[] out = new boolean[in.length];
	for(int i = 0; i < out.length; i++)
		out[i] = in[i];
	return out;
}
static public void printIntArray(int[] array) {
    System.out.println(toString(array));
}
/** will be slow on large arrays.  Algorithm is O(n^2) */
static public void randomize(int[] array) {
    ExtendedVector randomized = new ExtendedVector();
    for(int i = 0; i < array.length; i++)
        randomized.add(new Integer(array[i]));
    for(int i = 0; i < array.length; i++) {
        Integer value = (Integer)randomized.getRandomElement();
        array[i] = value.intValue();
        randomized.remove(value);
    }
    Error.assertTrue(randomized.size() == 0);
}

/**
@return 0 if value == 0, -1 if value < 0, 1 otherwise
*/
static public int signOf(double value) {
    if (value == 0)
        return 0;
    if (value < 0)
        return -1;
    return 1;
}
static public int arraySum(int[] array) {
    int sum = 0;
    for(int i = 0; i < array.length; i++)
        sum += array[i];
    return sum;
}
static public double doubleArraySum(double[] array) {
    double sum = 0;
    for(int i = 0; i < array.length; i++)
        sum += array[i];
    return sum;
}
static public String getFirstLineOfFile(String filename) {
	java.io.BufferedReader file = IO.getBufferedReader(filename);
	String line = null;
	try {
		line = file.readLine();
	} catch (Exception e) {
		Error.fatal(e);
	}
	return line;
}
static public String getLastLineOfFile(String filename) {
	java.io.BufferedReader file = IO.getBufferedReader(filename);
	String lastLine = "";
	while(true) {
		String line = null;
		try {
			line = file.readLine();
		} catch (Exception e) {
			Error.fatal(e);
		}
		if (line == null)
			break;
		else
			lastLine = line;
	}
	return lastLine;
}
static public void deleteFile(String filename) {
    java.io.File file = new java.io.File(filename);
    file.delete();
}
static public Date stkDateString2Date(String stkDateString) {
    Date r = null;
    try {
        //typical format: 1 Jan 2001 00:00:00.00
        SimpleDateFormat f = new SimpleDateFormat("d MMM yyyy H:m:s.S");
        r = f.parse(stkDateString + "0"); // "0" added to turn last field into milliseconds
    } catch (Exception e) {Error.fatal(stkDateString + " caused " + e);}
    return r;
}
/**
@return number of seconds date represents.  Throws away any spare milliseconds.
*/
static public long date2seconds(Date date) {
    return date.getTime() / 1000;
}
public static boolean areDifferent(int[] array) {return areDifferent(array,array.length-1);}
/** 
@param upToIndex check the array up to this index (inclusive)
*/
public static boolean areDifferent(int[] array, int upToIndex) {
	final int limit = Math.min(array.length,upToIndex+1);
    for(int i = 0; i < limit; i++)
    for(int j = i+1; j < limit; j++) {
        if (array[i] == array[j])
            return false;
    }
    return true;
}
public static boolean areDifferent(Object[] array) {
    return areDifferent(array,false);
}
public static boolean areDifferent(Object[] array, boolean ignoreNull) {
    for(int i = 0; i < array.length; i++)
    for(int j = i+1; j < array.length; j++) {
        if (ignoreNull && array[i] == null)
            continue;
        if (array[i] == array[j])
            return false;
    }
    return true;
}

public static String[] removeDuplicates(String[] strings) {
  Hashtable existing = new Hashtable();
  ExtendedVector names = new ExtendedVector();
  for (int i = 0; i < strings.length; i++)
    if (!existing.containsKey(strings[i])) {
      names.addElement(strings[i]);
      existing.put(strings[i],strings[i]);
  }
  String[] r = new String[names.size()];
  names.copyInto((Object[])r);
  return r;
}
public static boolean lessThanOrEqual(double[] less, double[] more) {
  Error.assertTrue(less.length == more.length);
  for(int i = 0; i < less.length; i++)
    if (less[i] > more[i])
      return false;
  return true;
}
public static String readOneLineFile(String filename) {
  BufferedReader input = IO.getBufferedReader(filename);
  String contents = null;
  try {
    contents = input.readLine();
    input.close();
  } catch (IOException e) {
    	Error.fatal("IO exception reading file " + filename + " - "+ e);
  }
  return contents;
}
public static String list2String(List list) {
	String r = "";
	for(int i = 0; i < list.size(); i++)
		r += list.get(i).toString() + "\n";
	return r;
}
public static String[] file2StringArray(String filename) {
  Vector contents = new Vector();
  try {
    BufferedReader input = IO.getBufferedReader(filename);
    String line = null;
    while(true) {
        line = input.readLine();
        if (line == null)
            break;
        contents.add(line);
    }
    input.close();
  } catch (IOException e) {
    	Error.fatal("IO exception reading file " + filename + " - "+ e);
  }
  String[] array = new String[contents.size()];
  for(int i = 0; i < array.length; i++)
    array[i] = (String)contents.get(i);
  return array;
}
public static int numberOfThisCharInString(String string, char c) {
  char[] array = string.toCharArray();
  int count = 0;
  for(int i = 0; i < array.length; i++)
    if (array[i] == c) count++;
  return count;
}

public static double TanimotoDistance(double x, double y) {
  if (x == y) return 0;
  double numerator = Math.abs(x - y);
  double denominator = Math.abs(x) + Math.abs(y);
  Error.assertTrue(denominator != 0);
  return numerator/denominator;
}
public static double TanimotoDistanceOneSideOfZero(double x, double y) {
  double translate = Utility.translateToOneSideOfZero(x,y);
  return TanimotoDistance(x+translate,y+translate);
}
private static double translateToOneSideOfZero(double x, double y) {
  if (x > 0 && y > 0) return 0;
  if (x < 0 && y < 0) return 0;
  double translate = 2*(-1 * (Math.abs(x) < Math.abs(y) ? x : y));
  if (translate == 0)
    translate = 1;
  return translate; // move smallest absolute value to same place on the other side of 0
}
public static boolean nearlyEqual(Vector3d a, Vector3d b) {
	return nearlyEqual(a.x,b.x) && nearlyEqual(a.y,b.y) && nearlyEqual(a.z,b.z);
}
public static boolean nearlyEqual(double x, double y) {
  return nearlyEqual(x,y,1e-6);
}
public static boolean lessThanOrEqual(double smaller, double larger) {
	return smaller <= larger || nearlyEqual(smaller,larger);
}
public static boolean isPositive(double[] array) {
	if (array == null) 
		return false;
	for(int i = 0; i < array.length; i++)
		if (array[i] <= 0)
			return false;
	return true;
}
	
/**
 will always return false for values of opposite sign
*/
public static boolean nearlyEqual(double x, double y, double epsilon) {
	if (x == y) return true;
	if (y == 0) return Math.abs(x) < epsilon;
	if (x == 0) return Math.abs(y) < epsilon;
	double ratio = x/y;
	double value = Math.abs(ratio - 1);
	return value < epsilon && ratio > 0;
}
static public boolean nearlyEqual(double[][][] a, double [][][] b) {
	if (a == b)
		return true;
	if (a.length != b.length)
		return false;
	for(int i = 0; i < a.length; i++)
		if (!nearlyEqual(a[i],b[i]))
			return false;
	return true;
}
static public boolean nearlyEqual(double[][] a, double [][] b) {
	if (a == b)
		return true;
	if (a.length != b.length)
		return false;
	for(int i = 0; i < a.length; i++)
		if (!nearlyEqual(a[i],b[i]))
			return false;
	return true;
}
public static boolean nearlyEqual(double[] x, double[] y) {
	if (x.length != y.length)
		return false;
	for(int i = 0; i < x.length; i++)
		if (!nearlyEqual(x[i],y[i]))
			return false;
	return true;
}
/** @return true if at least two elements of x[] are nearlyEqual() */
public static boolean twoNearlyEqual(double[] x) {
	for(int i =0; i < x.length; i++)
		for(int j = i+1; j < x.length; j++)
			if (nearlyEqual(x[i],x[j]))
				return true;
	return false;
}
public static boolean isEqual(String[] x, String[] y) {
	if (x.length != y.length)
		return false;
	for(int i = 0; i < x.length; i++)
		if (!x[i].equals(y[i]))
			return false;
	return true;
}
public static boolean isEqual(int[] x, int[] y) {
	if (x.length != y.length)
		return false;
	for(int i = 0; i < x.length; i++)
		if (x[i] != y[i])
			return false;
	return true;
}
public static double degrees2radians(double degrees) {
	return degrees * Math.PI / 180.0;
}
public static double radians2degrees(double radians) {
	return radians * 180.0 / Math.PI;
}
/**
@return a new instance using the no arguments constructor of the class
object is an instance of
*/
public static Object newInstance(Object object) {
	Object r = null;
	try {
    r = object.getClass().newInstance();
  } catch (Exception exception) {Error.fatal(exception);}
  return r;
}
/**
@return a new instance using the no arguments constructor of the class
className
*/
public static Object newInstance(String className) {
	Object r = null;
	try {
		Class c = Class.forName(className);
    r = c.newInstance();
  } catch (Exception exception) {Error.fatal(exception);}
  return r;
}
/**
probably doesn't work. Don't use.
@return a new instance with the same class as instance. Use the instructor that takes
parameter argument
*/
public static Object newInstance(Object instance, Object argument) {
    try {
        Class c = instance.getClass();
        Class[] classArray = {argument.getClass()};
        Object[] argumentsArray = {argument};
        Constructor constructor = c.getConstructor(classArray);
        return constructor.newInstance(argumentsArray);
    } catch (Exception e) {Error.fatal("Can't make instance: " + e);}
    return null;
}
public static boolean string2boolean(String string) {
  if (string.equals("true"))
  	return true;
  if (string.equals("false"))
  	return false;
  Error.fatal("bad boolean format: " + string);
  return true;
}
public static int string2integer(String string) {
  java.lang.Integer integer = new java.lang.Integer(string);
  return integer.intValue();
}
public static long string2long(String string) {
  java.lang.Long integer = new java.lang.Long(string);
  return integer.longValue();
}
public static double string2double(String string) {
  try {
  	java.lang.Double real = new java.lang.Double(string);
  	return real.doubleValue();
  } catch (java.lang.NumberFormatException exception) {}
  return Double.POSITIVE_INFINITY; //BUG: should figure out what it really is
}
public static double[] toArrayOfDoubleValues(Vector ofDoubles) {
  double[] array = new double[ofDoubles.size()];
  for(int i = 0; i < array.length; i++)
    array[i] = Utility.doubleValue(ofDoubles,i);
  return array;
}
public static double doubleValue(Vector ofDoubles,int index) {
  return doubleValue(ofDoubles.elementAt(index));
}
public static double doubleValue(Object aDouble) {
  return ((Double)aDouble).doubleValue();
}
public static boolean isOdd(int i) {return (java.lang.Math.abs(i)&1) == 1;}
public static boolean isEven(int i) {return !isOdd(i);}
/**
@param table a hashtable with String keys
@return an array of strings containing the keys to table. They better really be strings!
*/
public static String[] getStringKeys(Hashtable table) {
    Vector v = new Vector();
    for (Enumeration e = table.keys(); e.hasMoreElements();) {
        String s = (String)e.nextElement();
	v.addElement(s);
    }
    String[] array = new String[v.size()];
    v.copyInto(array);
    return array;
}
public static Class getClass(String name) {
	Class c = null;
  try {
  	c = Class.forName(name);
  } catch (java.lang.ClassNotFoundException exception) {
  	Error.fatal("Can't find class " + name);
  }
  return c;
}
/**
  probably doesn't work. Don't use.
@param fullClassName a (potentially qualified) class name
@return the class name without package information
*/
public static String simpleClassName(String fullClassName){
    int index = fullClassName.lastIndexOf('.');
    if (index == -1) 
        return fullClassName;
    else
        return fullClassName.substring(index+1, fullClassName.length());
}
/**
@return the string used to separate lines on the current machine
*/
public static String lineSeparator(){
    return System.getProperty ("line.separator");
}
/**
@return the string used to separate filenames and directories on the current machine
*/
public static String fileSeparator(){
    return System.getProperty ("file.separator");
}
/**
@return "/"
*/
public static String stringSeparator(){return "/";}
/**
sort array using a bubble sort
*/
public static void sortArray (long[] array){
    boolean sorted = false;
    while (!sorted) {
		sorted = true;
		for (int i = 0; i + 1 < array.length; i++){
			if (array[i] > array[i + 1]) {
				long temporary = array[i];
				array[i] = array[i + 1];
				array[i + 1] = temporary;
				sorted = false;
			}
		}
    }
}
public static String toString(Object[] array) {
    String s = "";
    for(int i = 0; i < array.length; i++) {
        s += array[i];
        if (i != array.length-1)
            s += "\n";
    }
    return s;
}
public static String toString(boolean[] array) {
    String s = "";
    for(int i = 0; i < array.length; i++) {
        s += array[i];
        if (i != array.length-1)
            s += " ";
    }
    return s;
}
public static String toString(int[] array) {
    String s = "";
    for(int i = 0; i < array.length; i++) {
        s += array[i];
        if (i != array.length-1)
            s += " ";
    }
    return s;
}
public static String toString(double[][][][] array) {
    String s = "";
	for(int i = 0; i < array.length; i++)
		s += toString(array[i]) + "\n";
	return s;
}
public static String toString(double[][][] array) {
    String s = "";
	for(int i = 0; i < array.length; i++)
		s += toString(array[i]) + "\n";
	return s;
}
public static String toString(double[][] array) {
    String s = "";
	for(int i = 0; i < array.length; i++)
		s += toString(array[i]);
	return s;
}
public static String toString(double[] array) {
    String s = "";
    for(int i = 0; i < array.length; i++) {
        s += array[i];
        if (i != array.length-1)
            s += "\t";
    }
	s += "\n";
    return s;
}

public static boolean equals(int[] a, int[] b) {
    if (a.length != b.length)
        return false;
    for(int i = 0; i < a.length; i++)
        if (a[i] != b[i])
            return false;
    return true;
}
/**
sort array using a bubble sort
*/
public static void sort (int[] array){
    boolean sorted = false;
    while (!sorted) {
	sorted = true;
	for (int i = 0; i + 1 < array.length; i++){
	    if (array[i] > array[i + 1]) {
		    int temporary = array[i];
		    array[i] = array[i + 1];
		    array[i + 1] = temporary;
		    sorted = false;
	    }
	}
    }
}
/*
@return true if array always increases as index increases
*/
public static boolean isAscending(int[] array) {
    for(int i = 1; i < array.length; i++) 
        if (array[i-1] >= array[i])
            return false;
    return true;
}

public static void sortVectorOfDouble (Vector vector){
    boolean sorted = false;
    while (!sorted) {
	  sorted = true;
	  for (int i = 0; i + 1 < vector.size(); i++){
	    if (((Double)vector.elementAt(i)).doubleValue() > ((Double)vector.elementAt(i + 1)).doubleValue()) {
		    Object temporary = vector.elementAt(i);
		    vector.setElementAt(vector.elementAt(i + 1),i);
		    vector.setElementAt(temporary,i + 1);
		    sorted = false;
	    }
	  }
  }
}
public static ExtendedVector makeVector(double[] array) {
  ExtendedVector vector = new ExtendedVector();
  for(int i = 0; i < array.length; i++) {
    Double d = new Double(array[i]);
    vector.addElement(d);
  }
  return vector;
}

/**
@param v a vector of strings
@return a string containing each string in v separated by tabs
*/
public static String tabSeparatedString (Vector v) {
  String s = "";
  for(int i = 0; i < v.size(); i++){
    s += v.elementAt(i) + "";
	  if (i != v.size() - 1)
      s += "\t";
  }
  return s;
}
public static String arrayToTabSeparated(String[] array) {
  String s = "";
  for(int i = 0; i < array.length; i++){
    s += array[i];
	  if (i != array.length - 1)
      s += "\t";
  }
  return s;
}
public static String arrayToTabSeparated(double[] array) {
  String s = "";
  for(int i = 0; i < array.length; i++){
    s += array[i] + "";
	  if (i != array.length - 1)
      s += "\t";
  }
  return s;
}
public static void makeFileNoCR(String filename, String contents) {
    PrintWriter file = IO.getPrintWriter(filename);
	file.print(contents);
    file.close();
}

/**
make a file with a string in it, end with an additional "\n"
@param message the string to put in the file
*/
public static void makeFile (String filename, String message) {
    String[] contents = {message};
    makeFile(filename,contents);
}
/**
make a file with one line per string in it
@param contents the strings to put in the file
*/
public static void makeFile(String filename, String[] contents) {
    PrintWriter file = IO.getPrintWriter(filename);
    for(int i = 0; i < contents.length; i++)
        file.println(contents[i]);
    file.close();
}
static public void debugPrint(String s) {System.out.print(s);}
static public void debugPrintln(String s) {System.out.println(s);}
/**
@return filename open for writing.
*/
public static PrintWriter outputFile (String filename) {
    PrintWriter file = IO.getPrintWriter(filename);
    return file;
}
public static void removeFile (String filename) {
  IO.removeFile(filename);
}

/**
is this number a regular number?
I.e., not NaN or infinity.
*/
public static boolean normalNumber (double x) {
    if (Double.isNaN(x)) return false;
    if (Double.POSITIVE_INFINITY == x) return false;
    if (Double.NEGATIVE_INFINITY == x) return false;
    return true;
}
public static boolean hasNormalNumbers(Vector3d v) {
	return normalNumber(v.x) && normalNumber(v.y) && normalNumber(v.z);
}
/**
does d contain any numbers that aren't normal?
*/
public static boolean containsBadNumbers(double d[]) {
    for(int i = 0; i < d.length; i++){
        if (!normalNumber (d[i])) return true;
    }
    return false;
}
/**
@return the largest absolute value in Delta
*/
public static double largestAbsoluteValue (double d[]) {
    double value = 0;
    for(int i = 0; i < d.length; i++){
        if (value < Math.abs(d[i])) value = Math.abs(d[i]);
    }
    return value;
}

/**
convert a double to something that is a specific number.
NaN Becomes 0
positive infinity becomes maximum value
negative infinity becomes minimum value
*/
public static double fixDouble (double x) {
    if (Double.isNaN(x)) return 0;
    if (Double.POSITIVE_INFINITY == x) return Double.MAX_VALUE;
    if (Double.NEGATIVE_INFINITY == x) return Double.MIN_VALUE;
    return x;
}
/**
@param d the name of the directory to make
*/
public static void makeDirectory (String d) {
  IO.makeDirectory(d);
}
/**
@return x raised to the p
*/
public static double power (double x, int p) {
    int exponent;
    if (p < 0) exponent = -1 * p;
    else exponent = p;

    double d = 1;
    for (int i = 0; i < exponent; i++) d *= x;

    if (p < 0 && d != 0) return 1/d;
    else return d;
}
/**
@return the date as a string
*/
public static String date () {
 	Date date = new Date ();
	return date.toString();
/*
  SimpleDateFormat string = new SimpleDateFormat ();
  return string.format(date);
*/
}
/**
 serialize object and put it in filename
*/
public static void serialize (Serializable object, String filename) {
try { 
        String temporaryFilename = filename + "_temporary";
        FileOutputStream f = new FileOutputStream(temporaryFilename);
        ObjectOutputStream  s  =  new  ObjectOutputStream(f);
        s.writeObject(object);
        s.flush();
        s.close();
    
        File permanent = new File(filename);
        File temporary = new File(temporaryFilename);
        if (!temporary.renameTo(permanent)) // minimizes time process failure creates corrupt file
            Error.warning("Utility.serialize()","couldn't re-name " + temporaryFilename);
    } catch (IOException e) {
        Error.fatal ("serialization error in file " + filename + ": " + e);
    }
}
/**
@return the object serialized in filename
*/
public static Object getSerialized (String filename) {
try { 
    FileInputStream f = new FileInputStream(filename);
    ObjectInputStream  s  =  new  ObjectInputStream(f);
    Object o = s.readObject();
    s.close();
    return o;
    } catch (IOException e){
        Error.fatal ("serialization error in file " + filename + ": " + e);
    } catch (ClassNotFoundException e){
        Error.fatal ("serialization error in file " + filename + ": " + e);
    }
    return null; // should never happen
}
/**
@return the object serialized in url
*/
public static Object getSerialized (URL url) {
try { 
    InputStream f = url.openStream();
    ObjectInputStream  s  =  new  ObjectInputStream(f);
    Object o = s.readObject();
    s.close();
    return o;
    } catch (IOException e){
        Error.fatal ("serialization error in url " + url + ": " + e);
    } catch (ClassNotFoundException e){
        Error.fatal ("serialization error in url " + url + ": " + e);
    }
    return null; // should never happen
}
/**
sets the permissions of file to permissions using chmod. Doesn't seem to work on IRIX.
*/
public static void setPermissions (String file, String permissions) {
    try {
        Process child = Runtime.getRuntime().exec ("chmod " + permissions + " " + file);
        child.waitFor();
    } 
    catch (InterruptedException e) {Error.fatal ("chmod problem " + e + " filename " + file);}
    catch (IOException e) {Error.fatal ("chmod problem " + e + " filename " + file);}
}

/**
Make a number that, when used as a filename, will print
out sequential with others (i.e., is in ASCII order as
well as numerical order) by inserting leading zeros. Only works for numbers up to
999,999
*/
public static int makeSequentialNumber(int i) { return i + 1000000;}

}
