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
//  Created by Al Globus on Thu Jan 16 2003.
package gov.nasa.javaGenes.core;
import junit.framework.TestCase;
import gov.nasa.alsUtility.Error;

public class ChildMakerTest extends TestCase {
public ChildMakerTest(String name) {super(name);}

public void setUp() {
}

public void testReporting() {
    FitnessFunctionMultiObjective ff = new FitnessFunctionMultiObjective();
    ff.add(new FitnessFunctionFixed(1));
    ChildMaker c = new ChildMaker();
    c.setFitnessFunction(ff);
    
    FitnessMultiObjective f1 = new FitnessMultiObjective(ff);
    f1.add(new FitnessDouble(1));
    FitnessMultiObjective f2 = new FitnessMultiObjective(ff);
    f2.add(new FitnessDouble(2));
    
    Individual i1 = new Individual(f1);
    Individual i2 = new Individual(f2);
    
    Individual[] parents = {i1};
    c.results(i2,parents);
    Error.assertTrue("1", c.checkResults(0,1,0,0));
    assertTrue("1l", c.checkLastResults(0,1,0,0));
    c.results(i1,parents);
    assertTrue("2",c.checkResults(0,1,0,1));
    assertTrue("2l",c.checkLastResults(0,1,0,1));
    Individual[] parents1 = {i1,i2};
    c.results(i1,parents1);
    assertTrue("3",c.checkResults(0,1,0,2));
    assertTrue("3l",c.checkLastResults(0,1,0,2));
    
    ff.add(new FitnessFunctionFixed(2));
    f1.add(new FitnessDouble(1));
    f2.add(new FitnessDouble(2));
    c = new ChildMaker();
    c.setFitnessFunction(ff);

    c.results(i2,parents);
    assertTrue("3.1", c.checkResults(0,1,0,0));
    assertTrue("4", c.checkResults(1,1,0,0));
    c.results(i1,parents);
    assertTrue("5",c.checkResults(0,1,0,1));
    assertTrue("6",c.checkResults(1,1,0,1));
}
public void testLastReporting() {
    FitnessFunctionMultiObjective ff = new FitnessFunctionMultiObjective();
    ff.add(new FitnessFunctionFixed(1));
    ChildMaker c = new ChildMaker();
    c.setFitnessFunction(ff);
    
    FitnessMultiObjective f1 = new FitnessMultiObjective(ff);
    f1.add(new FitnessDouble(1));
    FitnessMultiObjective f2 = new FitnessMultiObjective(ff);
    f2.add(new FitnessDouble(2));
    
    Individual i1 = new Individual(f1);
    Individual i2 = new Individual(f2);
    
    Individual[] parents = {i1};
    c.results(i2,parents);
    assertTrue("1", c.checkResults(0,1,0,0));
    assertTrue("1l", c.checkLastResults(0,1,0,0));
	c.getAndClearLastTabSeparatedResults();
    assertTrue("1la", c.checkLastResults(0,0,0,0));
    c.results(i1,parents);
    assertTrue("2",c.checkResults(0,1,0,1));
    assertTrue("2l",c.checkLastResults(0,0,0,1));
	c.getAndClearLastTabSeparatedResults();
    assertTrue("2la", c.checkLastResults(0,0,0,0));
    Individual[] parents1 = {i1,i2};
    c.results(i1,parents1);
    assertTrue("3",c.checkResults(0,1,0,2));
    assertTrue("3l",c.checkLastResults(0,0,0,1));
}

}


