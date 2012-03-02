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

import gov.nasa.javaGenes.EOSscheduling.EOSModel;

public class Scheduler extends gov.nasa.javaGenes.EOSscheduling.HBSS.Scheduler {

public Scheduler(EOSModel model,float priorityWeight,float availableWeight,float SSRweight) {
    this(model,priorityWeight,availableWeight,SSRweight,0);
}
public Scheduler(EOSModel model,float priorityWeight,float availableWeight,float SSRweight,float nadirPointingWeight) {
    super(model);
    taskList = new TaskList(model,priorityWeight,availableWeight,SSRweight,nadirPointingWeight);
}

public void printContentionStatistics(String filename) {
    model.beginScheduling();
    taskList.reinitialize();
    ContentionStatistics cs = new ContentionStatistics((gov.nasa.javaGenes.EOSscheduling.HBSS.contention.TaskList)taskList);
    cs.printStatistics("contentionStatistics.tsd");
    model.endScheduling();
}
}
