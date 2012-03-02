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

import gov.nasa.javaGenes.core.Parameters;

/**
run a genetic algorithm with a Chromosome (two-dimensional array) representation
*/
public class RunChromosome extends gov.nasa.javaGenes.core.Run {
public RunChromosome() {}

/**
get ChromosomeParameters and ChromosomePopulation objects

@param arguments should be empty
*/
protected void startSpecialized(String[] arguments) {
    ChromosomeParameters p = getChromosomeParameters(arguments);
    parameters = (Parameters)p;
    generationTimer.start();
    population = new ChromosomePopulation(p);
    reporter = new ChromosomeReporter((Parameters)p);
    if (p.immigrants != null)
        p.immigrants.immigrate((ChromosomePopulation)population,p.alleles,p.fitnessFunction);
    generationTimer.stop();
}
public ChromosomeParameters getChromosomeParameters(String[] arguments) {
    return new ChromosomeParameters();
}
}

