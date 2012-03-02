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
package gov.nasa.javaGenes.core.HFC;

import java.util.TreeSet;
import java.lang.Integer;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.IntegerInterval;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.javaGenes.core.FitnessDouble;
import gov.nasa.javaGenes.core.Fitness;
import gov.nasa.javaGenes.core.Population;
import gov.nasa.javaGenes.core.ChildMaker;
import gov.nasa.javaGenes.core.Individual;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.javaGenes.core.FitnessFunction;
import gov.nasa.javaGenes.core.ChildMakerProvider;
import gov.nasa.javaGenes.core.Tournament;
import gov.nasa.javaGenes.core.AntiTournament;
import gov.nasa.javaGenes.core.RandomEvolvableProducer;
import gov.nasa.javaGenes.core.ChooseParents;
import gov.nasa.javaGenes.core.ChooseForDeath;

public class SubBreeder implements java.io.Serializable {
static public boolean debug = false;
static public int MAX_TRIES = 100; // used to avoid infinite recursion when generating random individuals

protected Breeder breeder;
protected ChildMakerProvider childMakerProvider;
protected FitnessFunction fitnessFunction;
protected SubBreeder promotionsTo;
protected Fitness bestFitnessAllowed;
protected IntegerInterval indicesRange;
protected TreeSet deathRow = new TreeSet();
protected ChooseParents parentChooser = new Tournament(2);
protected ChooseForDeath grimReaper = new AntiTournament(1);
protected Fitness acceptRandomIndividualFitnessThreshold; // random individuals must be better than this to be acceptable

public SubBreeder(ChildMakerProvider childMakerProvider, FitnessFunction fitnessFunction) {
	Error.assertNotNull(childMakerProvider);
	this.childMakerProvider = childMakerProvider;
	Error.assertNotNull(fitnessFunction);
	this.fitnessFunction = fitnessFunction;
}
public void setAcceptRandomIndividualFitnessThreshold(Fitness acceptRandomIndividualFitnessThreshold) {
	this.acceptRandomIndividualFitnessThreshold = acceptRandomIndividualFitnessThreshold;
}
public boolean areIndividualsInFitnessRange(Population population) {
	for(int i = indicesRange.low(); i <= indicesRange.high(); i++)
		if (population.getIndividual(i).getFitness().fitterThan(bestFitnessAllowed))
			return false;
	return true;
}
public void assertIndividualsInFitnessRange(Population population) {
	for(int i = indicesRange.low(); i <= indicesRange.high(); i++)
		Error.assertFalse(population.getIndividual(i).getFitness().fitterThan(bestFitnessAllowed));
}
public void promote(Individual individual, Population population) {
	promotionsTo.acceptPromotion(individual,population);
}
public void acceptPromotion(Individual individual, Population population) {
	if (shouldPromote(individual))
		promote(individual,population);
	else
		setIndividual(population,getIndexToKill(population),individual);
}
public void parentsOfPromoted(int[] parentIndices, Population population) {
	Error.notImplemented(); // must be implemented by subclass.  Not abstract so unit test can have constructor
}

public void promoteTooFitIndividuals(Population population) {
	for(int i = indicesRange.low(); i <= indicesRange.high(); i++) {
		final Individual individual = population.getIndividual(i);
		if (shouldPromote(individual)) {
			promote(individual,population);
			createRandomIndividual(i,population);
		}
	}
}
protected void createRandomIndividual(int index, Population population) {
	createRandomIndividual(0,index,population);
}
protected void createRandomIndividual(int level,int index, Population population) {
	Evolvable evolvable = getRandomEvolvable();
	Individual individual = population.makeIndividual(evolvable,getFitnessFunction());
	if (acceptRandomIndividualFitnessThreshold != null && !individual.getFitness().fitterThan(acceptRandomIndividualFitnessThreshold)  && level < MAX_TRIES) {
		createRandomIndividual(level+1,index,population);
		return;
	}
	newChild(individual); // in previous case assume no time consuming operations, so don't count as another child
	if (shouldPromote(individual) && level < MAX_TRIES) {
		promote(individual,population);
		createRandomIndividual(level+1,index,population);
		return;
	}
	if (level >= MAX_TRIES)
		Error.warning("gov.nasa.javaGenes.core.HFC.SubBreeder.createRandomIndividual() MAX_TRIES recursions!  Probably something wrong\n");
	setIndividual(population,index,individual);
}
public void breedOnce(Population population) {
	ChildMaker maker = childMakerProvider.getChildMaker(getTotalNumberOfKidsProduced());
	int[] parentIndices = getParentIndices(maker.numberOfParents(),population);
	makeChildren(maker,parentIndices,population);
}
protected void makeChildren(ChildMaker maker, int[] parentIndices, Population population) {
	Evolvable[] c = maker.makeChildren(population.makeIndividualArray(parentIndices));
	if (debug) {System.out.println("Children:");}
	for(int i = 0; i < c.length; i++){
		Individual individual = population.makeIndividual(c[i],getFitnessFunction());
		newChild(individual);
		maker.results(individual,population.makeIndividualArray(parentIndices));
		if (debug) {Utility.debugPrintln(individual.toString());}
		if (shouldPromote(individual)) {
			promote(individual,population);
			parentsOfPromoted(parentIndices,population);
		} else
			setIndividual(population,getIndexToKill(population),individual);
	}
}
protected void setIndividual(Population population, int index, Individual individual) {
	Error.assertTrue(isValidIndex(index));
	if (individual.getFitness().fitterThan(bestFitnessAllowed))
		Error.warning("too high of fitness in gov.nasa.javaGenes.HFC.setIndividual");
	population.setIndividual(index,individual);
}
public int[] getParentIndices(int number, Population population) {
	if (number <= 0)
		return new int[0];
	Error.assertTrue(numberOfIndices() >= number);
	int[] parentIndices = parentChooser.getParentIndices(number,indicesRange,population);
	Error.assertTrue(parentIndices.length == number);
	Error.assertTrue(areValidIndices(parentIndices));
	if (debug) {
		Utility.debugPrintln("Parents:");
		for(int i = 0; i < parentIndices.length; i++)
			Utility.debugPrintln(population.getIndividual(parentIndices[i]).toString());
	}
	return parentIndices;
}
public void setBreeder(Breeder breeder) {this.breeder = breeder;}
public void newChild(Individual individual) {breeder.newChild(individual);}
public int getTotalNumberOfKidsProduced() {return breeder.getTotalNumberOfKidsProduced();}
public Evolvable getRandomEvolvable() {return breeder.getRandomEvolvable();}

public void setPromotionsTo(SubBreeder subBreeder) {promotionsTo = subBreeder;}

public boolean shouldPromote(Individual individual) {
	return individual.getFitness().fitterThan(bestFitnessAllowed);
}

public void toDeathRow(int[] indices) {
	for(int i = 0; i < indices.length; i++) 
		toDeathRow(indices[i]);
}
public void toDeathRow(int index) {
	Error.assertTrue(isValidIndex(index));
	deathRow.add(new Integer(index)); // deathRow is a Set so duplicates won't happen
}
public int getIndexToKill(Population population) {
	int index = -1; // illegal value
	if (deathRow.isEmpty())
		index = grimReaper.getDeathRowIndex(null,indicesRange,population);
	else {
		Object[] array = deathRow.toArray();
		Integer indexToKill = (Integer)RandomNumber.getObjectFromArray(array);
		Error.assertTrue(deathRow.remove(indexToKill));
		index = indexToKill.intValue();
	}
	Error.assertTrue(isValidIndex(index));
	return index;
}
public boolean areValidIndices(int[] indices) {
	for(int i = 0; i < indices.length; i++)
		if (!isValidIndex(indices[i]))
			return false;
	return true;
}
public boolean isValidIndex(int index) {
	return indicesRange.isBetween(index);
}
/** assumes best fitness is lower value */
public void setBestFitnessAllowed(double value) {
	bestFitnessAllowed = new FitnessDouble(value);
}
public Fitness getBestFitnessAllowed() {
	Error.assertNotNull(bestFitnessAllowed);
	return bestFitnessAllowed;
}
public void incrementBestFitnessAllowed(double increment) {
	setBestFitnessAllowed(getBestFitnessAllowed().asDouble() + increment);
}
public void setIndexRange(int low, int high) {
	Error.assertTrue(low <= high);
	indicesRange = new IntegerInterval(low,high);
}
public int numberOfIndices() {
	return indicesRange.interval()+1;
}
public void setParentChooser(Tournament tournament) { 
	Error.assertNotNull(tournament);
	parentChooser = tournament;
}
public void setGrimReaper(AntiTournament antiTournament) {
	Error.assertNotNull(antiTournament);
	grimReaper = antiTournament;
}	
public FitnessFunction getFitnessFunction() {return fitnessFunction;}
 // just put out selected parameters so subClasses can use
public String toString() {
	return 
		" bestFitnessAllowed=" + bestFitnessAllowed +
		" lowerIndex=" + indicesRange.low() +
		" higherIndex=" + indicesRange.high();
}
}