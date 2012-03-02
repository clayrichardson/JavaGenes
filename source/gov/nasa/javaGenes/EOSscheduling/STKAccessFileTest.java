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
//  Created by Al Globus on Mon Jul 29 2002.
package gov.nasa.javaGenes.EOSscheduling;

import junit.framework.TestCase;
import gov.nasa.alsUtility.Utility;

public class STKAccessFileTest extends TestCase {
private String filename = "TEMP.STKAccessFileTest";
private java.io.PrintWriter file;
private int timeBetweenSamples = 60; // seconds

public STKAccessFileTest(String name) {super(name);}
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
    file.println("");
    file.println("");
    file.close();
}
protected void tearDown() {
    Utility.deleteFile(filename);
}

public void testSTKAccessFile() {
    STKAccessFile stk = new STKAccessFile(filename,timeBetweenSamples);
    Horizon horizon = stk.getHorizon();
    assertTrue("horizon duration", horizon.getDuration() == 6*60*60*24);
    assertTrue("line number", stk.getCurrentLineNumber() == 2);
    assertTrue("filename", stk.getFilename().equals(filename));

    AccessWindow[] w = stk.readaTask();
    assertTrue("w length", w.length == 2);
    check("w[0]",w[0],0,5*60 + 22);
    check("w[1]",w[1],11*60*60 + 29*60 + 25,3*60);

    w = stk.readaTask();
    assertTrue("0w length", w.length == 0);

    w = stk.readaTask();
    assertTrue("2w length", w.length == 2);
    check("2 w[0]",w[0],0,5*60 + 22);
    check("2 w[1]",w[1],11*60*60 + 29*60 + 25,1*60);

    w = stk.readaTask();
    assertTrue("4w length", w.length == 0);

    w = stk.readaTask();
    assertTrue("5w length", w.length == 0);

    w = stk.readaTask();
    assertTrue("7w length", w.length == 3);
    check("7 w[0]",w[0],0,5*60 + 22);
    check("7 w[1]",w[1],11*60*60 + 29*60 + 25,1*60);
    check("7 w[2]",w[2],35*60*60 + 29*60 + 25,1*60);

    w = stk.readaTask();
    assertTrue("3w length", w.length == 0);

    assertTrue(stk.readaTask() == null);
}
private void check(String name, AccessWindow w, int start, int duration) {
    assertTrue(name + " start",w.getStart() == start);
    assertTrue(name + " duration",w.getDuration() == duration);
    assertTrue(name + " end",w.getEnd() == start + duration);
}
}
