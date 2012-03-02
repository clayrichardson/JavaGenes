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

public class DirectedEdge extends Edge {
/**
not implemented
*/
public void setVertices(Vertex in, Vertex out) {
	Error.notImplemented();
}
public void setDirectedVertices(DirectedVertex in, DirectedVertex out) {
	setInVertex(in);
  setOutVertex(out);
}
public void setInVertex(DirectedVertex vertex) {vertices[0] = vertex;}
public void setOutVertex(DirectedVertex vertex) {vertices[1] = vertex;}
public DirectedVertex getInVertex() {return (DirectedVertex)vertices[0];}
public DirectedVertex getOutVertex() {return (DirectedVertex)vertices[1];}
public boolean isInVertex(Vertex vertex) {return vertex == vertices[0];}
public boolean isOutVertex(Vertex vertex) {return vertex == vertices[1];}
public String toString() {return "directedEdge";}
/**
it is an error to call this
*/
public void stealVertices(Edge e) {Error.notApplicable();}
} 