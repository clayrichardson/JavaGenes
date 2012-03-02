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
package gov.nasa.javaGenes.chemistry;
//  Created by Al Globus on Fri Jun 14 2002.
import junit.framework.TestCase;
import java.lang.Math;
import gov.nasa.alsUtility.Utility;
import gov.nasa.javaGenes.graph.Vertex;

public class UnitCellTest extends TestCase {

public UnitCellTest(String name) {super(name);}
private UnitCell unitCell;

public void testConstructor() {
    UnitCell u1 = new UnitCell(10);
    UnitCell u2 = new UnitCell(10,10,10);
    UnitCell u3 = new UnitCell(0,10,0,10,0,10);
    UnitCell u4 = new UnitCell(0,10,0,10,0.1,10);
    assertTrue("1",u1.nearlyEquals(u2));
    assertTrue("2",u2.nearlyEquals(u3));
    assertTrue("bad",!u3.nearlyEquals(u4));
}
public void testCopy() {
    UnitCell u = new UnitCell(10);
    UnitCell c = u.copy();
    assertTrue("1", u.nearlyEquals(c));
    u = new UnitCell(1,2,3);
    c = u.copy();
    assertTrue("2",u.nearlyEquals(c));
    u = new UnitCell(1,2,3,4,5,6);
    c = u.copy();
    assertTrue("3",u.nearlyEquals(c));
    u = new UnitCellNone();
    c = u.copy();
    assertTrue("4", c instanceof UnitCellNone);
}
public void testScaleBy() {
    UnitCell u = new UnitCell(1,2,3,4,5,6);
    UnitCell c = u.copy();
    c.scaleBy(1.2);
    UnitCell correct = new UnitCell(1.2,2.4,3.6,4.8,6.0,7.2);
    assertTrue("1",c.nearlyEquals(correct));
    
    c = u.copy();
    c.scaleBy(0.5);
    correct = new UnitCell(0.5,1.0,1.5,2.0,2.5,3.0);
    assertTrue("2",c.nearlyEquals(correct));
}
public void testMoveInside() {
    UnitCell u = new UnitCell(1);
    Vertex v = new Vertex();
    v.setXyz(1.5,7.1,-0.3);
    u.moveInside(v);
    double xyz[] = v.getXyz();
    assertTrue("0", Utility.nearlyEqual(xyz[0],0.5));
    assertTrue("1", Utility.nearlyEqual(xyz[1],0.1));
    assertTrue("2", Utility.nearlyEqual(xyz[2],0.7));
}
public void testToString() {
    UnitCell c = new UnitCell(1,2,3,4,5,6);
    assertTrue(c.toString().equals("1.0,2.0,3.0,4.0,5.0,6.0"));
}
public void testGetSeparation() {
    unitCell = new UnitCell(1,1,1);
    check("1",0, 0.0,0.5, 0.5);
    check("2",1, 0.4,0.0, 0.4);
    check("2",2, 0.1,0.9, 0.2);
    check("2",0, 0.9,0.1, 0.2);
}
private void check(String name, int dimension, double x, double y, double separation) {
    assertTrue(name, Utility.nearlyEqual(unitCell.getSeparation(dimension,x,y),separation));
}
public void testDistance() {
    unitCell = new UnitCell(10);
    Atom a = new Atom("Si");
    Atom b = new Atom("C");
    Atom x = new Atom("Ge");
    a.setXyz(1,0,0);
    b.setXyz(2,0,0);
    assertTrue("simple", Utility.nearlyEqual(unitCell.getDistance(a,b),1));
    b.setXyz(9,0,0);
    assertTrue("simple longer", Utility.nearlyEqual(unitCell.getDistance(a,b),2));

    a.setXyz(1,2,3);
    b.setXyz(9,8,9.5);
    x.setXyz(-1,-2,-0.5);
    check("a-b",a,b,x);
    assertTrue("b-a",Utility.nearlyEqual(unitCell.getDistance(b,a),unitCell.getDistance(a,b)));

    a.setXyz(0,0,0);
    check("a0-b",a,b,x);
    a.setXyz(0,1,0);
    check("a1-b",a,b,x);
    
    b.setXyz(2,2,2);
    check("a-b2",a,b,b);
    
    a.setXyz(1,1,1);
    b.setXyz(10,10,10);
    x.setXyz(0,0,0);
    check("extremes",a,b,x);
    
    b.setXyz(10,9,0);
    x.setXyz(0,-1,0);
    check("some extremes",a,b,x);
}
private void check(String name, Atom a, Atom b, Atom check) {
    assertTrue(name,Utility.nearlyEqual(unitCell.getDistance(a,b),a.getDistanceTo(check)));
}
public void testAngle() {
    unitCell = new UnitCell(10);
    Atom a = new Atom("Si");
    Atom b = new Atom("C");
    Atom c = new Atom("GE");
    Atom x = new Atom("H");
    a.setXyz(1,1,1);
    b.setXyz(2,1,1);
    c.setXyz(1,2,1);
    double angle = unitCell.getAngle(b,a,c);

    assertTrue("90 degress",Utility.nearlyEqual(angle,Math.PI/2));
    a.setXyz(1,1,1);
    b.setXyz(2,1,1);
    c.setXyz(1,9,1);
    angle = unitCell.getAngle(b,a,c);
    assertTrue("90 degress reflected",Utility.nearlyEqual(angle,Math.PI/2));

    a.setXyz(1,2,3);
    b.setXyz(9,8,9.5);
    c.setXyz(2,2,2);
    x.setXyz(-1,-2,-0.5);
    assertTrue("1",Utility.nearlyEqual(unitCell.getAngle(b,a,c),a.getAngleBetween(x,c)));
    assertTrue("2",Utility.nearlyEqual(unitCell.getAngle(b,a,c),unitCell.getAngle(c,a,b)));
}
public void testIsInside() {
    unitCell = new UnitCell(10);
    Atom a = new Atom("Si");
    a.setXyz(5,5,5);
    assertTrue("1",unitCell.isInside(a));
    a.setXyz(5,5,11);
    assertTrue("2",!unitCell.isInside(a));
    a.setXyz(5,-1,5);
    assertTrue("3",!unitCell.isInside(a));
}
public void testIsCutoffInBounds() {
    UnitCell unitCell = new UnitCell(10);
    assertTrue("good",unitCell.isCutoffInBounds(3));
    assertTrue("bad",!unitCell.isCutoffInBounds(6));
}
}
