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

/* Class for conjugate gradient method. */

public class ConjugateGradients extends FirstOrderOptimizationProcedure {

	private double magnitudeOfPreviousGradientSquared;
	private double previousDescentDirection [] [] = null;
	private double restartThreshold = 0;

	public ConjugateGradients (Graph g, ForceModel fm, double acc) {
		super (g, fm, acc); restartThreshold = 0;
	}

	public ConjugateGradients (Graph g, ForceModel fm, double acc, double rt) {
		super (g, fm, acc); restartThreshold = rt;
	}

	public void reset () {negativeGradient = null; descentDirection = null;}

	protected void computeDescentDirection () {
		int n = graph.numberOfVertices, d = graph.getDimensions ();
		double magnitudeOfCurrentGradientSquared = 0;
		if ((descentDirection == null) || (descentDirection.length != n)) {
			descentDirection = new double [n] [d];
			previousDescentDirection = new double [n] [d];
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < d; j++) {
					double temp = negativeGradient [i] [j];
					descentDirection [i] [j] = temp;
					magnitudeOfCurrentGradientSquared += square (temp);
				}
			}
		}
		else {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < d; j++) {
					double temp = negativeGradient [i] [j];
					magnitudeOfCurrentGradientSquared += square (temp);
				}
			}
			if (magnitudeOfCurrentGradientSquared < 0.000001) {
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < d; j++) {
						previousDescentDirection [i] [j] = 0;
						descentDirection [i] [j] = 0;
					}
				}
				return;
			}
			double w = magnitudeOfCurrentGradientSquared / magnitudeOfPreviousGradientSquared;
			double dotProduct = 0, magnitudeOfDescentDirectionSquared = 0, m;
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < d; j++) {
					descentDirection [i] [j] = negativeGradient [i] [j] +
					                           w * previousDescentDirection [i] [j];
					dotProduct += descentDirection [i] [j] * negativeGradient [i] [j];
					magnitudeOfDescentDirectionSquared += square (descentDirection [i] [j]);
				}
			}
			m = magnitudeOfCurrentGradientSquared * magnitudeOfDescentDirectionSquared;
			if (dotProduct / Math.sqrt (m) < restartThreshold) {
				descentDirection = null; computeDescentDirection (); return;
			}
		}
		magnitudeOfPreviousGradientSquared = magnitudeOfCurrentGradientSquared;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < d; j++) {
				previousDescentDirection [i] [j] = descentDirection [i] [j];
			}
		}
	}
}