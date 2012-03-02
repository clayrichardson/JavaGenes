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

public class ReinitializableInt implements java.io.Serializable {
protected int value = 0;
protected int initialValue = 0;

public ReinitializableInt() {this(0);}
public ReinitializableInt(int inValue) {setInitial(inValue);}
public void reinitialize() {value = initialValue;}
public void setInitial(int inValue) {initialValue = inValue; reinitialize();}
public void addToInitial(int a) {initialValue += a; reinitialize();}
public void add(int a) {value += a;}
public void multiply(int m) {value *= m;}
public int getCurrent() {return value;}
public int getInitial() {return initialValue;}
}


