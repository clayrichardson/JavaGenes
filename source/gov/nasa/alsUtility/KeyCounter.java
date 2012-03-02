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


import java.util.Hashtable;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.integer;
import java.util.Enumeration;
import java.math.BigInteger;

/**
used to count the number of several different kinds of objects. The different kinds
are represented by different keys. Uses integer objects for counting.

@see integer
*/
public class KeyCounter extends Hashtable implements java.io.Serializable {
/**
overrides Hashtable.put to insure that the element is always a integer object

@see integer
*/
public synchronized Object put(Object key, Object element) {
	Error.assertTrue(element instanceof BigInteger);
	return super.put(key,element);
}
/**
add to key's count
*/
public void add(Object key) {add(key,1);}
public void add(Object key, int count) {add(key,new BigInteger(count+""));}
public void add(Object key, BigInteger count) {
	Error.assertTrue(count.compareTo(BigInteger.ZERO) >= 0);
	if (containsKey(key)) {
       BigInteger i = (BigInteger)get(key);
       put(key,i.add(count));
   } else
       put(key, count);
}
public void addKeyCounter(KeyCounter other) {
    for (Enumeration e = other.keys(); e.hasMoreElements();) {
         Object key = e.nextElement();
		 add(key,other.getCount(key));
    }
}
/**
@return the number of times key was added
*/
public BigInteger getCount(Object key) {
	if (containsKey(key))
       return (BigInteger)get(key);
   else
       return BigInteger.ZERO;
}
/** For testint only. Don't use if counts can be greater than an int can hold */
protected int getIntCount(Object key) {
	return getCount(key).intValue();
}
/**
BUG: only works for counts that fit in an int.
@return the Tanimoto distance to other
@see Tanimoto
*/
public double tanimotoDistance(KeyCounter other) {
	return Tanimoto.distance(this,other);
}
public String toString() {
	String r = "";
    for (Enumeration e = keys(); e.hasMoreElements();) {
         Object key = e.nextElement();
         BigInteger count = getCount(key);
         r += key.toString() + "\t" + count + "\n";
    }
	return r;
}
}
