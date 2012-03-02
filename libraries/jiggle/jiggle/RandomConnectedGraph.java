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

public class RandomConnectedGraph extends jiggle.Graph {

	public RandomConnectedGraph (int n, int m) {super (); initialize (n, m);}
	public RandomConnectedGraph (int n, int m, int d) {
		super (d); initialize (n, m);
	}

	private int randomInt (int n) {return (int) (Math.random () * n);}

	private void initialize (int n, int m) {
		Vertex V [] = new Vertex [n];
		for (int i = 0; i < n; i++) V [i] = insertVertex ();
		for (int i = 1; i < n; i++)
			insertEdge (V [randomInt (i - 1)], V [i]);
		for (int i = 0; i < m - (n - 1); i++) {
			outerLoop: while (true) {
				int from = randomInt (n-1);
				int to = randomInt (n-1);
				if (from >= to) continue;
				for (int j = 0; j < n - 1 + i; j++) {
					Edge e = edges [j];					
					if ((V [from] == e.getFrom ()) &&
					    (V [to] == e.getTo ()))
						continue outerLoop;
				}
				insertEdge (V [from], V [to]);
				break;
			}
		}
	}
}
