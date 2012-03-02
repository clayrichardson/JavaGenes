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

// Class for graphs. */

import java.util.*;

public class Graph extends Cell {
  
	public int numberOfVertices = 0, numberOfEdges = 0;
	public Vertex vertices [] = new Vertex [1];
	public Edge edges [] = new Edge [1];

	/* NOTE: the above are made publicly accessible for reasons of
	efficiency.  They should NOT, however, be modified except by
	insertVertex, deleteVertex, insertEdge, and deleteEdge methods
	below. */

	public Graph () {}
	public Graph (int d) {setDimensions (d);}

	public Vertex insertVertex () {
		Vertex v = new Vertex (this);
		vertices = DynamicArray.add (vertices, numberOfVertices++, v);
		return v;
	}

	public Edge insertEdge (Vertex from, Vertex to) {
		return insertEdge (from, to, false);
	}

	public Edge insertEdge (Vertex from, Vertex to, boolean dir) {
		Edge e = new Edge (this, from, to, dir);
		from.insertNeighbor (e); to.insertNeighbor (e);
		edges = DynamicArray.add (edges, numberOfEdges++, e);
		return e;
	}
	
	public void deleteVertex (Vertex v) {
		try {
			for (int i = 0; i < v.inDegree; i++) {
				Edge e = v.undirectedEdges [i];
				v.undirectedNeighbors [i].deleteNeighbor (e);
				DynamicArray.remove (edges, numberOfEdges--, e);
			}
			for (int i = 0; i < v.inDegree; i++) {
				Edge e = v.inEdges [i];
				v.inNeighbors [i].deleteNeighbor (e);
				DynamicArray.remove (edges, numberOfEdges--, e);
			}
			for (int i = 0; i < v.outDegree; i++) {
				Edge e = v.outEdges [i];
				v.outNeighbors [i].deleteNeighbor (e);
				DynamicArray.remove (edges, numberOfEdges--, e);
			}
			DynamicArray.remove (vertices, numberOfVertices--, v);
		} catch (NotFoundException exc) {throw new Error (v + " not found");}
	}

	public void deleteEdge (Edge e) {
		try {
			e.getFrom ().deleteNeighbor (e); e.getTo ().deleteNeighbor (e);
			DynamicArray.remove (edges, numberOfEdges--, e);
		} catch (NotFoundException exc) {throw new Error (e + " not found");}
	}

	void recomputeBoundaries () {
		int d = getDimensions ();
		double lo [] = getMin (), hi [] = getMax ();
		for (int i = 0; i < d; i++) {
			lo [i] = Double.MAX_VALUE; hi [i] = -Double.MAX_VALUE;
		}
		for (int i = 0; i < numberOfVertices; i++) {
			Vertex v = vertices [i]; double c [] = v.getCoords ();
				for (int j = 0; j < d; j++) {
					lo [j] = Math.min (lo [j], c [j]);
					hi [j] = Math.max (hi [j], c [j]);
				}
		}
		recomputeSize ();
	}

	// The isConnected method tests whether a graph is connected.
	// An empty graph is considered to be not connected.

	boolean isConnected () {
		if (numberOfVertices == 0) return false;
		for (int i = 0; i < numberOfVertices; i++)
			vertices [i].booleanField = false;
		numberOfMarkedVertices = 0;
		dft (vertices [0]);
		return (numberOfMarkedVertices == numberOfVertices);
	}
	private int numberOfMarkedVertices = 0;
	private void dft (Vertex v) {
		v.booleanField = true; ++numberOfMarkedVertices;
		for (int i = 0; i < v.undirectedDegree; i++) {
			Vertex neighbor = v.undirectedNeighbors [i];
			if (! neighbor.booleanField) dft (neighbor);
		}
		for (int i = 0; i < v.undirectedDegree; i++) {
			Vertex neighbor = v.inNeighbors [i];
			if (! neighbor.booleanField) dft (neighbor);
		}
		for (int i = 0; i < v.undirectedDegree; i++) {
			Vertex neighbor = v.outNeighbors [i];
			if (! neighbor.booleanField) dft (neighbor);
		}
	}
}

