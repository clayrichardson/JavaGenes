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
package gov.nasa.javaGenes.permutation;

import gov.nasa.javaGenes.core.Population;
import gov.nasa.javaGenes.core.FitnessFunction;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.javaGenes.core.Individual;
public class PermutationPopulation extends Population {

public PermutationPopulation (PermutationParameters parameters) {
    this(parameters.permutationLength, parameters.populationSize, parameters.fitnessFunction);
}
public PermutationPopulation (int permutationLength, int populationSize, FitnessFunction fitness) {
    super(populationSize);
    for (int i = 0; i < population.length; i++)
        population[i] = new PermutationIndividual(new PermutationEvolvable(permutationLength),fitness);
}

/**
create an empty population with int size members
*/
protected PermutationPopulation(int size) {super(size);}
/**
@return a new empty population with int size members
*/
public Population makePopulation(int size) {
    return new PermutationPopulation(size);
}

public Individual makeIndividual(Evolvable e, FitnessFunction f) {
  return new PermutationIndividual((PermutationEvolvable)e,f);
}

}