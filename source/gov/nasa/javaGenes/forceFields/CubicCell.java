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
package gov.nasa.javaGenes.forceFields.crystals;

import gov.nasa.javaGenes.forceFields.*;
import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.chemistry.Atom;
import gov.nasa.javaGenes.chemistry.Molecule;
import gov.nasa.javaGenes.graph.VertexIterator;

public class CubicCell extends MultiBodiesForOneEnergy {
/**
used to avoid eliminating atoms that are 'too far away' by
a very small amount due to floating point inaccuracies.  Results
in very small performance penalty every once in a great while
*/
protected static final double distanceSafetyFactor = 1.001;
protected double[][] uniqueAtomLocations; // slight misnomer, the atoms are unique in their location in the cubic cell only
protected Atom[] uniqueAtoms;

/**
copy the graph and make copies of all edges and vertices.  Does not make copies of uniqueAtomLocations and/or uniqueAtoms since these are not normally modified after the crystal has been created.
*/
public CubicCell copy() {
    try {
        return (CubicCell)clone();
    } catch (CloneNotSupportedException e) {
        Error.fatal("can't clone object: " + e);
        return null;
    }
}

public CubicCell(String element, double maxDistanceFromUniqueAtom,double[][] inUniqueAtomLocations) {
    Error.assertTrue(maxDistanceFromUniqueAtom > 0);    
    Error.assertTrue(element != null);
    Error.assertTrue(inUniqueAtomLocations != null);
    Error.assertTrue(inUniqueAtomLocations.length > 0);
    
    uniqueAtomLocations = inUniqueAtomLocations;
    uniqueAtoms = new Atom[uniqueAtomLocations.length];
    for(int i = 0; i < uniqueAtomLocations.length; i++) {
        uniqueAtoms[i] = new Atom(element);
        uniqueAtoms[i].setXyz(uniqueAtomLocations[i]);
    }

    addNonUniqueAtomsToMolecule(element,maxDistanceFromUniqueAtom);

    double maxR = maxDistanceFromUniqueAtom * distanceSafetyFactor;
    addTwoBodies(maxR); // must be before addThreeBodies(uniqueAtom)
    addThreeBodies(maxR);

    for(int i = 0; i < uniqueAtoms.length; i++)
        molecule.add(uniqueAtoms[i]);
}
protected void addNonUniqueAtomsToMolecule(String element, double maxDistanceFromUniqueAtom){
    molecule = new Molecule();
    int maxIndex = (int)Math.ceil(maxDistanceFromUniqueAtom);
    int minIndex = -maxIndex;
    for(int i = minIndex; i <= maxIndex; i++)
    for(int j = minIndex; j <= maxIndex; j++)
    for(int k = minIndex; k <= maxIndex; k++) {
        if (i == 0 && j == 0 && k == 0)
            continue; // don't add the unique atom.  
        for(int a = 0; a < uniqueAtoms.length; a++) {
            Atom atom = new Atom(element);
            atom.setXyz(i+uniqueAtomLocations[a][0],j+uniqueAtomLocations[a][1],k+uniqueAtomLocations[a][2]);
            for(int b = 0; b < uniqueAtoms.length; b++) {
                if (atom.getDistanceTo(uniqueAtoms[b]) <= maxDistanceFromUniqueAtom * distanceSafetyFactor) {
                    molecule.add(atom);
                    break;
                }
            }
        }
    }
}
protected void addTwoBodies(double maxR) {
    for(int i = 0; i < uniqueAtoms.length; i++) {
        for(int j = 0; j < uniqueAtomLocations.length; j++) {
            if (j == i)
                continue;
            molecule.add(uniqueAtoms[j]);
        }
        addOneAtomsTwoBodies(uniqueAtoms[i],maxR);
        molecule.removeLastNAtoms(uniqueAtoms.length - 1);
    }
    for(int i = 0; i < multiBodies.size(); i++) {
        TwoBody twoBody = (TwoBody)multiBodies.get(i);
        twoBody.divideHowManyBy(2); // each unique atom is involved in the same TwoBody twice
    }
} 
protected void addThreeBodies(double maxR) {
    int startIndexForThreeBodies = multiBodies.size();    
    for(int i = 0; i < uniqueAtoms.length; i++) {
        for(int j = 0; j < uniqueAtomLocations.length; j++) {
            if (j == i)
                continue;
            molecule.add(uniqueAtoms[j]);
        }
        addOneAtomsThreeBodies(uniqueAtoms[i],maxR,startIndexForThreeBodies);
        molecule.removeLastNAtoms(uniqueAtoms.length - 1);
    }
} 
// must be before addThreeBodies(Atom) (for search optimization over this.multiBodies)
protected void addOneAtomsTwoBodies(Atom uniqueAtom, double maxR) {
    Error.assertTrue(molecule != null);
    Error.assertTrue(uniqueAtom != null);
    for(int a = 0; a  < molecule.getNumberOfAtoms(); a++){
        Atom atom = molecule.getAtom(a);
        if (atom.getDistanceTo(uniqueAtom) > maxR)
            continue;
        TwoBody newTwoBody = new TwoBody(atom,uniqueAtom);
        boolean needNewTwoBody = true;
        for(int i = 0; i < multiBodies.size(); i++) {
            TwoBody twoBody = (TwoBody)multiBodies.get(i);
            if (newTwoBody.nearlyEqual(twoBody)) {
                twoBody.incrementHowMany();
                needNewTwoBody = false;
                break;
            }
        }
        if (needNewTwoBody)
            add(newTwoBody);
    }
}
protected void addOneAtomsThreeBodies(Atom uniqueAtom, double maxR, int startIndexForThreeBodies) {
    Error.assertTrue(molecule != null);
    Error.assertTrue(uniqueAtom != null);
    
    for(int a = 0; a  < molecule.getNumberOfAtoms(); a++){
        Atom atom1 = molecule.getAtom(a);
        if (atom1.getDistanceTo(uniqueAtom) > maxR)
            continue;
        for(int b = a+1; b  < molecule.getNumberOfAtoms(); b++){
            Atom atom2 = molecule.getAtom(b);
            if (atom2.getDistanceTo(uniqueAtom) > maxR)
                continue;
            ThreeBody newThreeBody = new ThreeBody(atom1,uniqueAtom,atom2);
            boolean needNewThreeBody = true;
            for(int i = startIndexForThreeBodies; i < multiBodies.size(); i++) {
                ThreeBody threeBody = (ThreeBody)multiBodies.get(i);
                if (newThreeBody.nearlyEqual(threeBody)) {
                    threeBody.incrementHowMany();
                    needNewThreeBody = false;
                    break;
                }
            }
            if (needNewThreeBody)
                add(newThreeBody);
        }
    }
}
public int getNumberOfAtoms() {
    return uniqueAtomLocations.length;
}
}

