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
import java.io.PrintWriter;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.javaGenes.core.Individual;

public class ForceFitnessRMSTest extends TestCase {
private Potential potential;
private Chromosome chromosome;
private double distance = 9.8;
private ForceFitnessRMS fitness;
private StillingerWeberSiF form;

public ForceFitnessRMSTest(String name) {super(name);}

public void setUp() {
  DoubleInterval unused = new DoubleInterval(-100,100);
  form = new StillingerWeberSiF(unused,unused,unused);
  potential = form;
  chromosome = form.getChromosome();
  potential.setChromosome(chromosome);

  String filename = "ForceFitnessRMSTest.temporary";
  PrintWriter file = Utility.outputFile(filename);
  file.println("Si-SiDistance\tforce");
  for(double d = 0.8; d < 4; d += 0.3) {
    TwoBody twoBody = new TwoBody("Si","Si",d*StillingerWeberSiF.getLengthScale());
    double force = potential.getForce(twoBody);
    file.println(d + "\t" + (force+distance));
  }
  file.close();
  fitness = new ForceFitnessRMS(potential,form.getLengthScale(),filename);
  Utility.removeFile(filename);
}

public void testfitness() {
  Individual individual = new Individual(chromosome,fitness);
  double f = individual.getFitness().asDouble();
  assertTrue(Utility.nearlyEqual(f,distance));
}
}
