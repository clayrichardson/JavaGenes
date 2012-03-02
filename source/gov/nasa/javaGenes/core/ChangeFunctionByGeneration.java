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
import java.lang.Integer;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.ExtendedVector;

public class ChangeFunctionByGeneration extends FitnessFunction  {
protected ExtendedVector lastGenerations = new ExtendedVector();
protected ExtendedVector fitnessFunctions = new ExtendedVector();

protected void check() {
  Error.assertTrue(!fitnessFunctions.isEmpty());
  Error.assertTrue(lastGenerations.size() == fitnessFunctions.size());
}
public void add(int generations,FitnessFunction function) {
	Error.assertTrue(generations >= 0);
  int lastGen = getLastGeneration() + generations;
	lastGenerations.addElement(new Integer(lastGen));
	fitnessFunctions.addElement(function);
  check();
}
public FitnessFunction getFunction(int generation) {
  check();
  for(int i = 0; i < size(); i++)
    if (generation <= getLastGeneration(i)) return getFunctionByIndex(i);
  return (FitnessFunction)fitnessFunctions.lastElement();
}
protected FitnessFunction getFunctionByIndex(int i ) {
  return (FitnessFunction)fitnessFunctions.elementAt(i);
}
protected int getLastGeneration(int index) {
  return ((Integer)lastGenerations.elementAt(index)).intValue();
}
protected int size() {return fitnessFunctions.size();}
public int getLastGeneration() {
  if (lastGenerations.isEmpty())
    return 0;
  return ((Integer)lastGenerations.lastElement()).intValue();
}
public boolean isNewFunction(int generation) {
  for(int i = 0; i < size()-1; i++)
    if (generation == getLastGeneration(i)+1) return true;
  return false;
}

/**
@return the fitness of evolvable.
*/
public Fitness evaluateFitness (Evolvable evolvable) {
  check();
  return getFunctionByIndex(0).evaluateFitness(evolvable);
}

public String toString() {
	String s = new String(this.getClass() + Utility.lineSeparator());
	for(int i = 0; i < size(); i++){
		s += "\t to generation " + getLastGeneration(i) + " " + getFunctionByIndex(i);
		if (i != size()-1)
			s += Utility.lineSeparator();
	}
	return s;
}
}