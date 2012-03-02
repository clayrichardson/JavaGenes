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

import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.ExtendedVector;

public class FitnessFunctionMultiObjective extends FitnessFunction  {
protected ExtendedVector weights = new ExtendedVector();
protected ExtendedVector fitnessFunctions = new ExtendedVector();
/**
@param weight the weight for this fitness function. Smaller values indicate more influence since 0 is perfect fitness
@param function the fitness function.
*/
public void add(double weight, FitnessFunction function) {
	Error.assertTrue(weight >= 0);
	weights.addElement(new Double(weight));
	fitnessFunctions.addElement(function);
}
public void add(FitnessFunction function) {
  add(1,function);
}
public int numberOfObjectives() {return fitnessFunctions.size();}
public double bestDouble() {Error.notImplemented(); return super.bestDouble();}
public double worstDouble() {Error.notImplemented(); return super.worstDouble();}

public Fitness evaluateFitness (Evolvable evolvable) {
    FitnessMultiObjective f = getNewFitness();
    for(int i = 0; i < fitnessFunctions.size(); i++)
        f.add(getFitnessFunction(i).evaluateFitness(evolvable));
    return f;
}
// subclasses can change class of return value
public FitnessMultiObjective getNewFitness() {
  return new FitnessMultiObjective(this);
}
public FitnessFunction getFitnessFunction(int i) {
  return (FitnessFunction)fitnessFunctions.elementAt(i);
}
public double getWeight(int i) {
  return ((Double)weights.elementAt(i)).doubleValue();
}
/**
tells the constituent fitness functions to makeFiles()
*/
public void makeFiles() {
	for(int i = 0; i < fitnessFunctions.size(); i++){
		((FitnessFunction)fitnessFunctions.elementAt(i)).makeFiles();
	}
}
public String[] getNameArray() {
  String[] array = new String[fitnessFunctions.size()];
	for(int i = 0; i < fitnessFunctions.size(); i++)
    array[i] = getFitnessFunction(i).getName();
  return array;
}
public String toString() {
	String s = "fitness function " + new String(getName()) + "\n";
	for(int i = 0; i < fitnessFunctions.size(); i++){
		s += weights.elementAt(i).toString() + "\t" + fitnessFunctions.elementAt(i).toString() + "\n";
	}
	s += "end fitness function\n";
	return s;
}
}