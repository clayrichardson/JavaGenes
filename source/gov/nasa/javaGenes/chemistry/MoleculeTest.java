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
import gov.nasa.alsUtility.PropertiesList;
import gov.nasa.alsUtility.Utility;

public class MoleculeTest extends TestCase {
private Atom Si;
private Atom F;
private Atom Si2;
private Atom C;
private Molecule molecule;

public void setUp() {
  Si = new Atom("Si");
  F = new Atom("F");
  Si2 = new Atom("Si");
  C = new Atom("C");
  molecule = new Molecule();
  molecule.add(Si);
  molecule.add(F);
  molecule.add(Si2);
  molecule.add(C);
  molecule.makeBond(1,3,1); // Si to Si2
  molecule.makeBond(2,3,1);  // F to Si2
  molecule.makeBond(3,4,1); // C to Si2
}

public MoleculeTest(String name) {super(name);}

public void testPropertiesFromCommentAndPropertiesCopy() {
  Molecule molecule = new Molecule();
  molecule.setComment("energy = 0.15");
  molecule.setPropertiesFromComment();
  assertTrue("1", Utility.nearlyEqual(molecule.getEnergy(),0.15));
  assertTrue("1.1",!molecule.hasUnitCell());
  Molecule m = molecule.copy();
  assertTrue("1.2", Utility.nearlyEqual(molecule.getEnergy(),0.15));
  assertTrue("1.2",!molecule.hasUnitCell());

  molecule.setComment("energy = 0.15 dimerForce = 7.7");
  molecule.setPropertiesFromComment();
  assertTrue("2", Utility.nearlyEqual(molecule.getEnergy(),0.15));
  assertTrue("3", Utility.nearlyEqual(molecule.getDimerForce(),7.7));
  assertTrue("3.1",!molecule.hasUnitCell());

  molecule.setComment("energy = 0.15 unitCell = 0,1,2,3,4,5 dimerForce = 7.7");
  molecule.setPropertiesFromComment();
  assertTrue("4", Utility.nearlyEqual(molecule.getEnergy(),0.15));
  assertTrue("5", Utility.nearlyEqual(molecule.getDimerForce(),7.7));
  assertTrue("6", molecule.getUnitCell().nearlyEquals(new UnitCell(0,1,2,3,4,5)));
  assertTrue("6.1",molecule.hasUnitCell());
m = molecule.copy();
  assertTrue("4.2", Utility.nearlyEqual(m.getEnergy(),0.15));
  assertTrue("5.2", Utility.nearlyEqual(m.getDimerForce(),7.7));
  assertTrue("6.2", m.getUnitCell().nearlyEquals(new UnitCell(0,1,2,3,4,5)));
  assertTrue("6.2",m.hasUnitCell());
   
  molecule.setCommentFromProperties();
  String properties = molecule.getComment();
  PropertiesList list = new PropertiesList(molecule.getComment());
    assertTrue("10", list.hasProperty("energy"));
    assertTrue("11", list.getProperty("energy").equals("0.15"));
    assertTrue("12", list.hasProperty("unitCell"));
    assertTrue("13", list.getProperty("unitCell").equals("0.0,1.0,2.0,3.0,4.0,5.0"));
    assertTrue("14", list.hasProperty("dimerForce"));
    assertTrue("15", list.getProperty("dimerForce").equals("7.7"));
    assertTrue("16", !list.hasProperty("foo"));
    assertTrue("17", list.size() == 3);

}
public void testGetAtom() {
  assertTrue("Si", Si == molecule.getAtom("Si"));
  assertTrue("C", C == molecule.getAtom("C"));
  assertTrue("F", F == molecule.getAtom("F"));
  assertTrue("null", null == molecule.getAtom("H"));
  assertTrue("null2", null == molecule.getAtom("lkj"));
}
public void testGetAllAtoms() {
  Atom[] a = molecule.getAllAtoms("Si");
  assertTrue("length", a.length == 2);
  assertTrue("Si", (Si == a[0] && Si2 == a[1]) || (Si == a[1] && Si2 == a[0]));
  a = molecule.getAllAtoms("H");
  assertTrue("length = 0", a.length == 0);
  a = molecule.getAllAtoms("lik");
  assertTrue("length = 0 again", a.length == 0);
}
}
