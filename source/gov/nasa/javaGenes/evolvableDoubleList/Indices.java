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
package gov.nasa.javaGenes.evolvableDoubleList;

import java.lang.Integer;
import gov.nasa.alsUtility.ExtendedTreeSet;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.Utility;

public class Indices implements java.io.Serializable {
protected ExtendedTreeSet indices = new ExtendedTreeSet(); // of Integer

/** under normal conditions this constructor is used */
public Indices() {}
/** for testing */
public Indices(int evolvableSize, int modulo) {
	Error.assertTrue(modulo > 0);
	for(int i = 0; i < evolvableSize; i += modulo)
		addIndex(i);
}
public void addIndex(int value) {indices.add(new Integer(value));}
public int getFirstIndex() {
    Error.assertTrue(indices.size() > 0);
    return ((Integer)indices.first()).intValue();
}
public void remove(int index) {indices.remove(index);}
public int[] getArray() {
    int[] array = new int[indices.size()];
    Object[] integers = indices.toArray();
    Error.assertTrue(array.length == integers.length);
    for(int i = 0; i < integers.length; i++)
        array[i] = ((Integer)integers[i]).intValue();
    return array;
}
public int size() {return indices.size();}
public void addAll(int size) {
    for(int i = 0; i < size; i++)
        addIndex(i);
}
public boolean isEqual(Indices other) {
	return Utility.isEqual(getArray(),other.getArray());
}
}