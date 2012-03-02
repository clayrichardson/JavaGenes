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

public class FitnessMultiObjectiveTest extends TestCase {
private FitnessFunctionMultiObjective function ;
private FitnessMultiObjective f1;
private FitnessMultiObjective f2;

public FitnessMultiObjectiveTest(String name) {super(name);}

public void setUp() {
  function = new FitnessFunctionMultiObjective();
  f1 = new FitnessMultiObjective(function);
  f2 = new FitnessMultiObjective(function);
}
public void testEvaluateFitness() {
  add(1,1,2);
  assertTrue("1", f1.fitterThan(f2));
  assertTrue("1.5", !f2.fitterThan(f1));
  add(1,3,1);
  assertTrue("2", !f1.fitterThan(f2));
  assertTrue("3", f2.fitterThan(f1));
  add(1,1,2);
  assertTrue("4", f1.fitterThan(f2));
  add(2,2,1);
  assertTrue("5", f2.fitterThan(f1));
}
public void testIsDominatedBy() {
  add(1,1,1);
  assertTrue("1",!f1.isDominatedBy(f2));
  add(1,2,1);
  assertTrue("2",f1.isDominatedBy(f2));
  assertTrue("3",!f2.isDominatedBy(f1));
  add(1,1,2);
  assertTrue("3",!f1.isDominatedBy(f2));
  assertTrue("4",!f2.isDominatedBy(f1));
}

private void add(double weight, double f1Value, double f2Value) {
  final FitnessFunction dummy = new FitnessFunctionFixed(0);
  function.add(weight,dummy);
  f1.add(new FitnessDouble(f1Value));
  f2.add(new FitnessDouble(f2Value));
}

}