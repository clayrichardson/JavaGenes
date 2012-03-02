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
import gov.nasa.javaGenes.core.FitnessFunction;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.javaGenes.core.Fitness;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.javaGenes.core.FitnessDouble;

public class ImmigrantsTest extends TestCase {
protected Immigrants immigrants;
protected AlleleTemplate alleles;

public ImmigrantsTest(String name) {super(name);}

public void setUp() {
  String filename = "ImmigrantsTest.temporary";
  PrintWriter file = Utility.outputFile(filename);
  file.println(""+2);
  file.println("zero\t0");
  file.println("one\t1");
  file.println("two\t2");
  file.println("three\t3");
  file.println("end");
  file.println(Immigrants.INFINIT_TOKEN);
  file.println("four\t4");
  file.println("five\t5");
  file.println("end");
  file.close();

  immigrants = new Immigrants(filename);
  Utility.removeFile(filename);

  int[] sizes = {2,4};
  alleles = new AlleleTemplate(sizes);
  DoubleInterval interval = new DoubleInterval(-1.0,-1.0);
  alleles.setAllele(new Allele("zero",interval),0,0);
  alleles.setAllele(new Allele("one",interval),0,1);
  alleles.setAllele(new Allele("two",interval),1,0);
  alleles.setAllele(new Allele("three",interval),1,1);
  alleles.setAllele(new Allele("four",interval),1,2);
  alleles.setAllele(new Allele("five",interval),1,3);
}
public void testGetImmigrant() {
  Immigrant i = immigrants.getImmigrant(0);
  assertTrue("check infinity", !i.isInfinit());
  assertTrue("check number", i.getNumber() == 2);
  assertTrue("check value", i.getValue(1) == 1);
  i = immigrants.getImmigrant(1);
  assertTrue("check infinity", i.isInfinit());
  assertTrue("check value", i.getValue(0) == 4);
}
private class f extends  FitnessFunction {
  public Fitness evaluateFitness (Evolvable evolvable){return new FitnessDouble(0);}
}
public void testImmigrate() {
  final int size = 10;
  FitnessFunction fitness = new f();
  ChromosomePopulation population = new ChromosomePopulation(10,alleles,fitness);
  immigrants.immigrate(population,alleles,fitness);
  for(int i = 0; i < population.getSize(); i++) {
    Chromosome c = (Chromosome)population.getIndividual(i).getEvolvable();
    if (i == 0 || i == 2)
      checkForZero(c);
    else
      checkForOne(c);
  }

  population = new ChromosomePopulation(10,alleles,fitness);
  immigrants.getImmigrant(1).setNumberInfinit(false);
  immigrants.getImmigrant(1).setNumber(1);
  immigrants.immigrate(population,alleles,fitness);
  for(int i = 0; i < population.getSize(); i++) {
    Chromosome c = (Chromosome)population.getIndividual(i).getEvolvable();
    if (i == 0 || i == 2)
      checkForZero(c);
    else if (i == 1)
      checkForOne(c);
    else
      checkForNothing(c);
  }
}
private void checkForZero(Chromosome c) {
  checkValue(c,0.0,0,0);
  checkValue(c,1.0,0,1);
  checkValue(c,2.0,1,0);
  checkValue(c,3.0,1,1);
  checkValue(c,-1.0,1,2);
  checkValue(c,-1.0,1,3);
}
private void checkForOne(Chromosome c) {
  checkValue(c,-1.0,0,0);
  checkValue(c,-1.0,0,1);
  checkValue(c,-1.0,1,0);
  checkValue(c,-1.0,1,1);
  checkValue(c,4.0,1,2);
  checkValue(c,5.0,1,3);
}
private void checkForNothing(Chromosome c) {
  checkValue(c,-1.0,0,0);
  checkValue(c,-1.0,0,1);
  checkValue(c,-1.0,1,0);
  checkValue(c,-1.0,1,1);
  checkValue(c,-1.0,1,2);
  checkValue(c,-1.0,1,3);
}
private void checkValue(Chromosome c, double value, int i, int j) {
  assertTrue(c.getValue(i,j) == value);
}
}

