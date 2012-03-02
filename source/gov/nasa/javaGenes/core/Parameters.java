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


import java.io.PrintWriter;
import java.io.Serializable;
import java.io.File;
import java.lang.Class;
import java.lang.reflect.Field;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;


/**
stores parameters for a run
*/ 
public class Parameters implements Serializable {

public static final String SEPARATOR = " = ";
/**
minimum 3 to allow tournament selection
*/
public int populationSize = 100;
public int kidsPerGeneration = populationSize;
public int maximumGenerations = 100;
public double tournamentProbability = 1.0;
public FitnessFunction fitnessFunction;
public FitnessFunction evaluationFunction; // used by Reporter for independent quality checks. Not used for fitness.  Can be slow (used rarely)
public FitnessFunction paretoFitness; // used by Reporter to generate a pareto front. Useful when using WeigthedFitness in multi-objective cases
public Breeder breeder;
public ChildMakerProvider childMakerProvider = new ChildMakerProvider();
public boolean separateLogAndEvolvableFiles = false;
public boolean logBestEvolvableEachGeneration = true;
public boolean reportVariationOperatorPerformanceEachGeneration = false;

public int randomIndividualTriesPerSpecification = 10;

public int frequencyOfASCIIPopulations = 5;

/**
after each generation check for this file. If exists, end the run.
*/
public File stopFile =  new File ("stop");
/**
after each generation check for this file. If exists, suspend for suspendTime milliseconds.
*/
public File suspendFile = new File ("suspend");
public int suspendTime = 10000;
/**
if true, end run when best fitness equals 0
*/
public boolean stopAtPerfection = false;

/**
create any files necessary to describe the parameters.
Used for things that won't show up in the string representation.
*/
public void makeFiles() {}
/**
does not be to be changed when fields are changed, but has a bug that
occurs when a field is a interface
*/
public String toString(){
    String r = "";
    Class currentClass = this.getClass();
    try {
    	while(currentClass != null) {
		    Field[] fields = currentClass.getDeclaredFields();
		    for(int i = 0; i < fields.length; i++){
                        if (!(fields[i].get(this) instanceof Population)) {
                            r += fields[i].getName();
                            r += SEPARATOR + fields[i].get(this) + Utility.lineSeparator();
                        }
    		}
    		if (currentClass.equals(Parameters.class))
    			break;
   			currentClass = currentClass.getSuperclass();
  		}
    } catch (Exception e) {Error.fatal ("Parameters.toString(): " + e);}
    
    return r;
}
}
