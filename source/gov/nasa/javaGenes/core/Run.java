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


import java.io.File;
import java.lang.Thread;
import java.lang.Exception;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.LogFile;
import gov.nasa.alsUtility.Timer;

/**
run JavaGenes under control of a Checkpointer object
*/
public abstract class Run extends AbstractCheckpointable {

protected Parameters parameters;
protected Population population;
protected boolean foundPerfection = false;
protected Timer generationTimer = new Timer();   // DEFICIENCY: won't include checkpoint time
protected int generation;
protected Reporter reporter;
protected transient LogFile checkpointTimesFile;
protected transient Timer checkpointTimer;
private boolean timeRun = false;

public Parameters getParameters() {return parameters;}
/**
creates a file called checkpointTimes.tsd to hold information about the time that checkpointing took
*/
private void setCheckpointMonitoring() {
	checkpointTimesFile = new LogFile("checkpointTimes.tsd", true);
	checkpointTimer = new Timer();
}
/**
start a run.

@param arguments usually the arguments to main()
*/
public void start(String[] arguments) {
    setCheckpointMonitoring();
    if (!checkpointTimesFile.containsData())
        checkpointTimesFile.println("time\tlength(ms)");
    generation = 0;
    startSpecialized(arguments);
    if (reporter == null)
        reporter = new Reporter(parameters);
    reporter.report(parameters);
    report();
    Timer timer = null;
    if (timeRun) {
        timer = new Timer();
        timer.start();
    }
    if (!shouldStop()) {
        beforeNextGeneration();
        run();
    }
    if (timeRun) {
        timer.stop();
        System.out.println(timer.toString());
    }
    atEnd();
}
/**
used to do the special processing necessary at startup by subclasses
*/
protected abstract void startSpecialized(String[] arguments);

/**
start the run after having read any checkpoint file
*/
public void restart() {
	setCheckpointMonitoring();
  generationTimer.start();
  if (!shouldStop())
	  run();
	atEnd();
}
/**
called by start and restart to run the genetic algorithm
*/
protected void run() {
	while (true) {
    final FitnessFunction f = parameters.fitnessFunction.getFunction(generation);
    parameters.breeder.setFitnessFunction(f);
    if (parameters.fitnessFunction.isNewFunction(generation)) {
      population.evaluateFitness(f);
      report();
    }
    population = parameters.breeder.breed(population,parameters.kidsPerGeneration);

		afterNextGeneration();
		if (shouldStop()) return;
    beforeNextGeneration();
  }
}

protected void beforeNextGeneration() {
  generationTimer.reset();
  generationTimer.start();
 	generation++;
}
protected void afterNextGeneration() {
	report();
}
protected boolean shouldStop() {
  if (generation >= parameters.maximumGenerations)
    	return true;

 	// handle perfect individual
  /*
  if (population.bestIndividual().getFitness.isPerfect()) {
     	if (!foundPerfection) {
     		Utility.makeFile("perfectGeneration.txt", generation + "");
     		foundPerfection = true;
     	}
     	if (parameters.stopAtPerfection)
     		return true; // can't beat perfection
  }
  */
 
 	// stop or suspend if appropriate files exist
  if (parameters.stopFile.exists()) return true;
 	while (parameters.suspendFile.exists())  {
 		try {
 			Thread.sleep (parameters.suspendTime);
 		} catch (java.lang.InterruptedException e) {
 			// does nothing
 	    }
 	}
	return false;
}

protected void atEnd(){
   reporter.done(population);
}
public void beforeCheckpoint() {
	generationTimer.stop();
  checkpointTimer.reset();
  checkpointTimer.start();
}
public void afterCheckpoint() {
  Checkpointer.dontCheckpoint();
	checkpointTimer.stop();
  checkpointTimesFile.println(Utility.date() + "\t" + checkpointTimer.timeElapsed());
	generationTimer.start();
}
protected void report() {
	Runtime runTime = Runtime.getRuntime();
	population.setGeneration(generation);
	population.setTime(generationTimer.timeElapsed());
	population.setMemory(runTime.totalMemory() - runTime.freeMemory());
	reporter.report (population,parameters.breeder);
}
protected void checkpoint() {
	Checkpointer.checkpoint(); // BUG: saving report and restart files should be indivisible operation. Need database.
}

/*
// used to compare to molecules. Not normally run.
public static void compareMolecules(String[] arguments) {
	Error.assertTrue(arguments.length == 2);
	FitnessFunction fitnessFunction = new VertexPairsDistanceSimilarity(molFormat.read(arguments[0]));
	double fitness = fitnessFunction.evaluateFitness(molFormat.read(arguments[1]));
	System.out.println("fitness = " + fitness);
}
*/
}

