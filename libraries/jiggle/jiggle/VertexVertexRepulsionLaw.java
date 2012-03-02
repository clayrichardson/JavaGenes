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

public abstract class VertexVertexRepulsionLaw extends ForceLaw {

	protected double preferredEdgeLength;

	abstract double pairwiseRepulsion (Cell c1, Cell c2);

	private double barnesHutTheta = 0;

	protected VertexVertexRepulsionLaw (Graph g, double k) {
		super (g); preferredEdgeLength = k;
	}

	public double getBarnesHutTheta () {return barnesHutTheta;}
	public void setBarnesHutTheta (double t) {barnesHutTheta = t;}
	
	void apply (double [][] negativeGradient) {
		if (barnesHutTheta > 0)
			applyUsingBarnesHut (negativeGradient);
		int n = graph.numberOfVertices, d = graph.getDimensions ();
		for (int i = 0; i < n - 1; i++) {
			Vertex v1 = graph.vertices [i];
			double v1Coords [] = v1.getCoords (), weight1 = v1.getWeight ();
			for (int j = i + 1; j < n; j++) {
				Vertex v2 = graph.vertices [j];
				double w = Math.min (pairwiseRepulsion (v1, v2),
				                     cap / Vertex.getDistance (v1, v2));
				double v2Coords [] = v2.getCoords (), weight2 = v2.getWeight ();
				for (int k = 0; k < d; k++) {
					double force = (v1Coords [k] - v2Coords [k]) * w;
					negativeGradient [i] [k] += force * weight2;
					negativeGradient [j] [k] -= force * weight1;
				}
			}
		}
	}

	private void applyUsingBarnesHut (double [][] negativeGradient) {
		int n = graph.numberOfVertices, d = graph.getDimensions ();
		if (n <= 1) return;
		graph.recomputeBoundaries ();
		QuadTree root = new QuadTree (graph);
		for (int i = 0; i < n; i++) {
			Vertex v = graph.vertices [i];
			QuadTree qt = (QuadTree) v.getContext ();
			JiggleObject cur = qt;
			while (cur.getContext () != graph) {
				QuadTree p = (QuadTree) cur.getContext ();
				int numberOfSubtrees = power (2, d);
				for (int j = 0; j < numberOfSubtrees; j++) {
					QuadTree st = p.subtrees [j];
					if (cur != st) {
						computeQTRepulsion (qt, st, negativeGradient);
					}
				}
				cur = p;
			}
		}
		pushForcesDownTree (root);
		for (int i = 0; i < n; i++) {
			Vertex v = graph.vertices [i];
			QuadTree qt = (QuadTree) v.getContext ();
			for (int j = 0; j < d; j++) negativeGradient [i] [j] += qt.force [j];
			v.setContext (graph);
		}
	}

	private void computeQTRepulsion (QuadTree leaf, QuadTree cell, double [][] negativeGradient) {
		if (cell == null) return;
		int d = leaf.getDimensions ();
		if ((cell.objectField == null) && (! wellSeparated (leaf, cell))) {
			int numberOfSubtrees = power (2, d);
			for (int i = 0; i < numberOfSubtrees; i++) {
				computeQTRepulsion (leaf, cell.subtrees [i], negativeGradient);
			}
		}
		else {
			double w = Math.min (pairwiseRepulsion (leaf, cell),
			                     cap / Cell.getDistance (leaf, cell));
			double leafWeight = leaf.getWeight (), cellWeight = cell.getWeight ();
			double leafCoords [] = leaf.getCoords (), cellCoords [] = cell.getCoords ();
			int i = leaf.intField;
			for (int j = 0; j < d; j++) {
				double force = 0.5 * w * (leafCoords [j] - cellCoords [j]);
				negativeGradient [i] [j] += force * cellWeight;
				cell.force [j] -= force * leafWeight;
			}
		}
	}

	private boolean wellSeparated (QuadTree leaf, QuadTree cell) {
		if (cell == null) throw new Error ("cell == null");
		if (cell.objectField != null) return true;
		else {
			int d = cell.getDimensions ();
			double len = Double.MAX_VALUE;
			double lo [] = cell.getMin (), hi [] = cell.getMax ();
			for (int i = 0; i < d; i++) len = Math.min (len, hi [i] - lo [i]);
			double dist = Cell.getDistance (leaf, cell);
			return ((len / dist) < barnesHutTheta);
		}
	}

	private void pushForcesDownTree (QuadTree qt) {
		if ((qt != null) && (qt.objectField == null) && (qt.getWeight () > 0)) {
			int d = qt.getDimensions (), numberOfSubtrees = power (2, d);
			for (int i = 0; i < numberOfSubtrees; i++) {
				for (int j = 0; j < d; j++) {
					qt.subtrees [i].force [j] += qt.force [j];
				}
			}
			for (int i = 0; i < numberOfSubtrees; i++) {
				pushForcesDownTree (qt.subtrees [i]);
			}
		}
	}
}
