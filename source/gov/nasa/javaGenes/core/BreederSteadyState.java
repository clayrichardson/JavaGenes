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

import gov.nasa.alsUtility.Utility;

/**
implements steady state breeding
*/
public class BreederSteadyState extends Breeder {

public BreederSteadyState(Parameters p) {super(p);}

public Population breed (Population parents, int kidsPerGeneration) {
     while (generationNotComplete(kidsPerGeneration)){
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
      if (debug) {
        Utility.debugPrintln("Parents:");
        for(int i = 0; i < theParents.length; i++)
          Utility.debugPrintln(theParents[i].toString());
      }
      Evolvable[] c = maker.makeChildren(theParents);
      for(int i = 0; i < c.length; i++){
        if (generationNotComplete(kidsPerGeneration)){
            Individual individual = parents.makeIndividual(c[i],getFitnessFunction());
            parents.setIndividual(antiTournamentGetIndex(parents),individual);
            maker.results(individual,theParents);
            newChild(individual);
        }
      }
      if (debug) {
        Utility.debugPrintln("Children:");
        for(int i = 0; i < c.length; i++)
          Utility.debugPrintln(c[i].toString());
      }
      Checkpointer.ok();
    }
    generationIsComplete();
    return parents;
}
public String toString() {return "BreederSteadyState";}
}

