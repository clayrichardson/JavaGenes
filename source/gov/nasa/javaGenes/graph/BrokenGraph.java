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

import java.lang.CloneNotSupportedException;
import java.lang.Class;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.ExtendedVector;
/**
Two of these classes are used to represent the sub-graphs produced
when ripping a graph in half during crossover. This representation includes
the broken edges.
*/
public class BrokenGraph implements java.lang.Cloneable {
ExtendedVector brokenEdges = new ExtendedVector();
Graph graph;

public BrokenGraph(Class c) {
    try {
    	graph = (Graph)c.newInstance();
    } catch (Exception e) {Error.fatal(e);}
}
public ExtendedVector getBrokenEdges() {return brokenEdges;}
public Graph getGraph() {return graph;}
public Class getGraphClass() {return graph.getClass();}
/**
make a copy of the brokenEdges vector, make new BrokenEdges,
but don't make copies of the edges themselves. Do find the equivalent
associated vertices in the BrokenEdges
*/
public BrokenGraph deepCopyBrokenGraph() {
    try {
        return (BrokenGraph)clone();
    } catch (CloneNotSupportedException e) {
        Error.fatal("can't clone object: " + e);
        return null;
    }
}
/**
make a copy of the brokenEdges vector, make new BrokenEdges,
?old but don't make copies of the edges themselves?. Do find the equivalent
associated vertices in the BrokenEdges
*/
public Object clone() throws CloneNotSupportedException{
    BrokenGraph g = (BrokenGraph)super.clone();
    g.brokenEdges = new ExtendedVector();
    g.graph = (Graph)graph.clone();
    for(int i = 0; i < brokenEdges.size(); i++){
    	BrokenEdge old = (BrokenEdge)brokenEdges.elementAt(i);
      Error.assertTrue(old.vertex == old.edge.getVertex(0) || old.vertex == old.edge.getVertex(1));
      BrokenEdge b = new BrokenEdge();
      // old  b.edge = old.edge
      b.edge = old.edge.shallowCopyEdge();
      b.edge.vertices[0] = g.getGraph().getEquivalent(graph,old.edge.getVertex(0));
      b.edge.vertices[1] = g.getGraph().getEquivalent(graph,old.edge.getVertex(1));
      b.vertex = g.getGraph().getEquivalent(graph,old.vertex);
      Error.assertTrue(b.isLegal());
      g.brokenEdges.addElement(b);
    }
    return g;
}


public void add(BrokenEdge b){
	Error.assertTrue(getGraph().vertices.contains(b.vertex));
	brokenEdges.addElement(b);
}
public String toString() {
	String string = "";
  String graphString = graph.toString();
	for(int i = 0; i < brokenEdges.size(); i++)
  	string += brokenEdges.elementAt(i).toString() + ",";
  string += graphString;
  return string;
}
}
