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

import java.util.Hashtable;
import gov.nasa.javaGenes.core.Population;
import gov.nasa.javaGenes.core.FitnessFunction;
import gov.nasa.javaGenes.core.TokenizeOutput;
import gov.nasa.javaGenes.core.TokenizeInput;
import gov.nasa.javaGenes.core.Population;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.javaGenes.core.Individual;
import gov.nasa.alsUtility.ManySamples;
import gov.nasa.alsUtility.Sample;

public class ChromosomePopulation extends Population {
protected AlleleTemplate alleles = null;
/**
Generate a random population
*/
public ChromosomePopulation (ChromosomeParameters parameters) {
    this(parameters.populationSize, parameters.alleles, parameters.fitnessFunction);
}

public ChromosomePopulation (int size, AlleleTemplate a, FitnessFunction fitness) {
    super(size);
    alleles = a;
    for (int i = 0; i < population.length; i++)
        population[i] = new ChromosomeIndividual(
            new Chromosome(alleles),fitness);
}

/**
checkpoint
*/
public void stateSave(TokenizeOutput tokenizer) {
	tokenizer.putInteger(population.length);
  for (int i = 0; i < population.length; i++)
  	population[i].stateSave(tokenizer);
}
/**
checkpoint
*/
public ChromosomePopulation(TokenizeInput tokenizer) {
	super(tokenizer.getInteger());
  for (int i = 0; i < population.length; i++)
  	population[i] = new ChromosomeIndividual(tokenizer);
}
/**
create an empty population with int size members
*/
public ChromosomePopulation(int size) {super(size);}
/**
@return a new empty population with int size members
*/
public Population makePopulation(int size) {
    ChromosomePopulation p = new ChromosomePopulation(size);
    p.alleles = alleles;
    return p;
}
/**
@return a new Individual that has a Chromosome evolvable.
*/
public Individual makeIndividual(Evolvable e, FitnessFunction f) {
    return new ChromosomeIndividual((Chromosome)e,f);
}
/**
Add Sample objects to ManySamples samples that summarize the population.
Used for data analysis of evolution.

@see Sample
*/
public void fillSamples (ManySamples samples) {
    if (population.length <= 0) return;
    Sample fitness = new Sample();
    Individual best = null;
    for (int index = 0; index < population.length; index++){
    	Individual i = (Individual)population[index];
			if (best == null || i.fitterThan(best))
		    best = i;
			fitness.addDatum(i.getFitness().asDouble());
    }

    samples.getSample("bestFitness").addDatum(best.getFitness().asDouble());
    samples.getSample("meanFitness").addDatum(fitness.getMean());
    samples.getSample("fitnessStandardDeviation").addDatum(fitness.getStandardDeviation());
   	samples.getSample("time").addDatum(getTime());
}
public String getEvolvableHeader() {
  String header = alleles.getHeader();
  return header;
}
} 
