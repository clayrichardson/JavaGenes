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
import gov.nasa.javaGenes.chemistry.Constants;

public class StillingerWeberSiFTest extends TestCase {

public StillingerWeberSiFTest(String name) {super(name);}
private Potential potential;
private MultiBody[][] firstOneLower;
private MultiBody[] zeroEnergy;
private MultiBody[][] equal;
public void setUp() {
  StillingerWeberSiF form = new StillingerWeberSiF();
  potential = form;
  potential.setChromosome(form.getChromosome());
  MultiBody[][] f = {
    {new TwoBody("Si","Si",1.6), new TwoBody("Si","Si",1.7)},
    {new TwoBody("F","F",1.8), new TwoBody("F","F",1.9)},
    {new TwoBody("Si","Si",0.4), new TwoBody("Si","Si",0.3)},
    {new TwoBody("F","Si",0.4), new TwoBody("F","Si",0.3)},
    {new ThreeBody("Si","Si","Si",1,1,Constants.TetrahedronAngle),new ThreeBody("Si","Si","Si",1,1,Constants.TetrahedronAngle+0.1)},
    {new ThreeBody("Si","Si","Si",1,1,Constants.TetrahedronAngle),new ThreeBody("Si","Si","Si",1,1,Constants.TetrahedronAngle-0.1)}
  };
  firstOneLower = f;
  MultiBody[][] e = {
    {new TwoBody("Si","F",1.6), new TwoBody("F","Si",1.6)},
    {new TwoBody("Si","F",0.3), new TwoBody("F","Si",0.3)}
  };
  equal = e;
  MultiBody[] z = {
    new TwoBody("Si","Si",2.0),
    new TwoBody("F","Si",4),
    new ThreeBody("Si","Si","Si",1.9,1,Constants.TetrahedronAngle),
    new ThreeBody("Si","Si","Si",1,2,Constants.TetrahedronAngle),
    new ThreeBody("Si","Si","Si",3,2,Constants.TetrahedronAngle)
  };
  zeroEnergy = z;
}
public void testPotential() {
  for(int i = 0; i < firstOneLower.length; i++) {
    double lower = potential.getEnergy(firstOneLower[i][0]);
    double higher = potential.getEnergy(firstOneLower[i][1]);
    assertTrue("normal number: " + i + " lower", Utility.normalNumber(lower));
    assertTrue("normal number: " + i + " higher", Utility.normalNumber(higher));
    assertTrue("not lower: " + i, lower < higher);
  }
  for(int i = 0; i < equal.length; i++) {
    double a = potential.getEnergy(equal[i][0]);
    double b = potential.getEnergy(equal[i][1]);
    assertTrue("normal number: " + i + " a", Utility.normalNumber(a));
    assertTrue("normal number: " + i + " b", Utility.normalNumber(b));
    assertTrue("equal: " + i, a == b);
  }
  for(int i = 0; i < zeroEnergy.length; i++) {
    double a = potential.getEnergy(zeroEnergy[i]);
    assertTrue("normal number: " + i + " a", Utility.normalNumber(a));
    assertTrue("zero: " + i, a == 0);
  }
}
}

