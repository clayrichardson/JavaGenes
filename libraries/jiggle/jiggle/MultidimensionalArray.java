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

public class MultidimensionalArray extends JiggleObject {

	private int dimensions = 0;
	private int size [] = null;
	private int numberOfCells = 0;
	private Object cells [] = null;

	MultidimensionalArray (int d, int [] s) {
		dimensions = d; size = new int [d]; numberOfCells = 1;
		for (int i = 0; i < d; i++) {
			numberOfCells *= (size [i] = s [i]);
		}
		cells = new Object [numberOfCells];
	}

	int getDimensions () {return dimensions;}

	Object get (int [] index) {return cells [rankOf (index)];}

	void set (int [] index, Object obj) {cells [rankOf (index)] = obj;}

	private int rankOf (int [] index) {
		int rank = 0, column = 1;
		for (int i = 0; i < dimensions; i++) {
			rank += index [i] * column;
			column *= size [i];
		}
		return rank;
	}
}
