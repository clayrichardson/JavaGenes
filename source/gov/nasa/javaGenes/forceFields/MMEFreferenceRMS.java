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
//  Created by Al Globus on Wed Mar 26 2003.
package gov.nasa.javaGenes.forceFields;

import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.alsUtility.Error;

/**
the first cluster in the ManyMultiBodiesForOneEnergy is used to calulate differences from
*/
public class MMEFreferenceRMS extends ManyMoleculesEnergyFitness {
protected double referenceEnergy; // 'correct' energy for first cluster
protected DoubleInterval referenceEnergyUncertainty; // upper and lower bound for 'correct' energy
protected double energyRangeDivideBy; // when within referenceEnergyUncertainty improve fitness by division
protected double targetReferenceEnergy;  // target energy of first cluster

public MMEFreferenceRMS(Potential p, ManyMultiBodiesForOneEnergy m, double inEnergyRangeDivideBy, double inReferenceEnergy, double referenceEnergyLowerBound, double referenceEnergyUpperBound, boolean inDoPerAtomEnergies) {
    super(p,m,inDoPerAtomEnergies);
    Error.assertTrue(energies.size() > 0);
    targetReferenceEnergy = ((Double)energies.elementAt(0)).doubleValue();
    
    int numberOfAtoms = m.getMultiBodies(0).getNumberOfAtoms();
    referenceEnergy = inReferenceEnergy;
    Error.assertTrue(referenceEnergyLowerBound <= referenceEnergy && referenceEnergy <= referenceEnergyUpperBound);
    if (inDoPerAtomEnergies) referenceEnergy /= numberOfAtoms;

    double low = referenceEnergyLowerBound;
    if (inDoPerAtomEnergies) low /= numberOfAtoms;
    double high = referenceEnergyUpperBound;
    if (inDoPerAtomEnergies) high /= numberOfAtoms;
    referenceEnergyUncertainty = new DoubleInterval(low-referenceEnergy,high-referenceEnergy);

    energyRangeDivideBy = inEnergyRangeDivideBy;
    Error.assertTrue(energyRangeDivideBy >= 1);
}
public MMEFreferenceRMS(Potential p, ManyMultiBodiesForOneEnergy m, double inEnergyRangeDivideBy, double referenceEnergy) {
    this(p,m,inEnergyRangeDivideBy,referenceEnergy,referenceEnergy,referenceEnergy,false);
}
public MMEFreferenceRMS(Potential p, ManyMultiBodiesForOneEnergy m, boolean inDoPerAtomEnergies) {
    this(p,m,1,m.getEnergy(0),m.getEnergy(0),m.getEnergy(0),inDoPerAtomEnergies);
}
protected double calculateDistance(double energyToCheck, double targetEnergy) {
    double target = targetEnergy - targetReferenceEnergy;
    double energy = energyToCheck - referenceEnergy;
    double delta = calculateDelta(energy,target);
    if (referenceEnergyUncertainty.isBetween(delta))
        delta /= energyRangeDivideBy;
    return delta;
}
protected double calculateDelta(double energy,double target) {return energy - target;}
}
