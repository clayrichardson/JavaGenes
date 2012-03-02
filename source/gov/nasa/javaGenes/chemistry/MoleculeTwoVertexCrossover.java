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
package gov.nasa.javaGenes.chemistry;

import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.javaGenes.graph.TwoVertexGraphCrossover;
import gov.nasa.javaGenes.graph.BrokenEdge;
import gov.nasa.javaGenes.graph.BrokenGraph;
import gov.nasa.javaGenes.graph.Graph;
import gov.nasa.javaGenes.graph.Edge;
import gov.nasa.javaGenes.graph.AcceptableSecondBrokenEdge;
import gov.nasa.javaGenes.graph.Vertex;
import gov.nasa.javaGenes.graph.AcceptableSecondVertex;

/**
Implement the molecule specific part of the crossover operator described in
"JavaGenes: Evolving Graphs with Crossover," Al Globus, Sean Atsatt,
John Lawton, Todd Wipke.
*/
public class MoleculeTwoVertexCrossover extends TwoVertexGraphCrossover {
/**
find and connect a second broken edge
@param b1 the first broken edge to be connected
*/
public void processInitialBrokenEdge(Graph g, BrokenEdge b1, BrokenGraph second){
	Molecule graph = (Molecule)g;
	if (!b1.vertex.canAcceptEdge())
  	return;
  Edge e = b1.edge.shallowCopyEdge();
  BrokenEdge b2 =
      	(BrokenEdge)second.getBrokenEdges().getRandomElement(new AcceptableSecondBrokenEdge(b1,true));
  if (b2 == null)
  	b2 = (BrokenEdge)second.getBrokenEdges().getRandomElement(new AcceptableSecondBrokenEdge(b1,false));
  if (b2 != null){
  	second.getBrokenEdges().removeElement(b2);
    e.setVertices(b1.vertex,b2.vertex);
    e.makeCompatibleWithVertices();
    b1.vertex.add(e);
    b2.vertex.add(e);
    graph.add(e);
  } else if (RandomNumber.getBoolean()) {
  	// BUG: doesn't try to conserve edge type
    Vertex v = second.getGraph().getRandomVertex(new AcceptableSecondVertex(b1.vertex));
    if (v != null) {
    	e.setVertices(b1.vertex,v);
      e.makeCompatibleWithVertices();
      b1.vertex.add(e);
      v.add(e);
      graph.add(e);
    }
  }
}
}