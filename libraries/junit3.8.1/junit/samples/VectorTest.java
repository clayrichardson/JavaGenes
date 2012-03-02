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
package junit.samples;

import junit.framework.*;
import java.util.Vector;

/**
 * A sample test case, testing <code>java.util.Vector</code>.
 *
 */
public class VectorTest extends TestCase {
	protected Vector fEmpty;
	protected Vector fFull;

	public static void main (String[] args) {
		junit.textui.TestRunner.run (suite());
	}
	protected void setUp() {
		fEmpty= new Vector();
		fFull= new Vector();
		fFull.addElement(new Integer(1));
		fFull.addElement(new Integer(2));
		fFull.addElement(new Integer(3));
	}
	public static Test suite() {
		return new TestSuite(VectorTest.class);
	}
	public void testCapacity() {
		int size= fFull.size(); 
		for (int i= 0; i < 100; i++)
			fFull.addElement(new Integer(i));
		assertTrue(fFull.size() == 100+size);
	}
	public void testClone() {
		Vector clone= (Vector)fFull.clone(); 
		assertTrue(clone.size() == fFull.size());
		assertTrue(clone.contains(new Integer(1)));
	}
	public void testContains() {
		assertTrue(fFull.contains(new Integer(1)));  
		assertTrue(!fEmpty.contains(new Integer(1)));
	}
	public void testElementAt() {
		Integer i= (Integer)fFull.elementAt(0);
		assertTrue(i.intValue() == 1);

		try { 
			fFull.elementAt(fFull.size());
		} catch (ArrayIndexOutOfBoundsException e) {
			return;
		}
		fail("Should raise an ArrayIndexOutOfBoundsException");
	}
	public void testRemoveAll() {
		fFull.removeAllElements();
		fEmpty.removeAllElements();
		assertTrue(fFull.isEmpty());
		assertTrue(fEmpty.isEmpty()); 
	}
	public void testRemoveElement() {
		fFull.removeElement(new Integer(3));
		assertTrue(!fFull.contains(new Integer(3)) ); 
	}
}