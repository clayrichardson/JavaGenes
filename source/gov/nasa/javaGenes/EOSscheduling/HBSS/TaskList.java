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
package gov.nasa.javaGenes.EOSscheduling.HBSS;

import gov.nasa.javaGenes.EOSscheduling.EOSModel;
import gov.nasa.javaGenes.weightNetwork.RouletteWheel;

// NOTE: Unit test in TaskWeightTest.java

public class TaskList extends RouletteWheel {
protected TaskList(){}
public TaskList(EOSModel model,float priorityWeight,float numberOfAccessWindowsWeight,float SSRweight) {
    for(int i = 0; i < model.getNumberOfTasks(); i++) 
        if (model.getTask(i).numberOfAccessWindows() > 0)
            add(new TaskWeight(model.getTask(i),priorityWeight,numberOfAccessWindowsWeight,SSRweight));
    initializeWeightSum();
    reinitialize();
}
/** does nothing. Used by subclass */
public void assertCorrect() {}
}
