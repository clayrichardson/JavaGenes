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
import java.io.PrintWriter;

public class Satellite implements java.io.Serializable {
protected String name = "defaultSatellite";
protected int SSRcapacity;
protected SSRTimeline SSRtimeline = new SSRTimelineNone();
protected Vector sensors = new Vector();
protected Vector slewMotors = new Vector();
protected AccessWindow[] groundStationAccessWindows;
protected int number = -1;

public Satellite(String inName) {
    name = inName;
    Error.assertNotNull(name);
}
public String getName() {return name;}
public void setSSR(int inCapacity, Horizon horizon, int typicalTimeBetweenSSRdumps) {
    Error.assertTrue(inCapacity >= 0);
    SSRcapacity = inCapacity;
    SSRtimeline = new SSRTimeline(horizon,typicalTimeBetweenSSRdumps,this);
}
public int getSSRcapacity() {return SSRcapacity;}
public SSRTimeline getSSRtimeline(){return SSRtimeline;}
public void addSensor(Sensor s) {
    Error.assertNotNull(s);
    sensors.addElement(s);
}
public void addSlewMotor(SlewMotor s) {
    Error.assertNotNull(s);
    slewMotors.addElement(s);
}
public SlewMotor[] getSlewMotors() {
    return (SlewMotor[])slewMotors.toArray(new SlewMotor[slewMotors.size()]);
}
public Sensor[] getSensors() {
    return (Sensor[])sensors.toArray(new Sensor[sensors.size()]);
}
/**
must be called before setSSR
*/
public void generateGroundStationAccessWindows(STKAccessFile csvFile){
    groundStationAccessWindows = csvFile.readaTask();
    Error.assertTrue(groundStationAccessWindows != null && groundStationAccessWindows.length > 0);
}
/** for testing */
public void setGroundStationAccessWindows(AccessWindow[] accessWindows) {
    groundStationAccessWindows = accessWindows;
    Error.assertTrue(groundStationAccessWindows != null && groundStationAccessWindows.length > 0);
}
    
// TEMPORARY
public void initializeGroundStationAccess() {
    convertAllGroundStationAccessWindowsToSSRdump();
}
public void convertAllGroundStationAccessWindowsToSSRdump() {
    if (SSRtimeline instanceof SSRTimelineNone || groundStationAccessWindows == null)
        return;
    for(int i = 0; i < groundStationAccessWindows.length; i++) {
        AccessWindow window = groundStationAccessWindows[i];
        Horizon horizon = SSRtimeline.getHorizon();
        if (!horizon.includes(window)) {
            Error.warning("GS Window (" 
                + window.getStart() + "," + window.getEnd() + ") outside of horizon ("
                + horizon.getStart() + "," + horizon.getEnd());
            continue;
        }
        if (window.getDuration() <= 0) {
            Error.warning("Window (" 
                + window.getStart() + "," + window.getEnd() + ") has 0 duration.");
            continue;
        }
        SSRtimeline.insertDumpAt(window.getTimeAtMiddle());
    }
}
static public String getGroundStationReportHeader() {
    return "satellite\tstartTime\tendTime\n";
}
public void reportGroundStationAccessWindowsTo(PrintWriter out) {
    if (groundStationAccessWindows == null)
        return;
    for(int i = 0; i < groundStationAccessWindows.length; i++) {
        AccessWindow window = groundStationAccessWindows[i];
        out.println(name + "\t" + window.getStart() + "\t" + window.getEnd());
    }
}
public void setNumber(int i ) {number = i;}
public int getNumber() {return number;}
}

