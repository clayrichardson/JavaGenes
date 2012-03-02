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

/* Class for edges of a graph.  NOTE: the only mutable characteristics
of an edge are its label, directedness, and preferred length. */

public class Edge extends JiggleObject {

	private Vertex from, to; /* endpoints of the edge */
	private EdgeLabel label = null; /* label of edge */
	private boolean directed = false; /* is the edge directed? */
	private double preferredLength = 0; /* preferred length of edge */

	Edge (Graph g, Vertex f, Vertex t) {from = f; to = t; setContext (g);}

	Edge (Graph g, Vertex f, Vertex t, boolean dir) {
		from = f; to = t; setContext (g); directed = dir;
	}

	public Vertex getFrom () {return from;}
	public Vertex getTo () {return to;}

	EdgeLabel getLabel () {return label;}
	void setLabel (EdgeLabel lbl) {label = lbl;}

	boolean getDirected () {return directed;}
	void setDirected (boolean d) {directed = d;}

	double getPreferredLength () {return preferredLength;}
	void setPreferredLength (double len) {preferredLength = len;}

	double getLengthSquared () {return Vertex.getDistanceSquared (from, to);}
	double getLength () {return Vertex.getDistance (from, to);}

	public String toString () {
		return "(Edge: " + from + ", " + to + ", " +
			 (directed ? "directed" : "undirected") + ")";
	}
}