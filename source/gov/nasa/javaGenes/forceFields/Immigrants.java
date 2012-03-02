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
import gov.nasa.alsUtility.FieldRecordText;
import java.io.Serializable;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import java.util.Vector;
import gov.nasa.javaGenes.core.FitnessFunction;

public class Immigrants implements Serializable {
public static final String INFINIT_TOKEN = "infinit";
protected Vector immigrants = new Vector();

public Immigrants(String filename) {
  this(new FieldRecordText(filename,"\t"));
}
public Immigrants(FieldRecordText file) {
  String[] fields;
  while((fields = file.readLine()) != null) {
    Error.assertTrue(fields.length == 1);
    Immigrant i = new Immigrant(file);
    immigrants.addElement(i);
    if (fields[0].equals(INFINIT_TOKEN))
      i.setNumberInfinit(true);
    else
      i.setNumber(Utility.string2integer(fields[0]));
  }
}
public Immigrant getImmigrant(int index) {
  return (Immigrant)immigrants.elementAt(index);
}
public void immigrate(ChromosomePopulation population,AlleleTemplate alleles,FitnessFunction fitness) {
  final int infinit = population.getSize() + 1;
  if (immigrants.size() <= 0) return;
  int[] number = new int[immigrants.size()];
  int totalImmigrants = 0;
  for(int i = 0; i < number.length; i++) {
    number[i] = getImmigrant(i).isInfinit() ? infinit : getImmigrant(i).getNumber();
    totalImmigrants += number[i];
  }
  if (totalImmigrants <= 0) return;

  int immigrantIndex = 0;
  int populationIndex = 0;
  while(populationIndex < population.getSize() && totalImmigrants > 0) {
    if (immigrantIndex >= number.length) immigrantIndex = 0;
    ChromosomeIndividual chromosomeIndividual = (ChromosomeIndividual)population.getIndividual(populationIndex);
    if (number[immigrantIndex] > 0) {
      getImmigrant(immigrantIndex).setupChromosome((Chromosome)chromosomeIndividual.getEvolvable(),alleles);
      chromosomeIndividual.evaluateFitness(fitness);
      number[immigrantIndex]--;
      totalImmigrants--;
      populationIndex++;
    }
    immigrantIndex++;
  }
}
}
