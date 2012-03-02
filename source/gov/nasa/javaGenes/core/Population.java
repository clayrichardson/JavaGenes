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

 
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Hashtable;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.ManySamples;
import gov.nasa.alsUtility.Iterator;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.integer;
import gov.nasa.alsUtility.IntegerInterval;


/**
Represents a population of evolvable Graph objects
*/
public class Population implements Serializable {
protected Individual[] population;
protected int generation = -1;
/**
time to generate this population
*/
protected long time = -1;
/**
memory used by the program after population generated
*/
protected long memory = -1;

/**
read from a checkpoint file
*/
public void stateSave(TokenizeOutput tokenizer) {
	tokenizer.putInteger(generation);
	tokenizer.putLong(time);
	tokenizer.putLong(memory);
	tokenizer.putInteger(population.length);
}
/**
write to a checkpoint file
*/
public void stateRestore(TokenizeInput tokenizer) {
	generation = tokenizer.getInteger();
	time = tokenizer.getLong();
	memory = tokenizer.getLong();
  population = new Individual[tokenizer.getInteger()];
}
/**
create an empty population with int size members
*/
public Population(int size) {population = new Individual[size];}

/**
@return a new empty population with int size members
*/
public Population makePopulation(int size) {
	return new Population(size);
}

/**
Meant to be a general purpose method that creates individuals of whatever
type are in this population. Must be rewritten to actually do that.

@return a new Individual that has a evolvable.  Normally replaced by subclass.
*/
public Individual makeIndividual(Evolvable e, FitnessFunction f) {
	return new Individual(e,f);
}
public IntegerInterval getIndexRange() {return new IntegerInterval(0,getLastIndex());}
public int getLastIndex() {return getSize()-1;}

/**
Calculate the fitness of all individuals in the population.
*/
public void evaluateFitness (FitnessFunction f) {
  for (int i = 0; i <  population.length; i++)
    population[i].evaluateFitness (f);
}
public Fitness getFitness(int i) {return getIndividual(i).getFitness();}
public Evolvable getEvolvable(int i) {return getIndividual(i).getEvolvable();}
public void retestStudents(StudentFitnessFunction f) {
  for (int i = 0; i <  population.length; i++)
    f.testStudent((StudentFitness)getFitness(i));
}
public Population getParetoFront() {
  boolean[] isPareto = new boolean[getSize()];
  for(int i = 0; i < isPareto.length; i++)
    isPareto[i] = true;
  for(int i = 0; i < isPareto.length; i++) {
    if (!isPareto[i])
      continue;
    Fitness me = getIndividual(i).getFitness();
    for(int j = i+1; j < isPareto.length; j++) {
      Fitness him = getIndividual(j).getFitness();
      if (me.isDominatedBy(him)) {
        isPareto[i] = false;
        break; // out of j for loop
      }
      if (him.isDominatedBy(me))
        isPareto[j] = false;
    }
  }
  int count = 0;
  for(int i = 0; i < isPareto.length; i++)
    if (isPareto[i])
      count++;
  Population pareto = makePopulation(count);
  count = 0;
  for(int i = 0; i < isPareto.length; i++)
    if (isPareto[i]) {
      pareto.setIndividual(count,getIndividual(i));
      count++;
    }
  return pareto;
}
/**
@return the sum of the sizes of each individual in the population.
*/
public int totalEvolvableSize(){
    int size = 0;
    for (int i = 0; i <  population.length; i++) 
        size += population[i].evolvableSize();
    return size;
}
/**
@return the index (starting at 0) of GraphIndividual individual
*/
// PERFORMANCE: put indices in the individuals
public int getIndex(Individual individual) {
    for (int i = 0; i <  population.length; i++) 
        if (individual.equals(population[i]))
        	return i;
    return -1;
}

/**
@return the mean of the fitness of all individuals that have a normal number for fitness.
Return 0 if no individuals have a normal number for the fitness.
*/
public double averageFitness() {
  double mean = 0;
  int number = 0;
  for (int i = 0; i < population.length; i++){
    Fitness f = population[i].getFitness();
    if (f instanceof FitnessDouble && f.isValid()){
      double d = ((FitnessDouble)f).asDouble();
      mean += d;
      number++;
    }
  }
  return mean/(number != 0 ? (double)number : 1);
}
/**
@return the most fit individual in the population
*/
public Individual bestIndividual() {return population[bestIndividualIndex()];}
/**
@return the fitness of the most fit individual
*/
public Fitness bestFitness() {return bestIndividual().getFitness();}
/**
@return the index of the most fit individual
*/
public int bestIndividualIndex() {
	return Individual.bestIndividualIndex(population);
}
/*
public int bestIndividualIndex() {
    Individual best = null;
    int index = 0;
    for (int i = 0; i < population.length; i++)
        if (best == null || population[i].fitterThan(best)) {
            best = population[i];
            index = i;
        }
    return index;
}
*/
public Fitness worstFitness() {
	try {
		return worstIndividual(null).getFitness();
	} catch (NotFoundException e) {
		Error.fatal("should never happen");
	}
	Error.fatal("should never happen");
	return null;
}
public Individual worstIndividual() {
	try {
		return population[worstIndividualIndex(null)];
	} catch (NotFoundException e) {
		Error.fatal("should never happen");
	}
	Error.fatal("should never happen");
	return null;
}
public int worstIndividualIndex() {
	try {
		return worstIndividualIndex(null);
	} catch (NotFoundException e) {
		Error.fatal("should never happen");
	}
	Error.fatal("should never happen");
	return -1;
}
public Individual worstIndividual(Fitness mustBeBetterThan)  throws NotFoundException {return population[worstIndividualIndex(mustBeBetterThan)];}
public Fitness worstFitness(Fitness mustBeBetterThan)  throws NotFoundException {return worstIndividual(mustBeBetterThan).getFitness();}
/**
@return the index of the least fit individual better than mustBeBetterThan.  It's a fatal error for there to
be no better
*/
public int worstIndividualIndex(Fitness mustBeBetterThan) throws NotFoundException {
    Individual worst = null;
    int index = -1;
    for (int i = 0; i < population.length; i++)
        if ((worst == null || worst.fitterThan(population[i])) && (mustBeBetterThan == null || population[i].getFitness().fitterThan(mustBeBetterThan))) {
            worst = population[i];
            index = i;
        }
	if (!isIndexLegal(index))
		throw new NotFoundException();
    return index;
}
public boolean isIndexLegal(int index) {
	return 0 <= index && index < getSize();
}

public Individual[] makeIndividualArray(int[] indices) {
	Error.assertNotNull(indices);
	Individual[] individuals = new Individual[indices.length];
	for(int i = 0; i < indices.length; i++) {
		individuals[i] = getIndividual(indices[i]);
		Error.assertNotNull(individuals[i]);
	}
	return individuals;
}
/**
@return a random individual
*/
public Individual pick () { return population[RandomNumber.getIndex (population.length)];}
/**
@return a random individual that isn't notThis. If the population only has one individual
this method will loop forever.
*/
public Individual pick (Individual notThis) {
    Individual candidate;
    do
        candidate = pick();
    while (candidate == notThis);
    return candidate;
}
/**
@return a random individual that isn't notThis or orThis. If the population only has one individual
this method will loop forever.
*/
public Individual pick (Individual notThis, Individual orThis) {
    Individual candidate;
    do
        candidate = pick(orThis);
    while (candidate == notThis);
    return candidate;
}
/**
Add Sample objects to ManySamples samples that summarize the population.
Used for data analysis of evolution.

@see Sample
*/
public void fillSamples (ManySamples samples) {Error.notImplemented();}

protected int countParts(Iterator i,Hashtable table) {
	int count = 0;
	for (; i.more(); i.next()) {
	    String c = i.object().toString();
	    if (table.containsKey(c))
	        ((integer)table.get(c)).increment();
	    else
	        table.put(c, new integer(1));
	    count++;
	}
	return count;
}
protected void fillEntropy(ManySamples samples, Hashtable table, int count, String name) {
    String[] s = Utility.getStringKeys(table);
    double entropy = 0;
    for(int i = 0; i < s.length; i++){
        double d = (double)((integer)table.get(s[i])).getValue();
		double probability = d/(double)count;
        samples.getSample(s[i] + "Probability").addDatum(probability);
		entropy -= probability * Math.log(probability);
    }
    samples.getSample(name+"Entropy").addDatum(entropy);
}
/**
Write out an ASCII version of the population
*/
public void report (PrintWriter out) {
    out.println (Individual.headerString());
    for (int i = 0; i < population.length; i++)
        out.println (population[i]);
}
public boolean isDuplicate(Individual individual) {
    for (int i = 0; i < population.length; i++)
        if (individual.isSame(getIndividual(i)))
        	return true;
    return false;
}
/**
@return a tab separated string for getLogString

@see #getLogString
*/
public String getLogHeaderString() {
    return  getStandarPartOfLogHeaderString() + getEvolvableHeader();
}
public String getStandarPartOfLogHeaderString() {
    return  "generation \tdistance \ttime \t" + getUniqueLogValueHeader() + "\tbestFitness \taverageFitness \t";
}
public String getEvolvableHeader() {return "bestEvolvable";}
/**      
@return a tab separated string summarizing the population
*/
public String getLogString(FitnessFunction distanceFunction) {
    Individual best = bestIndividual();
    Fitness distance = new FitnessBad();
    if (distanceFunction != null)
      distance = distanceFunction.evaluateFitness(best.getEvolvable());
    return  generation + " \t" +
            distance + " \t" +
            time  + " \t" +
            getUniqueLogValue(best)  + " \t" +
    		best.getFitness() + " \t" +
		    averageFitness() + "\t" +
		    best.getEvolvable();
}
public String getUniqueLogValueHeader() {return "size ";}
public String getUniqueLogValue(Individual best) {
  return "" + totalEvolvableSize();
}
public void setIndividual (int i, Individual person) {population[i] = person;}
public Individual getIndividual (int i) {
	Error.assertTrue(0 <= i && i < getSize());
	return population[i];
}
public int getSize() {return population.length;}
public void setTime(long t) {time = t;}
public long getTime() {return time;}
public void setGeneration(int g) {generation = g;}
public int getGeneration() {return generation;}
public void setMemory(long m) {memory = m;}
public long getMemory() {return memory;}
}
