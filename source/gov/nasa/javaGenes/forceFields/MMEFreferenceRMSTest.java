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

import junit.framework.TestCase;
import gov.nasa.alsUtility.Utility;
import gov.nasa.javaGenes.chemistry.Molecule;
import gov.nasa.javaGenes.chemistry.Atom;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.javaGenes.chemistry.xyzFormat;

public class MMEFreferenceRMSTest extends TestCase {
private ManyMultiBodiesForOneEnergy m;
private String moleculeFile = "TEMP1";
private java.io.PrintWriter file;
final private double targetReferenceEnergy = 2;
final private int numberOfAtoms = 2;

public MMEFreferenceRMSTest(String name) {super(name);}

protected void setUp() {
    Utility.makeFile(moleculeFile,"");
    file = Utility.outputFile(moleculeFile);
    file.println(numberOfAtoms +"");
    file.println("energy = " + targetReferenceEnergy);
    file.println("Si 0.5 1 1");
    file.println("Si 1.5 1 1");
    file.close();
    AtomicSpecies[] as = {new AtomicSpecies("Si")};
    DoubleInterval d = new DoubleInterval(1,2);
    m = new ManyMultiBodiesForOneEnergy(new StillingerWeber(as,d,d,d),moleculeFile);
}
protected void tearDown() {
    java.io.File file = new java.io.File(moleculeFile);
    file.delete();
}

public void testCalculateDistance() {
    final double referenceEnergy = -1;
    final double referenceLower = -2;
    final double referenceUpper = 1;
    final double divideBy = 4;
    MMEFreferenceRMS f = new MMEFreferenceRMS(new StillingerWeber(), m, divideBy, referenceEnergy, referenceLower, referenceUpper,false);
    double delta;
    delta = f.calculateDistance(referenceEnergy,targetReferenceEnergy);
    assertTrue("1",Utility.nearlyEqual(delta,0));
    delta = f.calculateDistance(referenceEnergy+1,targetReferenceEnergy+1);
    assertTrue("1.5",Utility.nearlyEqual(delta,0));
    delta = f.calculateDistance(referenceEnergy+1,targetReferenceEnergy);
    assertTrue("2",Utility.nearlyEqual(delta,1/divideBy));
    delta = f.calculateDistance(referenceEnergy+6,targetReferenceEnergy+1);
    assertTrue("3",Utility.nearlyEqual(delta,5));
    delta = f.calculateDistance(referenceEnergy-6,targetReferenceEnergy-1);
    assertTrue("4",Utility.nearlyEqual(delta,-5));
    delta = f.calculateDistance(referenceEnergy-6,targetReferenceEnergy+1);
    assertTrue("5",Utility.nearlyEqual(delta,-7));
    
    f = new MMEFreferenceRMS(new StillingerWeber(), m, divideBy, referenceEnergy);
    delta = f.calculateDistance(referenceEnergy,targetReferenceEnergy);
    assertTrue("1.1",Utility.nearlyEqual(delta,0));
    delta = f.calculateDistance(referenceEnergy+1,targetReferenceEnergy+1);
    assertTrue("1.5.1",Utility.nearlyEqual(delta,0));
    delta = f.calculateDistance(referenceEnergy+1,targetReferenceEnergy);
    assertTrue("2.1",Utility.nearlyEqual(delta,1));
    delta = f.calculateDistance(referenceEnergy+6,targetReferenceEnergy+1);
    assertTrue("3.1",Utility.nearlyEqual(delta,5));
    delta = f.calculateDistance(referenceEnergy-6,targetReferenceEnergy-1);
    assertTrue("4.1",Utility.nearlyEqual(delta,-5));
    delta = f.calculateDistance(referenceEnergy-6,targetReferenceEnergy+1);
    assertTrue("5.1",Utility.nearlyEqual(delta,-7));

    f = new MMEFreferenceRMS(new StillingerWeber(), m, divideBy, referenceEnergy, referenceLower, referenceUpper,true);
    delta = f.calculateDistance(referenceEnergy/numberOfAtoms,targetReferenceEnergy/numberOfAtoms);
    assertTrue("8.1",Utility.nearlyEqual(delta,0));
    delta = f.calculateDistance((referenceEnergy+1)/numberOfAtoms,(targetReferenceEnergy+1)/numberOfAtoms);
    assertTrue("8.1.5",Utility.nearlyEqual(delta,0));
    delta = f.calculateDistance((referenceEnergy+1.2)/numberOfAtoms,(targetReferenceEnergy)/numberOfAtoms);
    assertTrue("8.2",Utility.nearlyEqual(delta,1.2/(numberOfAtoms*divideBy)));
    delta = f.calculateDistance((referenceEnergy+0.8)/numberOfAtoms,(targetReferenceEnergy)/numberOfAtoms);
    assertTrue("8.2.5",Utility.nearlyEqual(delta,0.8/(divideBy*numberOfAtoms)));
    delta = f.calculateDistance((referenceEnergy-0.2)/numberOfAtoms,(targetReferenceEnergy)/numberOfAtoms);
    assertTrue("8.2.6",Utility.nearlyEqual(delta,-0.2/(divideBy*numberOfAtoms)));
    delta = f.calculateDistance((referenceEnergy+6)/numberOfAtoms,(targetReferenceEnergy+1)/numberOfAtoms);
    assertTrue("8.3",Utility.nearlyEqual(delta,5.0/numberOfAtoms));
    delta = f.calculateDistance((referenceEnergy-6)/numberOfAtoms,(targetReferenceEnergy-1)/numberOfAtoms);
    assertTrue("8.4",Utility.nearlyEqual(delta,-5.0/numberOfAtoms));
    delta = f.calculateDistance((referenceEnergy-6)/numberOfAtoms,(targetReferenceEnergy+1)/numberOfAtoms);
    assertTrue("8.5",Utility.nearlyEqual(delta,-7.0/numberOfAtoms));
}
}

