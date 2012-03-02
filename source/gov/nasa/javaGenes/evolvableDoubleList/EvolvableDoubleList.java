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

import java.util.Vector;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.ExtendedVector;
import gov.nasa.alsUtility.DoubleInterval;

public class EvolvableDoubleList extends gov.nasa.javaGenes.core.Evolvable {
protected ExtendedVector list = new ExtendedVector();

public EvolvableDoubleList() {}
public EvolvableDoubleList(int length) {
    Error.assertTrue(length >= 0);
    for(int i = 0; i < length; i++)
        addDouble(new EvolvableDouble());
}
/** used for testing */
public EvolvableDoubleList(int length, double value) {
    Error.assertTrue(length >= 0);
    for(int i = 0; i < length; i++)
        addDoubleValue(value);
}
public EvolvableDoubleList(double[] array) {
    this(array,1);
}
/** used for testing */
public EvolvableDoubleList(double[] array, double divideArrayBy) {
	double[] trueValues = new double[array.length];
    for(int i = 0; i < array.length; i++)
        trueValues[i] = array[i] / divideArrayBy;
    Error.assertTrue(array.length > 0);
    for(int i = 0; i < trueValues.length; i++)
        addDoubleValue(trueValues[i]);
}
public void removeAll() {
	list = new ExtendedVector();
}
public void remove(int index) {
    Error.assertTrue(0 <= index && index < list.size());
    list.remove(index);
}
public void remove(EvolvableDouble d) {
    Error.assertTrue(list.contains(d));
    list.remove(d);
}
public void insertBefore(int index, EvolvableDouble[] toInsert) {
    Error.assertTrue(0 <= index && index < list.size());
    Vector v = array2vectorOfCopies(toInsert);
    list.insertBefore(index,v);
}
public void insertAfter(int index, EvolvableDouble[] toInsert) {
    Error.assertTrue(0 <= index && index < list.size());
    Vector v = array2vectorOfCopies(toInsert);
    list.insertAfter(index,v);
}
private Vector array2vectorOfCopies(EvolvableDouble[] array) {
    Vector v = new Vector();
    for(int i = 0; i < array.length; i++)
        v.add(array[i].copy());
    return v;
}
public void addDoubleValue(double d) {addDouble(new EvolvableDouble(d));}
public void setDoubleValue(int index, double value) {
	Error.assertTrue(0 <= index && index < getSize());
	list.set(index,new EvolvableDouble(value));
}
public void addDouble(EvolvableDouble d) {list.add(d);}
public EvolvableDouble getDouble(int index) {return (EvolvableDouble)list.get(index);}
public double getDoubleValue(int index) {return getDouble(index).getValue();}
public EvolvableDouble getDoubleModulo(int index) {
    Error.assertTrue(getSize() > 0, "index = " + index);
    return (EvolvableDouble)list.get(index%getSize());
}
public double interpolateInto(int index,DoubleInterval interval) {
	return getDouble(index).interpolateInto(interval);
}
public double getDoubleValueModulo(int index) {return getDoubleModulo(index).getValue();}
public int getSize() {return list.size();}
public int getLastIndex() {return list.size() - 1;}
public gov.nasa.javaGenes.core.Evolvable copyForEvolution() {
    return copyForEvolution(new EvolvableDoubleList());
}
public EvolvableDoubleList copyForEvolution(EvolvableDoubleList empty) {
    Error.assertTrue(empty.getSize() == 0);
    for(int i = 0; i < getSize(); i++)
        empty.addDouble(getDouble(i).copy());
    return empty;
}
public boolean isEqual(EvolvableDoubleList other) {
    if (getSize() != other.getSize())
        return false;
    for(int i = 0; i < getSize(); i++)
        if (!getDouble(i).isEqual(other.getDouble(i)))
            return false;
    return true;
}
public String toString() {
    String s = "";
    for(int i = 0; i < getSize(); i++)
        s += getDouble(i).toString() + ((i == getSize()-1) ? "" : "\t");
    return s;
}
}