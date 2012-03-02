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


import java.lang.Double;
import java.lang.Math;
import java.io.Serializable;
import gov.nasa.alsUtility.Error;

/**
Represents a genetic software individual.
*/
public class Individual implements Serializable {

protected Fitness fitness = null;
protected Evolvable evolvable = null;
/**
Sets the evolvable to Evolvable e and calculates the fitness using
FitnessFunction f.
*/
public Individual(Evolvable e, FitnessFunction f) {
	evolvable = e;
	evaluateFitness(f);
}
public Individual(TokenizeInput tokenizer) {
  Error.notImplemented();
	//fitness = tokenizer.getDouble();
}
/** for testing only */
public Individual(Fitness f) { 
  fitness = f;
  evolvable = new Evolvable();
}

public void stateSave(TokenizeOutput tokenizer) {
  Error.notImplemented();
	//	tokenizer.putDouble(fitness);
}

/**
@return the fitness and make sure it's a number. Returns
the maximum possible double if it's not a number
*/
/*
public double getSafeFitness() {
	Error.assertTrue(fitness != null);
  if (Utility.normalNumber (fitness)) return fitness;
  else return Double.MAX_VALUE;
}
*/
public Fitness getFitness() {
	Error.assertTrue(fitness != null);
	return fitness;
}
/**
@return true if this fitter than i
*/                                     
public boolean fitterThan(Individual i) {
  Error.assertTrue(isCompatible(i));
  return fitness.fitterThan(i.getFitness());
}
/*
public boolean fitnessCloserThan(double target,Individual i)  {
  Error.assertTrue(isCompatible(i));
  return Math.abs(getFitness() - target) < Math.abs(i.getFitness() - target);
}
*/
public boolean isCompatible(Individual i) {
  return evolvable.getClass() == i.evolvable.getClass();
}
static public int bestIndividualIndex(Individual[] population) {
    Individual best = null;
    int index = 0;
    for (int i = 0; i < population.length; i++)
        if (best == null || population[i].fitterThan(best)) {
            best = population[i];
            index = i;
        }
    return index;
}

/**
evaluate the fitness
*/
public void evaluateFitness (FitnessFunction f) {fitness = f.evaluateFitness(evolvable);}
public int evolvableSize(){return evolvable.getSize();}
public Evolvable getEvolvable() {return evolvable;}
/**
@return the tab separated header for the information in toString()
*/
public static String headerString(){return "fitness\tsize\tevolvable";}
/**
@return a tab separated string suitable for use in spreadsheets
*/
public String toString() {return fitness+"\t"+evolvableSize()+"\t"+evolvable;}
/**
@return true if fitnesses are equal and the distanceFrom is 0
*/
public boolean isSame(Individual individual) {
	return equals(individual) ||
    (fitness.equals(individual.getFitness()) && getEvolvable().distanceFrom(individual.getEvolvable()) == 0);
}	
}


