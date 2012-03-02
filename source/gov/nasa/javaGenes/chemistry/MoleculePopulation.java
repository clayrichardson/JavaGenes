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
package gov.nasa.javaGenes.chemistry;

import gov.nasa.alsUtility.IntegerInterval;
import gov.nasa.javaGenes.graph.GraphPopulation;
import gov.nasa.javaGenes.core.Population;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.javaGenes.core.FitnessFunction;
import gov.nasa.javaGenes.core.Individual;


public class MoleculePopulation extends GraphPopulation {
/**
create a random population of molecules
*/
public MoleculePopulation (MoleculeParameters parameters) {
    super(parameters.populationSize);
    IntegerInterval vertices = parameters.verticesInterval;
    IntegerInterval cycles = parameters.cyclesInterval;
		int v = vertices.random();
    int c = cycles.random();
    int tries = 0;
    for (int i = 0; i < population.length; i++) {
    	Molecule molecule = new Molecule(parameters.provider,v,c);
			if (c == molecule.getNumberOfCycles() && v == molecule.getVerticesSize()) {
      	population[i] = new MoleculeIndividual(molecule,parameters.fitnessFunction);
        v = vertices.random();
    		c = cycles.random();
        tries = 0;
      }
      else {   // molcule wasn't to spec.  Try again.
      	i--;
        tries++;
        if (tries >= parameters.randomIndividualTriesPerSpecification) {
        	c--; // the number of cycles may be impossible to achieve
          tries = 0;
        }
      }
    }
}
/*
public MoleculePopulation(TokenizeInput tokenizer) {
	super(tokenizer.getInteger());
  for (int i = 0; i < population.length; i++)
  	population[i] = new MoleculeIndividual(tokenizer);
}

public void stateSave(TokenizeOutput tokenizer) {
	tokenizer.putInteger(population.length);
  for (int i = 0; i < population.length; i++)
  	population[i].stateSave(tokenizer);
}
*/

/**
create an empty population with int size members
*/
protected MoleculePopulation(int size) {super(size);}
/**
@return a new empty population with int size members
*/
public Population makePopulation(int size) {
    return new MoleculePopulation(size);
}
/**
@return a MoleculeIndividual
*/
public Individual makeIndividual(Evolvable e, FitnessFunction f) {
	//Error.assertTrue(e.getClass() == (new Molecule()).getClass());
  return new MoleculeIndividual((Molecule)e,f);
}

} 