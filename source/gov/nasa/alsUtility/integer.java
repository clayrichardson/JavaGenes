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


import java.io.Serializable;

/**
represent a changeable integer (Java API class Integer cannot be modified)
*/
public class integer implements Serializable{
public integer() {value = 0;} // for testing ObjectCache
public integer (int i) {value = i;} 
public int value;
public void increment(){value++;}
public void setValue(int i) {value = i;}
public void divideBy(int i) {value = value/i;}
public int getValue(){return value;} 
public void add(int i) {value += i;}
public String toString() {return value + "";}
public boolean equals(Object other) {return getValue() == ((integer)other).getValue();}
public int hashCode() {return getValue();}
}
