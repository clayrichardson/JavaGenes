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
import gov.nasa.alsUtility.RandomNumber;

public class SchedulerTest extends TestCase {

public SchedulerTest(String name) {super(name);}

// only really checks anything is EOSscheduling.HBSSScheduler.debug == true
public void testRandomly() {
    RandomNumber.initialize(); // allows setting the seed to a known value if a failure occures
    //RandomNumber.setSeed(1059677636010L);
    modelTest(TestNetwork.buildModel1());
    modelTest(TestNetwork.buildModel2());
    modelTest(TestNetwork.buildModel3());
    modelTest(TestNetwork.buildModel4());
    modelTest(TestNetwork.buildModel5());
    modelTest(TestNetwork.buildModel6());
    modelTest(TestNetwork.buildModel7());
    //modelTest(TestNetwork.buildModel8()); // one access window too small, FIX
    modelTest(TestNetwork.buildModel9());
}
// only really checks anything is EOSscheduling.HBSSScheduler.debug == true
public void modelTest(EOSModel model) {
    final int repetitions = 10;

    Scheduler scheduler = new Scheduler(model,2,3,4,1.2f);
    EOSschedulingEvolvable evolvable = new EOSschedulingEvolvable(model.getNumberOfTasks());
    for(int i = 0; i < repetitions; i++) {
        scheduler.createSchedule(evolvable);
        scheduler.rescheduleFromPermutation(evolvable);
    }
}
}
