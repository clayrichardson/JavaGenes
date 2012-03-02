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
import java.lang.Double;
import java.util.Vector;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;

/**
implements an interval between two double values
*/
public class DoubleInterval implements Serializable {
/**
the lowest value in the interval
*/
protected double bottom = 0;
/**
the highest value in the interval
*/
protected double top = 1;
/**
does this interval include the bottom most value?
*/
protected boolean includeBottom = true;
/**
does this interval include to most value?
*/
protected boolean includeTop = true;

/**
create an interval 0 to 1
*/
public DoubleInterval() {}
/**
create an interval with between x and y. Will figure out which is lowest.
*/
public DoubleInterval (double x, double y) { set (x, y);}
public DoubleInterval (double x, boolean includeX, double y, boolean includeY) {
  set(x, includeX, y, includeY);
}
public DoubleInterval (DoubleInterval i) {
  set(i);
}
public DoubleInterval(Vector ofDoubles) {
  Error.assertTrue(ofDoubles.size() > 0);
  setToExtremes(ofDoubles);
}
public DoubleInterval(double[] ofDoubles) {
  Error.assertTrue(ofDoubles.length > 0);
  setToExtremes(ofDoubles);
}
public boolean isPositive() {
  return top > 0 && bottom > 0;
}
/**
change the interval of this by a factor keeping the center of the interval the same.

@param factor the factor by which to change the size of the interval. factor < 1 will
shrink the interval. factor > 1 will expand the interval.  Negative factors
with be converted to positive.
*/
public void expand (double factor) {
	double newInterval = interval() * Math.abs(factor);
	set(midPoint()-newInterval/2,midPoint()+newInterval/2);
}
/** @arg expandBy will be converted to positive so this is always an expansion of the interval */
public void expandByAddition(double expandBy) {
	double expansion = Math.abs(expandBy);
    top    += expansion;
    bottom -= expansion;
}
public double midPoint() {return low() + interval()/2;}
/**
 set the high and low values of the interval. Figures out which is lowest.
*/
public DoubleInterval set (double x, double y) {
    if (x > y) {bottom = y; top = x;}
    else       {bottom = x; top = y;}
  return this;
}
public DoubleInterval setLow(double v) {
    Error.assertTrue(top >= v);
    bottom = v;
    return this;
}
public DoubleInterval setHigh(double v) {
    Error.assertTrue(bottom <= v);
    top = v;
    return this;
}
/**
make this equal to interval
*/
public DoubleInterval set(DoubleInterval interval) {
	top = interval.top;
  bottom = interval.bottom;
  includeTop = interval.includeTop;
  includeBottom = interval.includeBottom;
  return this;
}
public DoubleInterval set(double x, boolean includeX, double y, boolean includeY) {
	set(x,y);
  if (x == bottom) {
    includeBottom = includeX;
    includeTop = includeY;
  } else {
  	Error.assertTrue(y == bottom);
    includeBottom = includeY;
    includeTop = includeX;
  }
  return this;
}
public DoubleInterval limitTo(DoubleInterval interval) {
    if (top >= interval.top) {
        top = interval.top;
        includeTop = interval.includeTop || includeTop;
    }
    if (bottom <= interval.bottom) {
        bottom = interval.bottom;
        includeBottom = interval.includeBottom || includeBottom;
    }
    set(top,bottom); // necessary for case when floating point imprecision reverses top and bottom
    return this;
}
public boolean nearlyEqual(DoubleInterval other) {
	return includeBottom == other.includeBottom
		&& includeTop == other.includeTop
		&& Utility.nearlyEqual(top, other.top)
		&& Utility.nearlyEqual(bottom,other.bottom);
}
public double limitTo(double value) {
    if (value > top)
        return top;
    if (value < bottom)
        return bottom;
    return value;
}
public boolean isBetween(double value) {
	if (value == bottom && value == top) return includeBottom || includeTop;
	if (value == bottom) return includeBottom;
  if (value == top) return includeTop;
  return bottom < value && value < top;
}
/** how far outside of the interval is value? */
public double distanceFrom(double value) {
	if (isBetween(value))
		return 0;
	if (value <= low())
		return value - low();
	if (value >= high())
		return value - high();
	return 0; // shouldn't happen, but numerical error could rear it's ugly head
}
/**
are there any numbers that fall within this interval?
*/
public boolean isEmpty() {
	if (top < bottom) return true;
  if (top > bottom) return false;
  return !(includeBottom || includeTop);
}

/**
set the bounds to the extreme values in array
*/
public void setToExtremes(double[] array) {
		includeTop = true;
    includeBottom = true;
    bottom = Double.MAX_VALUE;
    top = -Double.MAX_VALUE;
    for(int i = 0; i < array.length; i++)
      extendIfNecessary(array[i]);
}
public void setToExtremes(Vector ofDoubles) {
  double[] array = Utility.toArrayOfDoubleValues(ofDoubles);
  setToExtremes(array);
}
public void extendIfNecessary(double d) {
        if (bottom > d) bottom = d;
        if (top < d) top = d;
}
public String toString() {return bottom + "," + top;}
/**
choose a random number in the interval (including the high and low values)
*/
public double random() {
	Error.assertTrue(!isEmpty());
	double value;
  while(true) {
    value = RandomNumber.getDouble(this);
    if (isBetween(value)) return value;
  }
}
/**
choose a random number in the interval (including the high and low values)
from a log distribution whether you start from the top or the bottom
*/
public double randomLogFromBothEnds() {
	Error.assertTrue(!isEmpty());
	double value = randomLog();
  if (RandomNumber.getBoolean())
  	return value;
  else {
  	value =  (top - value) + bottom;
  	if (isBetween(value))
    	return value;
    else
    	return randomLogFromBothEnds();
  }
}
/**
choose a random number in the interval from a Gaussian distribution
@param center the mean of the Gaussian
@param sd the standard deviation of the Gaussian
*/
public double randomGaussian(double center, double sd) {
    Error.assertTrue(!isEmpty());
    double value;
    int count = 0;
    do {
            value = RandomNumber.getGaussian();
            value *= sd;
            value += center;
    if ( count++ > 100) 
        return limitTo(value); // used because floating point imprecision can result in infinite loop when interval small
    } while(!isBetween(value));
    return value;
}
/**
choose a random number in the interval (including the high and low values)
from a log distribution
*/
public double randomLog() {
	Error.assertTrue(!isEmpty());
	DoubleInterval interval = new DoubleInterval(this);
  double translation = 0;
  if (interval.bottom <= 0) {
  	translation = 1 - interval.bottom;
  	interval.translate(translation);
  }
  interval.setToLog();
  double log = interval.isEmpty() ? interval.bottom : interval.random();
  double temp = Math.exp(log);
  double value = temp - translation;
  return value;
}
/** interpolate or exterpolate to a 0-1 space */
public double terpolate0to1(double value) {
	Error.assertTrue(interval() != 0);
	return (value - low())/interval();
}
/**
add distance to the top and bottom
*/
public DoubleInterval translate(double distance) {
	bottom += distance;
  top += distance;
  bottom = Utility.fixDouble(bottom);
  top = Utility.fixDouble(top);
  return this;
}
/**
convert the top and bottom values to their natural logarithm
*/
public DoubleInterval setToLog() {
	Error.assertTrue(top >= 0);
  Error.assertTrue(bottom >= 0);
  top = Math.log(top);
  bottom = Math.log(bottom);
  return this;
}
/**
@return bottom
*/
public double low() {return bottom;}
/**
@return top
*/
public double high() {return top;}
/** @arg 0 is low(), 1 return high() */
public double get(int i) { 
	switch(i) {
		case 0: return low();
		case 1: return high();
		default: Error.fatal("bad case in DoubleInterval.get(int)"); return -1;
	}
}
	
public double interval() {
  return top - bottom;
}
/**
@return a value that will divide the interval into division increments
*/
public double equalIncrements (int divisions) {
    Error.assertTrue (divisions > 1);
    return interval()/(divisions-1);
}
}
