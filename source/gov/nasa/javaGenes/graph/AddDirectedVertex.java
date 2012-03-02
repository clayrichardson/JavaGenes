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

import gov.nasa.alsUtility.Predicate;
import gov.nasa.alsUtility.And;
import gov.nasa.alsUtility.Not;
import gov.nasa.alsUtility.IsInstanceOf;

/**
Mutation operator to add a DirectedVertex by a DirectedEdge to a
random vertex in a DirectedGraph. Won't add DigitalOutput or DigitalInput vertices.
@see DirectedGraph
*/
public class AddDirectedVertex extends GraphMutation {
protected VertexAndEdgeProvider provider;
protected Predicate predicate = new And(new And(new Not(new IsInstanceOf("DigitalOutput")),
                                                new Not(new IsInstanceOf("DigitalInput" ))),
                                        new VertexAcceptsOutputEdge());
/**
@param p will be used to get the directed edges to be added to graphs
*/
public AddDirectedVertex(VertexAndEdgeProvider p) {provider = p;}

/**
@param c must be a DirectedGraph
@return c with an added vertex/edge or null if a they can be added
*/
public Graph makeChild(Graph c) {
	DirectedGraph child = (DirectedGraph)c;
	if (child.growDirectedVertex(provider.getDirectedVertex(predicate),provider.getDirectedEdge()))
		return child;
  else
  	return null;
}
}

