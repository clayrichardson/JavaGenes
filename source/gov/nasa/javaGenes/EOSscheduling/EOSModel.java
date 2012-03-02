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

import java.util.Vector;
import gov.nasa.alsUtility.ExtendedVector;
import gov.nasa.alsUtility.Utility;
import java.io.PrintWriter;
import gov.nasa.alsUtility.IO;
import gov.nasa.alsUtility.Error;

public class EOSModel implements java.io.Serializable {
protected ExtendedVector tasks = new ExtendedVector();
protected Vector satellites = new Vector();
protected Horizon horizon;
public EOSModel() {}
public EOSModel(Horizon inHorizon) {
    setHorizon(inHorizon);
}
public Vector getSensors() {
    Vector allSensors = new Vector();
    for(int i = 0; i < satellites.size(); i++) {
        Sensor[] sensors = getSatellite(i).getSensors();
        for(int j = 0; j < sensors.length; j++)
            allSensors.add(sensors[j]);
    }
    return allSensors;
}
public String toString() {return "EOSModel";}
public void beginScheduling() {
    createFreshTimelines();
    for(int i = 0; i < satellites.size(); i++) {
        Satellite satellite = (Satellite)satellites.elementAt(i);
        satellite.initializeGroundStationAccess();
    }
}
public void endScheduling() {
    doneWithTimelines();
}
protected void createFreshTimelines() {Timeline.initializeAllTimelines();}
protected void doneWithTimelines() {}

public Satellite getSatellite(int index) {return (Satellite)satellites.get(index);}
public void addSatellite(Satellite s) {satellites.addElement(s);}
public Satellite[] getSatellites() {return (Satellite[])satellites.toArray(new Satellite[satellites.size()]);}
public int getNumberOfSatellites() {return satellites.size();}
public void numberSatellites() {
    for(int i = 0; i < satellites.size(); i++)
        getSatellite(i).setNumber(i);
}

public void addTask(Task task) {tasks.addElement(task);}
public Task getTask(int index) {return (Task)tasks.elementAt(index);}
public int getNumberOfTasks() {return tasks.size();}
public void numberTasksAndAccessWindows() {
    for(int i = 0;i < tasks.size(); i++)
        getTask(i).setNumbers(i);
}

public void printUnexecutableTasks(String filename) {
    PrintWriter taskFile = IO.getPrintWriter(filename);
    for(int i = 0;i < tasks.size(); i++)
        if (!getTask(i).isExecutable())
            taskFile.println(i+"");
    taskFile.close();
}
public void removeUnexecutableTasks() {
    for(int i = 0;i < tasks.size(); i++)
        if (!getTask(i).isExecutable()) {
            tasks.removeElementAt(i);
            i--;
        }
}
public void setTaskPriorities(double startPriority, double priorityIncrement, int tasksPerIncrement) {
    Error.assertTrue(tasksPerIncrement > 0);
    double currentPriority = startPriority;
    double taskCount = 0;
    for(int i = 0; i < getNumberOfTasks(); i++) {
        if (taskCount >= tasksPerIncrement) {
            taskCount = 0;
            currentPriority += priorityIncrement;
        }
        Task task = getTask(i);
        getTask(i).setPriority(currentPriority);
        taskCount++;
    }
}
public double getTaskPrioritySum() {
    double sum = 0;
    for(int i = 0; i < getNumberOfTasks(); i++) 
        sum += getTask(i).getPriority();
    return sum;
}  
public void setHorizon(Horizon inHorizon) {horizon = inHorizon.copy();}
/**
@return the horizon for this model. Never modify it.
*/
public Horizon getHorizon() {return horizon;}
public void report(String directoryName) {
    Utility.makeDirectory(directoryName);
    final String sep = Utility.fileSeparator();
    PrintWriter tasksP = IO.getPrintWriter(directoryName + sep + "tasks.tsd");
    tasksP.println(Task.getReportHeader());
    PrintWriter windows = IO.getPrintWriter(directoryName + sep + "accessWindows.tsd");
    windows.println(AccessWindow.getReportHeader());
    for(int i = 0; i < tasks.size(); i++) {
        Task t = (Task)tasks.elementAt(i);
        t.reportTo(tasksP);
        t.reportAccessWindowsWindowsTo(i,windows);
    }
    tasksP.close();
    windows.close();
    
    PrintWriter groundStationFile = IO.getPrintWriter(directoryName + sep + "groundStationAccess.tsd");
    groundStationFile.println(Satellite.getGroundStationReportHeader());
    for(int i = 0; i < satellites.size(); i++) {
        Satellite satellite = (Satellite)satellites.elementAt(i);
        satellite.reportGroundStationAccessWindowsTo(groundStationFile);
    }
    groundStationFile.close();
}
}
