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

/* Abstract base class for first-order graph-drawing optimization procedures.
Includes concrete method for performing adaptive line search. */

public abstract class FirstOrderOptimizationProcedure extends ForceDirectedOptimizationProcedure {

	protected double maxCos = 1;
  protected int maximumIterations = 1000;
	FirstOrderOptimizationProcedure (Graph g, ForceModel fm, double accuracy) {
		super (g, fm); maxCos = accuracy;
	}
public void setMaxLineSearchIterations(int iterations) {
    maximumIterations = iterations;
	}

	protected double negativeGradient [] [] = null;
	protected double descentDirection [] [] = null;
	protected double penaltyVector [] [] = null;
	protected double penaltyFactor = 0;

	public double improveGraph () {
		int n = graph.numberOfVertices, d = graph.getDimensions ();
		if ((negativeGradient == null) || (negativeGradient.length != n)) {
			negativeGradient = new double [n] [d];
			penaltyVector = new double [n] [d];
			getNegativeGradient ();
		}
		computeDescentDirection ();
		return lineSearch ();
	}

	public void reset () {negativeGradient = null; penaltyFactor = 0;}

	private void computePenaltyFactor () {
		double m1 = l2Norm (negativeGradient);
		double m2 = l2Norm (penaltyVector);
		if (m2 == 0) penaltyFactor = 0;
		else if (m1 == 0) penaltyFactor = 1;
		else {
			double cos = dotProduct (negativeGradient, penaltyVector) / (m1*m2);
			penaltyFactor = Math.max (0.00000001, (0.00000001 - cos)) * Math.max (1, (m1 / m2));
		}
	}

	private void getNegativeGradient () {
		forceModel.getNegativeGradient (negativeGradient);
		if (constrained) {
			getPenaltyVector (); computePenaltyFactor ();
			int n = graph.numberOfVertices, d = graph.getDimensions ();
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < d; j++) {
					negativeGradient [i] [j] += penaltyFactor * penaltyVector [i] [j];
				}
			}
		}
	}

	private void getPenaltyVector () {
		forceModel.getPenaltyVector (penaltyVector);
	}

	protected abstract void computeDescentDirection ();

	private double stepSize = 0.1, previousStepSize = 0;

	protected double lineSearch () {
		previousStepSize = 0;
		int n = graph.numberOfVertices;
		double magDescDir = l2Norm (descentDirection);
		if (magDescDir < 0.0001) return 0;
		double magLo = l2Norm (negativeGradient);
		step (); getNegativeGradient ();
		double magHi = l2Norm (negativeGradient);
		double m = magDescDir * magHi;
		double cos = dotProduct (negativeGradient, descentDirection) / m;
		double lo = 0, hi = Double.MAX_VALUE;
		int i = 0;
		while ((i < maximumIterations) && ((cos < 0) || (cos > maxCos) && (hi - lo > 0.00000001))) {
			if (cos < 0) {hi = stepSize; stepSize = (lo+hi)/2;}
			else {
				if (hi < Double.MAX_VALUE) {lo = stepSize; stepSize = (lo+hi)/2;}
				else {lo = stepSize; stepSize *= 2;}
			}
			step (); getNegativeGradient ();
			m = magDescDir * l2Norm (negativeGradient);
			cos = dotProduct (negativeGradient, descentDirection) / m;
      i++;
		}
		return l2Norm (negativeGradient);
	}

	private void step () {
		int n = graph.numberOfVertices;
		double s = stepSize - previousStepSize;
		for (int i = 0; i < n; i++)
			graph.vertices [i].translate (s, descentDirection [i]);
		previousStepSize = stepSize;
	}

	protected double dotProduct (double [] [] u, double [] [] v) {
		int n = graph.numberOfVertices, d = graph.getDimensions ();
		double sum = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < d; j++) {
				sum += u [i] [j] * v [i] [j];
			}
		}
		return sum;
	}

	protected double l2Norm (double [] [] vect) {
		return Math.sqrt (dotProduct (vect, vect));
	}

	protected double lInfinityNorm (double [] [] vect) {
		int n = graph.numberOfVertices, d = graph.getDimensions ();
		double max = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < d; j++) {
				max = Math.max (max, Math.abs (vect [i] [j]));
			}
		}
		return max;
	}
}