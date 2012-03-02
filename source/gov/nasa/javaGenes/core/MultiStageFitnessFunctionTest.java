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
package gov.nasa.javaGenes.core;
import junit.framework.TestCase;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.Utility;

public class MultiStageFitnessFunctionTest extends TestCase {

public MultiStageFitnessFunctionTest(String name) {super(name);}

public void testEvaluateFitness() {
    double[] w1 = {1,1};
    double[] f1 = {0.5,3};
    test("1", w1, f1, 3);
    w1[0] = 1.1;
    test("2", w1, f1, 3);
    w1[0] = 0.5;
    test("3", w1, f1, 0.5);
    
    double[] w2 = {1,2,3};
    double[] f2 = {1,3,2};
    test("4", w2, f2, 1);
    w2[0] = 1.1;
    f2[1] = 1.5;
    test("5", w2, f2, 2);
    f2[1] = 3;
    w2[0] = 6;
    test("6", w2, f2, 3);
    
    double[] w3 = {3};
    double[] f3 = {4};
    test("7", w3, f3, 4);
    f3[0] = 1;
    test("8", w3, f3, 1);
}
private void test(String name, double[] weights, double[] fitness, double answer) {
    Error.assertTrue(weights.length == fitness.length);
    MultiStageFitnessFunction multiStageFitnessFunction = new MultiStageFitnessFunction();
    for(int i = 0; i < weights.length; i++)
        multiStageFitnessFunction.add(weights[i], new FitnessFunctionFixed(fitness[i]));
    double a = multiStageFitnessFunction.evaluateFitness(new Evolvable()).asDouble();
    assertTrue(name + " " + a, Utility.nearlyEqual(a,answer));
}
}