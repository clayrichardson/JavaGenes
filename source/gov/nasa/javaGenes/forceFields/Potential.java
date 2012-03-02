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
package gov.nasa.javaGenes.forceFields;

import gov.nasa.alsUtility.Error;

/**
this classes used to find energy of a multibody system given a particular
Chromosome for the potential parameters.  Originally designed for molecular force fields.
*/
abstract public class Potential {
public static final int STILLINGER_WEBER = 0;
public static final int TERSOFF = 1;
/**
@param target use this chromosome to establish the parameters of the potential
*/
abstract public void setChromosome(Chromosome target);
abstract public AlleleTemplate getAlleles();
public void mustModel(ManyMultiBodiesForOneEnergy bodies) {
    Error.notImplemented();
}
public double getCutoff(TwoBody m) {Error.notImplemented(); return 0;}
public double getCutoff(ThreeBody m) {Error.notImplemented(); return 0;}
/**
@return the energies associated with bodies
*/
public double[] getEnergy(Bodies[] bodies) {
  Error.notImplemented();
  return null;
}
public double getForce(TwoBody pair) {
  return 0;
}
/**
@param energies the energies associated with bodies
*/
public void getEnergy(Bodies[] bodies, double[] energies){
  Error.notImplemented();
}
public double getEnergy(MultiBodiesForOneEnergy bodies){
  Error.notImplemented();
  return 0;
}
public double getEnergy(MultiBody bodies){
  Error.notImplemented();
  return 0;
}
public String toString() {return getClass().toString();}
}
