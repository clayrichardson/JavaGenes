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
//  Created by Al Globus on Wed Oct 02 2002.
package gov.nasa.javaGenes.EOSscheduling;

import gov.nasa.javaGenes.core.Reporter;
import gov.nasa.alsUtility.LogFile;
import gov.nasa.javaGenes.core.Parameters;
import gov.nasa.javaGenes.core.Population;
import gov.nasa.javaGenes.core.Breeder;
import gov.nasa.javaGenes.core.Individual;
import gov.nasa.alsUtility.Utility;

public class EOSschedulingReporter extends Reporter {
public final String fitnessEachGenerationFilename = "generationFitness.tsd";

public EOSschedulingReporter(Parameters p) {
    super(p);
}
public void done(Population population) {
    super.done(population);
    EOSModel model = ((EOSschedulingParameters)parameters).model;
    Satellite[] satellites = model.getSatellites();
    for(int i = 0; i < satellites.length; i++) {
        SSRTimeline timeline = satellites[i].getSSRtimeline();
        timeline.printToTsd("SSR_" + satellites[i].getName() + ".tsd");
    }
    for(int i = 0; i < satellites.length; i++) {
        SlewMotor[] motors = satellites[i].getSlewMotors();
        for(int j = 0; j < motors.length; j++) {
            SlewTimeline timeline = motors[j].getSlewTimeline();
            final String separator = "_";
            String filename = "SlewMotor";
            Sensor[] sensors = motors[j].getSensors();
            for(int k = 0; k < sensors.length; k++)
                filename += separator + sensors[k].getNumber();
            filename += ".tsd";
            timeline.printToTsd(filename);
        }
    }
    for(int i = 0; i < satellites.length; i++) {
        Sensor[] sensors = satellites[i].getSensors();
        for(int j = 0; j < sensors.length; j++) {
            AvailabilityTimeline timeline = sensors[j].getAvailabilityTimeline();
            String filename = "Sensor_" + sensors[j].getNumber() + ".tsd";
            timeline.printToTsd(filename);
        }
    }
}

}
