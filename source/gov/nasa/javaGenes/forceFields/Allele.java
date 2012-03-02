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

import java.io.Serializable;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.DoubleInterval;

/**
Used to control a single double value in a chromosome.  Includes a name
and the minimum and maximum allowed values (in a DoubleInterval)
*/
public class Allele implements Serializable {
protected String name = "default";
protected boolean noEvolution = false;
protected double valueToSet = 0.0; // used when noEvolution is true
/**
holds the maximum and minimum values for this allele
*/
protected DoubleInterval interval = new DoubleInterval(); //minimum and maximum

public Allele(DoubleInterval i) {
    interval.set(i);
    Error.assertTrue(isLegal());
}
public Allele(double low, double high) {
    interval.set(low,high);
    Error.assertTrue(isLegal());
}
/**
@param n name of the allele
*/
public Allele(String n, DoubleInterval i) {
	this(i);
  name = n;
}
public DoubleInterval getInterval() {return interval;}
public void setNoEvolution(double value) {
  noEvolution = true;
  valueToSet = value;
  setInterval(value);
}
public boolean dontEvolve() {return noEvolution;}
public double getNoEvolutionValue() {return valueToSet;}

/**
set the interval to only include value
*/
public void setInterval(double value) {
  interval.set(value,true,value,true);
}
/**
@param value is value within the allowed interval?
*/
public boolean valueFits(double value) {
  return interval.isBetween(value);
}
/**
is there any value that can fit in the interval?
*/
public boolean isLegal() {
	return !interval.isEmpty();
}
/**
@return a random value within the interval (flat distribution)
*/
public double getRandomValue(){
    Error.assertTrue(isLegal());
    return interval.random();
}
public double getRandomValueAbove(double value) {
    Error.assertTrue(valueFits(value));
    DoubleInterval i = new DoubleInterval(interval);
    i.setLow(value);
    return i.random();
}
public double getRandomValueBelow(double value) {
    Error.assertTrue(valueFits(value));
    DoubleInterval i = new DoubleInterval(interval);
    i.setHigh(value);
    return i.random();
}
/**
@return a random value within the interval (log distribution)
*/
public double getRandomLogValue(){
	Error.assertTrue(isLegal());
  return interval.randomLogFromBothEnds();
}
/**
@return a random value within the interval (Gaussian distribution)
@param center the center of the Gaussian curve
@param sd standard deviation as a fraction of the interval
*/
public double getRandomGaussianValue(double center, double sd) {
	Error.assertTrue(isLegal());
  return interval.randomGaussian(center,sd*interval.interval());
}
public String getName() {return name;}
public String toString() {
	return name + ": " + interval;
}
} 