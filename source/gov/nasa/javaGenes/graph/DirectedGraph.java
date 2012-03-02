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
import gov.nasa.alsUtility.Predicate;

public class DirectedGraph extends Graph {
private static final boolean debug = false;

/**
copy the graph and make copies of all edges and vertices
*/
public DirectedGraph deepCopyDirectedGraph() {
    try {
        return (DirectedGraph)clone();
    } catch (CloneNotSupportedException e) {
        Error.fatal("can't clone object: " + e);
        return null;
    }
}
/**
copy the graph and make copies of all edges and vertices

@exception CloneNotSupportedException
*/
public Object clone() throws CloneNotSupportedException {
		if (debug) System.out.println("DirectedGraph clone()");
		if (debug) System.out.println("this:   " + toString());
    DirectedGraph graph = (DirectedGraph)super.shallowClone();
    for(VertexIterator i = getVertexIterator(); i.more(); i.next())
        graph.add((Vertex)i.vertex().clone());
    for(EdgeIterator i = getEdgeIterator(); i.more(); i.next())
        graph.add((Edge)i.edge().clone());
    for(int i = 0; i < vertices.size(); i++) {
        DirectedVertex vOld = getDirectedVertex(i);
        DirectedVertex vNew = graph.getDirectedVertex(i);
        for(EdgeIterator e = vOld.getInputEdgeIterator(); e.more(); e.next())
            vNew.addInputEdge((DirectedEdge)graph.getEquivalent(this,e.edge()));
        for(EdgeIterator e = vOld.getOutputEdgeIterator(); e.more(); e.next())
            vNew.addOutputEdge((DirectedEdge)graph.getEquivalent(this,e.edge()));
    }
    numberVertices(0);
    for(int i = 0; i < edges.size(); i++){
        DirectedEdge edge = getDirectedEdge(i);
        DirectedVertex v0 = graph.getDirectedVertex(edge.getInVertex().getNumber());
        DirectedVertex v1 = graph.getDirectedVertex(edge.getOutVertex().getNumber());
        graph.getDirectedEdge(i).setInVertex(v0);
        graph.getDirectedEdge(i).setOutVertex(v1);
    }
		if (debug) System.out.println("graph: " + graph.toString());
    return graph;
}
public DirectedVertex getDirectedVertex(int index) {return (DirectedVertex)getVertex(index);}
public DirectedEdge getDirectedEdge(int index) {return (DirectedEdge)getEdge(index);}
/**
attached edge and acceptsOutput and attach them to a random vertex
that will accept an input edge
*/
public boolean growDirectedVertex(DirectedVertex acceptsOutput, DirectedEdge edge) {
  DirectedVertex acceptsInput = getRandomDirectedVertex(new VertexAcceptsInputEdge());
  if (acceptsInput == null) return false;
  edge.setDirectedVertices(acceptsOutput,acceptsInput);
  acceptsOutput.addOutputEdge(edge);
  acceptsInput.addInputEdge(edge);
  add(acceptsOutput);
  add(edge);
  return true;
}
/**
this predicate decides if a vertex is acceptable as a second vertex when adding an edge
during graph construction
*/
protected class SecondVertex implements Predicate {
	protected DirectedVertex acceptsOutput;
	public SecondVertex(DirectedVertex vertex) {acceptsOutput = vertex;}
  public boolean execute(Object object) {
  	DirectedVertex acceptsInput = (DirectedVertex)object;
    return !acceptsOutput.equals(acceptsInput)
    			 && !acceptsInput.hasInputEdgeFrom(acceptsOutput)
           && acceptsInput.canAcceptInputEdge();
  }
}
/**
add edge creating an undirected cycle
*/
public boolean growDirectedEdge(DirectedEdge edge) {
  DirectedVertex acceptsOutput = getRandomDirectedVertex(new VertexAcceptsOutputEdge());
  if (acceptsOutput == null) return false;
 	DirectedVertex acceptsInput = getRandomDirectedVertex(new SecondVertex(acceptsOutput));
  if (acceptsInput == null) return false;
  edge.setDirectedVertices(acceptsOutput,acceptsInput);
  acceptsOutput.addOutputEdge(edge);
  acceptsInput.addInputEdge(edge);
  add(edge);
  return true;
}
public DirectedVertex getRandomDirectedVertex(Predicate predicate) {
	return (DirectedVertex)getRandomVertex(predicate);
}
}