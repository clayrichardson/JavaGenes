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

import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;

/**
lower values are fitter
*/
public class FitnessDouble extends Fitness {
protected double fitness = BAD_FITNESS;

public FitnessDouble(double d) {
  fitness = d;
}
public boolean fitterThan(Fitness ff) {
  Error.assertTrue(ff instanceof FitnessDouble);
  FitnessDouble f = (FitnessDouble)ff;
  if (!Utility.normalNumber(f.fitness))
    return true;
  return fitness < f.fitness;
}
public boolean isDominatedBy(Fitness ff) {
  Error.assertTrue(ff instanceof FitnessDouble);
  FitnessDouble f = (FitnessDouble)ff;
  if (!Utility.normalNumber(f.fitness))
    return false;
  return fitness > f.fitness;
}

public double[] getFitnessArray() {
  double[] array = new double[1];
  array[0] = fitness;
  return array;
}
public void setFitness(double d) {
  fitness = d;
}
public double asDouble() {return fitness;}

public boolean equals(FitnessDouble f) {
  return fitness == f.fitness;
}
public int hashCode() {
  Double d = new Double(fitness);
  return d.hashCode();
}
public boolean isValid() {
  return Utility.normalNumber(fitness);
}
public String toString() {return fitness + "";}
}
