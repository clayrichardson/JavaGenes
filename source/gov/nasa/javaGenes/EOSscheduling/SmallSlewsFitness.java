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
//  Created by Al Globus 
package gov.nasa.javaGenes.EOSscheduling;

import gov.nasa.javaGenes.core.Fitness;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.javaGenes.core.FitnessDouble;

public class SmallSlewsFitness extends SchedulingFitnessFunction {
protected EOSModel model;
public SmallSlewsFitness(Scheduler scheduler,EOSModel inModel) {
    super(scheduler);
    model = inModel;
}
/**
@return sum of squares of the slews
*/
public Fitness evaluateFitness (Evolvable evolvable){
    EOSschedulingEvolvable schedule = (EOSschedulingEvolvable)evolvable;
    createSchedule(schedule);
    double totalSlewing = 0;
    for(int i = 0; i < schedule.getSize(); i++) {
        SlewRequirement slew = schedule.getSlewRequirement(i);
        if (slew != null)
            for(int j = 0; j < slew.numberOfParameters(); j++)
                totalSlewing += java.lang.Math.abs(slew.getParameter(j));
    }
    return normalizedFitness(totalSlewing,schedule,model);
}
public String toString() {return "SmallSlewsFitness, normization " + normalization;}
}
