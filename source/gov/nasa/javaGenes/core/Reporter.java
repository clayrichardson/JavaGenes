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



import java.io.Serializable;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.lang.Process;
import java.lang.Runtime;
import gov.nasa.alsUtility.IO;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.EOSscheduling.EOSschedulingEvolvable;
import gov.nasa.alsUtility.LogFile;
import gov.nasa.alsUtility.Sample;

/**
Create files and directories describing a genetic run.

@see Parameters
@see Population
*/
public class Reporter implements java.io.Serializable {
protected Parameters parameters = null;
public static final String logFilename = "log.tsd";
public static final String bestEvolvablesFilename = "bestEvolvables.tsd";
public static final String totalTimeFilename = "totalTime.txt";
public static final String parametersFilename = "parameters.txt";
public static final String populationDirectory = "population";
public static final String fittestTextFilename = "fittest.txt";
public static final String paretoFilename = "pareto.tsd";
public static final String childMakerFilename = "variationOperatorPerformance.tsd";
public static final String childMakerSoFarFilenameBase = "variationOperatorPerformanceSoFar";
public static final String variationOperatorDirectory = "variationOperatorPerformance";
public static final String childMakersForBestEvolvableFilename = "variationOperatorsForBestEvolvable.tsd";
public static final String doneFilename = "done";
public static final String breederReportFilename = "breederReport.tsd";

/**
@param restart if true, use existing files and directories adding information.
*/
public Reporter(Parameters p) {
    parameters = p;
}
/**
restore state from checkpoint file
*/
public Reporter(Parameters p, TokenizeInput tokenizer) {
	this(p);
}
/**
save state to checkpoint file
*/
public void stateSave(TokenizeOutput tokenizer) {
}

public void report(Parameters p) {
   Utility.makeFile(parametersFilename,p.toString());
   parameters.makeFiles();
}
/**
will always return a number.

@return 0 if x == 0, otherwise Math.log (Math.abs(x))
*/
public double specialLog (double x) {
    if (x == 0) return 0;
    else return Math.log (Math.abs(x));
}
/**
 Report on a population
*/
public void report (Population population, Breeder breeder) {
	Individual best = population.bestIndividual();
	int generation = population.getGeneration();
	
	if (generation == 0) {
		Utility.makeDirectory (populationDirectory);
		Utility.makeDirectory (variationOperatorDirectory);
	} else if (parameters.reportVariationOperatorPerformanceEachGeneration) {
		childMakerReport(generation,variationOperatorDirectory + Utility.fileSeparator() + Utility.makeSequentialNumber (generation) + ".tsd",false);
		Utility.makeFile(variationOperatorDirectory + Utility.fileSeparator() + Utility.makeSequentialNumber (generation) + "best.tsd",best.getEvolvable().getChildMakersUsed().toString());
	}
	if (generation != 0) {
		childMakerReport(generation,childMakerSoFarFilenameBase + ".tsd",true);
		Utility.makeFile(childMakerSoFarFilenameBase + "Best.tsd",best.getEvolvable().getChildMakersUsed().toString());
	}
	parameters.breeder.reportOnGeneration(generation,breederReportFilename);
	if (shouldWrite(generation,parameters.frequencyOfASCIIPopulations)) {
		String name = populationDirectory + Utility.fileSeparator() + Utility.makeSequentialNumber (generation) + ".tsd";
		PrintWriter out = IO.getPrintWriter(name);
		population.report(out);
		out.close();
		
		if (breeder instanceof BreederWithTeachers) {
			TeacherPopulation p = ((BreederWithTeachers)breeder).getTeachers();
			if (p != null) {
				String tname = populationDirectory + Utility.fileSeparator() + "t" + Utility.makeSequentialNumber (generation) + ".tsd";
				PrintWriter tout = IO.getPrintWriter(tname);
				p.report(tout);
				tout.close();
			}
		}
	}
	
    // write log file with summary from each generation
    LogFile log;
    LogFile evolvables = null;
    if (generation == 0) {
		log = new LogFile(logFilename, false);
		final String[] everyHeader = {"generation"};
		String header = Utility.arrayToTabSeparated(everyHeader);
		if (parameters.evaluationFunction != null)
			header += "\t" + Utility.arrayToTabSeparated(parameters.evaluationFunction.getNameArray());
		String[] nameArray = parameters.fitnessFunction.getNameArray();
		header += "\t" + Utility.arrayToTabSeparated(nameArray);
		header += "\t" + addPostfix(nameArray,"Mean");
		header += "\t" + addPostfix(nameArray,"StdDev");
		if (parameters.separateLogAndEvolvableFiles) {
			if (parameters.logBestEvolvableEachGeneration) {
				evolvables = new LogFile(bestEvolvablesFilename,false);
				evolvables.println("generation\tfitness\t" + population.getEvolvableHeader());
			}
		} else
			header += "\t" + population.getEvolvableHeader();
		log.println(header);
    } else {
		log = new LogFile(logFilename, true);
        if (parameters.separateLogAndEvolvableFiles && parameters.logBestEvolvableEachGeneration)
            evolvables = new LogFile(bestEvolvablesFilename,true);
    }
    String results = generation + "";
    if (parameters.evaluationFunction != null) {
        best.getEvolvable().prepareForEvaluator(parameters);
        results += "\t" + Utility.arrayToTabSeparated(parameters.evaluationFunction.evaluateFitness(best.getEvolvable()).getFitnessArray());
    }
    results += "\t" + Utility.arrayToTabSeparated(best.getFitness().getFitnessArray());
    results += fitnessMeanAndStdDevString(population);
    if (parameters.logBestEvolvableEachGeneration) {
        if (parameters.separateLogAndEvolvableFiles) {
            evolvables.println(generation + "\t" + best.getFitness().asDouble() + "\t" + best.getEvolvable().toString());
            evolvables.close();
        } else 
            results += "\t" + best.getEvolvable().toString();
    }
    log.println(results);
    log.close();
}
protected String addPostfix(String[] array, String post) {
  String[] out = new String[array.length];
  for(int i = 0; i < array.length; i++)
    out[i] = array[i] + post;
  return  Utility.arrayToTabSeparated(out);
}
protected String fitnessMeanAndStdDevString(Population population) {
  String results = "";
  Sample[] data = new Sample[parameters.fitnessFunction.numberOfObjectives()];
  for(int i = 0; i < data.length; i++)
    data[i] = new Sample();
  for(int i = 0; i < population.getSize(); i++) {
    Fitness f = population.getIndividual(i).getFitness();
    double[] datum = f.getFitnessArray();
    Error.assertTrue(datum.length == data.length);
    for(int j = 0; j < data.length; j++)
      data[j].addDatum(datum[j]);
  }
  for(int i = 0; i < data.length; i++)
    results += "\t" + data[i].getMean();
  for(int i = 0; i < data.length; i++)
    results += "\t" + data[i].getStandardDeviation();
  return results;
}

protected boolean shouldWrite(int generation, int parameter) {
	if (parameter == 1) return true;
	return generation % parameter == 0;
}     
/**
Call when the run is finished.

@param time the total time for the run.
*/  
public void done(Population population) {
  Individual best = population.bestIndividual();
  Utility.makeFile(fittestTextFilename,best.toString());
  
  childMakerReport(population.getGeneration(),childMakerFilename,true);
  Utility.makeFile(childMakersForBestEvolvableFilename,best.getEvolvable().getChildMakersUsed().toString());

  // start pareto front reporting
  if (parameters.paretoFitness != null)
    population.evaluateFitness(parameters.paretoFitness);
  Population pareto = population.getParetoFront();
  LogFile out = new LogFile(paretoFilename, false);

  String header = "";
  if (parameters.evaluationFunction != null)
    header += Utility.arrayToTabSeparated(parameters.evaluationFunction.getNameArray()) + "\t";
  String[] nameArray = parameters.paretoFitness == null ? parameters.fitnessFunction.getNameArray() : parameters.paretoFitness.getNameArray();
  header += Utility.arrayToTabSeparated(nameArray);
   header += "\t" + population.getEvolvableHeader();
  out.println(header);

  for(int i = 0; i < pareto.getSize(); i++) {
    Individual current = pareto.getIndividual(i);
    String results = "";
    if (parameters.evaluationFunction != null)
      results += Utility.arrayToTabSeparated(parameters.evaluationFunction.evaluateFitness(current.getEvolvable()).getFitnessArray()) + "\t";
    results += Utility.arrayToTabSeparated(current.getFitness().getFitnessArray());
    results += "\t" + pareto.getEvolvable(i).toString();
    out.println(results);
  }
  out.close();
  // end pareto front reporting

  Utility.makeFile(doneFilename,"");
  System.out.println("done");
}
public void childMakerReport(int generation,String filename, boolean complete) {
    LogFile out = new LogFile(filename,false);
 
    String[] nameArray = parameters.fitnessFunction.getNameArray();
    out.print("generation\tname");
    String headerFragment = parameters.childMakerProvider.get(0).headerFragment();
    for(int i = 0; i < nameArray.length; i++)
        out.print("\t" + nameArray[i] + "\t" + headerFragment);
    out.println("");

   for(int i = 0; i < parameters.childMakerProvider.size(); i++) {
        ChildMaker cm = parameters.childMakerProvider.get(i);
        out.println(generation + "\t" + cm.toString() + "\t" + (complete ? cm.tabSeparatedResults() : cm.getAndClearLastTabSeparatedResults()));
    }
	out.close();
}
public void totalTime(long time) {
	Utility.makeFile(totalTimeFilename,"" + time);
}
public String toString() {return "gov.nasa.javaGenes.core.Reporter";}
}
