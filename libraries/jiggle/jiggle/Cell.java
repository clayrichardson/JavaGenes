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

/* Abstract base class for all JIGGLE objects that have rectangular
representations.  Known subclasses: Vertex, EdgeLabel, QuadTree. */

public abstract class Cell extends JiggleObject {

	private int dimensions = 2; /* default is a 2-D cell */
	private double weight; /* weight of cell */
	private double coords []; /* coordinates of center of cell */
	private double min [], max []; /* bounding box of cell */
	private double size []; /* dimensions of cell */

	protected Cell () {setDimensions (2); weight = 0;}

	double getWeight () {return weight;}
	void setWeight (double w) {weight = w;}

	public int getDimensions () {return dimensions;}
	public void setDimensions (int d) {
		dimensions = d; coords = new double [d]; size = new double [d];
		min = new double [d]; max = new double [d];
	}

	public double [] getCoords () {return coords;}
	public void setCoords (double [] c) {
		for (int i = 0; i < dimensions; i++) coords [i] = c [i];
	}

	double [] getMin () {return min;}
	void setMin (double [] c) {
		for (int i = 0; i < dimensions; i++) min [i] = c [i];
		recomputeSize ();
	}

	double [] getMax () {return max;}
	void setMax (double [] c) {
		for (int i = 0; i < dimensions; i++) max [i] = c [i];
		recomputeSize ();
	}

	protected void recomputeSize () {
		for (int i = 0; i < dimensions; i++) size [i] = max [i] - min [i];
	}

	public double [] getSize () {return size;}
	void setSize (double [] c) {
		for (int i = 0; i < dimensions; i++) size [i] = c [i];
		recomputeBoundaries ();
	}

	void recomputeBoundaries () {
		for (int i = 0; i < dimensions; i++) {
			min [i] = coords [i] - size [i] / 2;
			max [i] = coords [i] + size [i] / 2;
		}
	}

	void translate (double [] vector) {translate (1, vector);}
	void translate (double scalar, double [] vector) {
		for (int i = 0; i < dimensions; i++) {
			double translation = scalar * vector [i];
			coords [i] += translation;
			min [i] += translation;
			max [i] += translation;
		}
	}

	static double getDistanceSquared (Cell c1, Cell c2) {
		double sum = 0; int d = c1.getDimensions ();
		for (int i = 0; i < d; i++)
			sum += square (c1.coords [i] - c2.coords [i]);
		return sum;
	}

	static double getDistanceSquared (Cell cell, double [] point) {
		double sum = 0; int d = cell.getDimensions ();
		for (int i = 0; i < d; i++)
			sum += square (cell.coords [i] - point [i]);
		return sum;
	}

	static double getDistance (Cell c1, Cell c2) {
		return Math.sqrt (getDistanceSquared (c1, c2));
	}

	static double getDistance (Cell cell, double [] point) {
		return Math.sqrt (getDistanceSquared (cell, point));
	}

	static double sumOfRadii (Cell c1, Cell c2) {
		int d = c1.getDimensions ();
		double coords1 [] = c1.getCoords (), coords2 [] = c2.getCoords ();
		double seg [] = new double [d];
		for (int i = 0; i < d; i++) seg [i] = coords2 [i] - coords1 [i];
		return radius (d, c1.getSize (), seg) + radius (d, c2.getSize (), seg); 
	}

	static double radius (Cell cell, double [] point) {
		int d = cell.getDimensions ();
		double coords [] = cell.getCoords ();
		double seg [] = new double [d];
		for (int i = 0; i < d; i++) seg [i] = point [i] - coords [i];
		return radius (d, cell.getSize (), seg); 
	}

	private static double radius (int d, double [] cellSize, double [] segment) {
		double sum = 0;
		for (int i = 0; i < d; i++) sum += cellSize [i];
		if (sum == 0) return 0;
		double t = Double.MAX_VALUE;
		for (int i = 0; i < d; i++) {
			t = Math.min (t, Math.abs (cellSize [i] / segment [i]));
		}
		double lengthSquared = 0;
		for (int i = 0; i < d; i++) lengthSquared += square (t * segment [i]);
		return Math.sqrt (lengthSquared) / 2;
	}
}