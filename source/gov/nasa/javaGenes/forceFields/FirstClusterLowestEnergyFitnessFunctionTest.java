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
//  Created by Al Globus on Wed Oct 30 2002.
package gov.nasa.javaGenes.forceFields;

import junit.framework.TestCase;
import gov.nasa.javaGenes.chemistry.Atom;
import gov.nasa.javaGenes.chemistry.Molecule;

public class FirstClusterLowestEnergyFitnessFunctionTest extends TestCase {

public FirstClusterLowestEnergyFitnessFunctionTest(String name) {super(name);}

public void testEvaluateFitness() {
    double cliff = 10;
    ManyMultiBodiesForOneEnergy clusters = new ManyMultiBodiesForOneEnergy();
    addDimer(clusters,1);
    addDimer(clusters,0.9);
    addDimer(clusters,3);
    StillingerWeberSi potential = new StillingerWeberSi();
    Chromosome chromosome = potential.getChromosome();
    
    FirstClusterLowestEnergyFitnessFunction ff = new FirstClusterLowestEnergyFitnessFunction(potential,clusters,cliff);
    assertTrue("1",ff.evaluateFitness(chromosome).asDouble() == 0);
    addDimer(clusters,1.1);
    assertTrue("2",ff.evaluateFitness(chromosome).asDouble() > 10);
}
private void addDimer(ManyMultiBodiesForOneEnergy clusters,double length) {
    Atom a = new Atom("Si");
    Atom b = new Atom("Si");
    a.setXyz(0,0,0);
    b.setXyz(length,0,0);
    Molecule molecule = new Molecule();
    molecule.add(a);
    molecule.add(b);
    clusters.add(new MultiBodiesForOneEnergy(molecule));
}
}