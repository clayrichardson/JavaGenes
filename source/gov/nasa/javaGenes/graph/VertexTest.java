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
package gov.nasa.javaGenes.graph;
import junit.framework.TestCase;
import java.lang.Math;
import gov.nasa.alsUtility.Vector3d;
import gov.nasa.alsUtility.Utility;

public class VertexTest extends TestCase {

public VertexTest(String name) {super(name);}
private Vertex a = new Vertex();
private Vertex b = new Vertex();
private Vertex c = new Vertex();
public void setUp() {
  a = new Vertex();
  Vertex b = new Vertex();
  Vertex c = new Vertex();
}
public void testTranslate() {
  Vector3d t = new Vector3d(1,2,3);
  Vector3d t2 = Vector3d.multiply(t,2);
  a.translate(t);
  assertTrue("first translation", t.nearlyEqual(a.getLocationVector()));
  a.translate(t);
  assertTrue("second translation", t2.nearlyEqual(a.getLocationVector()));
}
public void testGetDistanceTo() {
  a.setXyz(0,0,0);
  b.setXyz(1,0,0);
  assertTrue(a.getDistanceTo(b) == 1);
  b.setXyz(0,-1,0);
  assertTrue(a.getDistanceTo(b) == 1);
  b.setXyz(0,3,4);
  assertTrue(a.getDistanceTo(b) == 5);
}
public void testGetAngleBetween() {
  a.setXyz(1,0,0);
  b.setXyz(0,0,0);
  c.setXyz(2,0,0);
  assertTrue(a.getAngleBetween(b,c)==Math.PI);
  a.setXyz(-1,0,0);
  assertTrue(a.getAngleBetween(b,c)==0);
  a.setXyz(-1,0,0);
  c.setXyz(-1,1,0);
  assertTrue(Utility.nearlyEqual(a.getAngleBetween(b,c),Math.PI/2));
  a.setXyz(1,0,0);
  c.setXyz(1.5,1.0,0);
  double larger = a.getAngleBetween(b,c);
  c.setXyz(0.75,1,0);
  double smaller = a.getAngleBetween(b,c);
  assertTrue(smaller < larger);
}
public void testScaleBy() {
    a.setXyz(1,-1,2);
    a.scaleBy(1.2);
    double xyz[] = a.getXyz();
    assertTrue("1",xyz[0] == 1.2);
    assertTrue("2",xyz[1] == -1.2);
    assertTrue("3",xyz[2] == 2.4);
}
public void testMoveInside() {
    a.setXyz(0,1,2.1);
    a.moveInsideInterval(0,0.0,1.0);
    a.moveInsideInterval(1,0.0,1.0);
    a.moveInsideInterval(2,0.0,1.0);
    double xyz[] = a.getXyz();
    assertTrue("1",xyz[0] == 0);
    assertTrue("2",xyz[1] == 1);
    assertTrue("3",Utility.nearlyEqual(xyz[2],0.1));    

    a.setXyz(0,-1,-2.1);
    a.moveInsideInterval(0,0.0,1.0);
    a.moveInsideInterval(1,0.0,1.0);
    a.moveInsideInterval(2,0.0,1.0);
    xyz = a.getXyz();
    assertTrue("4",xyz[0] == 0);
    assertTrue("5",Utility.nearlyEqual(xyz[1],0));
    assertTrue("6",Utility.nearlyEqual(xyz[2],0.9));    
}
}

