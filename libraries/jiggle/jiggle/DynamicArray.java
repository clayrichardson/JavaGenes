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

/* Methods for manipulating dynamic arrays of JiggleObjects. */

public abstract class DynamicArray {

	public static Vertex [] add (Vertex [] arr, int size, Vertex elem) {
		if (size == arr.length) {
			Vertex newArr [] = new Vertex [2 * size];
			for (int i = 0; i < size; i++) newArr [i] = arr [i];
			arr = newArr;
		}
		arr [size] = elem; return arr;
	}

	public static Edge [] add (Edge [] arr, int size, Edge elem) {
		if (size == arr.length) {
			Edge newArr [] = new Edge [2 * size];
			for (int i = 0; i < size; i++) newArr [i] = arr [i];
			arr = newArr;
		}
		arr [size] = elem; return arr;
	}

	public static void remove (JiggleObject [] arr, int size, JiggleObject elem)
			throws NotFoundException {
		for (int i = 0; i < size; i++)
			if (arr [i] == elem) {arr [i] = arr [size - 1]; return;}
		throw new NotFoundException ();
	}
}
