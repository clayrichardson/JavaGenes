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


import java.lang.Double;
import java.util.Vector;
import java.io.Serializable;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;

/**
holds doubles (called "datums") from a statistical sample and does simple statistics on them
*/
public class Sample implements Serializable{
protected Vector data = new Vector();
protected boolean statisticsCorrect = true;
protected double mean = Double.NaN;
protected double variance = Double.NaN;
protected double standardDeviation = Double.NaN;
protected DoubleInterval range = new DoubleInterval(Double.MAX_VALUE,Double.MAX_VALUE); 
//protected boolean isGaussian = false;

/**
add datum d to the sample
*/
public void addDatum (double d) {
    statisticsCorrect = false;
    if (Utility.normalNumber(d))
        data.addElement (new Double(d));
}
/**
@return the ith datum
*/
public double getDatum(int i) {return datum(i);}
/**
@return the ith datum
*/
public double datum(int i) {
  Error.assertTrue(data.size() > i && i >= 0);
  return ((Double)data.elementAt(i)).doubleValue();
}
/**
divides every value with by
@param by value to divide every datum with
*/
public void normalize (double by) {
    for(int i = 0; i < data.size(); i++){
        data.setElementAt (new Double(datum(i)/by), i);
    }
}
/**
is there enough data to calculate descriptive statistics?
*/
public boolean valid(){
    return data.size() >= 2;
}
/**
@return statistics in tab separated format
*/
public String statisticsString(){
    calculate();
    String s = "\t";
    return
        mean + s +
	variance + s +
	standardDeviation + s +
	range.low() + s +
	range.high() + s +
        getN();
}
public static String getHeaderString() {
	return "mean\tvariance\tstandardDeviation\tmin\tmax\tnumber";
}
public static String toString(Sample[] array) {
	String s = getHeaderString() + "\n";
	for(int i = 0; i < array.length; i++)
		s += array[i].statisticsString() + "\n";
	return s;
}
public static String toString(Sample[][] array) {
	String s = "";
	for(int i = 0; i < array.length; i++)
		s += toString(array[i]) + "\n";
	return s;
}

/**
calculate (if necessary) and cache all statistics
*/
public void calculate(){
    if (statisticsCorrect) 
        return;
    else {
        // convert the data into a java array
        double[] d = new double[data.size()];
        for(int i = 0; i < data.size(); i++){
            d[i] = datum(i);
    }
    
    range = new DoubleInterval(0,0);
    mean = 0;
    variance = 0;
    standardDeviation = 0;
    statisticsCorrect = true;	
    
    if (d.length == 0)
        return;
    
    range.setToExtremes (d);
    
    mean = 0;
    for(int i = 0; i < d.length; i++)
        mean += d[i];
    mean /= d.length;
    
    variance = 0;
    for(int i = 0; i < d.length; i++)
        variance += (d[i] - mean) * (d[i] - mean);
    variance /= d.length;

    standardDeviation = Math.sqrt (variance);	
}
}
/**
@return number of datums
*/
public int getN(){return data.size();}

public double getMean() {
    calculate();
    return mean;
}

public double getStandardDeviation() {
    calculate();
    return standardDeviation;
}

public double getVariance() {
    calculate();
    return variance;
}

public double getRange() {
    calculate();	
    return range.interval();
}

public double getLow(){
    calculate();
    return range.low();
}

public double getHigh(){
    calculate();
    return range.high();
}
}
