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

import java.lang.Math;
import gov.nasa.alsUtility.DoubleInterval;

/**
this class is an interface to the jiggle package that does graph layout for viewing.
*/
public class GraphLayout implements java.io.Serializable {
// search parameters
public int iterations = 100;
public int maxLineSearchIterations = 1000;
public double vertexSize = 0.1;
public double edgeLength = 1.4;
public double border = 1.0;
public double BarnesHutTheta = 0.9;
public double accuracyOfLineSearch = 0.5;
public double restartThreshold = 0.2;

public String toString() {return "GraphLayout";}
/**
provided xyz locations for every vertex in the graph in such a way that the graph looks ok
in two dimensions
*/
public void layout2d(Graph graph) {
    jiggle.Graph g = makeGraph(graph);
    scramble(g,graph.getVerticesSize()*edgeLength + border);
    jiggle.ForceModel model = makeForceModel(g);
    jiggle.FirstOrderOptimizationProcedure optimization = makeOptimization(g,model);
    for(int i = 0; i < iterations; i++)
        optimization.improveGraph();
    updatePositions(g,graph);
}
/**
used before layout to turn a JavaGenes graph into a jiggle graph
*/
protected jiggle.Graph makeGraph(Graph graph) {
    graph.numberVertices(0);
    jiggle.Graph g = new jiggle.Graph(3);
    for(VertexIterator v = graph.getVertexIterator(); v.more(); v.next())
        g.insertVertex();
    for(EdgeIterator e = graph.getEdgeIterator(); e.more(); e.next()) {
        Edge edge = e.edge();
        Vertex v0 = edge.getVertex(0);
        Vertex v1 = edge.getVertex(1);
        g.insertEdge(g.vertices[v0.getNumber()],g.vertices[v1.getNumber()]);
    }
    for (int i = 0; i < g.numberOfVertices; i++) {
        double size [] = g.vertices[i].getSize();
        size[0] = vertexSize;
        size[1] = vertexSize;
    }
    return g;
}
protected jiggle.ForceModel makeForceModel(jiggle.Graph graph) {
    double k = Math.max (1, edgeLength);
    jiggle.SpringLaw springLaw = new jiggle.QuadraticSpringLaw (graph, k);
    jiggle.VertexVertexRepulsionLaw vvRepulsionLaw = new jiggle.InverseSquareVertexVertexRepulsionLaw (graph, k);
    vvRepulsionLaw.setBarnesHutTheta (BarnesHutTheta);
    jiggle.ForceModel model = new jiggle.ForceModel (graph);
    model.addForceLaw (springLaw);
    model.addForceLaw (vvRepulsionLaw);
    model.addConstraint (new jiggle.ProjectionConstraint(graph, 2));
    return model;
}
protected jiggle.FirstOrderOptimizationProcedure makeOptimization(jiggle.Graph graph, jiggle.ForceModel model) {
    double accuracy = Math.max (accuracyOfLineSearch, 0.00000001);
    jiggle.FirstOrderOptimizationProcedure optimization =
    new jiggle.ConjugateGradients (graph, model, accuracy, restartThreshold);
    optimization.setMaxLineSearchIterations(maxLineSearchIterations);
    optimization.setConstrained (true);
    return optimization;
}
/**
set the xyz locations of vertices in a graph to random values

@param size edge length of the cube within which all vertices will fall
*/
protected void scramble (jiggle.Graph graph, double size) {
for (int i = 0; i < graph.numberOfVertices; i++) {
    double coords [] = graph.vertices [i].getCoords ();
    coords [0] = Math.random () * size;
    coords [1] = Math.random () * size;
    coords [2] = Math.random () * size;
}
}

/**
put the xy is the locations of each vertex near the points in a three-dimensional grid
*/
protected void grid (jiggle.Graph graph, double size) {
    DoubleInterval interval = new DoubleInterval(0,edgeLength*0.1);
    CoordinateProvider provider = new CoordinateProvider(edgeLength, size);
    for (int i = 0; i < graph.numberOfVertices; i++) {
        double coords [] = graph.vertices [i].getCoords ();
        coords [0] = provider.get(0) + interval.random();
        coords [1] = provider.get(1) + interval.random();
        coords [2] = provider.get(2) + interval.random();
        provider.next();
    }
}
protected class CoordinateProvider {
    protected double edge;
    protected double maximum;
    protected double[] position = new double[3];
    public CoordinateProvider(double edgeLength, double size) {
        edge = edgeLength;
        maximum = size;
    }
    public double get(int index) {
        return position[index];
    }
    public void next() {
        if (position[0]+edge > maximum && position[1]+edge > maximum) {
            position[0] = 0;
            position[1] = 0;
            position[2] += edge;
        } else if (position[1]+edge > maximum) {
            position[0] += edge;
            position[1] = 0;
        } else
            position[1] += edge;
    }
}

/**
called after layout to move xyz locations to a JavaGenes graph
*/
protected void updatePositions(jiggle.Graph from, Graph to) {
    for (int i = 0; i < from.numberOfVertices; i++) {
        double coordinates[] = from.vertices[i].getCoords();
        Vertex vertex = to.getVertex(i);
        vertex.setXyz(coordinates);
    }
}
}