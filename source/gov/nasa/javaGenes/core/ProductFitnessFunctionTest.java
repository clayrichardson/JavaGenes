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
//  Created by Al Globus on Wed Oct 30 2002.
package gov.nasa.javaGenes.core;

import junit.framework.TestCase;

public class ProductFitnessFunctionTest extends TestCase {

public ProductFitnessFunctionTest(String name) {super(name);}

public void testEvaluateFitness() {
  Evolvable e = new Evolvable();
  ProductFitnessFunction f = new ProductFitnessFunction();
  f.add(1,new FitnessFunctionFixed(0));
  assertTrue("1", f.evaluateFitness(e).asDouble() == 1);
  f.add(2,new FitnessFunctionFixed(2));
  assertTrue("2 " + f.evaluateFitness(e).asDouble(), f.evaluateFitness(e).asDouble() == 5);
  f.add(0,new FitnessFunctionFixed(5));
  assertTrue("3",f.evaluateFitness(e).asDouble() == 5);
  f.add(2,new FitnessFunctionFixed(5));
  assertTrue("4",f.evaluateFitness(e).asDouble() == 55);
}
public void testNumberOfObjectives() {
  WeightedSumFitness f = new WeightedSumFitness();
  assertTrue(f.numberOfObjectives() == 1);
}
public void testNames() {
  WeightedSumFitness f = new WeightedSumFitness();
  final String name = "foo";
  f.setName(name);
  String[] names = f.getNameArray();
  assertTrue(name.equals(names[0]));
  assertTrue(names.length == 1);
}
}

