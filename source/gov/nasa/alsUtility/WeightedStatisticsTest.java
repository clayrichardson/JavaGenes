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

public class WeightedStatisticsTest extends TestCase {

public WeightedStatisticsTest(String name) {super(name);}

public void test() {
  double[] data1 = {1,2,3};
  WeightedStatistics stats = new WeightedStatistics();
  stats.addData(data1);
  test("1", data1, null, 2, 1, 3, 2, 3, 3);
  test("2", data1, data1, 14.0/6, 1, 9, 8, 6, 3);
  double[] weights1 = {4,1,1};
  test("3", data1, weights1, 9.0/6.0, 2, 4, 2, 6, 3);
  double[] data2 = {5};
  test("4", data2, null, 5, 5, 5, 0, 1, 1);
  double[] weights2 = {0};
  test("5", data2, weights2, 0, Double.MAX_VALUE, -Double.MAX_VALUE, 0, 0, 0);
  double[] weights3 = {1,0,1};
  test("6", data1, weights3, 2, 1, 3, 2, 2, 2);
  double[] weights4 = {0,2,1};
  test("7", data1, weights4, 7.0/3.0, 3, 4, 1, 3, 2);
}
private void test(String name, double[] data, double[] weights, double mean, double min, double max, double spread, double weightSum, int count) {
	WeightedStatistics stats = new WeightedStatistics();
	if (weights == null)
		stats.addData(data);
	else
		stats.addData(data,weights);
	assertTrue(name + " mean " + stats.getMean(), Utility.nearlyEqual(mean,stats.getMean()));
	assertTrue(name + " min " + stats.getMin(), Utility.nearlyEqual(min,stats.getMin()));
	assertTrue(name + " max " + stats.getMax(), Utility.nearlyEqual(max,stats.getMax()));
	assertTrue(name + " spread " + stats.getSpread(), Utility.nearlyEqual(spread,stats.getSpread()));
	assertTrue(name + " weightSum", Utility.nearlyEqual(weightSum,stats.getSumOfWeights()));
	assertTrue(name + " count " + stats.getN(), count == stats.getN());
}
public void testRMS() {
	double[] data = {3,4};
	double[] weights = {2,1};
	WeightedStatistics stats = new WeightedStatistics();
	stats.addData(data,weights);
	assertTrue("" + stats.getRMS(), Utility.nearlyEqual(3.366501646120693,stats.getRMS()));
}
}