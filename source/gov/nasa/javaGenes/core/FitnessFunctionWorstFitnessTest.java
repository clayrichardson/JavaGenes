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

public class FitnessFunctionWorstFitnessTest extends TestCase {

public FitnessFunctionWorstFitnessTest(String name) {super(name);}

public void testEvaluateFitness() {
    double[] f1 = {1,2,3};
    test("1", f1, 3);
    double[] f2 = {3,2,1};
    test("2", f2, 3);
    double[] f3 = {2,3,1};
    test("3", f3, 3);
    double[] f4 = {5};
    test("4", f4, 5);
    double[] f5 = {3,4,6,2,7,10,4};
    test("5", f5, 10);
}
private void test(String name, double[] fitness,  double answer) {
    FitnessFunctionWorstFitness worst = new FitnessFunctionWorstFitness(new MyFitnessFunction(), new MyChanger(fitness));
    double a = worst.evaluateFitness(new Evolvable()).asDouble();
    assertTrue(name + " " + a, a == answer);
}
private class MyChanger implements PhenotypeChanger {
    private double[] fitness;
    public MyChanger(double[] fitness) {this.fitness = fitness;}
    public Evolvable[] getVariations(Evolvable evolvable) {
        MyEvolvable[] variations = new MyEvolvable[fitness.length];
        for(int i = 0; i < variations.length; i++)
            variations[i] = new MyEvolvable(fitness[i]);
        return variations;
    }
}
private class MyEvolvable extends Evolvable {
    double fitness;
    public MyEvolvable(double fitness) {this.fitness = fitness;}
    public double getFitness() {return fitness;}
}
private class MyFitnessFunction extends FitnessFunction {
    public Fitness evaluateFitness (Evolvable evolvable) {
        return new FitnessDouble(((MyEvolvable)evolvable).getFitness());
    }
}
}