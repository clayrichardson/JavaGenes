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

/**
Mutation operator to add an Edge between two Vertex in a Graph.
@see Graph
*/
public class AddEdge extends GraphMutation {
protected VertexAndEdgeProvider provider;
/**
@param p will be used to get the directed edges to be added to graphs
*/
public AddEdge(VertexAndEdgeProvider p) {provider = p;}

/**
@param child graph to be mutated
@return child with an added edge or null if an edge cannot be added
@see VertexAcceptsEdge
@see AcceptableSecondVertex
*/
public Graph makeChild(Graph child) {
         Vertex v1 = child.getRandomVertex(new VertexAcceptsEdge());
         if (v1 == null)
            return null;
         Vertex v2 = child.getRandomVertex(new AcceptableSecondVertex(v1));
         if (v2 == null)
            return null;
         Edge edge = provider.getEdge();
         edge.setVertices(v1,v2);
         edge.makeCompatibleWithVertices();
         v1.add(edge);
         v2.add(edge);
         child.add(edge);
         return child;
}
}