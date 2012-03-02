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
Mutation operator to add a Vertex by an Edge to a
random vertex in a Graph.
@see Graph
*/
public class AddVertex extends GraphMutation {
protected VertexAndEdgeProvider provider;
/**
@param p will be used to get the directed edges to be added to graphs
*/
public AddVertex(VertexAndEdgeProvider p) {provider = p;}

/**
@param child graph to be mutated
@return child with an added vertex/edge or null if a they can be added
*/
public Graph makeChild(Graph child) {
        Edge edge = provider.getEdge();
 
        Vertex v1 = child.getRandomVertex(new VertexAcceptsEdge(edge));
        if (v1 == null)
            v1 = child.getRandomVertex(new VertexAcceptsEdge());
        if (v1 == null) return null;
 
        Vertex v2 = provider.getVertex(new VertexAcceptsEdge(edge));
        if (v2 == null)
            v2 = provider.getVertex(new VertexAcceptsEdge());
        if (v2 == null) return null;

        edge.setVertices(v1,v2);
        edge.makeCompatibleWithVertices();
        v1.add(edge);
        v2.add(edge);
        child.add(v2);
        child.add(edge);
        return child;
}
} 