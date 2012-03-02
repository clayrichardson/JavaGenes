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

/**
 this fitness function is a product of fitness functions times their weights with one added to
 the fitness.  One is the minimum fitness (the objectives are assumed to have positive fitness).
 Weights are generally expected be less than one, although that is not required.  If the weighted
 fitenss before adding one is smaller than one, then the weight will have a strong effect on
 the total fitness.  If the weighted fitness before adding one is large, then the scaling by
 weight will have little effect.
 <p>
 One is added because the best fitness is assumed to be zero and multiplication changes
 direction when below one.
 <p>
 This approach was recommended by Greg Hornby
*/
public class ProductFitnessFunction extends FitnessFunctionMultiObjectiveToOne  {

public Fitness evaluateFitness (Evolvable evolvable) {
    double fitness = 1;
    for(int i = 0; i < weights.size(); i++){
        double weight = getWeight(i);
        double f = 1 + getFitnessFunction(i).evaluateFitness(evolvable).asDouble()*weight;
        if (f < 1)
            f = 1;
		fitness *= f;
    }
    return new FitnessDouble(fitness);
}
}

