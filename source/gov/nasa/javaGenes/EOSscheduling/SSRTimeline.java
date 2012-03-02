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
//  Created by Al Globus on Mon Nov 18 2002.
package gov.nasa.javaGenes.EOSscheduling;

import gov.nasa.alsUtility.Error;
import java.io.PrintWriter;
import gov.nasa.alsUtility.IO;

public class SSRTimeline extends Timeline {
protected Satellite satellite;
protected int SSRcapacity;
private boolean firstSetUpNodeList = true;
private Node dummyLastNode;

public SSRTimeline() {super();}

public SSRTimeline(Horizon inHorizon,int typicalTimeBetweenDumps,Satellite inSatellite) {
    super(inHorizon);
    satellite = inSatellite;
    SSRcapacity = satellite.getSSRcapacity();
    Error.assertTrue(satellite != null);
    nodeArray = new NodeArray(horizon, typicalTimeBetweenDumps);
    initialize();
}
// see comments for places the code depends on setting up the nodeList like this
public void setUpNodeList() {
    nodeList = nodeFactory.newSSRNode(SSRcapacity,horizon.getStart(),null,null);
    dummyLastNode = nodeFactory.newSSRNode(SSRcapacity, horizon.getEnd(),nodeList,null);
    nodeList.setNext(dummyLastNode);
    firstSetUpNodeList = false;
}
public int getNumberOfSSRsegments() {
    int count = 0;
    for(Node node = nodeList; node != null; node = node.next())
        count++;
    return count-1; // account for dummy node at end
}
/**
for test and debug
*/
public double[] getCapacityArray() {
    int count = 0;
    for(Node node = nodeList; node.next() != null; node = node.next())
        count++;
    double[] a = new double[count];
    int i = 0;
    for(Node node = nodeList; node.next() != null; node = node.next(),i++) {
        SSRNode s = (SSRNode)node;
        a[i] = s.getRemainingCapacity();
    }
    return a;
}
public int[] getStartTimesArray() {
    int count = 0;
    for(Node node = nodeList; node.next() != null; node = node.next())
        count++;
    int[] a = new int[count];
    int i = 0;
    for(Node node = nodeList; node.next() != null; node = node.next(),i++) 
        a[i] = node.getStart();
    return a;
}
public void printToTsd(String filename) {
    PrintWriter out = IO.getPrintWriter(filename);
    out.println("time\tcapacity");
    for(Node node = nodeList; node.next() != null; node = node.next()) {
        SSRNode s = (SSRNode)node;
        out.print(s.getStart() + "\t" + s.getRemainingCapacity());
        out.println();
    }
    out.close();
}
public void assertIsValid() {
    super.assertIsValid();
    nodeArray.assertIsValid();
    for(Node node = nodeList; node != null; node = node.next()) {
        SSRNode s = (SSRNode)node;
        double capacity = s.getRemainingCapacity();
        Error.assertTrue(capacity >= 0);
    }
}
public boolean fits(int start, SchedulingData schedulingData) {
    final int duration = schedulingData.getDuration();
    SSRNode node = (SSRNode)getNodeAtTime(start);
    return node != null && node.getRemainingCapacity() >= schedulingData.getSSRuse();
}
/**
SSRuse is modelled as instantaneous.  That means that SSR constraints won't apply at the
moment of download.
*/
public int findEarliest(int start,int end,SchedulingData schedulingData) {
    double SSRuse = schedulingData.getSSRuse();
    if (Debug.debug) 
        Error.assertTrue(SSRuse >= 0);
    if (SSRuse == 0)
        return start;
    return findEarliest(start,end,SSRuse);
}
/**
This will happen a lot and must be very fast.  Avoid unnecessary tests and object creation.

@return time time >= start time < end || NOT_SCHEDULED
*/
protected int findEarliest(int start, int end, double SSRuse) {
    for(Node node = getNodeAtTime(start); node.getStart() < end; node = node.next()) {
        SSRNode s = (SSRNode)node;
        if (s.getRemainingCapacity() >= SSRuse)
            return Math.max(start,s.getStart());
       }
    return NOT_SCHEDULED;
}

/**
assumes the SSR capacity is available
*/
public void insertAt(int start, SchedulingData schedulingData) {
    ((SSRNode)getNodeAtTime(start)).removeCapacity(schedulingData.getSSRuse());
}
/**
assumes SSR is completely emptied.  Doesn't propogate effects!  Meant to be used before
any scheduling happens.
*/
public void insertDumpAt(int time) {
    Node previous = getNodeAtTime(time);
    if (previous.getStart() == time)
        return;
    Node next = previous.next();
    SSRNode node = nodeFactory.newSSRNode(SSRcapacity,time,previous,next);
    previous.setNext(node);
    next.setPrevious(node);
    nodeArray.updateWith(node);
}
}


