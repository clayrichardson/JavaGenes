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
package gov.nasa.javaGenes.forceFields;

import junit.framework.TestCase;
import gov.nasa.alsUtility.Utility;
import gov.nasa.javaGenes.chemistry.Atom;
import gov.nasa.alsUtility.DoubleInterval;

public class TwoBodyTest extends TestCase {
private Atom a = new Atom("Si");
private Atom b = new Atom("Si");
private Atom c = new Atom("F");
private Atom d = new Atom("F");

public TwoBodyTest(String name) {super(name);}

public void setUp() {
  a.setXyz(0,0,0);
  b.setXyz(1,0,0);
  c.setXyz(0,-4,0);
  d.setXyz(0,0,0);
}
public void testNearlyEqual() {
    TwoBody t1 = new TwoBody(a,b);
    TwoBody t2 = new TwoBody(b,d);
    TwoBody t3 = new TwoBody(d,b);
    Atom e = new Atom("Si");
    e.setXyz(1.0000001,0,-0.00000001);
    TwoBody t4 = new TwoBody(a,e);
    Atom f = new Atom("Si");
    f.setXyz(1.1,0,-0.9);
    TwoBody t5 = new TwoBody(a,f);

    assertTrue(t1.nearlyEqual(t1));
    assertTrue(!t1.nearlyEqual(t3));
    assertTrue(t2.nearlyEqual(t3));
    assertTrue(t1.nearlyEqual(t4));
    assertTrue(!t1.nearlyEqual(t5));
}
public void testConstructor() {
  TwoBody t;
  t = new TwoBody(a,b);
  assertTrue("check distance 1",Utility.nearlyEqual(t.getR(),1));
  assertEquals("SiSi",t.getName());
  t = new TwoBody(a,c);
  assertTrue("check distance 4",Utility.nearlyEqual(t.getR(),4));
  assertEquals("FSi",t.getName());
  t = new TwoBody(c,a);
  assertEquals("FSi",t.getName());
}
public void testScaleLengthsBy() {
  TwoBody t = new TwoBody(a,b);
  t.scaleLengthsBy(0.5);
  assertTrue("scale check",Utility.nearlyEqual(t.getR(),0.5));
}
public void testWithinCutoff() {
  DoubleInterval interval = new DoubleInterval();
  StillingerWeberSiF form = new StillingerWeberSiF(interval,interval,interval);
  c.setXyz(0,2.07,0);
  TwoBody t = new TwoBody(c,d);
  assertTrue("inside",t.withinCutoff(form));
  c.setXyz(0,2.1,0);
  t = new TwoBody(d,c);
  assertTrue("outside",!t.withinCutoff(form));
}
}