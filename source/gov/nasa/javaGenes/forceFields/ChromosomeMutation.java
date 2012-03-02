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
Mutates some fraction of the alleles with a random number chosen
from a Gaussian distribution
*/
public class ChromosomeMutation extends ChildMaker {
protected boolean limitToOriginalInterval = true;
protected AlleleTemplate alleles;
/**
The standard deviation to use expressed as a fraction of the allele interval
*/
protected double standardDeviation;

// testing only
protected ChromosomeMutation(){}
/**
average fraction of the alleles to be used.  Whether any alleles is to be used is a randomized event.
If this too small, can get effective infinite loop since at least one change is required
for the makeChildren() method to complete!
Value defaults to 1.
*/
protected double frequency = 1;
public ChromosomeMutation(AlleleTemplate a) {
    this(a,0);
}
/**
@param sd standard deviation to use
*/
public ChromosomeMutation(AlleleTemplate a, double sd) {
	alleles = a;
  standardDeviation = sd;
}
/**
@param sd standard deviation to use
@param f average fraction of the alleles to be used.  Whether any alleles is to be used is a randomized event.
*/
public ChromosomeMutation(AlleleTemplate a, double sd, double f) {
	alleles = a;
  standardDeviation = sd;
  frequency = f;
  Error.assertTrue(frequency > 0);
}
public int numberOfParents() {return 1;}
public void setLimitToOriginalInterval(boolean limit) {
  limitToOriginalInterval = limit;
}
public String toString() {
	return "ChromosomeMutation standardDeviation: " + standardDeviation + " frequency: " + frequency;
}
/**
If this.fraction too small, can get effective infinite loop since at least one change is required
for the method to complete!
*/
public Evolvable[] makeChildren(Evolvable[] parents) {
    Error.assertTrue(parents.length == 1);
    Chromosome mom = (Chromosome)parents[0];
    Chromosome[] chromosome = new Chromosome[1];
    chromosome[0] = getNewChromosome(alleles);
    boolean changed = false;
    do {
        for(int i = 0; i < alleles.numberOfArrays(); i++)
        for(int j = 0; j < alleles.getSize(i); j++) {
            Allele allele = alleles.getAllele(i,j);
            if (allele.dontEvolve()) {
                chromosome[0].setValue(allele.getNoEvolutionValue(),i,j);
                continue;
            }
        
            double momValue = mom.getValue(i,j);
            double value;
            if (RandomNumber.getDouble() > frequency)
                value = momValue;
            else
                value = allele.getRandomGaussianValue(momValue,standardDeviation);
            if (limitToOriginalInterval)
                value = forceInsideAlleleLimits(allele,value,momValue);
            chromosome[0].setValue(value,i,j);
            if (value != momValue)
                changed = true;
        }
    } while (!changed);
    return chromosome;
}
protected double forceInsideAlleleLimits(Allele allele, double value, double parentValue) {
    if (allele.valueFits(value))
        return value;
    if (!allele.valueFits(parentValue))
        return allele.getRandomValue();
    else if (value < parentValue)
        return allele.getRandomValueBelow(parentValue);
    else
        return allele.getRandomValueAbove(parentValue);
}
protected Chromosome getNewChromosome(AlleleTemplate alleles) { // should be re-implemented by subclasses
	return new Chromosome(alleles);
}
}


