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

import java.lang.Double;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import java.util.Vector;
import gov.nasa.javaGenes.core.FitnessFunction;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.javaGenes.core.Fitness;
import gov.nasa.javaGenes.core.Population;
import gov.nasa.javaGenes.chemistry.Molecule;
import gov.nasa.alsUtility.RootMeanSquares;
import gov.nasa.javaGenes.core.FitnessDouble;

public class ManyMoleculesEnergyFitness extends FitnessFunction {
protected Potential potential;
protected ManyMultiBodiesForOneEnergy molecules;
protected Vector energies = new Vector();
protected DoubleInterval scaleTo;
protected DoubleInterval originalEnergyInterval;
protected String filename = "";
protected boolean doPerAtomEnergies = false;

public void setFilename(String f) {filename = f;}
public String getFilename() {return filename;}
protected ManyMoleculesEnergyFitness(){} // for testing only

/**
@arg m must have a molecule and that molecule must have an energy or this will fail.
*/
public ManyMoleculesEnergyFitness(Potential p, ManyMultiBodiesForOneEnergy m, boolean inDoPerAtomEnergies) {
  potential = p;
  molecules = m;
  doPerAtomEnergies = inDoPerAtomEnergies;
  for(int i = 0; i < molecules.size(); i++) {
    double energy = molecules.getEnergy(i);
    if (doPerAtomEnergies)
        energy /= molecules.getMolecule(i).getNumberOfAtoms();
    addEnergy(energy);
  }
}
public ManyMoleculesEnergyFitness(Potential p, ManyMultiBodiesForOneEnergy m) {
    this(p,m,false);
}
public void addEnergy(double energy) {energies.addElement(new Double(energy));}
public void setEnergy(int index, double energy) {energies.setElementAt(new Double(energy),index);}
public double getEnergy(int index) {return ((Double)energies.elementAt(index)).doubleValue();}

public MultiBodiesForOneEnergy getMultiBodiesForOneEnergy(int index) {
    return molecules.getMultiBodies(index);
}
public void add(MultiBodiesForOneEnergy m, double energyOrWhatever) {
  molecules.add(m);
  energies.addElement(new Double(energyOrWhatever));
}
/*
find RMS distance in energy. The distance measure may be redefined by subclasses
by overriding calculateDistance(double,double)
*/
public Fitness evaluateFitness (Evolvable evolvable){
    Chromosome chromosome = (Chromosome)evolvable;
    potential.setChromosome(chromosome);
    RootMeanSquares rms = new RootMeanSquares();
    for(int i = 0; i < molecules.size(); i++) {
        MultiBodiesForOneEnergy test = molecules.getMultiBodies(i);
        double energy = calculateEnergy(test);
        if (doPerAtomEnergies)
            energy /= test.getNumberOfAtoms();
        double targetEnergy = getEnergy(i);
        if (scaleTo != null) {
            energy -= originalEnergyInterval.low();
            energy *= scaleTo.interval() / originalEnergyInterval.interval();
            energy += scaleTo.low();
        }
        double distance = calculateDistance(energy,targetEnergy);
        rms.addDatum(distance);
    }
    return new FitnessDouble(rms.rms());
}
protected double calculateEnergy(MultiBodiesForOneEnergy test) {
  return potential.getEnergy(test);
}
protected double calculateDistance(double energy, double targetEnergy) {
  return energy - targetEnergy;
}
public void scaleEnergiesTo(DoubleInterval spread) {
  Error.assertTrue(spread.isPositive());
  Error.assertTrue(scaleTo == null); // only one scaling operation is allowed

  scaleTo = new DoubleInterval(spread);
  originalEnergyInterval = getEnergyInterval();
  addToEnergies(-originalEnergyInterval.low()); // move lowest energy to 0
  multiplyEnergiesBy(scaleTo.interval() / originalEnergyInterval.interval()); // put energies in correct range
  addToEnergies(scaleTo.low()); // position lowest energy properly
}
public void addToEnergies(double value) {
  for (int i = 0; i < energies.size(); i++)
    setEnergy(i,value+getEnergy(i));
}
public void multiplyEnergiesBy(double value) {
  for (int i = 0; i < energies.size(); i++)
    setEnergy(i,value*getEnergy(i));
}
public DoubleInterval getEnergyInterval() {
  Error.assertTrue(energies.size() > 0);
  return new DoubleInterval(energies);
}
public void report (Population population){
}

public String toString() {
    return getClass() + ", Molecules = " + getFilename()  +
            ", Potential = " + potential;
}
}


