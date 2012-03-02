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


import java.lang.Math;
import java.io.Serializable;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.RandomNumber;

/**
Used to breed a population.
*/
public abstract class Breeder implements Serializable {
static public final boolean debug = false;
protected ChildMakerProvider childMakerProvider; 
protected double tournamentProbability = 1;
private FitnessFunction fitnessFunction;
private int index = 0; //number of individuals replaced in this generation.  Here, rather than local variable, for checkpointing.
private int totalNumberOfKidsProduced = 0;
/**
The argument contains many parameters including those necessary for breeding
*/
public Breeder(Parameters p) {
  childMakerProvider = p.childMakerProvider;
  tournamentProbability = p.tournamentProbability;
  setFitnessFunction(p.fitnessFunction);
}
public boolean useTournament() {
  return RandomNumber.getProbability(tournamentProbability);
}
public void setFitnessFunction(FitnessFunction f) {
  Error.assertTrue(f != null);
  fitnessFunction = f;
}
public FitnessFunction getFitnessFunction() {return fitnessFunction;}

/**
restore from a checkpoint
*/
public void stateRestore(TokenizeInput tokenizer) {
	index = tokenizer.getInteger();
}
/**
checkpoint
*/
public void stateSave(TokenizeOutput tokenizer) {
	tokenizer.putInteger(index);
}
/**
execute the breeding. Return the population produced.
*/
public Population breed (Population parents) {
  return breed(parents,parents.getSize());
}
public abstract Population breed(Population parents,int kidsPerGeneration);
public void newChild(Individual child) {
    newChildrenCreated(1);
}
public void newChildrenCreated(int number) {
	Error.assertTrue(number >= 0);
	index += number;
	totalNumberOfKidsProduced += number;
}
public boolean generationNotComplete(int kidsPerGeneration) {
    return index < kidsPerGeneration;
}
public void generationIsComplete() {
   index = 0;
}
public boolean generationJustStarting() {
    return index == 0;
}
public void setGenerationIndex(int value) {
    index = value;
}
public int getGenerationIndex() {
    return index;
}
public int getTotalNumberOfKidsProduced(){
    return totalNumberOfKidsProduced;
}
public void setTotalNumberOfKidsProduced(int value) {
    totalNumberOfKidsProduced = value;
}
/**
implements a two-way tournament.
@param p population to take tournament contenders from
@param alreadyChosen don't ever pick this individual. May be null.
*/
public Individual tournament (Population p, Individual alreadyChosen) {
    Individual first = p.pick(alreadyChosen);
    Individual second = p.pick(first, alreadyChosen);
    return chooseBest(first,second);
}
public Individual pickOne(Population p) {
  return p.pick();
}
protected Individual chooseBest(Individual first, Individual second) {
    return first.fitterThan(second) ? first : second;
}
/**
returns index of tournament loser
@param p population to take tournament contenders from
*/
public int antiTournamentGetIndex (Population p) {
    Individual first = p.pick();
    Individual second = p.pick (first);
    Individual candidate = chooseWorst(first,second);
    if (isProtected(candidate))
      return antiTournamentGetIndex(p);
    return p.getIndex(candidate);
}
protected Individual chooseWorst(Individual first, Individual second) {
  return !first.fitterThan(second) ? first : second;
}
public void reportOnGeneration(int generation, String filename) {}
/**
can protect certain individuals from losing an antiTournament and being replaced
*/
protected boolean isProtected(Individual i) {return false;}
}
