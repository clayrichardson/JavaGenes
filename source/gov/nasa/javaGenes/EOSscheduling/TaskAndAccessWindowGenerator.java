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
//  Created by Al Globus on Mon Jul 08 2002.
package gov.nasa.javaGenes.EOSscheduling;

import gov.nasa.alsUtility.Error;
import java.util.Vector;

public class TaskAndAccessWindowGenerator implements java.io.Serializable {
/**
@arg windowDuration all windows will be of almost exactly this length.  Shorter ones will be discarded. 
Longer ones will be shrunk to this length with the center staying the same.
@arg csvFile STK access file of satellite to point targets.  Assume the real target is centered on the point
and exactly big enough to take windowDuration time to image with a sensor having no along-track slewing.
*/
static public void generateTakeImages(EOSModel model, int windowDuration, Sensor sensor, int taskSSRuse, STKAccessFile csvFile) {
    Sensor[] sensors = {sensor};
    STKAccessFile[] files = {csvFile};
    generateTakeImages(model,windowDuration,sensors,taskSSRuse,files);
}
/**
for tasks with no SSR use.  Avoids needing changes to existing regression test.
*/
static public void generateTakeImages(EOSModel model, int windowDuration, Sensor sensors,  STKAccessFile csvFiles) {
    generateTakeImages(model,windowDuration,sensors,0,csvFiles);
}
/**
The sensors and csvFiles arrays are parallel with the accesses for each sensor in the corresponding file.

@arg sensors must all be of same type.
@arg csvFiles must all have the same number of takeImages (targets) in them.  STK puts out blank lines when
a target never sees a sensor so STKAccessFile.java can deal with this.
*/
static public void generateTakeImages(EOSModel model, int windowDuration, Sensor[] sensors, int taskSSRuse, STKAccessFile[] csvFiles) {
    Error.assertTrue(sensors.length == csvFiles.length);
    Error.assertTrue(sensors.length > 0);
    SensorType sensorType = sensors[0].getSensorType();
    for(int i = 0; i < sensors.length; i++)
        Error.assertTrue(sensors[i].getSensorType().equals(sensorType), "not all sensors of same type, found at index = " + i);
    
    TaskAndAccessWindowGenerator generator = new TaskAndAccessWindowGenerator(model.getHorizon(),windowDuration,taskSSRuse);
    for(int i = 0; i < sensors.length; i++)
        generator.generate(i == 0,sensors[i],csvFiles[i]);
    Vector allTakeImages = generator.getAllTakeImages();
    for(int i = 0; i < allTakeImages.size(); i++) {
        TakeImage t = (TakeImage)allTakeImages.elementAt(i);
        t.hasAllAccessWindowsNow();
        model.addTask(t);
    }
}

protected Horizon horizon;
protected int windowDuration;
protected Vector allTakeImages = new Vector();
protected int taskSSRuse;
protected TaskAndAccessWindowGenerator(Horizon inHorizon, int inWindowDuration, int inTaskSSRuse) {
    horizon = inHorizon;
    windowDuration = inWindowDuration;
    taskSSRuse = inTaskSSRuse;
    Error.assertTrue(taskSSRuse >= 0);
}
protected Vector getAllTakeImages() {return allTakeImages;}
protected void generate(boolean firstTime, Sensor sensor, STKAccessFile csvFile){
    for(int taskCount = 0; true; taskCount++) {
        AccessWindow[] windows = csvFile.readaTask();
        if (windows == null) {
            Error.assertTrue(taskCount == allTakeImages.size());
            return;
        }

        TakeImage takeImage;
        if (firstTime) {
            takeImage = new TakeImage(windowDuration,sensor.getSensorType(),taskSSRuse);
            allTakeImages.addElement(takeImage);
        } else
            takeImage = (TakeImage)allTakeImages.elementAt(taskCount);

        if (windows.length == 0)
            continue;
        for(int i = 0; i < windows.length; i++) {
            AccessWindow window = windows[i];
             if (!horizon.includes(window)) {
                Error.warning("Window (" 
                    + window.getStart() + "," + window.getEnd() + ") outside of horizon ("
                    + horizon.getStart() + "," + horizon.getEnd() + ") found in "
                    + csvFile.getFilename() + " near line " 
                    + csvFile.getCurrentLineNumber());
                continue;
            }
            if (window.getDuration() <= 0) {
                Error.warning("Window (" 
                    + window.getStart() + "," + window.getEnd() + ") has 0 duration. Found in "
                    + csvFile.getFilename() + " near line " 
                    + csvFile.getCurrentLineNumber());
                continue;
            }
            window.shrinkAroundMiddle(windowDuration);
            if (window.getDuration() == windowDuration) {
                window.setSensor(sensor);
                window.setSlewingToLargestPointing(sensor);
                window.deletePointingData();
                takeImage.addAccessWindow(window);
            }
        }
    }
}
}
