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
package gov.nasa.javaGenes.graph;


import java.util.Hashtable;
import gov.nasa.alsUtility.ManySamples;
import gov.nasa.alsUtility.IntegerInterval;
import gov.nasa.alsUtility.Sample;
import gov.nasa.javaGenes.core.Population;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.javaGenes.core.FitnessFunction;
import gov.nasa.javaGenes.core.Individual;

/**
Represents a population of evolvable Graph objects
*/
public class GraphPopulation extends Population {
/**
Generate a random population
*/
public GraphPopulation (GraphParameters parameters) {
    super(parameters.populationSize);
    IntegerInterval vertices = parameters.verticesInterval;
    IntegerInterval cycles = parameters.cyclesInterval;
    for (int i = 0; i < population.length; i++)
        population[i] = new GraphIndividual(
            new Graph(parameters.provider,vertices.random(),cycles.random()),parameters.fitnessFunction);
}
/**
create an empty population with int size members
*/
public GraphPopulation(int size) {super(size);}
/**
@return a new empty population with int size members
*/
public Population makePopulation(int size) {
    return new GraphPopulation(size);
}
/**
@return a new Individual that has a Graph evolvable.
*/
public Individual makeIndividual(Evolvable e, FitnessFunction f) {
    return new GraphIndividual((Graph)e,f);
}

/**
Add Sample objects to ManySamples samples that summarize the population.
Used for data analysis of evolution.

@see Sample
*/
public void fillSamples (ManySamples samples) {
    if (population.length <= 0) return;
    Sample fitness = new Sample();
    Sample cycles = new Sample();
    Individual best = null;
    Hashtable vertexCounts = new Hashtable();
    int numberOfVertices = 0;
    Hashtable edgeCounts = new Hashtable();
    int numberOfEdges = 0;
    for (int index = 0; index < population.length; index++){
    	Individual i = (Individual)population[index];
			if (best == null || i.fitterThan(best))
		    best = i;
			fitness.addDatum(i.getFitness().asDouble());
      Graph graph = (Graph)i.getEvolvable();
			numberOfVertices += countParts(graph.getVertexIterator(),vertexCounts);
			numberOfEdges += countParts(graph.getEdgeIterator(),edgeCounts);
			cycles.addDatum(graph.getNumberOfCycles());
    }

    samples.getSample("bestFitness").addDatum(best.getFitness().asDouble());
    samples.getSample("bestSize").addDatum(best.getEvolvable().getSize());
    samples.getSample("meanFitness").addDatum(fitness.getMean());
    samples.getSample("meanSize").addDatum((double)(numberOfEdges+numberOfVertices)/(double)population.length);
    samples.getSample("meanVertices").addDatum((double)numberOfVertices/(double)population.length);
    samples.getSample("meanEdges").addDatum((double)numberOfEdges/(double)population.length);
    samples.getSample("meanCycles").addDatum(cycles.getMean());
    samples.getSample("time").addDatum(getTime());

	fillEntropy(samples, vertexCounts, numberOfVertices, "vertex");
	fillEntropy(samples, edgeCounts, numberOfEdges, "edge");
}
}
