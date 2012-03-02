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
//  Created by Al Globus on Wed Jan 29 2003.
package gov.nasa.javaGenes.hillClimbing;

import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.core.Individual;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.javaGenes.core.Population;
import gov.nasa.javaGenes.core.Parameters;
import gov.nasa.javaGenes.core.ChildMaker;
import gov.nasa.javaGenes.core.Checkpointer;
import gov.nasa.javaGenes.core.Individual;

/**
Population size must be 2.  One climbs and the other holds the best so far.
*/
public class Breeder extends gov.nasa.javaGenes.core.Breeder {
private final int CURRENT = 0;
private final int BEST_SO_FAR = 1;
private static final int POPULATION_SIZE = 2;

protected Accepter accepter;
protected RestartPolicy restartPolicy;
protected ChildMaker randomChildMaker;

public Breeder(Parameters p,Accepter inAccepter,RestartPolicy inRestartPolicy,ChildMaker inRandomChildMaker) {
    super(p);
    accepter = inAccepter; Error.assertTrue(accepter != null);
    restartPolicy = inRestartPolicy; Error.assertTrue(restartPolicy != null);
    randomChildMaker = inRandomChildMaker; 
    Error.assertTrue(randomChildMaker != null || (restartPolicy instanceof RestartNever));
}
static public int requiredPopulationSize() {return POPULATION_SIZE;}

public Population breed (Population parents, int kidsPerGeneration) {
    // this can happen in the initial population
    if (parents.getIndividual(CURRENT).fitterThan(parents.getIndividual(BEST_SO_FAR)) ){
        Individual temp = parents.getIndividual(CURRENT);
        parents.setIndividual(CURRENT,parents.getIndividual(BEST_SO_FAR));
        parents.setIndividual(BEST_SO_FAR,temp);
    }
    Error.assertTrue(parents.getSize() == POPULATION_SIZE);
    while (generationNotComplete(kidsPerGeneration)) {
        ChildMaker maker = null;
        if (restartPolicy.shouldRestart()) {
            restartPolicy.restarting();
            maker = randomChildMaker;
            setTotalNumberOfKidsProduced(0);
        } else {
            maker = childMakerProvider.getChildMaker(getTotalNumberOfKidsProduced());
        }
        Error.assertTrue(maker.numberOfParents() == 1);
      
        Individual[] theParents = new Individual[1];
        Evolvable[] p = new Evolvable[1];

        theParents[0] = parents.getIndividual(CURRENT);
        p[0] = theParents[0].getEvolvable();
        Evolvable kidEvolvable = maker.makeChildren(p)[0];
        Individual kid = parents.makeIndividual(kidEvolvable,getFitnessFunction());
        maker.results(kid,theParents);
        restartPolicy.childCreated(kid,theParents[0]);
        if (accepter.accept(kid.getFitness(),theParents[0].getFitness())) {
            restartPolicy.lastChildAccepted();
            parents.setIndividual(CURRENT,kid);
            if (kid.fitterThan(parents.getIndividual(BEST_SO_FAR)))
                parents.setIndividual(BEST_SO_FAR,kid);
        }
        newChild(kid);
        Checkpointer.ok();
    }
    generationIsComplete();
    return parents;
}
public String toString() {
    return this.getClass().toString() + ": accepter = " + accepter.toString()
            + " RestartPolicy = " + restartPolicy.toString()
            + " RandomChildMaker = " + (randomChildMaker == null ? "null" : randomChildMaker.toString());
}
}


