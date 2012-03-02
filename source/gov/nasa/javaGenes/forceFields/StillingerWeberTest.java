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
//  Created by Al Globus on Fri May 24 2002.

import junit.framework.TestCase;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.DoubleInterval;

public class StillingerWeberTest extends TestCase {

public StillingerWeberTest(String name) {super(name);}
public void testConstructorWithElements() {
    DoubleInterval i = new DoubleInterval();
    AtomicSpecies[] e1 = {new AtomicSpecies("Si")};
    StillingerWeber sw = new StillingerWeber(e1,i,i,i);
    assertTrue("1", sw.canModel(new TwoBody("Si", "Si")));
    assertTrue("2", sw.canModel(new ThreeBody("Si", "Si", "Si")));
    assertTrue("3", !sw.canModel(new TwoBody("Si", "C")));

    AtomicSpecies[] e2 = {new AtomicSpecies("C",1),new AtomicSpecies("Si",2)};
    sw = new StillingerWeber(e2,i,i,i);
    assertTrue("4", sw.canModel(new TwoBody("Si", "Si")));
    assertTrue("5", sw.canModel(new ThreeBody("Si", "Si", "Si")));
    assertTrue("6", sw.canModel(new TwoBody("Si", "C")));
    assertTrue("7", sw.canModel(new TwoBody("C", "C")));
    assertTrue("8", sw.canModel(new ThreeBody("C", "C", "C")));
    assertTrue("9", sw.canModel(new TwoBody("C", "Si")));
    assertTrue("10", sw.canModel(new ThreeBody("C", "Si", "C")));
    assertTrue("11", sw.canModel(new ThreeBody("Si", "C", "Si")));
    assertTrue("12", sw.canModel(new ThreeBody("Si", "C", "C")));
    assertTrue("13", sw.canModel(new ThreeBody("C", "Si", "Si")));
    assertTrue("13.5", !sw.canModel(new ThreeBody("C", "Ge", "Si")));
    assertTrue("13.6", sw.getCutoff(new TwoBody("C","Si")) == 2);
    assertTrue("13.7", sw.getCutoff(new TwoBody("C","C")) == 1);
    assertTrue("13.8", sw.getCutoff(new TwoBody("Si","Si")) == 2);
    assertTrue("13.9", sw.getCutoff(new ThreeBody("C","Si","Si")) == 2);
    assertTrue("13.10", sw.getCutoff(new ThreeBody("Si","Si","Si")) == 2);
    assertTrue("13.11", sw.getCutoff(new ThreeBody("C","C","C")) == 1);

    AtomicSpecies[] e3 = {new AtomicSpecies("C",1),new AtomicSpecies("Ge",3),new AtomicSpecies("Si",2)};
    sw = new StillingerWeber(e3,i,i,i);
    assertTrue("4.1", sw.canModel(new TwoBody("Si", "Si")));
    assertTrue("5.1", sw.canModel(new ThreeBody("Si", "Si", "Si")));
    assertTrue("6.1", sw.canModel(new TwoBody("Si", "C")));
    assertTrue("7.1", sw.canModel(new TwoBody("C", "C")));
    assertTrue("8.1", sw.canModel(new ThreeBody("C", "C", "C")));
    assertTrue("9.1", sw.canModel(new TwoBody("C", "Si")));
    assertTrue("10", sw.canModel(new ThreeBody("C", "Si", "C")));
    assertTrue("11", sw.canModel(new ThreeBody("Si", "C", "Si")));
    assertTrue("12", sw.canModel(new ThreeBody("Si", "C", "C")));
    assertTrue("13", sw.canModel(new ThreeBody("C", "Si", "Si")));
    assertTrue("14", sw.canModel(new TwoBody("Ge", "Ge")));
    assertTrue("15", sw.canModel(new ThreeBody("Ge", "Ge", "Ge")));
    assertTrue("16", sw.canModel(new ThreeBody("Ge", "Si", "C")));
    assertTrue("16.1", sw.canModel(new ThreeBody("Si", "Ge", "C")));
    assertTrue("16.2", sw.canModel(new ThreeBody("Si", "C", "Ge")));
    assertTrue("17", sw.canModel(new ThreeBody("C", "Si", "Ge")));
    assertTrue("18", sw.canModel(new ThreeBody("Ge", "Ge", "C")));
    assertTrue("19", sw.canModel(new ThreeBody("Ge", "Si", "Ge")));
    assertTrue("20", sw.canModel(new ThreeBody("Si", "Si", "Ge")));
    assertTrue("21", !sw.canModel(new ThreeBody("Si", "S", "Ge")));
    assertTrue("21.6", sw.getCutoff(new TwoBody("C","Si")) == 2);
    assertTrue("21.7", sw.getCutoff(new TwoBody("C","C")) == 1);
    assertTrue("21.8", sw.getCutoff(new TwoBody("Si","Si")) == 2);
    assertTrue("21.9", sw.getCutoff(new ThreeBody("C","Si","Si")) == 2);
    assertTrue("21.10", sw.getCutoff(new ThreeBody("Si","Si","Si")) == 2);
    assertTrue("21.11", sw.getCutoff(new ThreeBody("C","C","C")) == 1);
    assertTrue("21.12", sw.getCutoff(new ThreeBody("C","C","Ge")) == 3);
    assertTrue("21.13", sw.getCutoff(new ThreeBody("Si","C","Ge")) == 3);
    assertTrue("21.14", sw.getCutoff(new TwoBody("Ge","Si")) == 3);
}
public void testSetCutoff() {
    StillingerWeber sw = new StillingerWeber();

    TwoBody twoBody = new TwoBody("C","C");
    sw.mustModel(twoBody);
    final double cutoff1 = 3;
    sw.setCutoff(twoBody,cutoff1);
    assertTrue("CC cutoff",Utility.nearlyEqual(sw.getCutoff(twoBody),cutoff1));

    ThreeBody threeBody = new ThreeBody("C","C","Si");
    sw.mustModel(threeBody);
    final double cutoff2 = 5;
    sw.setCutoff(threeBody,cutoff2);
    assertTrue("CCSi cutoff",Utility.nearlyEqual(sw.getCutoff(threeBody),cutoff2));


    threeBody = new ThreeBody("F","F","H");
    sw.mustModel(threeBody);
    sw.setCutoff(threeBody,cutoff1,cutoff2);
    assertTrue("FFH cutoff",Utility.nearlyEqual(sw.getCutoff(threeBody),cutoff2));
}
}


