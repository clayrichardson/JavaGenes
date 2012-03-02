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
package gov.nasa.javaGenes.EOSscheduling.HBSS.contention;

import junit.framework.TestCase;
import gov.nasa.alsUtility.Utility;
import gov.nasa.javaGenes.EOSscheduling.*;
import gov.nasa.alsUtility.IntegerInterval;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.javaGenes.EOSscheduling.Horizon;

/** test small random models similar to TwoSatellite experiments */
public class TwoSatellitesTest extends TestCase {
public static String time[] = {
    "1 Jan 2002 00:00:00.00",
    "1 Jan 2002 00:10:00.00",
};
public static final int SSR_SIZE = 10;
public static final int TAKEIMAGE_TIME = 24;
public static final double SLEW_RATE = 0.5;
public static final int SLEW_LIMIT = 48;

public TwoSatellitesTest(String name) {super(name);}

// only really checks anything is EOSscheduling.HBSSScheduler.debug == true
public void testRandomly() {
    RandomNumber.initialize(); // allows setting the seed to a known value if a failure occures
    RandomNumber.setSeed(1060022576607L);
    final int numberOfModels = 2;
    for(int i = 0; i < numberOfModels; i++) {
        EOSModel model = makeRandomModel();
        modelTest(model);
    }
}
// only really checks anything if EOSscheduling.HBSSScheduler.debug == true
public void modelTest(EOSModel model) {
    final int repetitions = 2;
    Scheduler scheduler = new Scheduler(model,4,3,2,1.3f); // numbers somewhat arbitrary
    EOSschedulingEvolvable evolvable = new EOSschedulingEvolvable(model.getNumberOfTasks());
    for(int i = 0; i < repetitions; i++) {
        scheduler.createSchedule(evolvable);
        scheduler.rescheduleFromPermutation(evolvable);
    }
}
static public EOSModel makeRandomModel() {
    final int numberOfSatellites = 2;
    final IntegerInterval numberOfTasks = new IntegerInterval(3,10);
    final IntegerInterval accessWindowsPerTask = new IntegerInterval(3,10);
    final IntegerInterval SSRuse = new IntegerInterval(1,SSR_SIZE);
    final DoubleInterval priority = new DoubleInterval(1,2);
    final DoubleInterval pointing = new DoubleInterval(-SLEW_LIMIT,SLEW_LIMIT);

    Horizon horizon = new Horizon(time[0],time[1]);
    SensorType sensorType = new SensorType("sensorType");
    EOSModel model = new EOSModel();
    model.setHorizon(horizon);

    Satellite[] satellites = new Satellite[numberOfSatellites];
    Sensor[] sensors = new Sensor[numberOfSatellites];

    for(int i = 0; i < numberOfSatellites; i++) {
        satellites[i] = new Satellite("satellite" + i);
        satellites[i].setSSR(SSR_SIZE + i,horizon,TAKEIMAGE_TIME);
        sensors[i] = new Sensor(satellites[i],sensorType,horizon,TAKEIMAGE_TIME);
        satellites[i].addSensor(sensors[i]);
        SlewMotor slewMotor = new SlewMotor(SLEW_RATE,-SLEW_LIMIT,SLEW_LIMIT,horizon,TAKEIMAGE_TIME);
        sensors[i].setSlewMotor(slewMotor);
        satellites[i].addSlewMotor(slewMotor);
        slewMotor.addSensor(sensors[i]);
        model.addSatellite(satellites[i]);
    }
    
    for(int i = numberOfTasks.random(); i >= 0; i--) {
        TakeImage t = new TakeImage(TAKEIMAGE_TIME,sensorType,SSRuse.random());
        int lastEndTime = 0;
        for(int j = accessWindowsPerTask.random(); j >= 0; j--) {
            int startTime = new IntegerInterval(lastEndTime,horizon.getEnd() - TAKEIMAGE_TIME).random();
            lastEndTime = startTime+TAKEIMAGE_TIME;
            AccessWindow aw = new AccessWindow(startTime,lastEndTime,sensors[RandomNumber.getIndex(numberOfSatellites)]);
            aw.setSlewRequirement(new CrossTrackSlew(pointing.random()));
            t.addAccessWindow(aw);
            if (lastEndTime > horizon.getEnd() - TAKEIMAGE_TIME)
                break;
        }
        t.hasAllAccessWindowsNow();
        t.setPriority(priority.random());   
        model.addTask(t);
    }
    return model;
}
}

