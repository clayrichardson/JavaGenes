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
public class WeightedStatistics implements Serializable{
protected double sum = 0;
protected double sumSquares = 0;
protected double weightSum = 0;
protected int count = 0;
protected int countOfNonNormalNumbers = 0;
protected double min = Double.MAX_VALUE; // looks at getSpread() if you change this
protected double max = -Double.MAX_VALUE;

/**
add datum d to the sample
*/
public void addDatum (double value) {addDatum(value,1);}
public void addDatum(double value, double weight) {
    if (Utility.normalNumber(value)) {
		sum += value * weight;
		sumSquares += value * value * weight;
		weightSum += weight;
		if (weight != 0) {
			count++;
			double weightedValue = value * weight;
			if (weightedValue < min)
				min = weightedValue;
			if (weightedValue > max)
				max = weightedValue;
		}
	} else
		countOfNonNormalNumbers++;
}
public void addData(double[] values) {
	for(int i = 0; i < values.length; i++)
		addDatum(values[i]);
}
public void addData(double[] values, double[] weights) {
	Error.assertTrue(values.length == weights.length);
	for(int i = 0; i < values.length; i++)
		addDatum(values[i], weights[i]);
}
/**
@return statistics in tab separated format
*/
public String statisticsString(){
    String s = "\t";
    return
        getMean() + s +
        getRMS() + s +
		getMin() + s +
		getMax() + s +
		getSumOfWeights() + s +
        getN();
}

public double getWeightedSum() {return sum;}
public double getSumOfWeights() {return weightSum;}
public double getMean() {
	if (weightSum <= 0)
		return 0;
    return sum/weightSum;
}
/** of values with non-zero weight */
public double getMin() {
    return min;
}
/** of values with non-zero weight */
public double getMax(){
    return max;
}
public double getSpread() {
	if (min == Double.MAX_VALUE)
		return 0;
	return max - min;
}
public double getRMS() {
	if (weightSum <= 0)
		return 0;
    return Math.sqrt(sumSquares/weightSum);
}
/** @return number of entries with non-zero weight */
public int getN() {return count;}
public int getBadNumberCount() {return countOfNonNormalNumbers;}
}
