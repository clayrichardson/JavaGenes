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
growing list of objects that will never need the garbage collector (for speed)
*/
public class GrowOnlyArray implements java.io.Serializable {
public final static int DEFAULT_INITIAL_SIZE = 10;
public final static int DEFAULT_GROWBY = 10;
protected int growBy = DEFAULT_GROWBY;
protected Object[] array;
protected int currentSize = 0;

public GrowOnlyArray() {this(DEFAULT_INITIAL_SIZE,DEFAULT_GROWBY);}
public GrowOnlyArray(int inInitialSize, int inGrowBy) {
    growBy = inGrowBy;
    Error.assertTrue(growBy > 0);
    Error.assertTrue(inInitialSize > 0);
    array = new Object[inInitialSize];
    currentSize = 0;
}
public int size() {return currentSize;}
public void reinitialize() {currentSize = 0;}
public void add(Object o) {
    currentSize++;
    if (currentSize > array.length)
        grow();
    array[currentSize-1] = o;
}
public Object get(int i) {return array[i];}
protected void grow() {
    Object[] newArray = new Object[array.length + growBy];
    try {
        System.arraycopy(array,0,newArray,0,array.length);
    } catch(Exception e) {Error.fatal("bad array copy");}
    array = newArray;
}

}
