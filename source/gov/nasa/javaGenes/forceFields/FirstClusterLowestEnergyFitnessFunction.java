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
//  Created by Al Globus on Wed Oct 30 2002.
package gov.nasa.javaGenes.forceFields;
import java.lang.Double;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.core.FitnessFunction;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.javaGenes.core.Fitness;
import gov.nasa.javaGenes.core.Population;
import gov.nasa.javaGenes.core.FitnessDouble;

public class FirstClusterLowestEnergyFitnessFunction extends FitnessFunction {
protected Potential potential;
protected ManyMultiBodiesForOneEnergy molecules;
protected String filename = "";
protected double cliff = 1;

public void setFilename(String f) {filename = f;}
public String getFilename() {return filename;}
protected FirstClusterLowestEnergyFitnessFunction(){} // for testing only

public FirstClusterLowestEnergyFitnessFunction(Potential p, ManyMultiBodiesForOneEnergy m, double inCliff) {
    cliff = inCliff;
    potential = p;
    molecules = m;
}
public void add(MultiBodiesForOneEnergy m) {
  molecules.add(m);
}
/*
@return if the first cluster is the minimum energy, return zero.  Otherwise, return 
cliff + sum-of-squares for the difference for cluster energies below first cluster-energy.
This is useful when you want to make sure the potential has a particular structure at
an energy minimum (e.g., to insure bond length is correct).
*/
public Fitness evaluateFitness (Evolvable evolvable){
    Chromosome chromosome = (Chromosome)evolvable;
    potential.setChromosome(chromosome);
    Error.assertTrue(molecules.size() > 1);

    double shouldBeMinimumEnergy = potential.getEnergy(molecules.getMultiBodies(0));
    double fitness = 0;
    for(int i = 1; i < molecules.size(); i++) {
        double energy = potential.getEnergy(molecules.getMultiBodies(i));
        if (energy < shouldBeMinimumEnergy) {
            double difference = shouldBeMinimumEnergy - energy;
            fitness += difference * difference;
        }
    }

    if (fitness > 0)
        fitness += cliff;
    return new FitnessDouble(fitness);
}
public void report (Population population){
}
public String toString() {
    return getClass() + ", Clusters = " + getFilename()  +
            ", Potential = " + potential + ",cliff = " + cliff;
}
}



