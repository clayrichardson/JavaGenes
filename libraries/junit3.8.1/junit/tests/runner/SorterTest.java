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
package junit.tests.runner;

import java.util.Vector;

import junit.framework.*;
import junit.runner.Sorter;

public class SorterTest extends TestCase {
	
	static class Swapper implements Sorter.Swapper {
		public void swap(Vector values, int left, int right) {
			Object tmp= values.elementAt(left); 
			values.setElementAt(values.elementAt(right), left); 
			values.setElementAt(tmp, right);
		}
	}
	
	public void testSort() throws Exception {
		Vector v= new Vector();
		v.addElement("c");
		v.addElement("b");
		v.addElement("a");
		Sorter.sortStrings(v, 0, v.size()-1, new Swapper());
		assertEquals(v.elementAt(0), "a");
		assertEquals(v.elementAt(1), "b");
		assertEquals(v.elementAt(2), "c");
	}
}