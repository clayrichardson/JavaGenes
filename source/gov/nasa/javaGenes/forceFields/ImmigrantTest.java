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

public class ImmigrantTest extends TestCase {
protected Immigrant immigrant;
protected AlleleTemplate alleles;

public ImmigrantTest(String name) {super(name);}

public void setUp() {
  String filename = "ImmigrantTest.temporary";
  PrintWriter file = Utility.outputFile(filename);
  file.println("zero\t0");
  file.println("one\t1");
  file.println("two\t2");
  file.println("three\t3");
  file.println("end");
  file.close();

  immigrant = new Immigrant(filename);
  Utility.removeFile(filename);

  int[] sizes = {2,3};
  alleles = new AlleleTemplate(sizes);
  DoubleInterval interval = new DoubleInterval(-1.0,-1.0);
  alleles.setAllele(new Allele("zero",interval),0,0);
  alleles.setAllele(new Allele("one",interval),0,1);
  alleles.setAllele(new Allele("two",interval),1,0);
  alleles.setAllele(new Allele("three",interval),1,1);
  alleles.setAllele(new Allele("four",interval),1,2);
}

public void testSetupChromosome() {
  Chromosome c = new Chromosome(alleles);
  immigrant.setupChromosome(c,alleles);
  checkValue(c,0.0,0,0);
  checkValue(c,1.0,0,1);
  checkValue(c,2.0,1,0);
  checkValue(c,3.0,1,1);
  checkValue(c,-1.0,1,2);
}
private void checkValue(Chromosome c, double value, int i, int j) {
  assertTrue(c.getValue(i,j) == value);
}
}

