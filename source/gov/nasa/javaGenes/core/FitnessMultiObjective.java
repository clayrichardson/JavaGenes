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
import gov.nasa.alsUtility.ExtendedVector;


public class FitnessMultiObjective extends Fitness {
protected FitnessFunctionMultiObjective function = null; // but each must implement asDouble(), only used for weights
protected ExtendedVector fitness = new ExtendedVector();
public FitnessMultiObjective(FitnessFunctionMultiObjective f) {
  function = f;
}
public void add(Fitness f) {
  fitness.addElement(f);
}
public Fitness getFitness(int i) {return (Fitness)fitness.elementAt(i);}
public boolean fitterThan(Fitness mm) {
  FitnessMultiObjective m = (FitnessMultiObjective)mm;
  double count = 0;
  double distance = 0;
  for(int i = 0; i < fitness.size(); i++) {
    Fitness fMe = getFitness(i);
    Fitness fThem = m.getFitness(i);
    double weight = function.getWeight(i);
    if (fMe.fitterThan(fThem))
      count += weight;
    else if (fThem.fitterThan(fMe))
      count -= weight;
    distance = weight * (fThem.asDouble() - fMe.asDouble()); // NOTE: low values are fitter
  }
  if (count > 0)
    return true;
  if (count < 0)
    return false;
  return distance > 0;
}
public boolean isDominatedBy(Fitness mm) {
  Error.assertTrue(mm instanceof FitnessMultiObjective);
  FitnessMultiObjective m = (FitnessMultiObjective)mm;
  boolean equalsCase = true;
  for(int i = 0; i < fitness.size(); i++) {
    Fitness fMe = getFitness(i);
    Fitness fThem = m.getFitness(i);
    if (fThem.isDominatedBy(fMe))
      return false;
    if (fThem.asDouble() != fMe.asDouble())
      equalsCase = false;
  }
  return !equalsCase;
}

public double[] getFitnessArray() {
  double[] array = new double[fitness.size()];
  for(int i = 0; i < fitness.size(); i++) {
    array[i] = getFitness(i).asDouble();
  }
  return array;
}
}
