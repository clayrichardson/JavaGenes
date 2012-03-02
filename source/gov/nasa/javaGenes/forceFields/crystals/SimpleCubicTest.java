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

import junit.framework.TestCase;
import gov.nasa.alsUtility.Utility;
import gov.nasa.javaGenes.chemistry.Molecule;
import gov.nasa.javaGenes.chemistry.Atom;
import gov.nasa.javaGenes.forceFields.*;
import java.lang.Math;

public class SimpleCubicTest extends TestCase {

public SimpleCubicTest(String name) {super(name);}

public void testCrossAndCopy() {
    SimpleCubic sc = new SimpleCubic("Si", 1.001);
    crossTest(sc, "original");
    crossTest((SimpleCubic)sc.copy(), "copy");
}
public void crossTest(SimpleCubic sc, String name) {
    assertTrue(name + "size", sc.size() == 3);
    assertTrue(name + "numberOfAtoms", sc.getNumberOfAtoms() == 1);
    for(int i = 0; i < sc.size(); i++) {
        MultiBody mb = sc.get(i);
        if (mb instanceof TwoBody) {
            TwoBody t = (TwoBody)mb;
            assertTrue(name + "2-R-" + i,Utility.nearlyEqual(1,t.getR()));
            assertTrue(name + "2-H-" + i,Utility.nearlyEqual(3,t.getHowMany()));
        } else {
            ThreeBody t = (ThreeBody)mb;
            assertTrue(name + "3-RJI-" + i,Utility.nearlyEqual(1,t.getRJI()));
            assertTrue(name + "3-RJK-" + i,Utility.nearlyEqual(1,t.getRJK()));
            if (Utility.nearlyEqual(Math.PI/2,t.getAngle()))
                assertTrue(name + "3-90-H-" + i,Utility.nearlyEqual(12,t.getHowMany()));
            else if (Utility.nearlyEqual(Math.PI,t.getAngle()))
                assertTrue(name + "3-180-H-" + i,Utility.nearlyEqual(3,t.getHowMany()));
            else
                assertTrue(name + "bad angle", false);
        }
    }
}
public void testTwoUnits() {
    SimpleCubic sc = new SimpleCubic("Si", 2);
    assertTrue("size", sc.size() == 47);
    for(int i = 0; i < sc.size(); i++)
        if (i < 4)
            assertTrue("twobody-" + i, sc.get(i) instanceof TwoBody);
        else
            assertTrue("threeobody-" + i, sc.get(i) instanceof ThreeBody);
    for(int i = 0; i < sc.size(); i++) {
        MultiBody mb = sc.get(i);
        if (mb instanceof TwoBody) {
            TwoBody t = (TwoBody)mb;
            if (Utility.nearlyEqual(1,t.getR()))
                assertTrue("2-1-H-" + i,Utility.nearlyEqual(3,t.getHowMany()));
            else if (Utility.nearlyEqual(2,t.getR()))
                assertTrue("2-2-H-" + i,Utility.nearlyEqual(3,t.getHowMany()));
            else if (Utility.nearlyEqual(Math.sqrt(2),t.getR()))
                assertTrue("2-sqrt2-H-" + i,Utility.nearlyEqual(6,t.getHowMany()));
        }
    }
}
}


