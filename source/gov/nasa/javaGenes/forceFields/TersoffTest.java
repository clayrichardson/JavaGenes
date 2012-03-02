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
//  Created by Al Globus on Thu Feb 20 2003.
package gov.nasa.javaGenes.forceFields;

import junit.framework.TestCase;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.javaGenes.chemistry.Molecule;
import gov.nasa.javaGenes.chemistry.Atom;
import gov.nasa.alsUtility.RandomNumber;

public class TersoffTest extends TestCase {

public TersoffTest(String name) {super(name);}
private Tersoff potential;
private Bodies bodies;

public void setUp() {
    potential = new Tersoff();

    final double boxSize = 2.0;
    Molecule molecule = new Molecule();
    for(int i = 0; i < 3; i++) {
        Atom a = new Atom("Si");
        a.setXyz(RandomNumber.getDouble()*boxSize,RandomNumber.getDouble()*boxSize,RandomNumber.getDouble()*boxSize);
        molecule.add(a);
    }
    bodies = new Bodies(molecule);
    bodies.setSpeciesIndices(potential.getSpecies2IndexMap());
}
public void testEnergiesDifferent() {
    RandomNumber.setSeed(990639400906L);
    for(int i = 0; i < 10; i++) {
        Chromosome c1 = new Chromosome(potential.getAlleles());
        Chromosome c2 = new Chromosome(potential.getAlleles());
        potential.setChromosome(c1);
        double e1 = potential.getEnergy(bodies);
        potential.setChromosome(c2);
        double e2 = potential.getEnergy(bodies);
        assertTrue("1-"+i,!Utility.nearlyEqual(e1,e2));
    }
}
}

