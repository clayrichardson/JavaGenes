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


import java.lang.Cloneable;
import java.lang.CloneNotSupportedException;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.Compatible;

/**
When an edge is cut during crossover, this class is used to remember the
cut edge and the vertex it is associated with. Two BrokenEdge objects
are created for each edge cut, one for each part of the graph being torn in
two. These sub-graphs are represented by BrokenGraph objects. The associated vertex is 
different for each BrokenEdge object depending
on which BrokenGraph the broken edge goes with.
*/
public class BrokenEdge implements Cloneable, Compatible {
public Edge edge; // the broken edge
public Vertex vertex; // the associated vertex
private static final boolean debugCompatible = false;

public BrokenEdge() {}
public BrokenEdge(Edge e, Vertex v) {
    edge = e;
    vertex = v;
    Error.assertTrue(isLegal());
}
public BrokenEdge shallowCopyBrokenEdge() {
    try {
        return (BrokenEdge)clone();
    } catch (CloneNotSupportedException e) {
        Error.fatal("can't clone object: " + e);
        return null;
    }
}
public Object clone() throws CloneNotSupportedException {return super.clone();}
public DirectedVertex getDirectedVertex() {return (DirectedVertex)vertex;}
public DirectedEdge getDirectedEdge() {return (DirectedEdge)edge;}
/**
used with DirectedEdge
@return true if this broken edge requires input from a new vertex
*/
public boolean wantsInput() {return getDirectedEdge().isOutVertex(vertex);}
/**
used with DirectedEdge
@return true if this broken edge requires output to a new vertex
*/
public boolean wantsOutput() {return getDirectedEdge().isInVertex(vertex);}
/**
are the edges compatible for merging (the associated vertex is not considered)
*/
public boolean isCompatible(Compatible c) {
	BrokenEdge brokenEdge = (BrokenEdge)c;
	if (brokenEdge.edge instanceof DirectedEdge) {
  	if (debugCompatible) {
    	boolean input = wantsInput();
      boolean output = wantsOutput();
    	boolean first = wantsInput() && brokenEdge.wantsOutput();
      boolean second = brokenEdge.wantsInput() && wantsOutput();
      boolean compatible = edge.isCompatible(brokenEdge.edge);
    }
  	return ((wantsInput() && brokenEdge.wantsOutput()) || (brokenEdge.wantsInput() && wantsOutput()))
        &&  edge.isCompatible(brokenEdge.edge);
  } else
		return edge.isCompatible(brokenEdge.edge);
}
public String toString() {
	String string = "";
  string = vertex == edge.getVertex(0) ? "0:" : "1:";
  if (edge.getVertex(0) != null)
  	string += edge.getVertex(0).getNumber();
  string += "-";
  if (edge.getVertex(1) != null)
  	string += edge.getVertex(1).getNumber();
 	return string;
}
public boolean isLegal() {
	return vertex != null && (vertex == edge.getVertex(0) || vertex == edge.getVertex(1));
}
}
