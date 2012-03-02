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

//  Created by Al Globus on Wed Jun 12 2002.

import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.alsUtility.RandomNumber;

public class ChromosomeXoverWithMutation extends ChromosomeIntervalCrossover {
protected ChromosomeMutation mutator;
protected double mutationProbability = 1; // between 0 and 1

/**
@arg inAlleles the alleles the chromosomes come from.
@arg inMutationProbability the probability of mutation after crossover
@arg standardDeviation the standard deviation of each mutation as a fraction of the interval in the AlleleTemplate
@arg alleleFrequency the (average) fraction of alleles that will be mutated
*/
public ChromosomeXoverWithMutation(AlleleTemplate inAlleles, double inMutationProbability, double standardDeviation, double alleleFrequency) {
    super(inAlleles);
    mutator = new ChromosomeMutation(inAlleles,standardDeviation,alleleFrequency);
    mutationProbability = inMutationProbability;
}
public String toString() {
	return "\nChromosomeXoverWithMutation mutationProbability:" + mutationProbability + " " + super.toString() + " " + mutator.toString();
}
public Evolvable[] makeChildren(Evolvable[] parents) {
    Evolvable[] children = super.makeChildren(parents);
    for(int i = 0; i < children.length; i++)
        if (RandomNumber.getProbability(mutationProbability)) {
            Evolvable p[] = {children[i]};
            children[i] = mutator.makeChildren(p)[0];
        }
    return children;
}
public void setLimitToOriginalInterval(boolean limit) {
    super.setLimitToOriginalInterval(limit);
    mutator.setLimitToOriginalInterval(limit);
}

}
