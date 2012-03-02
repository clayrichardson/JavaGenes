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
//  Created by Al Globus on Fri Jul 05 2002.
package gov.nasa.javaGenes.EOSscheduling;

import gov.nasa.alsUtility.Error;
import java.lang.Math;
import java.io.PrintWriter;
import gov.nasa.alsUtility.IO;
import java.lang.Math;

/**
when using duty cycle constraints, all task durations must fill the window
to schedule.  E.g., can't handle agile satellites
*/
public class AvailabilityTimeline extends Timeline {
protected DutyCycleConstraint dutyCycle[];
protected boolean[] dutyCycleMask;

private AvailableNode lastNodeFound; // OPTIMIZATION: cache node where insertion will happen. Set by time finding routines

public AvailabilityTimeline() {super();}

public AvailabilityTimeline(Horizon inHorizon, int inTypicalTaskDuration) {
    super(inHorizon);
    nodeArray = new NodeArray(horizon,inTypicalTaskDuration);
    initialize();
}
public void initialize() {
    super.initialize();
    if (dutyCycle != null) {
        for(int i = 0; i < dutyCycle.length; i++)
            dutyCycle[i].initialize();
    }
}

public void setDutyCycles(DutyCycleConstraint[] inDutyCycles) {
    dutyCycle = inDutyCycles;
    if (dutyCycle == null) // used in testing
        return;
    dutyCycleMask = new boolean[dutyCycle.length];
}
public Node getInitialNodeForNodeArray() {
    return nodeList.next();
}
// see comments for places the code depends on setting up the nodeList like this
public void setUpNodeList() {
    Node allTime = nodeFactory.newAvailableNode(horizon.getStart(),null,null);
    nodeList = nodeFactory.newUnAvailableNode(horizon.getStart()-1,null,allTime);
    Node end = nodeFactory.newUnAvailableNode(horizon.getEnd(),allTime,null);
    Node veryEnd = nodeFactory.newUnAvailableNode(horizon.getEnd()+1,end,null); // helps with loop termination conditions
    end.setNext(veryEnd);
    allTime.setPrevious(nodeList);
    allTime.setNext(end);
}
public Node getFirstNode() {return (AvailableNode)nodeList.next();}
public AvailableNode getAvailableNodeAtOrAfter(int atTime) {
    AvailableNode node = (AvailableNode)nodeArray.getNode(atTime);
    while(true) {
        if (node == null)
            return null;
        if (node.isAvailable() && node.getEnd() > atTime) 
            return node;
        node = (AvailableNode)node.next();
    }
}
/**
for test and debug
*/
public int[] getStartTimesArray() {
    int count = 0;
    for(Node node = nodeList; node != null; node = node.next())
        count++;
    int[] a = new int[count];
    int i = 0;
    for(Node node = nodeList; node != null; node = node.next())
        a[i++] = node.getStart();
    return a;
}

public void assertIsValid() {
    super.assertIsValid();
    // all except last node should alternate available and unavailable.  See setUpNodeList()
    boolean lastAvailable = true; // first node is unavailable (see assert in loop)
    for(AvailableNode node = (AvailableNode)nodeList; node.next() != null; node = (AvailableNode)node.next()) {
        Error.assertTrue(node.isAvailable() != lastAvailable);
        lastAvailable = node.isAvailable();
    }
    nodeArray.assertIsValid();
}
// used for testing
public boolean fitsDutyCycles() {
    if (dutyCycle == null)
        return true;
    for(int i = 0; i < dutyCycle.length; i++)
        if (!dutyCycle[i].fits(this))
            return false;
    return true;
}
/**
This will happen a lot and must be very fast.  Avoid unnecessary tests and object creation.

@window time time >= start time <= end. Must be within the horizon as well.
*/
public int insertEarliest(int start, int end, int duration) {
    int time = findEarliest(start,end,duration);
    if (time != NOT_SCHEDULED)
        insertAt(lastNodeFound,time,time+duration);
    return time;
}
public boolean fits(int start, SchedulingData schedulingData) {
    final int duration = schedulingData.getDuration();
    return fits(start, duration);
}
public boolean fits(int start, int duration) {
    final int end = start + duration;
    lastNodeFound = getAvailableNodeAtOrAfter(start); 
    if (lastNodeFound == null || start < lastNodeFound.getStart() || lastNodeFound.getEnd() < end)
        return false;
    return lastNodeFound.isDurationAvailable(start,duration) && fitsDutyCycles(lastNodeFound,start,duration);
}
public int findEarliest(int start,int end, SchedulingData schedulingData) {
    return findEarliest(start,end,schedulingData.getDuration());
}
protected int findEarliest(int start, int end, int duration) {
    int endTime = Math.min(horizon.getEnd(),end);
    if (start+duration > endTime)
        return NOT_SCHEDULED;

    AvailableNode node = getAvailableNodeAtOrAfter(start); 
    if (node == null || node.getStart() + duration > endTime)
        return NOT_SCHEDULED;
    int startTime = Math.max(start,node.getStart());
    while(true) {
        if (node.isDurationAvailable(startTime,duration) && fitsDutyCycles(node,startTime,duration)) {
            lastNodeFound = node;
            return startTime;
        }
        do {
            node = (AvailableNode)node.next();
            if (node.getStart()+duration > endTime) // see setUpNodeList() to see that this end condition works
                return NOT_SCHEDULED; 
        } while (!node.isAvailable());
        startTime = node.getStart();
    }
}
/**
is this time and duration compatible with the duty cylce constraint (if any).  Note: there is an assumption
in the calling code that durations always fill the entire access window (true for non-agile spacecraft). 
@arg node the times should fit in node, just here for optimization
*/
protected boolean fitsDutyCycles(AvailableNode node, int startTime, int duration) { 
    if (dutyCycle == null)
        return true;
    for(int i = 0; i < dutyCycleMask.length; i++)
        dutyCycleMask[i] = false;
    boolean moreTests = false;
    for(int i = 0; i < dutyCycle.length; i++) 
        if (!dutyCycle[i].fastFitCheck(startTime,duration)) {
            dutyCycleMask[i] = true;
            moreTests = true;
        }
    if (!moreTests)
        return true;
    for(int i = 0; i < dutyCycleMask.length; i++)
        if (dutyCycleMask[i] && !dutyCycle[i].fits(this,node,startTime,duration))
                return false;
    return true;
}
/*
for testing only, when you don't want to do the precheck
protected boolean fitsDutyCycles(AvailableNode node, int startTime, int duration) { 
    if (dutyCycle == null)
        return true;
    for(int i = 0; i < dutyCycle.length; i++)
        if (!dutyCycle[i].fits(this,node,startTime,duration))
                return false;
    return true;
}
*/
/**
assumes the insertion is legal
*/
public void insertAt(int start, SchedulingData schedulingData) {
    insertAt(start, start+schedulingData.getDuration());
}
/**
assumes the insertion is legal
*/
protected void insertAt(int startTime, int endTime) {
    insertAt(lastNodeFound,startTime,endTime);
    lastNodeFound = null;
}
/**
assumes the insertion is legal
*/
protected void insertAt(Node node, int startTime, int endTime) {
    if (debug) {
        Error.assertTrue(startTime >= node.getStart());
        Error.assertTrue(endTime <= node.getEnd());
    }
    if (dutyCycle != null)
        for(int i = 0; i < dutyCycle.length; i++)
            dutyCycle[i].insertAt(startTime,endTime);

    if (startTime == node.getStart()) {
        if (endTime == node.getEnd()) {
            deleteNodeAndNextNode(node);
            return;
        }
        node.setStart(endTime);
        nodeArray.updateWith(startTime,endTime,node.previous());
        return;
    }

    if (endTime == node.getEnd()) {
        node.setEnd(startTime);
        nodeArray.updateWith(startTime,endTime,node.next());
        return;
    }

    // otherwise need new unavailable node in the middle of available space (need new available node too)
    Node newAvailable = nodeFactory.newAvailableNode(node.getStart(),node.previous(),null); // first node
    Node newUnAvailable = nodeFactory.newUnAvailableNode(startTime,null,node);

    node.previous().setNext(newAvailable);
    newAvailable.setNext(newUnAvailable);
    newUnAvailable.setPrevious(newAvailable);

    node.setPrevious(newUnAvailable);
    node.setStart(endTime);

    nodeArray.updateWith(newAvailable);
    nodeArray.updateWith(newUnAvailable);
}
protected void deleteNodeAndNextNode(Node node) {
    Node next = node.next();
    Node previous = node.previous();
    int startTime = node.getStart();
    int endTime = next.getEnd();
    if (endTime > horizon.getEnd()) // can happen if second to last node is deleted
        endTime = horizon.getEnd();
    node.previous().setNext(next.next());
    next.next().setPrevious(node.previous());
    nodeArray.updateWith(startTime,endTime,previous);
}
public void printToTsd(String filename) {
    PrintWriter out = IO.getPrintWriter(filename);
    out.println("time\tinUse");
    for(Node node = getFirstNode(); node.next().next() != null; node = node.next()) {
        AvailableNode s = (AvailableNode)node;
        int time = s.getStart();
        boolean available = s.isAvailable();
        out.println(time + "\t" + (available ? "0" : "1"));
    }
    out.close();
}
}
