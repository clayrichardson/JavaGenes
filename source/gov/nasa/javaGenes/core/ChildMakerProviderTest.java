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
package gov.nasa.javaGenes.core;

import junit.framework.TestCase;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.Utility;

public class ChildMakerProviderTest extends TestCase {

public ChildMakerProviderTest(String name) {super(name);}
public void testAddRemoveSize() {
	ChildMakerProvider provider = new ChildMakerProvider();
	Error.assertTrue(provider.size() == 0);
	provider.add(new ChildMaker());
	Error.assertTrue(provider.size() == 1);
	provider.add(new ChildMaker());
	Error.assertTrue(provider.size() == 2);
	provider.removeLastChildMaker();
	Error.assertTrue(provider.size() == 1);
	provider.removeLastChildMaker();
	Error.assertTrue(provider.size() == 0);
	provider.removeLastChildMaker();
	Error.assertTrue(provider.size() == 0);
}
}