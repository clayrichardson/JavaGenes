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
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.javaGenes.core.Evolvable;

public class ChromosomeIntervalCrossoverTest extends TestCase {
protected AlleleTemplate alleles;

public ChromosomeIntervalCrossoverTest(String name) {super(name);}

public void setUp() {
  int[] sizes = {2,2};
  alleles = new AlleleTemplate(sizes);
  DoubleInterval interval = new DoubleInterval(-1.0,1.0);
  alleles.setAllele(new Allele("zero",interval),0,0);
  alleles.setAllele(new Allele("one",interval),0,1);
  alleles.setAllele(new Allele("two",interval),1,0);
  alleles.setAllele(new Allele("three",interval),1,1);
}
public void testMakeChildren() { // tests only equal values in mom and dad
  Chromosome mom = new Chromosome(alleles);
  Chromosome dad = new Chromosome(alleles);
  for(int i = 0; i < alleles.numberOfArrays(); i++)
  for(int j = 0; j < alleles.getSize(i); j++) {
    double value = 0.1 * i - 0.15;
    mom.setValue(value,i,j);
    dad.setValue(value,i,j);
  }
  ChromosomeIntervalCrossover c = new ChromosomeIntervalCrossover(alleles);
  Evolvable[] parents = {mom,dad};
  Evolvable[] kids = c.makeChildren(parents);
  Chromosome kid = (Chromosome)kids[0];
  for(int i = 0; i < alleles.numberOfArrays(); i++)
  for(int j = 0; j < alleles.getSize(i); j++) {
    double result = kid.getValue(i,j);
    double momValue = mom.getValue(i,j);
    assertTrue( i + "," + j, Utility.nearlyEqual(result,momValue));
  }
}
}