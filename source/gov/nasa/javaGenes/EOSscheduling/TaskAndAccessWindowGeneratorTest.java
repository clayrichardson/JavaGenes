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
//  Created by Al Globus on Wed Jul 31 2002.
package gov.nasa.javaGenes.EOSscheduling;

import junit.framework.TestCase;
import gov.nasa.alsUtility.Utility;

public class TaskAndAccessWindowGeneratorTest extends TestCase {
private String filename = "TEMP.TaskAndAccessWindowGeneratorTest";
private java.io.PrintWriter file;
private int timeBetweenSamples = 60; // seconds
private int numberOfTasks = 6;
private int[] numberOfAccessWindows = {2,0,2,0,0,3};

private int windowDuration;
private STKAccessFile stk;
private EOSModel model;
private SensorType st;
private Satellite sat;
private Sensor sensor;

public TaskAndAccessWindowGeneratorTest(String name) {super(name);}
protected void setUp() {
    Utility.makeFile(filename,"");
    file = Utility.outputFile(filename);
    file.println("\"Start\",\"Stop\"");
    file.println("1 Jan 2001 00:00:00.00,7 Jan 2001 00:00:00.00");
    file.println("\"Time (UTCG)\",\"Azimuth (deg)\",\"Elevation (deg)\",\"Range (km)\"");
    file.println("1 Jan 2001 00:00:00.00,66.169,-33.800,778.235785");
    file.println("1 Jan 2001 00:01:00.00,105.557,-33.944,774.580862");
    file.println("1 Jan 2001 00:02:00.00,133.091,-27.548,998.493583");
    file.println("1 Jan 2001 00:03:00.00,146.905,-22.870,1339.576593");
    file.println("1 Jan 2001 00:04:00.00,154.310,-20.617,1728.662499");
    file.println("1 Jan 2001 00:05:00.00,158.757,-19.835,2138.318245");
    file.println("1 Jan 2001 00:05:22.00,159.169,-19.784,2296.190120");
    file.println("1 Jan 2001 11:29:25.24,8.851,-19.784,2296.331760");
    file.println("1 Jan 2001 11:30:25.00,10.041,-20.229,1864.332327");
    file.println("1 Jan 2001 11:31:25.00,12.047,-22.112,1434.908833");
    file.println("1 Jan 2001 11:32:25.24,16.851,-27.146,1018.730618");
    file.println("");
    file.println("");
    file.println("\"Time (UTCG)\",\"Azimuth (deg)\",\"Elevation (deg)\",\"Range (km)\"");
    file.println("1 Jan 2001 00:00:00.00,66.169,-33.800,778.235785");
    file.println("1 Jan 2001 00:01:00.00,105.557,-33.944,774.580862");
    file.println("1 Jan 2001 00:02:00.00,133.091,-27.548,998.493583");
    file.println("1 Jan 2001 00:03:00.00,146.905,-22.870,1339.576593");
    file.println("1 Jan 2001 00:04:00.00,154.310,-20.617,1728.662499");
    file.println("1 Jan 2001 00:05:00.00,158.757,-19.835,2138.318245");
    file.println("1 Jan 2001 00:05:22.00,159.169,-19.784,2296.190120");
    file.println("1 Jan 2001 11:29:25.24,8.851,-19.784,2296.331760");
    file.println("1 Jan 2001 11:30:25.00,10.041,-20.229,1864.332327");
    file.println("");
    file.println("");
    file.println("");
    file.println("\"Time (UTCG)\",\"Azimuth (deg)\",\"Elevation (deg)\",\"Range (km)\"");
    file.println("1 Jan 2001 00:00:00.00,66.169,-33.800,778.235785");
    file.println("1 Jan 2001 00:01:00.00,105.557,-33.944,774.580862");
    file.println("1 Jan 2001 00:02:00.00,133.091,-27.548,998.493583");
    file.println("1 Jan 2001 00:03:00.00,146.905,-22.870,1339.576593");
    file.println("1 Jan 2001 00:04:00.00,154.310,-20.617,1728.662499");
    file.println("1 Jan 2001 00:05:00.00,158.757,-19.835,2138.318245");
    file.println("1 Jan 2001 00:05:22.00,159.169,-19.784,2296.190120");
    file.println("1 Jan 2001 11:29:25.24,8.851,-19.784,2296.331760");
    file.println("1 Jan 2001 11:30:25.00,10.041,-20.229,1864.332327");
    file.println("2 Jan 2001 11:29:25.24,8.851,-19.784,2296.331760");
    file.println("2 Jan 2001 11:30:25.00,10.041,-20.229,1864.332327");
    file.println("3 Jan 2001 11:29:25.24,8.851,-19.784,2296.331760");
    file.println("3 Jan 2001 11:29:30.00,10.041,-20.229,1864.332327"); // shorter than windowDuration = 24
    file.println("");
    file.close();

    windowDuration = 24;
    stk = new STKAccessFile(filename,timeBetweenSamples);
    model = new EOSModel(stk.getHorizon());
    st = new SensorType("aSensorType");
    sat = new Satellite("aSatellite");
    model.addSatellite(sat);
    sensor = new Sensor(sat,st,model.getHorizon(),windowDuration);
}
protected void tearDown() {
    Utility.deleteFile(filename);
}

public void testGenerateOne() {
    TaskAndAccessWindowGenerator.generateTakeImages(model,windowDuration,sensor,0,stk);
    assertTrue("numberOfTasks",model.getNumberOfTasks() == numberOfTasks);
    for(int i = 0; i < numberOfAccessWindows.length; i++) {
        assertTrue(i+"",numberOfAccessWindows[i] == model.getTask(i).getAccessWindows().length);
        AccessWindow[] ws = model.getTask(i).getAccessWindows();
        for(int j = 0; j < ws.length; j++) {
            assertTrue(i+" "+j,ws[j].getDuration() == windowDuration);
            assertTrue(i+" "+j,ws[j].getSlewRequirement() != null);
        }
    }
}

public void testGenerateMany() {
    Sensor[] sensors = {sensor,sensor,sensor};
    STKAccessFile[] stks = {
        new STKAccessFile(filename,timeBetweenSamples),
        new STKAccessFile(filename,timeBetweenSamples),
        new STKAccessFile(filename,timeBetweenSamples)
    };
    TaskAndAccessWindowGenerator.generateTakeImages(model,windowDuration,sensors,0,stks);
    assertTrue("numberOfTasks",model.getNumberOfTasks() == numberOfTasks);
    for(int i = 0; i < numberOfAccessWindows.length; i++) {
        assertTrue(i+"m",numberOfAccessWindows[i]*3 == model.getTask(i).getAccessWindows().length);
        AccessWindow[] ws = model.getTask(i).getAccessWindows();
        for(int j = 0; j < ws.length; j++) {
            assertTrue(i+" m "+ j,ws[j].getDuration() == windowDuration);
            assertTrue(i+" m "+j,ws[j].getSlewRequirement() != null);
        }
    }
}
}
