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
import gov.nasa.alsUtility.LogComparisons;

public class ChildMakerDownFractionComparatorTest extends TestCase {

public ChildMakerDownFractionComparatorTest(String name) {super(name);}
public void test() {
	ChildMakerDownFractionComparator comparator = new ChildMakerDownFractionComparator();
	ChildMaker c1 = new ChildMaker();
	ChildMaker c2 = new ChildMaker();
	String[] names = {"foo"};
	c1.forEvolution = new LogComparisons(names);
	c2.forEvolution = new LogComparisons(names);
	Error.assertTrue("1", comparator.compare(c1,c2) == 0);
	c1.forEvolution.setResults(0,0,1,1);
	Error.assertTrue("2", comparator.compare(c1,c2) == 1);
	c1.forEvolution.setResults(0,0,0,0);
	c2.forEvolution.setResults(0,0,1,1);
	Error.assertTrue("3", comparator.compare(c1,c2) == -1);
	c1.forEvolution.setResults(0,0,1,0);
	Error.assertTrue("4", comparator.compare(c1,c2) == -1);
	c1.forEvolution.setResults(0,1,1,1);
	Error.assertTrue("5", comparator.compare(c1,c2) == 1);
}
}