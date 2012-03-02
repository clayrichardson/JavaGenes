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
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.Utility;

/**
Implement elitist breeding.
*/
public class BreederElitist extends Breeder {
protected Population children;
protected int preservedNumber = 1;
protected int internalGenerationsPerExternalGeneration = 1; // used to implement kidsPerGeneration
protected int internalGeneration = 0; // used for start/restart from serialization file

public String toString() {
    return "BreederElitist preservedNumber = " + preservedNumber;
}
public BreederElitist(Parameters p, int inPreservedNumber) {
    super(p);
    preservedNumber = inPreservedNumber;
}

/**
@arg kidsPerGeneration should be an even multiple of parents.getSize() - preservedNumber.  Otherwise
a warning will be issued.
*/
public Population breed(Population parents, int kidsPerGeneration) {
    if (kidsPerGeneration % (parents.getSize()-preservedNumber) != 0)
        Error.warning("kidsPerGeneration = " + kidsPerGeneration + " cannot be exactly implemented");
    internalGenerationsPerExternalGeneration = kidsPerGeneration / (parents.getSize()-preservedNumber);
    Population p = parents;
    for(;internalGeneration < internalGenerationsPerExternalGeneration; internalGeneration++)
        p = breed(p);
    internalGeneration = 0;
    return p;
}
/**
 @return a new population
*/
public Population breed (Population parents) {
    if (generationJustStarting()) {
        children = parents.makePopulation(parents.getSize());
        setPreserved (parents, children, preservedNumber);
        setGenerationIndex(preservedNumber);
    }
    while (generationNotComplete(parents.getSize())){
        ChildMaker maker = childMakerProvider.getChildMaker(getTotalNumberOfKidsProduced());
        Individual[] theParents = new Individual[maker.numberOfParents()];
        for(int i = 0; i < theParents.length; i++){
            do {
                if (useTournament())
                    theParents[i] = tournament(parents,null);
                else
                    theParents[i] = pickOne(parents);
            } while(!Utility.areDifferent(theParents,true));
        }
        Evolvable[] c = maker.makeChildren(theParents);
        for(int i = 0; i < c.length; i++){
            if (generationNotComplete(parents.getSize())){
                Individual individual = parents.makeIndividual(c[i],getFitnessFunction());
                children.setIndividual(getGenerationIndex(),individual);
                maker.results(individual,theParents);
                newChild(individual);
            }
        }
        Checkpointer.ok();
    }
    generationIsComplete();
    return children;
}

/** the best parents become children without modifications */
public void setPreserved (Population parents, Population children, int preservedNumber) {
    Error.assertTrue(preservedNumber <= parents.getSize());
    Error.assertTrue(children.getSize() == parents.getSize());
    if (preservedNumber <= 0)
        return;

    Population live = parents.makePopulation(preservedNumber); // holds best individuals
    int i;
    // grab first individuals
    for (i = 0; i < preservedNumber; i++)
        live.setIndividual(i, parents.getIndividual(i));

    // replace worst individual in live when better one comes along    
    int worstIndividualIndex = live.worstIndividualIndex();
    Fitness worstFitness = live.getIndividual(worstIndividualIndex).getFitness();
    for (; i  < children.getSize(); i++)  {
        Individual candidate = parents.getIndividual(i);
        if (candidate.getFitness().fitterThan(worstFitness)) {
	    live.setIndividual(worstIndividualIndex, candidate);
	    worstIndividualIndex = live.worstIndividualIndex();
	    worstFitness = live.getIndividual(worstIndividualIndex).getFitness();
	}
    } 
    
    // copy live into children
    for (int j = 0; j < live.getSize(); j++)
        children.setIndividual(j, live.getIndividual(j));
}
}
