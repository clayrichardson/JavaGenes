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
package gov.nasa.javaGenes.core;

import gov.nasa.javaGenes.forceFields.ChromosomePopulation;
import gov.nasa.javaGenes.forceFields.AlleleTemplate;
import gov.nasa.javaGenes.forceFields.Chromosome;

public class TeacherPopulation extends ChromosomePopulation {
public TeacherPopulation(int size, AlleleTemplate a, TeacherFitnessFunction tff) {
  super(size,a,tff);
}
/*
  super(size);
  alleles = a;
  Error.assertTrue(alleles.numberOfArrays() == 1);
  for (int i = 0; i < population.length; i++)
    population[i] = new ChromosomeIndividual(new Chromosome(alleles),tff);
}
*/
/**
do not modify return value
@return teacher levels for student testing
*/
public double[] getArray(int i) {
  Chromosome c = (Chromosome)getEvolvable(i);
  return c.getArray(0);
}
}