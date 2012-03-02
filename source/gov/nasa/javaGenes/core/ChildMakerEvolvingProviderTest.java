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
//  Created by Al Globus on Fri Feb 21 2003.
package gov.nasa.javaGenes.core;
import junit.framework.TestCase;

public class ChildMakerEvolvingProviderTest extends TestCase {
public ChildMakerEvolvingProviderTest(String name) {super(name);}

public void setUp() {
}

public void testReporting() {
    FitnessFunctionMultiObjective ff = new FitnessFunctionMultiObjective();
    ff.add(new FitnessFunctionFixed(1));
    ChildMaker c1 = new ChildMaker();
    ChildMaker c2 = new ChildMaker();
    ChildMakerEvolvingProvider provider = new ChildMakerEvolvingProvider();
    provider.add(c1);
    provider.add(c2);
    provider.setFitnessFunction(ff);
    
    FitnessMultiObjective f1 = new FitnessMultiObjective(ff);
    f1.add(new FitnessDouble(1));
    FitnessMultiObjective f2 = new FitnessMultiObjective(ff);
    f2.add(new FitnessDouble(2));
    FitnessMultiObjective f3 = new FitnessMultiObjective(ff);
    f3.add(new FitnessDouble(3));
    
    Individual i1 = new Individual(f1);
    Individual i2 = new Individual(f2);
    Individual i3 = new Individual(f3);
    
    Individual[] parents = {i2};
    c1.results(i1,parents);
    assertTrue("1", c2 == provider.best(c1,c2));
    assertTrue("2", c2 == provider.best(c2,c1));
    c2.results(i2,parents);
    assertTrue("3", c1 == provider.best(c1,c2));
    assertTrue("4", c1 == provider.best(c2,c1));
    c2.results(i3,parents);
    assertTrue("4", c1 == provider.best(c1,c2));
    assertTrue("5", c1 == provider.best(c2,c1));
    c1.results(i1,parents);
    assertTrue("5", c1 == provider.best(c1,c2));
    assertTrue("6", c1 == provider.best(c2,c1));
    c1.results(i3,parents);
    c2.results(i1,parents);
    c2.results(i1,parents);
    c2.results(i1,parents);
    c2.results(i1,parents);
    c2.results(i1,parents);
    assertTrue("7", c2 == provider.best(c1,c2));
    assertTrue("8", c2 == provider.best(c2,c1));

}

}



