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
import junit.framework.TestCase;
import gov.nasa.alsUtility.Vector3d;

public class AtomTest extends TestCase {

public AtomTest(String name) {super(name);}
private Atom Si;
private Atom F;
private Atom Si2;
private Atom C;
private Atom[] array;
private Molecule molecule;

public void setUp() {
  Si = new Atom("Si");
  F = new Atom("F");
  Si2 = new Atom("Si");
  C = new Atom("C");
  array = new Atom[3];
  array[0] = Si;
  array[1] = F;
  array[2] = Si2;
  molecule = new Molecule();
  molecule.add(Si);
  molecule.add(F);
  molecule.add(Si2);
  molecule.add(C);
  molecule.makeBond(1,3,1); // Si to Si2
  molecule.makeBond(2,3,1);  // F to Si2
  molecule.makeBond(3,4,1); // C to Si2
}
public void testIsIn() {
  assertTrue("Si in", Si.isIn(array));
  assertTrue("F in", F.isIn(array));
  assertTrue("C out", !C.isIn(array));
}
public void testIsElement() {
  assertTrue("Si is", Si.isElement("Si"));
  assertTrue("Si isn't", !Si.isElement("C"));
  assertTrue("bad symbol", !Si.isElement("lskjfd"));
}
public void testTranslateWithNeighbors() {
  Vector3d translate = new Vector3d(1,1,1);
  Vector3d zero = new Vector3d(0,0,0);
  Atom a[] = {C};
  Si2.translateWithNeighbors(translate,a);
  assertTrue("C still", zero.nearlyEqual(C.getLocationVector()));
  for(int i = 0; i < array.length; i++)
    assertTrue(i + " moved", translate.nearlyEqual(array[i].getLocationVector()));
}
}
