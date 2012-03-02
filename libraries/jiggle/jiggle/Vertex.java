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
/*
Copyright Daniel Tunkelang (quixote@alum.mit.edu), 1999. 
Permission is granted for anyone to use, redistribute, and/or modify the software 
for any purpose including commercial. 
Daniel Tunkelang retains the rights to use the software in any way 
and to assign those rights to others.
*/
package jiggle;

/* Class for vertices of a graph. */

public class Vertex extends Cell {

	int undirectedDegree = 0, inDegree = 0, outDegree = 0;
	Edge undirectedEdges [] = new Edge [1];
	Edge         inEdges [] = new Edge [1];
	Edge        outEdges [] = new Edge [1];
	Vertex undirectedNeighbors [] = new Vertex [1];
	Vertex         inNeighbors [] = new Vertex [1];
	Vertex        outNeighbors [] = new Vertex [1];

	/* NOTE: the above are made package-accessible for reasons of
	efficiency.  They should NOT, however, be modified except by
	insertNeighbor and deleteNeighbor methods below. */

	private String name = ""; /* name of vertex */
	private boolean fixed = false; /* is the vertex anchored? */

	Vertex (Graph g) {
		super ();
		setContext (g); setWeight (1); setDimensions (g.getDimensions ());
	}

	String getName () {return name;}
	void setName (String str) {name = str;}

	boolean getFixed () {return fixed;}
	void setFixed (boolean f) {fixed = f;}

	void insertNeighbor (Edge e) {
		Vertex from = e.getFrom (), to = e.getTo ();
		Vertex v = null;
		if (this == from) v = to; else if (this == to) v = from;
		else throw new Error (e + " not incident to " + this);
		if (! e.getDirected ()) {
			undirectedEdges = DynamicArray.add
				(undirectedEdges, undirectedDegree, e);
			undirectedNeighbors = DynamicArray.add
				(undirectedNeighbors, undirectedDegree++, v);
		}
		else if (this == from) {
			outEdges = DynamicArray.add (outEdges, outDegree, e);
			outNeighbors = DynamicArray.add
				(outNeighbors, outDegree++, to);
		}
		else {
			inEdges = DynamicArray.add (inEdges, inDegree, e);
			inNeighbors = DynamicArray.add
				(inNeighbors, inDegree++, from);
		}
	}

	void deleteNeighbor (Edge e) {
		Vertex from = e.getFrom (), to = e.getTo ();
		Vertex v = null;
		if (this == from) v = to; else if (this == to) v = from;
		else throw new Error (e + " not incident to " + this);
		try {
			if (! e.getDirected ()) {
				DynamicArray.remove
					(undirectedEdges, undirectedDegree, e);
				DynamicArray.remove
					(undirectedNeighbors, undirectedDegree--, v);
			}
			else if (this == from) {
				DynamicArray.remove (outEdges, outDegree, e);
				DynamicArray.remove (outNeighbors, outDegree--, to);
			}
			else {
				DynamicArray.remove (inEdges, inDegree, e);
				DynamicArray.remove (inNeighbors, inDegree--, from);
			}
		} catch (NotFoundException exc) {
			throw new Error (e + " not incident to " + this);
		}
	}

	public String toString () {return "(Vertex: " + name + ")";}
}