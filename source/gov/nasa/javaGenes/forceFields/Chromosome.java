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

import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.javaGenes.core.TokenizeInput;
import gov.nasa.javaGenes.core.TokenizeOutput;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.RootMeanSquares;

/**
Implement the representation of a two-dimensional array of double.
*/
public class Chromosome extends Evolvable {
protected double[][] chromosome;
/**
Sets the array sizes to that of alleles.  Sets the values to random doubles within
the intervals defined by alleles.
*/
public Chromosome(AlleleTemplate alleles) {
	chromosome = new double[alleles.numberOfArrays()][];
  for(int i = 0; i < chromosome.length; i++) {
  	chromosome[i] = new double[alleles.getSize(i)];
  	for(int j = 0; j < chromosome[i].length; j++)
    	if (alleles.hasAllele(i,j)) {
        Allele allele = alleles.getAllele(i,j);
        if (allele.dontEvolve())
          setValue(allele.getNoEvolutionValue(),i,j);
        else {
      	  DoubleInterval interval = allele.getInterval();
      	  if (RandomNumber.getBoolean())
        	  chromosome[i][j] = RandomNumber.getBoolean() ? interval.randomLog() : interval.randomLogFromBothEnds();
          else
  				  chromosome[i][j] = interval.random();
        }
      }
  }
}
public void addArray(int size) {
  double[][] array = new double[chromosome.length+1][];
  for(int i = 0; i < chromosome.length; i++)
    array[i] = chromosome[i];
  array[array.length-1] = new double[size];
  chromosome = array;
}
/**
Creates a raise with the same structure as c, but does not copy the values
*/
public Chromosome(Chromosome c) {
	chromosome = new double[c.numberOfArrays()][];
  for(int i = 0; i < chromosome.length; i++)
  	chromosome[i] = new double[c.getSize(i)];
}
/**
Creates a new chromosome from within a checkpoint file
*/
public Chromosome(TokenizeInput tokenizer) {
	chromosome = new double[tokenizer.getInteger()][];
  for (int i = 0; i < chromosome.length; i++) {
		chromosome[i] = new double[tokenizer.getInteger()];
  	for(int j = 0; j < chromosome[i].length; j++)
  		chromosome[i][j] = tokenizer.getDouble();
	}
}
/**
Save the state of a chromosome to a checkpoint file
*/
public void stateSave(TokenizeOutput tokenizer) {
  tokenizer.putInteger(chromosome.length);
  for (int i = 0; i < chromosome.length; i++) {
  	tokenizer.putInteger(chromosome[i].length);
  	for (int j = 0; j < chromosome[i].length; j++)
    	tokenizer.putDouble(chromosome[i][j]);
  }
}
/**
@return chromosome.length
*/
public int numberOfArrays() {return chromosome.length;}
/**
@return the length of the array indicated by index
*/
public int getSize(int index) {return chromosome[index].length;}
public Chromosome deepCopyChromosome() {
    try {
        return (Chromosome)clone();
    } catch (CloneNotSupportedException e) {
        Error.fatal("can't clone object: " + e);
        return null;
    }
}
/**
deep copy

@exception CloneNotSupportedException
*/
public Object clone() throws CloneNotSupportedException {
	Chromosome newChromosome = (Chromosome)super.clone();
  newChromosome.chromosome = new double[chromosome.length][];
  for(int i = 0; i < chromosome.length; i++) {
  	newChromosome.chromosome[i] = new double[chromosome[i].length];
  	for (int j = 0; j < chromosome[i].length; j++)
  		newChromosome.chromosome[i][j] = chromosome[i][j];
  }
  return newChromosome;
}
/**
return the value at the indicated indices
*/
public double getValue(int i, int j) {return chromosome[i][j];}
public double[] getArray(int i) {return chromosome[i];} // DANGEROUS: do not modify returned array unless you know what you're doing
/**
set the value at the indicated indices
*/
public void setValue(double value, int i, int j) {chromosome[i][j] = value;}
/**
@return the number of doubles in this chromosome
*/
public int getSize(){
	int size = 0;
  for(int i = 0; i < chromosome.length; i++)
  	size += chromosome[i].length;
	return size;
}
/**
@return the root mean squared distance from the argument.
@param e must be a Chromosome with the same array structure as this
*/
public double distanceFrom(Evolvable e) { // Mean squares
	Chromosome other = (Chromosome)e;
  Error.assertTrue(getSize() == other.getSize());
  RootMeanSquares rms = new RootMeanSquares();
  for(int i = 0; i < chromosome.length; i++)
  	for (int j = 0; j < chromosome[i].length; j++) {
  		rms.addDatum(chromosome[i][j] - other.chromosome[i][j]);
  	}
  return rms.rms();
}
/**
@return a new chromosome with values = other - this
*/
public Chromosome createDifferenceChromosome(Chromosome other) {
	Chromosome difference = deepCopyChromosome();
  Error.assertTrue(difference.getSize() == other.getSize());
  for(int i = 0; i < chromosome.length; i++)
  	for (int j = 0; j < chromosome[i].length; j++) {
  		difference.setValue(other.chromosome[i][j] - chromosome[i][j],i,j);
  	}
  return difference;
}
public String toString() {
	StringBuffer s = new StringBuffer();
  for(int i = 0; i < chromosome.length; i++)
  	for (int j = 0; j < chromosome[i].length; j++)
  		s.append(chromosome[i][j] + "\t");
  return s.toString();
}
}
