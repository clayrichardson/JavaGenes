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


import java.io.Serializable;
import gov.nasa.alsUtility.ExtendedVector;
import gov.nasa.alsUtility.Predicate;

/**
holds a set of vertices and a set of edges. Vertices and edges should not be
connected to anything. Provides copies of these
at random when asked. Used to generate random graphs. Vertices and edges
are provided with equal probability. To skew the distribution add extra
copies of what you want more of.

@see Graph
*/
public class VertexAndEdgeProvider implements Serializable {
/**
the vertices to be provided.
*/
protected ExtendedVector vertices = new ExtendedVector();
/**
the edges to be provided
*/
protected ExtendedVector edges = new ExtendedVector();
/**
add v to the vertices to be provided
*/
public void add(Vertex v) {vertices.addElement(v);}
/**
@return a copy of a random vertex
*/
public Vertex getVertex() {
    return ((Vertex)vertices.getRandomElement()).shallowCopyVertex();
}
public DirectedVertex getDirectedVertex() {return (DirectedVertex)getVertex();}
/**
@return a copy of a random vertex that satisfies p
*/
public Vertex getVertex(Predicate p) {
  Vertex v = (Vertex)vertices.getRandomElement(p);
  if (v == null) return null;
  return v.shallowCopyVertex();
}
public DirectedVertex getDirectedVertex(Predicate p) {return (DirectedVertex)getVertex(p);}

/**
add e to the edges to be provided
*/
public void add(Edge e) {edges.addElement(e);}
/**
@return a copy of a random edge
*/
public Edge getEdge() {
    return ((Edge)edges.getRandomElement()).shallowCopyEdge();
}
/**
@return a copy of a random edge
*/
public DirectedEdge getDirectedEdge() {return (DirectedEdge)getEdge();}
/**
@return a copy of a random edge that satisfies p
*/
public Edge getEdge(Predicate p) {
  Edge e = (Edge)edges.getRandomElement(p);
  if (e == null) return null;
  return e.shallowCopyEdge();
}
/**
@return a copy of a random edge that satisfies p
*/
public DirectedEdge getDirectedEdge(Predicate p) {return (DirectedEdge)getEdge(p);}
public String toString() {
    StringBuffer s = new StringBuffer();
    s.append("vertices = (");
    for(VertexIterator i = new VertexIterator(vertices); i.more(); i.next())
        s.append(i.vertex()).append(",");
    s.setLength(s.length()-1);
    s.append(") ");
    s.append("edges = (");
    for(EdgeIterator i = new EdgeIterator(edges ); i.more(); i.next())
        s.append(i.edge()).append(",");
    s.setLength(s.length()-1);
    s.append(")");
    return s.toString();
}    

}
