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

public class QuadTree extends Cell {

	QuadTree subtrees [];

	double force [];

	QuadTree (Graph g) {
		setContext (g); objectField = null;
		int d = g.getDimensions (); setDimensions (d);
		subtrees = new QuadTree [power (2, d)];
		int n = g.numberOfVertices;
		setMin (g.getMin ()); setMax (g.getMax ());
		for (int i = 0; i < n; i++)
			g.vertices [i].objectField = null;
		for (int i = 0; i < n; i++)
			insert (g.vertices [i]);
		force = new double [d]; for (int i = 0; i < d; i++) force [i] = 0;
	}

	private QuadTree (double [] min, double [] max, QuadTree p) {
		setContext (p); objectField = null;
		int d = p.getDimensions (); setDimensions (d);
		subtrees = new QuadTree [power (2, d)];
		setMin (min); setMax (max);
		force = new double [d]; for (int i = 0; i < d; i++) force [i] = 0;
	}

	QuadTree lookUp (Vertex v) {
		if (objectField == v) return this;
		else if (objectField != null) return null;
		else return subtrees [getIndex (v)].lookUp (v);
	}

	private int getIndex (Vertex v) {
		double c [] = v.getCoords (), center [] = getCenter ();
		int d = getDimensions (), index = 0, column = 1;
		for (int i = 0; i < d; i++) {
			if (c [i] > center [i]) index += column;
			column *= 2;
		}
		return index;
	}

	private double [] getCenter () {
		int d = getDimensions ();
		double mp [] = new double [d];
		double lo [] = getMin (), hi [] = getMax ();
		for (int i = 0; i < d; i++) mp [i] = (lo [i] + hi [i]) / 2;
		return mp;
	}

	protected void recomputeSize () {}
	void recomputeBoundaries () {}
	/* NOTE: Size for quadtrees has nothing to do with min and max! It stores
	the average size of the vertices that have been inserted into the tree. */ 

	void insert (Vertex v) {
		double w = getWeight (), vw = v.getWeight ();
		int d = getDimensions ();
		double vCoords [] = v.getCoords (), vSize [] = v.getSize ();
		if (w == 0) {
			v.setContext (this); setWeight (v.getWeight ());
			setCoords (vCoords); setSize (v.getSize ());
			objectField = v; return;
		}
		if (objectField != null) splitCell ();
		double c [] = getCoords (), s [] = getSize ();
		for (int i = 0; i < d; i++) {
			c [i] = (c [i] * w + vCoords [i] * vw) / (w + vw);
			s [i] = (s [i] * w + vSize [i] * vw) / (w + vw);
		}
		setWeight (w + vw);
		subtrees [getIndex (v)].insert (v);
	}

	private void splitCell () {
		Vertex v = (Vertex) objectField;
		objectField = null;
		double cellMin [] = getMin (), cellMax [] = getMax ();
		double center [] = getCenter ();
		int d = getDimensions (), n = power (2, d);
		double lo [] = new double [d], hi [] = new double [d];
		for (int index = 0; index < n; index++) {
			int column = 1;
			for (int i = 0; i < d; i++) {
				if ((index & column) > 0) {
					lo [i] = center [i]; hi [i] = cellMax [i];
				}
				else {lo [i] = cellMin [i]; hi [i] = center [i];}
				column *= 2;
			}
			subtrees [index] = new QuadTree (lo, hi, this);
		}
		subtrees [getIndex (v)].insert (v);
	}
}

