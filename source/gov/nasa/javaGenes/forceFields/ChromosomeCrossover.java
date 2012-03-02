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

import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.core.ChildMaker;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.alsUtility.RandomNumber;

/**
Implements a uniform crossover operator where the value
of each double in the child has an equal probability of coming from the father
or the mother
*/
public class ChromosomeCrossover extends ChildMaker {

public ChromosomeCrossover() {}
public int numberOfParents() {return 2;}

public Evolvable[] makeChildren(Evolvable[] parents) {
	Error.assertTrue(parents.length == 2);
	Chromosome mom = (Chromosome)parents[0];
	Chromosome dad = (Chromosome)parents[1];
  Chromosome[] chromosome = new Chromosome[2];
  chromosome[0] = getNewChromosome(mom);
  chromosome[1] = getNewChromosome(dad);

  for(int i = 0; i < mom.numberOfArrays(); i++)
  for(int j = 0; j < mom.getSize(i); j++) {
  	double momValue = mom.getValue(i,j);
  	double dadValue = dad.getValue(i,j);
    if (RandomNumber.getBoolean()) {
    	chromosome[0].setValue(momValue,i,j);
    	chromosome[1].setValue(dadValue,i,j);
    } else {
    	chromosome[0].setValue(dadValue,i,j);
    	chromosome[1].setValue(momValue,i,j);
    }
  }

  return chromosome;
}

protected Chromosome getNewChromosome(Chromosome chromosome) { // should be re-implemented by subclasses
	return new Chromosome(chromosome);
}
}

