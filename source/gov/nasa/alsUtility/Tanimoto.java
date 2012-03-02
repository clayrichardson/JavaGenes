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

import java.util.Enumeration;
import java.math.BigInteger;

/**
calculates the Tanimoto coefficient for different data structures. The Tanimoto coefficient
for sets a and b is 1.0 - (|a|/|b|)
*/
public class Tanimoto {
/**
@return 1.0 maximum/minimum
*/
public static double distance(double a, double b) {
	double unionSize = Math.max(a,b);
	double intersectionSize = Math.min(a,b);
	return calculate(unionSize,intersectionSize);
}
/**
@return 1.0 - (|a|/|b|) considering the count of each element in a and b
*/
public static double distance(KeyCounter a, KeyCounter b) {
    double intersectionSize = 0;
    double unionSize = 0;
    for (Enumeration e = a.keys(); e.hasMoreElements();) {
         Object key = e.nextElement();
         BigInteger i1 = a.getCount(key);
         BigInteger i2 = b.getCount(key);
         unionSize += i1.max(i2).doubleValue();
         intersectionSize += i1.min(i2).doubleValue();
    }
    for (Enumeration e = b.keys(); e.hasMoreElements();) {
         Object key = e.nextElement();
         if (!a.containsKey(key))
            unionSize += b.getCount(key).doubleValue();
    }
	return calculate(unionSize,intersectionSize);
}
/**
@return Tanimoto coefficient handling 0 appropriately
*/
protected static double calculate(double unionSize, double intersectionSize) {
    if (intersectionSize == unionSize) return 0;
    if (unionSize == 0) return 0;
    if (intersectionSize == 0) return 1;
    return 1.0 - ((double)intersectionSize/(double)unionSize);
}
}
