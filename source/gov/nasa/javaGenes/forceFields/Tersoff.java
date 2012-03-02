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

import java.lang.Math;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.DoubleInterval;
import java.util.Vector;
import gov.nasa.javaGenes.chemistry.Element;
import gov.nasa.alsUtility.Distribution;
import gov.nasa.alsUtility.RandomGaussianDistribution;

/**
Implements the Tersoff potential.
Derived from "Modeling solid-state chemistry: Interatomic potentials for multicomponent systems,"
J. Tersoff, Physical Review B, 15 March 1989, volume 39, number 8, pages 556-558.
*/
public class Tersoff extends Potential {

protected AlleleTemplate alleles;
protected Chromosome chromosome;
protected AtomicSpecies[] elements;

protected Species2IndexMap speciesMap = new Species2IndexMap();
public Species2IndexMap getSpecies2IndexMap() {return speciesMap;}

public Tersoff(AtomicSpecies[] elementsIn) {
    elements = elementsIn;
    initialize();
    setChromosome(chromosome);
}

/**
only use for C, Si
*/
public Tersoff() {
    initializeCSi();
    setChromosome(chromosome);
}

public void mustModel(ManyMultiBodiesForOneEnergy bodies) {
    // BUG: check for Si and C only
}
public AlleleTemplate getAlleles() {return alleles;}
public Chromosome getChromosome() {return chromosome;}

private void setElement(AtomicSpecies element, int index) {
  alleles.setName(element.getElementName(),index);
  speciesMap.setMap(element,index);
}

private void setup(int i, int j, String name, double value, DoubleInterval interval) {
  alleles.setAllele(new Allele(name, interval),i,j);
  chromosome.setValue(value, i, j);
}
private void setup(int j, String name, double value1, double value2, DoubleInterval interval) {
    double[] values = {value1,value2};
    for(int i = 0; i < values.length; i++) {
        alleles.setAllele(new Allele(name, interval),i,j);
        chromosome.setValue(values[i], i, j);
    }
}
private void setup(int j, String name, double value, DoubleInterval interval) {
    for(int i = 0; i < elements.length; i++) {
        alleles.setAllele(new Allele(name, interval),i,j);
        chromosome.setValue(value, i, j);
    }
}
/*
used to disable evolution of an allele
*/
private void noEvolution(int j) {
  for(int i = 0; i < chromosome.numberOfArrays(); i++) {
    if (chromosome.getSize(i) <= j)
      continue;
    noEvolution(i,j);
  }
}
private void noEvolution(int species,int parameter) {
    alleles.getAllele(species,parameter).setNoEvolution(chromosome.getValue(species,parameter));
}
/**
must be called before anything else.
*/
public void initialize() {
    int sizes[] = new int[elements.length];
    for(int i = 0; i < sizes.length; i++)
        sizes[i] = i + numberOfOneBodyParameters;
  alleles = new AlleleTemplate(sizes);
  chromosome = new Chromosome(alleles);
  for(int i = 0; i < elements.length; i++)
  	setElement(elements[i],i);

  // parameters for each species.  Sets parameters, by default, to Si
  DoubleInterval interval = new DoubleInterval();
  interval.set(0,1e4);
  setup(0,"attractiveCoefficient",1.8308e3,interval);
  interval.set(0,1e3);
  setup(1,"repulsiveCoefficient",4.7118e2,interval);
  interval.set(0,25);
  setup(2,"repulsiveExponent",2.4799,interval);
  setup(3,"attractivExponent",1.7322,interval);
  setup(4,"beta^n",Math.pow(1.1e-6,7.8734e-1),interval.set(1e-16,1));  // note: beta^n is evolved
  setup(5,"n",7.8734e-1,interval.set(0,5));
  setup(6,"c^2",1.0039e5*1.0039e5,interval.set(0,1e12)); // note: c^2 is evolved, not c
  setup(7,"d^2",1.6217e1*1.6217e1,interval.set(1e-4,1e3)); // note: d^2 is evolved, not d
  setup(8,"h",-5.9825e-1,interval.set(-1,true,1,true));
  for(int i = 0; i < elements.length; i++) {
    double inner1 = elements[i].getCutoff() - elements[i].getToInnerCuttof();
    double inner2outer = elements[i].getToInnerCuttof();
    Error.assertTrue(0 < inner1 && 0 <= inner2outer);
    setup(i,9,"innerCutoff",inner1,interval.set(inner1,inner1));
    noEvolution(i,9);
    setup(i,10,"inner2outerCutoff",inner2outer,interval.set(inner2outer,inner2outer));
    noEvolution(i,10);
  }
  for(int i = 1; i < elements.length; i++)
    for(int j = i; j < elements.length; j++) {
        String name = elements[i].getElementName() + elements[j].getElementName() + "MixingCoefficient";
        setup(j,numberOfOneBodyParameters+i,name,0.9776,interval.set(0.5,1.5));
    }
}
public void initializeCSi() {
  // set up Tersoff parameters from paper. Use order in table 1. We use English names for some symbols.
  /*
  alleles[0] = C
  alleles[1] = Si
  last allele in Si array is the mixing coefficient with C.  Can generalize when additional species added
  */
    elements = new AtomicSpecies[2];
    elements[0] = new AtomicSpecies("C",1.8,2.1);
    elements[1] = new AtomicSpecies("Si",2.7,3.0);
    int sizes[] = {11,12};
  alleles = new AlleleTemplate(sizes);
  chromosome = new Chromosome(alleles);
  for(int i = 0; i < elements.length; i++)
  	setElement(elements[i],i);

  // parameters for each species.
  DoubleInterval interval = new DoubleInterval();
  interval.set(0,1e4);
  setup(0,"attractiveCoefficient",1.3936e3,1.8308e3,interval);
  interval.set(0,1e3);
  setup(1,"repulsiveCoefficient",3.467e2,4.7118e2,interval);
  interval.set(0,25);
  setup(2,"repulsiveExponent",3.4879,2.4799,interval);
  setup(3,"attractivExponent",2.2119,1.7322,interval);
  setup(4,"beta^n",Math.pow(1.5724e-7,7.2751e-1),Math.pow(1.1e-6,7.8734e-1),interval.set(1e-16,1));  // note: beta^n is evolved
  setup(5,"n",7.2751e-1,7.8734e-1,interval.set(0,5));
  setup(6,"c^2",3.8049e4*3.8049e4,1.0039e5*1.0039e5,interval.set(0,1e12)); // note: c^2 is evolved, not c
  setup(7,"d^2",4.384e0*4.384e0,1.6217e1*1.6217e1,interval.set(1e-4,1e3)); // note: d^2 is evolved, not d
  setup(8,"h",-5.7058e-1,-5.9825e-1,interval.set(-2,true,0,true));
  setup(0,9,"innerCutoff",1.8,interval.set(1.8,1.8));
  setup(1,9,"innerCutoff",2.7,interval.set(2.7,2.7));
  setup(0,10,"inner2outerCutoff",0.3,interval.set(0.3,0.3));
  setup(1,10,"inner2outerCutoff",0.3,interval.set(0.3,0.3));
  setup(1,11,"SiCMixingCoefficient",0.9776,interval.set(0.5,1.5));
}
public void evolve_c2d2betaN_Only() {
  noEvolution(0);
  noEvolution(1);
  noEvolution(2);
  noEvolution(3);
  noEvolution(5);
  noEvolution(8);
  noEvolution(9);
  noEvolution(10);
  noEvolution(11);
}
// BUG: only works if Si is 1, C is 0 and no other species
public void evolveSiOnly() {
    for(int i = 0; i < numberOfOneBodyParameters; i++)
        noEvolution(0,i);
}

// numbering for access to one body parameter arrays
private final int attractiveCoefficient = 0;
private final int repulsiveCoefficient = 1;
private final int repulsiveExponent = 2;
private final int attractiveExponent = 3;
private final int betaN = 4;
private final int n = 5;
private final int c2 = 6;
private final int d2 = 7;
private final int h = 8;
private final int innerCutoff = 9;
private final int inner2OuterCutoff = 10;
private final int numberOfOneBodyParameters = 11;

// numbering for access to two body parameter arrays
private final int ijAttractiveCoefficient = 0;
private final int ijRepulsiveCoefficient = 1;
private final int ijRepulsiveExponent = 2;
private final int ijAttractiveExponent = 3;
private final int ijInnerCutoff = 4;
private final int ijOuterCutoff = 5;
private final int ijInner2OuterCutoff = 6;
private final int ijMixingCoefficient = 7;
private final int numberOfTwoBodyParameters = 8;

private double[][] oneBodyParameter;
private double[][][] twoBodyParameter;

// current parameters for species
private int oneBodySpeciesIndex;
private double[] ip;
private double[] jp;
private double[] kp;
private double[] ijp;

/**
put the chromosome parameters in for future getEnergy calls
*/
public void setChromosome(Chromosome chromosome) {setParameters(chromosome);}
private void setParameters(Chromosome chromosome) {
  int numberOfSpecies = chromosome.numberOfArrays();
  Error.assertTrue(numberOfSpecies == speciesMap.size());
  oneBodyParameter = new double[numberOfSpecies][];
  int numberOfOneBodyChromosomes = chromosome.getSize(0);
  Error.assertTrue(numberOfOneBodyChromosomes == numberOfOneBodyParameters);

  for(int i = 0; i < numberOfSpecies; i++) {
    oneBodyParameter[i] = new double[numberOfOneBodyChromosomes];
    for(int j = 0; j < oneBodyParameter[i].length; j++)
      oneBodyParameter[i][j] = chromosome.getValue(i,j);
  }

  twoBodyParameter = new double[numberOfSpecies][numberOfSpecies][];
  for(int i = 0; i < numberOfSpecies; i++) {
    for(int j = i; j < numberOfSpecies; j++) {
      Error.assertTrue(chromosome.getSize(j) == numberOfOneBodyParameters+j);

      // set up two body parameters as a triangular matrix
      twoBodyParameter[i][j] = new double[numberOfTwoBodyParameters];
      twoBodyParameter[j][i] = twoBodyParameter[i][j];

      if (i == j)
        twoBodyParameter[i][j][ijMixingCoefficient] = 1;
      else {
        int index = numberOfOneBodyParameters + i;
        twoBodyParameter[i][j][ijMixingCoefficient] = chromosome.getValue(j,index);
      }
      setupArithmetic(i,j,repulsiveExponent,ijRepulsiveExponent);
      setupArithmetic(i,j,attractiveExponent,ijAttractiveExponent);
      setupGeometric(i,j,repulsiveCoefficient,ijRepulsiveCoefficient);
      setupGeometric(i,j,attractiveCoefficient,ijAttractiveCoefficient);
      setupGeometric(i,j,innerCutoff,ijInnerCutoff);
      double iValue = oneBodyParameter[i][innerCutoff] + oneBodyParameter[i][inner2OuterCutoff];
      double jValue = oneBodyParameter[j][innerCutoff] + oneBodyParameter[j][inner2OuterCutoff];
      twoBodyParameter[i][j][ijOuterCutoff] = Math.sqrt(iValue * jValue);
      twoBodyParameter[i][j][ijInner2OuterCutoff] = twoBodyParameter[i][j][ijOuterCutoff] - twoBodyParameter[i][j][ijInnerCutoff];
    }
  }
}
private void setupArithmetic(int i, int j, int parameter, int ijParameter) {
  double iValue = oneBodyParameter[i][parameter];
  double jValue = oneBodyParameter[j][parameter];
  twoBodyParameter[i][j][ijParameter] = (iValue + jValue) / 2.0;
}
private void setupGeometric(int i, int j, int parameter, int ijParameter) {
  double iValue = oneBodyParameter[i][parameter];
  double jValue = oneBodyParameter[j][parameter];
  twoBodyParameter[i][j][ijParameter] = Math.sqrt(iValue * jValue);
}


/**
Calculate the energies for a set of multi-bodies.
PERFORMANCE: doesn't optimize for repeditive two-body calculations.
That's not in the inner loop so might not matter that much.

@return the energy for each multi-body
*/
/*
public double[] getEnergy(Bodies[] bodies) {
    double[] energy = new double[bodies.length];
  getEnergy(bodies,energy);
  return energy;
}
*/
/**
Calculate the energies for a set of multi-bodies.
PERFORMANCE: doesn't optimize for repeditive two-body calculations.
That's not in the inner loop so might not matter that much.

@param energy the energies for bodies are put in here
*/
/*
public void getEnergy(Bodies[] bodies, double[] energy) {
  Error.assertTrue( bodies.length == energy.length);
  for(int i = 0; i < bodies.length; i++)
    energy[i] = getEnergy(bodies[i]);
}
*/
// the rest of these functions follow the form of the equations in the paper
public double getEnergy(MultiBodiesForOneEnergy bodies) {
    return getEnergy((Bodies)bodies);
}
// the rest of these functions follow the form of the equations in Tersoff's paper
private double getEnergy(Bodies bodies) {
  double energy = 0;
  for(int i = bodies.oneBody.length-1; i >= 0; i--) {
    OneBody body = bodies.oneBody[i];
    for(int j = body.secondBody.length-1; j >= 0 ; j--) {
      oneBodySpeciesIndex = body.speciesIndex;
      ip = oneBodyParameter[oneBodySpeciesIndex];
      SecondBody secondBody = body.secondBody[j];
      jp = oneBodyParameter[secondBody.speciesIndex];
      ijp = twoBodyParameter[body.speciesIndex][secondBody.speciesIndex];
      energy += 0.5 * bondPotential(secondBody);
    }
  }
  return energy;
}
private double bondPotential(SecondBody pair) {
  double cutoff = cutOffFunction(pair.radialDistance,ijp);
  if (cutoff == 0 )
    return 0;
  double energy = cutoff * (repulsiveFunction(pair) + bondOrder(pair) * attractiveFunction(pair));
  return energy;
}
private double cutOffFunction(double distance, double[] p) {
  if (distance < p[ijInnerCutoff])
    return 1;
  if (distance > p[ijOuterCutoff])
    return 0;
  double value = 0.5 + 0.5*Math.cos(Math.PI*(distance-p[ijInnerCutoff])/p[ijInner2OuterCutoff]);
  return value;
}
public double cutOffDistance(int s1, int s2) {
    setChromosome(getChromosome());
    double[] p = twoBodyParameter[s1][s2];
    return p[ijOuterCutoff];
}
private double repulsiveFunction(SecondBody pair) {
	if (!pair.twoBodyTermMatters)
  	return 0;
  double energy = -ijp[ijRepulsiveCoefficient] * Math.exp(-ijp[ijRepulsiveExponent]*pair.radialDistance);
  return energy;
}
private double attractiveFunction(SecondBody pair) {
	if (!pair.twoBodyTermMatters)
  	return 1;
  double energy = ijp[ijAttractiveCoefficient] * Math.exp(-ijp[ijAttractiveExponent]*pair.radialDistance);
  return energy;
}
private double bondOrder(SecondBody pair) {
  double threeBody = Math.pow(threeBodyContribution(pair),ip[n]);
  double onePlus = 1 + (ip[betaN]* threeBody);
  double energy = ijp[ijMixingCoefficient] * Math.pow(onePlus,-0.5*ip[n]);
  return energy;
}
private double threeBodyContribution(SecondBody pair) {
  double energy = 0;
  double cutoff;
  for(int i = pair.thirdBody.length-1; i >= 0 ; i--) {
    ThirdBody thirdBody = pair.thirdBody[i];
    double[] ikp = twoBodyParameter[oneBodySpeciesIndex][thirdBody.speciesIndex];
    cutoff = cutOffFunction(thirdBody.radialDistance,ikp);
    if (cutoff != 0)
      energy += cutoff * angular(thirdBody);
  }
  return energy;
}
private double angular(ThirdBody thirdBody) {
  double squaredTerm = ip[h] - Math.cos(thirdBody.angle);
  double energy = 1 + ip[c2]/ip[d2] - ip[c2]/(ip[d2] + squaredTerm*squaredTerm);
  return energy;
}
}
