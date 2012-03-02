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
import gov.nasa.alsUtility.DoubleInterval;

/**
Implements a form of crossover were each allele in the child is a function
of a random number and values in the mother and father.  An interval
is constructed between the values in the mother and father.  That interval
is extended by the value in this.extension.  Values larger than 1 will result
in the interval becoming larger.  The value for the child is then chosen at random
from within the interval.  However, if the father and mother double values for a
particular allele are equal, then an interval centered around this value
with a size this.whenEqualExtension * alleles-interval-size and then truncated
by the limits of the allele will be used to choose a random number.
*/
public class ChromosomeIntervalCrossover extends ChildMaker {
protected AlleleTemplate alleles;
/**
interval crossover fraction of distance between
*/
protected double extension = 2.0;

public ChromosomeIntervalCrossover(AlleleTemplate a) {alleles = a;}
protected boolean limitToOriginalInterval = true;
public void setLimitToOriginalInterval(boolean limit) {
  limitToOriginalInterval = limit;
}
public void setExtension(double e) {
  extension = e;
}
public int numberOfParents() {return 2;}
public String toString() {
	return "ChromosomeIntervalCrossover extension: " + extension;
}
public Evolvable[] makeChildren(Evolvable[] parents) {
    Error.assertTrue(parents.length == 2);
    Chromosome mom = (Chromosome)parents[0];
    Chromosome dad = (Chromosome)parents[1];
    DoubleInterval interval = new DoubleInterval();
  Chromosome chromosome = getNewChromosome(alleles);

  for(int i = 0; i < alleles.numberOfArrays(); i++)
  for(int j = 0; j < alleles.getSize(i); j++) {
    Allele allele = alleles.getAllele(i,j);
    if (allele.dontEvolve()) {
      chromosome.setValue(allele.getNoEvolutionValue(),i,j);
      continue;
    }
    double momValue = mom.getValue(i,j);
    double dadValue = dad.getValue(i,j);
    interval.set(momValue,dadValue);
    interval.expand(extension);
    if (limitToOriginalInterval)
      interval.limitTo(allele.getInterval());
    if (interval.interval() == 0.0)
      chromosome.setValue(interval.high(),i,j);
    else
      chromosome.setValue(interval.random(),i,j);
  }

  Evolvable[] array = new Evolvable[1];
  array[0] = chromosome;
  return array;
}

protected Chromosome getNewChromosome(AlleleTemplate alleles) { // should be re-implemented by subclasses
	return new Chromosome(alleles);
}
}