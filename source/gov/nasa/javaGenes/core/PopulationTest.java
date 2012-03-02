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

import junit.framework.TestCase;
import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.forceFields.ChromosomePopulation;

public class PopulationTest extends TestCase {
private FitnessMultiObjective[] fitness;
private Population population;
public PopulationTest(String name) {super(name);}

public void setUp() {
  final int size = 4;
  fitness = new FitnessMultiObjective[size];
  for(int i = 0; i < fitness.length; i++)
    fitness[i] = new FitnessMultiObjective(null);
  population = new ChromosomePopulation(size);
  for(int i = 0; i < size; i++)
    population.setIndividual(i,new Individual(fitness[i]));
}
public void testMakeIndividualArray() {
	Population population = new Population(50);
	for(int i = 0; i < population.getSize(); i++)
		population.setIndividual(i, new Individual(new FitnessDouble(i)));
	int[] indices = {1,3,40,2};
	Individual[] array = population.makeIndividualArray(indices);
	for(int i = 0; i < indices.length; i++)
		Error.assertTrue(i+"", array[i] == population.getIndividual(indices[i]));
}
public void testWorstFitness() {
	Population population = new Population(50);
	for(int i = 0; i < population.getSize(); i++)
		population.setIndividual(i, new Individual(new FitnessDouble(i)));
	Error.assertTrue("1", population.worstIndividualIndex() == 49);
	Error.assertTrue("2", population.worstFitness().asDouble() == 49);
	population.setIndividual(25, new Individual(new FitnessDouble(100)));
	Error.assertTrue("3", population.worstIndividualIndex() == 25);
	Error.assertTrue("4", population.worstFitness().asDouble() == 100);
	population.setIndividual(0, new Individual(new FitnessDouble(200)));
	Error.assertTrue("6", population.worstIndividualIndex() == 0);
	Error.assertTrue("7", population.worstFitness().asDouble() == 200);
	FitnessDouble threshold = new FitnessDouble(200);
	try {
		Error.assertTrue("8", population.worstFitness(threshold).asDouble() == 100);
		threshold = new FitnessDouble(80);
		Error.assertTrue("9", population.worstFitness(threshold).asDouble() == 49);
	} catch (NotFoundException e) {
		Error.fatal("shouldn't happen");
	}
	try {
		threshold = new FitnessDouble(0);
		population.worstFitness(threshold);
		Error.fatal("shouldn't get here");
	} catch (NotFoundException e) {}
}
public void testGetParetoFront() {
  add(0,3,4);
  add(1,1,2);
  add(2,4,3);
  add(3,2,1);
  Population pareto = population.getParetoFront();
  assertTrue("size", pareto.getSize() == 2);
  assertTrue("0", pareto.getIndividual(0) == population.getIndividual(1));
  assertTrue("1", pareto.getIndividual(1) == population.getIndividual(3));
}
public void testGetParetoFront2() {
  add(1,3,4);
  add(0,1,2);
  add(3,4,3);
  add(2,2,1);
  Population pareto = population.getParetoFront();
  assertTrue("size", pareto.getSize() == 2);
  assertTrue("0", pareto.getIndividual(0) == population.getIndividual(0));
  assertTrue("1", pareto.getIndividual(1) == population.getIndividual(2));
}
private void add(int index, double f1Value, double f2Value) {
  fitness[index].add(new FitnessDouble(f1Value));
  fitness[index].add(new FitnessDouble(f2Value));
}

}
