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
package gov.nasa.alsUtility;

import junit.framework.TestCase;

public class LogComparisonsTest extends TestCase {
public LogComparisonsTest(String name) {super(name);}


public void testReporting() {
	String[] names = {"foo"};
	LogComparisons results = new LogComparisons(names);
    
    double[] i1 = {1};
    double[] i2 = {2};
    
    double[][] parents = {{1}};
    results.results(i2,parents);
    assertTrue("1", results.checkResults(0,1,0,0));

    results.results(i1,parents);
    assertTrue("2",results.checkResults(0,1,0,1));
    double[][] parents1 = {{0,2}};
    results.results(i1,parents1);
    assertTrue("3",results.checkResults(0,1,0,2));
	
	String[] n = {"foo", "fee"};
	results = new LogComparisons(n);
	double[] ii1 = {1,2};
	i1 = ii1;
	double[] ii2 = {2,2};
	i2 = ii2;
	double[][]p = {{1},{2}};
	parents = p;

    results.results(i2,parents);
    assertTrue("3.1", results.checkResults(0,1,0,0));
    assertTrue("4", results.checkResults(1,0,0,1));
	results.clear();
    assertTrue("last",results.checkResults(0,0,0,0));
}
public void testScaleIntBy() {
	String[] names = {"foo"};
	LogComparisons results = new LogComparisons(names);
	Error.assertTrue(results.scaleIntBy(10, 0.1) == 1);
	Error.assertTrue(results.scaleIntBy(10, 0.15) == 1);
	Error.assertTrue(results.scaleIntBy(10, 0) == 0);
	Error.assertTrue(results.scaleIntBy(10, 1) == 10);
	Error.assertTrue(results.scaleIntBy(10, 0.52) == 5);
}
public void testScaleBy() {
	String[] names = {"foo"};
	LogComparisons results = new LogComparisons(names);
	results.setResults(0,10,20,30);
	results.scaleBy(0.1);
	Error.assertTrue(results.checkResults(0,1,2,3));
}
}


