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

import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.core.FitnessFunction;
import java.util.Vector;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.javaGenes.core.Fitness;
import gov.nasa.javaGenes.core.Population;
import gov.nasa.alsUtility.RootMeanSquares;
import gov.nasa.javaGenes.core.FitnessDouble;
import gov.nasa.alsUtility.LogFile;
import gov.nasa.alsUtility.EasyFile;

/**
fitness function based on RMS distance of potential energies
between the target and candidate Chromosome
*/
public class PotentialEnergyFitness extends FitnessFunction {
protected Potential potential;
protected Vector testCases = new Vector();
protected Chromosome target;

/**
@param p the potential energy function
@param t the target chromosome
*/
public PotentialEnergyFitness(Potential p, Chromosome t){
    potential = p;
    target = t;
}
/**
add a set of multi-bodies to create one test case
*/
public void add(Bodies[] bodies) {
	potential.setChromosome(target);
  PotentialEnergyTestCase test = new PotentialEnergyTestCase(potential.getEnergy(bodies),bodies);
  testCases.addElement(test);
}
/**
create multiple test cases (the first dimension of array bodies)
*/
public void add(Bodies[][] bodies) {
	potential.setChromosome(target);
	for(int i = 0; i < bodies.length; i++) {
  	PotentialEnergyTestCase test = new PotentialEnergyTestCase(potential.getEnergy(bodies[i]),bodies[i]);
  	testCases.addElement(test);
  }
}
/**
add a set of bodies to create one test case

@param bodies must consist of Bodies[] objects
*/
public void add(Vector bodies) {
	potential.setChromosome(target);
	for(int i = 0; i < bodies.size(); i++) {
  	Bodies[] current = (Bodies[])bodies.elementAt(i);
  	PotentialEnergyTestCase test = new PotentialEnergyTestCase(potential.getEnergy(current),current);
  	testCases.addElement(test);
  }
}

/*
find RMS distance for all bodies in all test cases from the target
*/
public Fitness evaluateFitness (Evolvable evolvable){
	Chromosome chromosome = (Chromosome)evolvable;
  potential.setChromosome(chromosome);
  RootMeanSquares rms = new RootMeanSquares();
	for(int i = 0; i < testCases.size(); i++) {
  	PotentialEnergyTestCase test = (PotentialEnergyTestCase)testCases.elementAt(i);
    double[] energies = test.getDifference(potential);
    for(int j = 0; j < energies.length; j++)
  		rms.addDatum(energies[j]);
  }
	return new FitnessDouble(rms.rms());
}
/**
Prints out a file called difference.tsd with the target chromosome in it
and the difference between the best and target chromosomes.
*/
public void report (Population population){
	Chromosome best = (Chromosome)population.bestIndividual().getEvolvable();

  String filename = "difference.tsd";
  LogFile log;
  if (population.getGeneration() == 0) {
  	log = new LogFile(filename, false);
    log.println("target\t" + target);
  } else
  	log = new LogFile(filename, true);
  Chromosome difference = target.createDifferenceChromosome(best);
  log.println(population.getGeneration() + "\t" + difference);
  log.close();
}
/**
creative fall column target.tsd with the target chromosome it
and a file testCases.tsd with all the test cases in it
*/
public void makeFiles() {
	EasyFile file = new EasyFile("testCases.tsd");
	for(int i = 0; i < testCases.size(); i++) {
  	PotentialEnergyTestCase test = (PotentialEnergyTestCase)testCases.elementAt(i);
    test.printTo(file);
  }
  file.close();

  Utility.makeFile("target.tsd", target.toString());
}

public String toString() {
    return getClass() + ": RMS to values from target chromosome. Target = " + target +
            " Potential = " + potential +
            " test cases = " + testCases;
}
}