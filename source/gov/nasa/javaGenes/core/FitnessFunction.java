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
package gov.nasa.javaGenes.core;


import java.io.Serializable;

/**
fitness function for Evolvables. Low values are highly
fit.

@see Evolvable
*/
public abstract class FitnessFunction implements Serializable {
protected String name = getClass().getName();
/**
@return the fitness. Low values are fitter than high values.
*/
public abstract Fitness evaluateFitness (Evolvable evolvable);
public int numberOfObjectives() {return 1;}
public String getName() {
  return name;
}
public double bestDouble() {return 0;}
public double worstDouble() {return java.lang.Double.MAX_VALUE;}
public void setName(String n) {name = n;}
public String[] getNameArray() {
  String[] array = new String[1];
  array[0] = name;
  return array;
}
public FitnessFunction getFunction(int generation) {
  return this;
}
public boolean isNewFunction(int generation) {
  return false;
}

/**
create any files needed, such as a representation of a target. Unclean, really
shouldn't be here. By default, does nothing.
*/
public void makeFiles() {}
public void report (Population population){}
}
