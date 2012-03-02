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

/* Abstract base class for all JIGGLE objects. */

public abstract class JiggleObject {

	private JiggleObject context = null;
	public JiggleObject getContext () {return context;}
	protected void setContext (JiggleObject c) {context = c;}

	/* The context of a JiggleObject identifies the parent JiggleObject
	(if any) that contains it.  The context of a Vertex or Cell is either
	a Graph or a Cell; the context of an Edge is a Graph; the context of
	an EdgeLabel is an Edge.  For now, we assume that the	context of a
	Graph is null; if, however, we extend the present implementation to
	include composite graphs, then the context of a Graph	could be a
	JiggleObject (e.g. a Vertex) that contains the graph inside	it. */

	boolean booleanField = false;
	int intField = 0;
	Object objectField = null;

	static double square (double d) {return d * d;}
	static double cube (double d) {return d * d * d;}
	static int intSquare (int n) {return n * n;}

	static int power (int base, int d) {
		if (d == 0) return 1;
		else if (d == 1) return base;
		else if (d % 2 == 0) return intSquare (power (base, d / 2));
		else return base * intSquare (power (base, d / 2));
	}
}