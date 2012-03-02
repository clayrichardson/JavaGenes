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

import java.io.Serializable;
import java.lang.Integer;
import gov.nasa.alsUtility.Error;

/**
represent an interval from some low to some high integer
*/
public class IntegerInterval implements Serializable {
protected int bottom = 0;
protected int top = 1;

/**
create an interval from 0 to 1
*/
public IntegerInterval() {}
/**
create an interval from x to y or y to x depending on which is lower
*/
public IntegerInterval (int x, int y) { set (x, y);}
public IntegerInterval (IntegerInterval interval) { set(interval.low(),interval.high());}

/** 
@return this limited to interval
*/
public IntegerInterval limitTo(IntegerInterval interval) {
    if (top > interval.top) 
        top = interval.top;
    if (bottom < interval.bottom) 
        bottom = interval.bottom;
    return this;
}

/**
change the interval of this by a factor keeping the center of the interval the same.
*/
public void expand (int factor) {
    int i = interval();
    top    += factor*i;
    bottom -= factor*i;
}
public IntegerInterval copy() {
    return new IntegerInterval(bottom,top);
}
/**
sets the interval from x to y or y to x depending on which is lower
*/
public void set (int x, int y) {
    if (x > y) {bottom = y; top = x;}
    else       {bottom = x; top = y;}
}
public void setLow(int x) {
    bottom = x;
    assertLegal();
}
public void setHigh(int x) {
    top = x;
    assertLegal();
}
public void addToHigh(int x) {
	top += x;
	assertLegal();
}
public void addToLow(int x ) {
	bottom += x;
	assertLegal();
}
public boolean isNonNegative() {
	return low() >= 0 && high() >= 0;
}
protected void assertLegal() {Error.assertTrue(bottom <= top);}
public boolean isBetween(int value) {return bottom <= value && value <= top;}
/** @return 0 if value within the interval, negative distance if value below interval, positive distance if value above interval */
public int distanceFrom(int value) {
	if (isBetween(value))
		return 0;
	if (value < bottom)
		return value - bottom;
	return value - top;
}
/**
sets the interval to the low and high values of array
*/
public void setToExtremes(int[] array) {
    bottom = Integer.MAX_VALUE;
    top = -Integer.MAX_VALUE;
    for(int i = 0; i < array.length; i++){
        if (bottom > array[i]) bottom = array[i];
        if (top < array[i]) top = array[i];
    }
}
public boolean equalsExtremes(int value) {
	return value == low() || value == high();
}
	
public String toString() {return bottom + "," + top;}
/**
@return a random interval between bottom and top inclusive
*/
public int random() {return RandomNumber.getInteger(this);}
/** 
for good performance, number must be fairly small and much smaller than intervalInclusive() 
@return  an array of unique integers within this
*/
public int[] getRandomUniqueIndices(int number) {
	Error.assertTrue(number <= intervalInclusive());
	Error.assertTrue(number > 0);
	int[] indices = new int[number];
	for(int i = 0; i < indices.length; i++){
		indices[i] = random(); 
		if (!Utility.areDifferent(indices,i))
			i--; // try again
	}
	return indices;
}
public int low() {return bottom;}
public int high() {return top;}
/**
@return the distance between top and bottom
*/
public int interval() {return top - bottom;}
public int intervalInclusive() {return interval()+1;}
public int[] getArrayOfAllValues() {
	int[] array = new int[intervalInclusive()];
	for(int i = 0; i < array.length; i++)
		array[i] = low() + i;
	return array;
}
/**
@return a value that will divide the interval into division increments, or
as close as possible.
*/
public int equalIncrements (int divisions) {
    Error.assertTrue (divisions > 1);
    return interval()/(divisions-1);
}
public boolean equals(Object obj) {
	IntegerInterval other = (IntegerInterval)obj;
	return top == other.top && bottom == other.bottom;
}
public int hashCode() {
	return top + bottom; // DEFICIENCY: not the best hash
}
}
