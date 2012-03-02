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

import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.ExtendedVector;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.javaGenes.core.ChildMaker;
import gov.nasa.javaGenes.core.Evolvable;

/**
implements the crossover operator described in "JavaGenes: Evolving Graphs with Crossover,"
Al Globus, Sean Atsatt, John Lawton, and Todd Wipke and found at
http://www.nas.nasa.gov/~globus/papers/JavaGenes/paper.html
*/
public abstract class TwoVertexGraphCrossover extends ChildMaker {
private static boolean debug = false;
public int numberOfParents() {return 2;}
public Evolvable[] makeChildren(Evolvable[] parents) {
  Evolvable[] kids = crossover((Graph)parents[0],(Graph)parents[1]);
  return kids;
}
/**
Mate 

@param e the Graph object to make with
@return an array of length 2 with the children in it
@see #randomDivision
@see BrokenGraph
@see #randomMerge
*/
public Evolvable[] crossover(Graph father, Graph mother) {
    if (debug) {
    	System.out.println("father: " + father);
   		System.out.println("mother: " + mother);
    }
    BrokenGraph[] fatherParts = randomDivision(father);
    if (fatherParts == null) return new Evolvable[0];
    if (debug) {
    	System.out.println("father part 0: " + fatherParts[0]);
    	System.out.println("father part 1: " + fatherParts[1]);
    }
    BrokenGraph[] motherParts = randomDivision(mother);
    if (motherParts == null) return new Evolvable[0];
    if (debug) {
    	System.out.println("mother part 0: " + motherParts[0]);
    	System.out.println("mother part 1: " + motherParts[1]);
    }
    Evolvable[] kids = new Evolvable[2];
    if (fatherParts[0].getGraph().canMate(motherParts[1].getGraph())) {
    	kids[0] = randomMerge(fatherParts[0],motherParts[1]);
    	Error.assertTrue(fatherParts[1].getGraph().canMate(motherParts[0].getGraph()));
      kids[1] = randomMerge(fatherParts[1],motherParts[0]);
    } else {
    	Error.assertTrue(fatherParts[0].getGraph().canMate(motherParts[0].getGraph()));
    	kids[0] = randomMerge(fatherParts[0],motherParts[0]);
    	Error.assertTrue(fatherParts[1].getGraph().canMate(motherParts[1].getGraph()));
      kids[1] = randomMerge(fatherParts[1],motherParts[1]);
    }
    if (debug) {
    	System.out.println("kid 0: " + kids[0]);
    	System.out.println("kid 1: " + kids[1]);
    }
    if (kids[0] == null && kids[1] == null)
    	return new Evolvable[0];
    if (kids [0] == null) {
    	Evolvable[] oneKid = new Evolvable[1];
      oneKid[0] = kids[1];
      return oneKid;
    }
    if (kids [1] == null) {
    	Evolvable[] oneKid = new Evolvable[1];
      oneKid[0] = kids[0];
      return oneKid;
    }
    return kids;
}
/**
Randomly rip a copy of this in two.

@return an array of length 2 with the pieces in it.
*/
public BrokenGraph[] randomDivision(Graph g) {
    BrokenGraph[] brokenGraphs = {new BrokenGraph(g.getClass()), new BrokenGraph(g.getClass())};  // return value
    Graph[] parts = new Graph [2];
    parts[0] = brokenGraphs[0].getGraph();
    parts[1] = brokenGraphs[1].getGraph();
    Graph graph = g.deepCopyGraph(); // make a copy we can destroy during processing
    ExtendedVector brokenEdges = new ExtendedVector(); // list of edges broken

    // find the edges to be broken
    Vertex[] divideBetween = getVerticesForDivision(graph);
    if (divideBetween[0] == null || divideBetween[1] == null)
    	return null;
    while (true) {
        Trail trail = graph.getTrailBetween(divideBetween[0],divideBetween[1]);
        if (trail == null)
            break;
        Edge e = trail.getRandomEdge();
        brokenEdges.addElement(e);
        graph.remove(e);
        e.getVertex(0).removeEdge(e);
        e.getVertex(1).removeEdge(e);
    }
    
    // create subgraphs
    parts[0].add(graph.getConnectedSubgraph(divideBetween[0]));
    parts[1].add(graph.getConnectedSubgraph(divideBetween[1]));
    
    // add new BrokenEdges to each part, although sometimes we must restore edges to one subgraph
    parts[0].setVertexMarks(true);
    parts[1].setVertexMarks(false);
    for (EdgeIterator i = new EdgeIterator(brokenEdges); i.more(); i.next()) {
    	Edge edge = i.edge();
      Vertex v0 = edge.getVertex(0);
      Vertex v1 = edge.getVertex(1);
      if (v0.getMark()){
				if (!v1.getMark()){ // edge really should be broken
					brokenGraphs[0].add(new BrokenEdge(edge,v0));
					brokenGraphs[1].add(new BrokenEdge(edge,v1));
				} else { // both vertices of edge ended out in one subgraph, put edge back in appropriate graph
					parts[0].add(edge);
          if (edge instanceof DirectedEdge) {
          	((DirectedVertex)v0).addOutputEdge((DirectedEdge)edge);
            ((DirectedVertex)v1).addInputEdge((DirectedEdge)edge);
          } else {
						v0.add(edge);
						v1.add(edge);
          }
				}
    	} else {
				if (v1.getMark()){ // edge really should be broken
					brokenGraphs[1].add(new BrokenEdge(edge,v0));
					brokenGraphs[0].add(new BrokenEdge(edge,v1));
				} else { // both vertices of edge ended out in one subgraph, put edge back in appropriate graph
        	parts[1].add(edge);
         	if (edge instanceof DirectedEdge) {
          	((DirectedVertex)v0).addOutputEdge((DirectedEdge)edge);
            ((DirectedVertex)v1).addInputEdge((DirectedEdge)edge);
          } else {
						v0.add(edge);
						v1.add(edge);
          }
				}
			}
	}
  return brokenGraphs;
}
protected Vertex[] getVerticesForDivision(Graph graph) {
	Vertex[] divideBetween = new Vertex[2];
  Edge edge = graph.getRandomEdge();
  divideBetween[0] = edge.getVertex(0);
  divideBetween[1] = edge.getVertex(1);
  return divideBetween;
}
/**
merge two BrokenGraph objects and return the resulting graph. Merge the
BrokenEdge objects of each BrokenGraph at random. Maintain edge compatibility
where possible. If one BrokenGraph has excess BrokenEdge objects, randomly
discard the excess or connect to a random vertex in the other BrokenGraph.
*/
public Graph randomMerge(BrokenGraph gg1, BrokenGraph gg2) {
    BrokenGraph g1 = gg1.deepCopyBrokenGraph(); // avoid accidental cross links between disparate graphs
    BrokenGraph g2 = gg2.deepCopyBrokenGraph();

    Graph graph = null;
    try {
    	graph = (Graph)gg1.getGraphClass().newInstance();
    } catch (Exception e) {Error.fatal(e);}
    graph.add(g1.getGraph());
    graph.add(g2.getGraph());
    while(g1.brokenEdges.size() != 0 || g2.brokenEdges.size() != 0) {
    	BrokenGraph first;
    	BrokenGraph second;
    	if (g1.brokenEdges.size() == 0) {
    		first = g2;
    		second = g1;
        if (debug) System.out.println("g2,g1");
      } else if (g2.brokenEdges.size() == 0) {
    		first = g1;
    		second = g2;
        if (debug) System.out.println("g1,g2");
    	} else if (RandomNumber.getBoolean()) {
    		first = g1;
    		second = g2;
        if (debug) System.out.println("g1,g2");
     } else {
    		first = g2;
    		second = g1;
        if (debug) System.out.println("g2,g1");
      }
      BrokenEdge b1 = (BrokenEdge)first.brokenEdges.getRandomElement();
      Error.assertTrue(b1 != null);
      first.brokenEdges.removeElement(b1);
      processInitialBrokenEdge(graph,b1,second);
	}
  if (debug)
  	System.out.println("merged graph: " + graph);
	if (graph.isLegal())
  	return graph;
  else
  	return null;
}
/**
find and connect a second broken edge
@param graph graph being created
@param b1 the first broken edge to be connected
@param second graph from which the second broken edge must come
*/
public abstract void processInitialBrokenEdge(Graph graph, BrokenEdge b1, BrokenGraph second);

/**
is it essential that is broken edge be satisfied during crossover.
@return false by default.  Subclasses must redefine.
*/
public boolean isRequired(BrokenEdge edge) {return false;}
}
