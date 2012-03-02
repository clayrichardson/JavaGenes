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

import gov.nasa.javaGenes.weightNetwork.Weight;
import java.lang.Math;
import gov.nasa.javaGenes.EOSscheduling.Task;
import gov.nasa.javaGenes.EOSscheduling.TakeImage;
import gov.nasa.alsUtility.Error;

public class TaskWeight extends Weight {

protected Task task;
protected AccessWindowsList accessWindowsList;
protected final static boolean debug = gov.nasa.javaGenes.EOSscheduling.Debug.debug;

public void reinitialize() {
    super.reinitialize();
    accessWindowsList.reinitialize();
}
protected TaskWeight() {
}
/**
assumes Task requires SSR
*/
public TaskWeight(Task inTask,float priorityWeight,float numberOfAccessWindowsWeight,float SSRweight) {
    task = inTask;
    if (debug) {
        Error.assertTrue(task != null);
        Error.assertTrue(task.numberOfAccessWindows() > 0);
    }
    accessWindowsList = new AccessWindowsList();
    int SSRuse = ((TakeImage)task).getSSRuse();

    setWeight((float)(
          task.getPriority()*priorityWeight 
        + (1/(float)task.numberOfAccessWindows())*numberOfAccessWindowsWeight 
        + SSRuse*SSRweight
    ));
    for(int i = 0; i < task.numberOfAccessWindows(); i++)
        accessWindowsList.add(new AccessWindowWeight(this,task.getAccessWindow(i)));
    initializeWeightSum();
    reinitialize();
}

public Task getTask() {
    return task;
}
public AccessWindowsList getAccessWindowList() {
    return accessWindowsList;
}
public void scheduled(AccessWindowWeight aw) {
    removeFromWeightList();
}
public void unschedulable(AccessWindowWeight aw) {
    aw.removeFromWeightList();
    if (!accessWindowsList.more())
        removeFromWeightList();
}
public void initializeWeightSum() {
    accessWindowsList.initializeWeightSum();
}

}
