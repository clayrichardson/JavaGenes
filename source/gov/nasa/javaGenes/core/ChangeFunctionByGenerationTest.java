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

public class ChangeFunctionByGenerationTest extends TestCase {
public ChangeFunctionByGenerationTest(String name) {super(name);}
private ChangeFunctionByGeneration cc;
private ee e = new ee();

public void setUp() {
  cc = new ChangeFunctionByGeneration();
  cc.add(10,new ff(10.0));
  cc.add(1,new ff(2.0));
  cc.add(5,new ff(15.0));
}

public void testGetLastGeneration() {
  assertTrue("getLastGeneration", cc.getLastGeneration() == 16);
}
public void testGetFunction() {
  assertTrue("default function", 10.0 == cc.evaluateFitness(e).asDouble());
  for(int i = -3; i <= 10; i++) // negative generations not recommended, but works.
    test(i,10.0);
  test(11,2.0);
  for(int i = 12; i <= 25; i++)
    test(i,15.0);
}
public void testIsNewFunction() {
  for(int i = -3; i <= 25; i++)
    if (i == 11 || i == 12)
      assertTrue("is new", cc.isNewFunction(i));
    else
      assertTrue("is not new", !cc.isNewFunction(i));
}
private void test(int generation, double value) {
  FitnessFunction f = cc.getFunction(generation);
  assertTrue("generation " + generation, value == f.evaluateFitness(e).asDouble());
}

private class ff extends FitnessFunction {
  private double value;
  public ff(double d) {value = d;}
  public Fitness evaluateFitness (Evolvable evolvable) {return new FitnessDouble(value);}
}
private class ee extends Evolvable {
  public int getSize() {return 1;}
}
}

