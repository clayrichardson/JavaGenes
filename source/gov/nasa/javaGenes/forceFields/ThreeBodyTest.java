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

public class ThreeBodyTest extends TestCase {
private Atom a = new Atom("C");
private Atom b = new Atom("Si");
private Atom c = new Atom("F");
private Atom d = new Atom("Si");
private Atom e = new Atom("Si");

public ThreeBodyTest(String name) {super(name);}

public void setUp() {
  a.setXyz(0,0,0);
  b.setXyz(1,0,0);
  c.setXyz(0,-4,0);
  d.setXyz(0,0,0);
  e.setXyz(1.79,0,0);
}
public void testNearlyEqual() {
    ThreeBody t1 = new ThreeBody(a,b,c);
    ThreeBody t2 = new ThreeBody(c,b,a);
    ThreeBody t3 = new ThreeBody(e,b,c);
    Atom g = new Atom("Si");
    e.setXyz(1.0000001,0,-0.00000001);
    ThreeBody t4 = new ThreeBody(a,e,c);

    assertTrue(t1.nearlyEqual(t1));
    assertTrue(t1.nearlyEqual(t2));
    assertTrue(t1.nearlyEqual(t4));
    assertTrue(!t1.nearlyEqual(t3));
}

public void testConstructor() {
  ThreeBody t = new ThreeBody(b,a,c);
  assertTrue("is not FFF", !t.requiresStillingWeberFFFform());
  ThreeBody tt = new ThreeBody(c,a,b);
  assertTrue("tt is not FFF", !tt.requiresStillingWeberFFFform());
  assertTrue("check angle",Utility.nearlyEqual(t.getAngle(),Math.PI/2.0));
  assertTrue("check angle tt",Utility.nearlyEqual(tt.getAngle(),Math.PI/2.0));
  assertTrue("check length ji", t.getRJI() == 4);
  assertTrue("check length ji tt", tt.getRJI() == 4);
  assertTrue("check length jk", Utility.nearlyEqual(t.getRJK(),1));
  assertTrue("check length jk tt", Utility.nearlyEqual(tt.getRJK(),1));
  assertEquals("FCSi",t.getName());
  assertEquals("FCSi",tt.getName());
  
  ThreeBody ttt = new ThreeBody("F","H","I");
  assertTrue("requires FFF", ttt.requiresStillingWeberFFFform());
}
public void testScaleLengthsBy() {
  ThreeBody t = new ThreeBody(c,a,b);
  t.scaleLengthsBy(0.5);
  assertTrue("scale check",Utility.nearlyEqual(t.getRJI(),2.0));
  assertTrue("scale check",Utility.nearlyEqual(t.getRJK(),0.5));
}
public void testWithinCutoff() {
  DoubleInterval interval = new DoubleInterval();
  StillingerWeberSiF form = new StillingerWeberSiF(interval,interval,interval);
  ThreeBody t = new ThreeBody(b,d,e);
  assertTrue("inside",t.withinCutoff(form));
  e.setXyz(1.81,0,0);
  t = new ThreeBody(b,d,e);
  assertTrue("outside",!t.withinCutoff(form));
  b.setXyz(1.82,0,0);
  e.setXyz(1,0,0);
  assertTrue("outside, first one", !t.withinCutoff(form));
  Atom x = new Atom("F");
  Atom y = new Atom("F");
  Atom z = new Atom("F");
  x.setXyz(1.7,0,0);
  y.setXyz(0,0,0);
  z.setXyz(0,0,1.7);
  t = new ThreeBody(x,y,z);
  assertTrue("FFF inside",t.withinCutoff(form));
  z.setXyz(0,0,2.1);
  t = new ThreeBody(x,y,z);
  assertTrue("FFF outside",!t.withinCutoff(form));
}
}