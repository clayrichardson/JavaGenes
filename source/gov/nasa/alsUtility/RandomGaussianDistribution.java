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


/**
this class produces random numbers from an interval according to a Gaussian distribution.
*/
public class RandomGaussianDistribution extends Distribution{
/**
the number of numbers in the distribution.  Actually, this is ignored by the code.
*/
protected int size;
/**
the interval from within which random numbers are produced
*/
protected DoubleInterval interval;
/**
center of the Gaussian distribution
*/
protected double center;
/**
standard deviation of the Gaussian distribution
*/
protected double standardDeviation;
/**
@param s the size of the distribution
@param i the interval
@param c center of the Gaussian
@param sd standard deviation of the Gaussian
*/
public RandomGaussianDistribution(int s, DoubleInterval i, double c, double sd){
	size = s;
  interval = new DoubleInterval(i);
  center = c;
  standardDeviation = sd;
}
public int size() {return size;}
/**
@return a random number from within the interval
@param i for this particular distribution class, this is ignored
*/
public double get(int i) {return interval.randomGaussian(center,standardDeviation);}
}