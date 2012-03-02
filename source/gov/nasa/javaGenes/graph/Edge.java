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
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.Compatible;

/**
 Edges in Graphs. An edge connects two vertices.
 
 @see Graph
 @see Vertex
*/
public class Edge extends GraphElement implements Compatible {
/**
the vertices this edge connects
*/
protected Vertex[] vertices = new Vertex[2];
/**
@return connects the same vertices
*/
public Edge shallowCopyEdge() {
    try {
        return (Edge)clone();
    } catch (CloneNotSupportedException e) {
        Error.fatal("can't clone object: " + e);
        return null;
    }
}
/**
@return connects the same vertices
*/
public Object clone() throws CloneNotSupportedException {
    Edge edge = (Edge)super.clone();
    edge.vertices = new Vertex[2];
    return edge;
}
/**
removes existing vertices
removes "this" from edge list of existing vertices
sets the vertices
forces itself to be compatible (subclass responsibility)
*/
public void setVertices(Vertex v1, Vertex v2){
    removeVertices();
    vertices[0] = v1;
    vertices[1] = v2;
}
/**
Force this edge to be compatible with its vertices.  If
any change is actually made, tell the vertices that their
state has changed (they now have different edges).
This is a noop here.  It is used in some subclasses (e.g.,
chemical bonds [Bond]).
*/
public void makeCompatibleWithVertices() {}
/**
sets the vertices to null
*/
public void removeVertices() {
    if (vertices[0] != null)
        vertices[0].removeEdge(this);
    vertices[0] = null;
    if (vertices[1] != null)
        vertices[1].removeEdge(this);
    vertices[1] = null;
}
/**
@param e will lose its vertices! Be careful using it after this call.
*/
public void stealVertices(Edge e) {
  setVertices(e.vertices[0],e.vertices[1]);
  e.removeVertices();
  makeCompatibleWithVertices();
  vertices[0].add(this);
  vertices[1].add(this);
}
/**
replace oldVertex with newVertex
*/
public void replaceVertex (Vertex oldVertex, Vertex newVertex) {
  if (vertices[0].equals(oldVertex))
    vertices[0] = newVertex;
  else if (vertices[1].equals(oldVertex))
    vertices[1] = newVertex;
  else
    Error.fatal("bad oldVertex");
}
/**
@param v one of the vertices this edge connects
@return  the other vertex
*/
public Vertex otherVertex(Vertex v) {
    if (v.equals(vertices[0]))
        return vertices[1];
    Error.assertTrue(v.equals(vertices[1]));
    return vertices[0];
}
/**
@return the common vertex between this and parameter edge, null if none
*/
public Vertex commonVertex(Edge edge) {
    if (vertices[0].equals(edge.vertices[0]) || vertices[0].equals(edge.vertices[1]))
        return vertices[0];
    if (vertices[1].equals(edge.vertices[0]) || vertices[1].equals(edge.vertices[1]))
        return vertices[1];
    return null;
}
public String getTypeString() {return toString();}
public String toString() {return "edge";}
public Vertex getVertex(int which) {return vertices[which];}
/**
@return true if argument is same class
*/
public boolean isCompatible(Compatible c) {return getClass() == c.getClass();}
public boolean isSame(Edge e) {return getClass().equals(e.getClass());}
public boolean dissimilarEdgeCompatibleWithVertices(Edge e) {
  return !isCompatible(e);
}
public boolean canAcceptVerticesOf(Edge e) {return true;}
/**
@return true if this edge connects to parameter v
*/
public boolean doesConnect(Vertex v) {return v.equals(vertices[0]) || v.equals(vertices[1]);}
public boolean isLegal() {return vertices[0] != null && vertices[1] != null;}
}
