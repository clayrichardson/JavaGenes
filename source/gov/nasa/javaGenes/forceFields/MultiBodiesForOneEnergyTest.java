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
import gov.nasa.javaGenes.chemistry.Molecule;
import gov.nasa.javaGenes.chemistry.xyzFormat;
import gov.nasa.javaGenes.chemistry.Atom;
import gov.nasa.alsUtility.DoubleInterval;

public class MultiBodiesForOneEnergyTest extends TestCase {
private Molecule molecule;
  private String unitCellFile = "TEMP1";
  private java.io.PrintWriter file;

public MultiBodiesForOneEnergyTest(String name) {super(name);}

protected void setUp() {
    Utility.makeFile(unitCellFile,"");
    file = Utility.outputFile(unitCellFile);
    file.println("3");
    file.println("unitCell = 0,10,0,10,0,10");
    file.println("Si 0.5 1 1");
    file.println("Si 1.5 1 1");
    file.println("Si 9.5 1 1");
    file.close();
    setUpMolecule();
}
protected void tearDown() {
    java.io.File file = new java.io.File(unitCellFile);
    file.delete();
}

public void testUnitCell() {
    Molecule molecule = xyzFormat.read(unitCellFile);
    molecule.setPropertiesFromComment();
    MultiBodiesForOneEnergy m = new MultiBodiesForOneEnergy(molecule);
    int ones = 0;
    int twos = 0;
    for(int i = 0; i < m.size(); i++) {
        MultiBody b = m.get(i);
        if (b instanceof TwoBody) {
            TwoBody t = (TwoBody)b;
            if (Utility.nearlyEqual(t.getR(),1))
                ones++;
            else if (Utility.nearlyEqual(t.getR(),2))
                twos++;
            else
                assertTrue("twobody bad value " + i, false);
        } else if (b instanceof ThreeBody) {
            ThreeBody tt = (ThreeBody)b;
            if (Utility.nearlyEqual(tt.getRJI(),1))
                assertTrue("JI 1 " + i, Utility.nearlyEqual(tt.getRJK(),1) || Utility.nearlyEqual(tt.getRJK(),2));
            else if (Utility.nearlyEqual(tt.getRJI(),2))
                assertTrue("JI 2 " + i, Utility.nearlyEqual(tt.getRJK(),1));
            else
                assertTrue("threebody bad value " + i, false);
        }
    }
    assertTrue("ones", ones == 2);
    assertTrue("twos", twos == 1);
}    

public void setUpMolecule() {
  Atom a = new Atom("Si");
  Atom b = new Atom("Si");
  Atom c = new Atom("F");
  a.setXyz(0,0,0);
  b.setXyz(1,0,0);
  c.setXyz(0,-4,0);
  molecule = new Molecule();
  molecule.add(a);
  molecule.add(b);
  molecule.add(c);
}
public void testConstructor() {
  MultiBodiesForOneEnergy m = new MultiBodiesForOneEnergy(molecule);
  assertTrue("size", m.size() == 6);
  int a[] = countBodies(m);
  assertTrue(a[0] == 3);
  assertTrue(a[1] == 3);
}
private int[] countBodies(MultiBodiesForOneEnergy m) {
  double lastTwoBodyR = 0;
  double lastAngle = 0;
  int twoBodyCount = 0;
  int threeBodyCount = 0;
  for(int i = 0; i < m.size(); i++) {
    if (m.get(i) instanceof TwoBody) {
      twoBodyCount++;
      TwoBody t = (TwoBody)m.get(i);
      assertTrue(t.getR() != lastTwoBodyR);
      lastTwoBodyR = t.getR();
    } else if (m.get(i) instanceof ThreeBody) {
      threeBodyCount++;
      ThreeBody t = (ThreeBody)m.get(i);
      assertTrue(t.getAngle() != lastAngle);
      lastAngle = t.getAngle();
    }
  }
  int[] r = new int[2];
  r[0] = twoBodyCount;
  r[1] = threeBodyCount;
  return r;
}
public void testRemoveBodiesAboveCutoff() {
  DoubleInterval interval = new DoubleInterval();
  Potential form = new StillingerWeberSiF(interval,interval,interval);
  MultiBodiesForOneEnergy m = new MultiBodiesForOneEnergy(molecule);
  m.removeBodiesAboveCutoff(form);
  int a[] = countBodies(m);
  assertTrue(a[0] == 1);
  assertTrue(a[1] == 0);
}
}