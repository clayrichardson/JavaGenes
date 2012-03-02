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

import java.lang.Double;
import gov.nasa.javaGenes.core.FitnessFunction;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.javaGenes.core.Fitness;
import gov.nasa.javaGenes.core.FitnessDouble;
import gov.nasa.javaGenes.core.Population;

public class LowestToHighestEnergyFitness extends FitnessFunction {
protected Potential potential;
protected ManyMultiBodiesForOneEnergy molecules;
protected String filename = "";
protected boolean doPerAtomEnergies = true;

public void setFilename(String f) {filename = f;}
public String getFilename() {return filename;}
protected LowestToHighestEnergyFitness(){} // for testing only

public LowestToHighestEnergyFitness(Potential p, ManyMultiBodiesForOneEnergy m) {
    potential = p;
    molecules = m;
}
/*
@return the number of clusters out of order (lowest energy to highest)
*/
public Fitness evaluateFitness (Evolvable evolvable){
    Chromosome chromosome = (Chromosome)evolvable;
    potential.setChromosome(chromosome);

    double[] energy = new double[molecules.size()];
    for(int i = 0; i < molecules.size(); i++) {
        energy[i] = potential.getEnergy(molecules.getMultiBodies(i));
        if (doPerAtomEnergies)
            energy[i] /= molecules.getMultiBodies(i).getNumberOfAtoms();
    }
    return new FitnessDouble(outOfOrder(energy));
}
protected double outOfOrder(double[] energy) {
    int fitness = 0;
    for(int i = 0; i < energy.length; i++)
        for(int j = i + 1; j < energy.length; j++)
            if (energy[i] >= energy[j])
                fitness++;
    return fitness;
}
public void report (Population population){
}
public String toString() {
    return getClass() + ", Clusters = " + getFilename()  +
            ", Potential = " + potential;
}
public void setDoPerAtomEnergies(boolean value) {
    doPerAtomEnergies = value;
}
}



