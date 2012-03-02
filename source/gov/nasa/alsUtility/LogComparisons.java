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

/* developed for use by gov.nasa.javaGenes.core.ChildMaker.java to keep track of success and failure rate */
public class LogComparisons implements java.io.Serializable {
protected String[] names = new String[0];
protected int[] up = new int[0];
protected int[] between = new int[0];
protected int[] down = new int[0];
protected double[] netChange = new double[0];

public LogComparisons(String[] names) {
	this.names = names;
	up = new int[names.length];
	down = new int[names.length];
	between = new int[names.length];
	netChange = new double[names.length];
}
public int getSize() {
	return names.length;
}
/** for testing only */
public boolean checkResults(int index, int upv, int downv, int betweenv) {
	Error.assertTrue(0 <= index && index < names.length);
    return up[index] == upv && down[index] == downv && between[index] == betweenv;
}
/** for testing only */
public void setResults(int index, int upv, int downv, int betweenv) {
	Error.assertTrue(0 <= index && index < names.length);
	up[index] = upv;
	down[index] = downv;
	between[index] = betweenv;
}

public void results(double[] values, double[][] compareTo) {
    Error.assertTrue(names.length == values.length);
    Error.assertTrue(names.length == compareTo.length);
	DoubleInterval extent = new DoubleInterval();
	for(int v = 0; v < values.length; v++) {
		extent.setToExtremes(compareTo[v]);

		if (values[v] < extent.low()) {
			down[v]++;
			netChange[v] += values[v] - extent.low();
		} else if (extent.isBetween(values[v])) {
			between[v]++;
		} else if (values[v] > extent.high()) {
			up[v]++;
			netChange[v] += values[v] - extent.high();
		} else
			Error.fatal("should never happen");
    }
}
// coordinate with headerFragment()
public String getTabSeparatedResults() {
    if (names == null)
        return "";
    String results = "\t";
    for(int i = 0; i < names.length; i++) {
		int total = up[i] + down[i] + between[i];
		results += netChange[i] + "\t";
        results += up[i] + "\t";
        results += down[i] + "\t";
        results += between[i] + "\t";
		results += total + "\t";
		results += getUpFraction(i) + "\t";
		results += getDownFraction(i) + "\t";
		results += getBetweenFraction(i) + "\t";
    }
    return results;
}
public int getN(int i) {return up[i] + down[i] + between[i];}
public double getUpFraction(int i) {if (getN(i) == 0) return -1; return (double)up[i] / (double)getN(i);}
public double getDownFraction(int i) {if (getN(i) == 0) return -1; return (double)down[i] / (double)getN(i);}
public double getBetweenFraction(int i) {if (getN(i) == 0) return -1; return (double)between[i] / (double)getN(i);}
public double getUpFranction() {if (getN() == 0) return -1; return (double)getUp() / (double)getN();}
public double getDownFranction() {if (getN() == 0) return -1; return (double)getDown() / (double)getN();}
public double getBetweenFranction() {if (getN() == 0) return -1; return (double)getBetween() / (double)getN();}
public int getN() {return Utility.arraySum(down) + Utility.arraySum(up) + Utility.arraySum(between);}
public int getUp() {return Utility.arraySum(up);}
public int getDown() {return Utility.arraySum(down);}
public int getBetween() {return Utility.arraySum(between);}
public String[] getNames() {return names;}

// coordinate with tabSeparatedResults()
public String getHeaderFragment() {return "\tup\tdown\tbetween\ttotal\tupFraction\tdownFraction\tbetweenFraction";}
public void clear() {
    for(int i = 0; i < names.length; i++) {
		netChange[i] = 0;
        up[i] = 0;
        down[i] = 0;
        between[i] = 0;
    }
}
/** used to scale down current values for evolution of ChildMakers */
public void scaleBy(double scale) {
	Error.assertTrue(scale >= 0);
    for(int i = 0; i < names.length; i++) {
		netChange[i] *= scale;
        up[i] = scaleIntBy(up[i],scale);
        down[i] = scaleIntBy(down[i],scale);
        between[i] = scaleIntBy(between[i],scale);
    }
}
protected int scaleIntBy(int value, double scale) {
	return Math.max(0,(int)Math.floor(value*scale));
}
public String header(String[] intro) {
	String s = "";
	for(int i = 0; i < intro.length; i++)
		s += "\t" + intro[i];
    for(int i = 0; i < names.length; i++)
        s += "\t" + names[i] + "\t" + getHeaderFragment();
	return s;
}
public String toString() {return "LogComparisons";}
}
