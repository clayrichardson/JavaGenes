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
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.alsUtility.RandomNumber;
import java.lang.Math;

/**
Mutates all alleles with a random number chosen
from a Gaussian distribution with the standard deviation
taken by the difference in two of the children
*/
public class Mutation3Parents extends ChromosomeMutation {
protected double standardDeviationFactor = 1;

public Mutation3Parents(AlleleTemplate a) {
    this(a,1);
}
public Mutation3Parents(AlleleTemplate a, double inStandardDeviationFactor) {
    super(a);
    standardDeviationFactor = inStandardDeviationFactor;
}
public Evolvable[] makeChildren(Evolvable[] parents) {
    Error.assertTrue(parents.length == 3);
    Chromosome parent = (Chromosome)parents[0];
    Chromosome limit1 = (Chromosome)parents[1];
    Chromosome limit2 = (Chromosome)parents[2];
    
    Chromosome[] chromosome = new Chromosome[1];
    chromosome[0] = getNewChromosome(alleles);
    for(int i = 0; i < alleles.numberOfArrays(); i++)
    for(int j = 0; j < alleles.getSize(i); j++) {
        Allele allele = alleles.getAllele(i,j);
        if (allele.dontEvolve()) {
            chromosome[0].setValue(allele.getNoEvolutionValue(),i,j);
            continue;
        }
    
        double mean = parent.getValue(i,j);
        double sd = Math.abs(limit1.getValue(i,j)-limit2.getValue(i,j)) * standardDeviationFactor;
        double value = allele.getRandomGaussianValue(mean,sd);
        if (limitToOriginalInterval)
            value = forceInsideAlleleLimits(allele,value,mean);
        chromosome[0].setValue(value,i,j);
    }
    return chromosome;
}
public int numberOfParents() {return 3;}
public String toString() {return "Mutation3Parents standardDeviationFactor: " + standardDeviationFactor;}
}


