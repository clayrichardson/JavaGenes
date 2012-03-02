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

public class DirectedVertex extends Vertex {
protected ExtendedVector inputEdges = new ExtendedVector();
protected ExtendedVector outputEdges = new ExtendedVector();

public DirectedVertex() {}
/**
restore from checkpoint file
*/
//public DirectedVertex(TokenizeInput tokenizer) {super(tokenizer);}
public Object clone() throws java.lang.CloneNotSupportedException {
    DirectedVertex vertex = (DirectedVertex)super.clone();
    vertex.inputEdges = new ExtendedVector();
    vertex.outputEdges = new ExtendedVector();
    return vertex;
}
public DirectedEdge getInputEdge(int i) {return (DirectedEdge)inputEdges.elementAt(i);}
public DirectedEdge getOutputEdge(int i) {return (DirectedEdge)outputEdges.elementAt(i);}
/**
not implemented
*/
public void add(Edge edge) {Error.notImplemented();}
public void addInputEdge(DirectedEdge edge) {
	inputEdges.addElement(edge);
	super.add(edge);
  stateChange();
}
public void addOutputEdge(DirectedEdge edge) {
	outputEdges.addElement(edge);
	super.add(edge);
  stateChange();
}
public void removeEdge(Edge edge) {
	super.removeEdge(edge);
  if (inputEdges != null) inputEdges.removeElement(edge);
  if (outputEdges != null) outputEdges.removeElement(edge);
  stateChange();
}
public boolean hasOutputEdgeTo(DirectedVertex vertex) {
	for(EdgeIterator e = vertex.getOutputEdgeIterator(); e.more(); e.next())
    if (e.edge().doesConnect(vertex)) return true;
  return false;
}
public boolean hasInputEdgeFrom(DirectedVertex vertex) {
	for(EdgeIterator e = getInputEdgeIterator(); e.more(); e.next())
    if (e.edge().doesConnect(vertex)) return true;
  return false;
}
public boolean hasOutputEdges() {return outputEdges.size() > 0;}
public boolean hasInputEdges() {return inputEdges.size() > 0;}
public EdgeIterator getInputEdgeIterator() {return new EdgeIterator(inputEdges);}
public EdgeIterator getOutputEdgeIterator() {return new EdgeIterator(outputEdges);}
/**
if an error to call this
*/
public void stealEdges (Vertex v) {Error.notImplemented();}
public boolean canAcceptEdge() {
	return canAcceptInputEdge() || canAcceptOutputEdge();
}
/**
if an error to call this
*/

public boolean canAcceptEdge(Edge e) {Error.notApplicable(); return false;}
public boolean canAcceptInputEdge() {return true;}
public boolean canAcceptOutputEdge() {return true;}
public String toString() {return "DirectedVertex";}
}