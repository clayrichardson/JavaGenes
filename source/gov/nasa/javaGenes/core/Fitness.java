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
import gov.nasa.alsUtility.Error;

public abstract class Fitness implements Serializable {
public static final double BAD_FITNESS = Double.NEGATIVE_INFINITY;

public boolean isValid() {return true;}
public abstract boolean fitterThan(Fitness f);
public abstract boolean isDominatedBy(Fitness f);

public double[] getFitnessArray() {
  double[] array = new double[1];
  array[0] = asDouble();
  return array;
}
public void setFitness(double d) {Error.notImplemented();}
public double asDouble() {Error.notImplemented(); return 0;}
public String toString() {
    return this.getClass().toString();
}
}