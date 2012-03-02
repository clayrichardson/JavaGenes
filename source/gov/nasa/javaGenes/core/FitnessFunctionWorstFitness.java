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

import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.LogFile;

/**
 this fitness function will run another fitness function N times on variations of the Evolvable.  Usually the variations
 will be in the phenotype.  The worst fitness of the variations will be the fitness of the Evolvable.
*/
public class FitnessFunctionWorstFitness extends FitnessFunction {
public static boolean debug = false;
public static final String debugFilename = "FitnessFunctionWorstFitnessDebug.tsd";
protected FitnessFunction fitnessFunction;
protected PhenotypeChanger phenotypeChanger;

public FitnessFunctionWorstFitness(FitnessFunction fitnessFunction, PhenotypeChanger phenotypeChanger) {
    this.fitnessFunction = fitnessFunction;
    this.phenotypeChanger = phenotypeChanger;
}
public Fitness evaluateFitness (Evolvable evolvable) {
    Evolvable[] allEvolvables = phenotypeChanger.getVariations(evolvable);
    Error.assertTrue(allEvolvables.length > 0);
	Fitness[] forDebuging =  new Fitness[allEvolvables.length];
    Fitness worst = getWorst(allEvolvables,forDebuging);
	if (debug) {
		LogFile out = new LogFile(debugFilename,true);
		for(int i = 0; i < allEvolvables.length && forDebuging[i] != null; i++)
			out.print(forDebuging[i].asDouble() + "\t");
		out.println("\n");
		out.close();
	}
    return worst;
}
protected Fitness getWorst(Evolvable[] allEvolvables, Fitness[] forDebuging) {
	Fitness worstSoFar = null;
    for(int i = 0; i < allEvolvables.length; i++) {
        Fitness fitness = fitnessFunction.evaluateFitness(allEvolvables[i]);
		forDebuging[i] = fitness;
        if (worstSoFar == null || worstSoFar.fitterThan(fitness))
            worstSoFar = fitness;
    }
	return worstSoFar;
}
public FitnessFunction getFitnessFunction() {return fitnessFunction;}
public String toString() {
	return "FitnessFunctionWorstFitness fitnessFunction=" + fitnessFunction.toString() + "\nphenotypeChanger=" + phenotypeChanger.toString();
}

}