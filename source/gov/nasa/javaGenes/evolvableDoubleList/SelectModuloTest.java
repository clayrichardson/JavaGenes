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
package gov.nasa.javaGenes.evolvableDoubleList;

import junit.framework.TestCase;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.Utility;

public class SelectModuloTest extends TestCase {

public SelectModuloTest(String name) {super(name);}

public void testGetDummyForSize() {
	testGetDummyForSize("1", 3, 2, 0, 2);
	testGetDummyForSize("2", 3, 2, 1, 1);
	testGetDummyForSize("3", 1, 2, 0, 1);
	testGetDummyForSize("4", 1, 12, 0, 1);
	testGetDummyForSize("5", 10, 3, 0, 4);
	testGetDummyForSize("6", 10, 3, 1, 3);
	testGetDummyForSize("7", 10, 3, 3, 3);
}
private void testGetDummyForSize(String name, int evolvableSize, int modulo, int offset, int result) {
	SelectModulo selector = new SelectModulo(new SelectOne(), modulo, offset);
	int r = selector.getDummySize(evolvableSize);
	assertTrue(name + " " + r, result == r);
}
public void testConvertBack() {
	testConvertBack("1", 2, 0, 0, 0);
	testConvertBack("2", 2, 0, 1, 2);
	testConvertBack("3", 2, 1, 0, 1);
	testConvertBack("4", 2, 1, 1, 3);
	testConvertBack("5", 3, 0, 2, 6);
	testConvertBack("6", 3, 2, 2, 8);
}
private void testConvertBack(String name, int modulo, int offset, int indexToDummy, int result) {
	SelectModulo selector = new SelectModulo(new SelectOne(), modulo, offset);
	int r = selector.convertBack(indexToDummy);
	assertTrue(name + " " + r, result == r);
}
public void testGetIndicesArray() {
	int[] a1 = {0,2,4};
	testGetIndicesArray("1", 5, 2, 0, a1);
	int[] a2 = {1,3};
	testGetIndicesArray("2", 5, 2, 1, a2);
	int[] a3 = {0,3,6,9};
	testGetIndicesArray("3", 10, 3, 0, a3);
	int[] a4 = {1,4,7};
	testGetIndicesArray("4", 10, 3, 1, a4);
	int[] a5 = {2,5,8};
	testGetIndicesArray("5", 10, 3, 2, a5);
}
private void testGetIndicesArray(String name, int evolvableSize, int modulo, int offset, int[] result) {
	SelectModulo selector = new SelectModulo(new SelectAll(), modulo, offset);
	int[] r = selector.getIndicesArray(evolvableSize);
	assertTrue(name + " " + Utility.toString(r), Utility.equals(result,r));
}
}