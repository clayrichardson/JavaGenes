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

public class BodiesTest extends TestCase {

public BodiesTest(String name) {super(name);}
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
public void testConstructorAndCutoffRemoval() {
    Atom a = new Atom("Si");
    a.setXyz(1,0,0);
    Atom b = new Atom("Si");
    b.setXyz(0,1,0);
    Atom c = new Atom("Si");
    c.setXyz(0,0,1);
    Atom d = new Atom("Si");
    d.setXyz(7,0,0);
    Atom e = new Atom("Si");
    e.setXyz(-8,0,0);
    
    Atom[] m1 = {a,b,c,d,e};
    setupMolecule("1-",m1,2);
    Atom[] m2 = {e,a,b,d,c};
    setupMolecule("2-",m2,2);
    Atom[] m3 = {a,b,d,c};
    setupMolecule("3-",m3,1);
    Atom[] m4 = {e,a,d};
    setupMolecule("4-",m4,3);
    Atom[] m5 = {b,e,a,d};
    setupMolecule("5-",m5,2);
}
private void setupMolecule(String name,Atom[] atoms,int farAway) {
    Molecule molecule = new Molecule();
    for(int i = 0; i < atoms.length; i++)
        molecule.add(atoms[i]);
    Bodies bodies = new Bodies(molecule);
    bodies.setSpeciesIndices(potential.getSpecies2IndexMap());
    int number = atoms.length;
    check(name,bodies,number,0);
    bodies.removeBodiesAboveCutoff(new Tersoff());
    number = atoms.length - farAway;
    check(name+"short-",bodies,number,farAway);
}
private void check(String name, Bodies bodies, int connectedAtoms, int unconnectedAtoms) {
    OneBody[] oneBodies = bodies.getOneBodyArray();
    assertTrue(name+1, oneBodies.length == connectedAtoms);
    int connectedCount = 0;
    for(int i = 0; i < oneBodies.length; i++) {
        OneBody b = oneBodies[i];
        SecondBody[] seconds = b.getSecondBodies();
        if (seconds.length > 0) {
            connectedCount++;
            assertTrue(name+2, seconds.length == connectedAtoms - 1);
            for(int j = 0; j < seconds.length; j++) {
                ThirdBody[] thirds = seconds[j].getThirdBodies();
                assertTrue(name+3, thirds.length == connectedAtoms - 2);
            }
        }
    }
    assertTrue(name+4, connectedCount == connectedAtoms);
}
}


