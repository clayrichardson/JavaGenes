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
package gov.nasa.javaGenes.EOSscheduling;

import gov.nasa.javaGenes.permutation.PermutationParameters;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.javaGenes.core.WeightedSumFitness;
import gov.nasa.javaGenes.core.FitnessFunction;
import gov.nasa.javaGenes.core.BreederSteadyState;
import gov.nasa.javaGenes.permutation.PermutationOrderMutation;
import gov.nasa.javaGenes.permutation.PermutationPositionCrossover;

public class EOSschedulingParameters extends PermutationParameters {
static {
    RandomNumber.setSeed(990639400906L); // used to generate repeatable runs. See seed.txt for seed of last run
}

public EOSModel model;
public Scheduler scheduler;
public int takeImageTime = 24; // seconds
public int timeBetweenSamples = 60; // seconds
public double slewRate = 1;

private void setParameters() {
    populationSize = 10;
    kidsPerGeneration = 20;
    maximumGenerations = 5;
    frequencyOfASCIIPopulations = 1;
    stopAtPerfection = false;

    model = new EOSModel();
    STKAccessFile csvFile = new STKAccessFile("../input/test.csv",timeBetweenSamples);
    model.setHorizon(csvFile.getHorizon());
    Satellite satellite = new Satellite("satellite");
    model.addSatellite(satellite);
    SensorType sensorType = new SensorType("sensor");
    Sensor sensor = new Sensor(satellite,sensorType,model.getHorizon(),takeImageTime);
    satellite.addSensor(sensor);
    SlewMotor slewMotor = new SlewMotor(slewRate,-24,24,model.getHorizon(),takeImageTime); 
    sensor.setSlewMotor(slewMotor);
    satellite.addSlewMotor(slewMotor);
    slewMotor.addSensor(sensor);
    TaskAndAccessWindowGenerator.generateTakeImages(model,takeImageTime,sensor,csvFile);
    model.printUnexecutableTasks("tasksWithNoWindows.tsd");
    model.report("ModelDirectory");

    permutationLength = model.getNumberOfTasks();
    scheduler = new Scheduler(model);
    scheduler.addPlacer(new EarliestFromChosenWindowPlacer());
        
    WeightedSumFitness fitness = new WeightedSumFitness();
    FitnessFunction f;
    f = new NumberOfTakeImagesFitness(scheduler);
    fitness.add(1.0,f);

    fitnessFunction = fitness;
    
    breeder = new BreederSteadyState(this);
    //childMakerProvider.add(new OrderMutation());
    childMakerProvider.add(new SqueakyWheelTournamentSwapMutation(model,2,5));
    childMakerProvider.add(new SqueakyWheelPlacedTSM(model,2,5,0.5,0.3));
    childMakerProvider.add(new SqueakyWheelShiftForward(model,2,5,0.5,0.3));
    childMakerProvider.setFitnessFunction(fitnessFunction);
}

/**
Sets up all parameters
*/
public EOSschedulingParameters() {
  setParameters();
}
public void makeFiles() {
}

}
