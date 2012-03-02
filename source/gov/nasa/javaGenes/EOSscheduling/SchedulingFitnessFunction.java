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
//  Created by Al Globus on Wed Jul 03 2002.
package gov.nasa.javaGenes.EOSscheduling;

import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.javaGenes.core.FitnessFunction;
import gov.nasa.javaGenes.core.Fitness;
import gov.nasa.javaGenes.core.FitnessDouble;
import gov.nasa.alsUtility.Error;

public class SchedulingFitnessFunction extends FitnessFunction {
protected Scheduler scheduler;
static public final int NO_NORMALIZATION = 0;
static public final int NORMALIZE_BY_NUMBER_OF_SCHEDULED_TAKEIMAGES = 1;
static public final int NORMALIZE_BY_SCHEDULED_TAKEIMAGES_PRIORITY = 2;
protected int normalization = NO_NORMALIZATION;

public SchedulingFitnessFunction(Scheduler inScheduler) {
    scheduler = inScheduler;
}
public Fitness evaluateFitness (Evolvable evolvable){
    Error.notImplemented(); // subclasses implement this
    return null;
}
public void setNormalization(int inNormalization) {
    normalization = inNormalization;
}
public Fitness normalizedFitness(double fitness,EOSschedulingEvolvable evolvable,EOSModel model) {
    switch(normalization) {
        case NO_NORMALIZATION: 
            return new FitnessDouble((double)fitness);
    	case NORMALIZE_BY_NUMBER_OF_SCHEDULED_TAKEIMAGES:
            return new FitnessDouble((double)fitness/evolvable.getScheduledTaskCount());
    	case NORMALIZE_BY_SCHEDULED_TAKEIMAGES_PRIORITY:
            return new FitnessDouble((double)fitness/evolvable.getScheduledPrioritySum(model));
        default:
            Error.fatal("bad normalization value");
    }
    return null;
}
/** 
this must always be called by subclasses before calculating fitness
*/
protected void createSchedule(EOSschedulingEvolvable evolvable) {
    scheduler.createSchedule(evolvable); // schedule object makes sure scheduling is only done once
}
 
}
