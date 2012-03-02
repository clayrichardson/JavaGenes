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

package gov.nasa.javaGenes.EOSscheduling;

import gov.nasa.alsUtility.Error;
import java.io.PrintWriter;
import gov.nasa.alsUtility.IO;

public class SlewTimeline extends Timeline {

public SlewTimeline() {super();}

public SlewTimeline(Horizon inHorizon, int typicalSlewDuration) {
    super(inHorizon);
    nodeArray = new NodeArray(horizon, typicalSlewDuration);
    initialize();
}
// see comments for places the code depends on setting up the nodeList like this
public void setUpNodeList() {
    nodeList = nodeFactory.newSlewNode(new SlewNone(),false, horizon.getStart(),null,null);
    Node end = nodeFactory.newSlewNode(new SlewNone(), false, horizon.getEnd(),nodeList,null);
    nodeList.setNext(end);
    Node veryEnd = nodeFactory.newSlewNode(new SlewNone(),false,horizon.getEnd()+1,end,null);
    end.setNext(veryEnd);
}
/**
for test and debug
*/
public double[][] getSlewArray() {
    int count = 0;
    for(Node node = nodeList; node.next() != null; node = node.next())
        count++;
    double[][] a = new double[count][getNumberOfSlewParameters()];
    int i = 0;
    for(Node node = nodeList; node.next() != null; node = node.next(),i++) {
        SlewNode s = (SlewNode)node;
        for(int j = 0; j < s.numberOfParameters(); j++)
            a[i][j] = s.getParameter(j);
    }
    return a;
}
public boolean[] getRampArray() {
    int count = 0;
    for(Node node = nodeList; node.next() != null; node = node.next())
        count++;
    boolean[] a = new boolean[count];
    int i = 0;
    for(Node node = nodeList; node.next() != null; node = node.next(),i++) {
        SlewNode s = (SlewNode)node;
        a[i] = s.getMustRampToEnd();
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

public int getTotalSlewTime(Slewable slewable) {
    int totalSlewTime = 0;
    for(Node node = nodeList; node.next() != null; node = node.next()) {
        SlewRequirement first = ((SlewNode)node).getSlew();
        SlewRequirement second = ((SlewNode)node.next()).getSlew();
        totalSlewTime += slewable.slewTime(first,second);
    }
    return totalSlewTime;
}

public void printToTsd(String filename) {
    PrintWriter out = IO.getPrintWriter(filename);
    out.println("time\tramp\tparameters");
    for(Node node = nodeList; node.next() != null; node = node.next()) {
        SlewNode s = (SlewNode)node;
        int time = s.getStart();
        boolean mustRamp = s.getMustRampToEnd();
        double[] parameters = s.getParameters();
        out.print(time + "\t" + (mustRamp ? "1" : "0"));
        for(int i = 0; i < parameters.length; i++)
            out.print("\t" + parameters[i]);
        out.println();
    }
    out.close();
}
private int getNumberOfSlewParameters() {
    int howMany = ((SlewNode)nodeList.next()).numberOfParameters();
    if (howMany <= 0) return 1;
    return howMany;
}
public void assertIsValid() {
    super.assertIsValid();
    nodeArray.assertIsValid();
}

// cache -- makes this class extremely unthreadsafe
// cache nodes where task can start and end
private SlewNode lastStartNode;
private SlewNode lastEndNode; 
/**for testing only*/
public boolean isLastStartNode(SlewNode node) {return node == lastStartNode;}
/**for testing only*/
public boolean isLastEndNode(SlewNode node) {return node == lastEndNode;}
/**
for testing only
*/
protected void setLastNodes(Node start, Node end) {
    lastStartNode = (SlewNode)start;
    lastEndNode = (SlewNode)end;
}
public boolean fits(int start, SchedulingData schedulingData) {
    final int duration = schedulingData.getDuration();
    final int end = start + duration;
    SlewRequirement slew = schedulingData.getSlewRequirement();
    Slewable slewable = schedulingData.getSlewable();
    SlewNode startNode = (SlewNode)getNodeAtTime(start);
    if (startNode == null)
        return false;
    if (getEarliestStartTimeInNode(startNode,start,slew,slewable) != start)
        return false;
    SlewNode endNode = (SlewNode)getNodeAtTime(end);
    if (endNode == null)
        return false;
    if (incompatibleSlewInMiddle(slew,startNode,endNode,slewable))
        return false;
    if (endTimeOK(endNode,end,slew,slewable)) {
        lastStartNode = startNode;
        lastEndNode = endNode;
        return true;
    }
    return false;
}
public int findEarliest(int start,int end, SchedulingData schedulingData) {
    return findEarliest(start,end,schedulingData.getDuration(),schedulingData.getSlewRequirement(),schedulingData.getSlewable());
}
/**
This will happen a lot and must be very fast.  Avoid unnecessary tests and object creation.

@return time time >= start time <= end || NOT_SCHEDULED
*/
protected int findEarliest(int start, int end, int duration, SlewRequirement slew, Slewable slewable) {
    if (end - start < duration)
        return NOT_SCHEDULED;
    for(SlewNode startNode = (SlewNode)getNodeAtTime(start); startNode.getStart() < end; startNode = (SlewNode)startNode.next()) {
        int startTime = getEarliestStartTimeInNode(startNode,start,slew,slewable);
        if (startTime == NOT_SCHEDULED || startTime+duration > end)
            continue;
        int thisEnd = startTime+duration;
        SlewNode endNode = (SlewNode)getNodeAtTime(thisEnd);
        if (incompatibleSlewInMiddle(slew,startNode,endNode,slewable))
            continue;
        if (endTimeOK(endNode,thisEnd,slew,slewable)) {
            lastStartNode = startNode;
            lastEndNode = endNode;
            return startTime;
        }
    }
    return NOT_SCHEDULED;
}
protected boolean incompatibleSlewInMiddle(SlewRequirement slew, SlewNode start, SlewNode end,Slewable slewable) {
    if (start == end)
        return false;
    if (start.next() == end)
        return !slewable.equivalent(slew,end.getSlew());
    for(SlewNode node = (SlewNode)start.next(); node != (SlewNode)end.next(); node = (SlewNode)node.next())
        if (!slewable.equivalent(slew,node.getSlew()))
            return true;
    return false;
}
/**
@arg start -- only really of value the first time through the loop in which this is called. After
that it won't even be inside of the node.
*/
protected int getEarliestStartTimeInNode(SlewNode node, int start, SlewRequirement slew, Slewable slewable) {
    SlewRequirement nodeSlew = node.getSlew();
    if (slewable.equivalent(slew,nodeSlew))
        return Math.max(start,node.getStart());
    if (node.getMustRampToEnd())
        return NOT_SCHEDULED; // FUTURE BUG: change when ramping really happens, now it's steady
    int slewTime = slewable.slewTime(nodeSlew,slew);
    int earliestTime = node.getStart()+slewTime;
    if (earliestTime <= start) // SLOW: never true after the first time through the loop where called
        return start;
    if (earliestTime < node.getEnd())
        return earliestTime;
    return NOT_SCHEDULED;
}
protected boolean endTimeOK(SlewNode node,int time,SlewRequirement slew,Slewable slewable) {
    if (node == null)
        return false;
    if (debug) Error.assertTrue(node.includes(time));
    SlewRequirement thisSlew = node.getSlew();
    if (node.getMustRampToEnd())
        return slewable.equivalent(slew,thisSlew);
    SlewRequirement nextSlew = ((SlewNode)node.next()).getSlew();
    return time <= node.getEnd() - slewable.slewTime(slew,nextSlew); 
} 

/**
assumes the insertion is legal
*/
public void insertAt(int start, SchedulingData schedulingData) {
    insertAt(start,start+schedulingData.getDuration(),schedulingData.getSlewRequirement());
}
/**
assumes the insertion is legal. Puts in two slew nodes, one at startTime and the other at endTime with
the same slew requirement (assumes the slew doesn't change during the observation).
*/
protected void insertAt(int startTime, int endTime, SlewRequirement slew) {     
    if (debug) {
        Error.assertNotNull(lastStartNode);
        Error.assertNotNull(lastStartNode.getSlew());
        Error.assertTrue(lastStartNode.includes(startTime));
        Error.assertNotNull(lastEndNode);
        Error.assertNotNull(lastEndNode.getSlew());
        Error.assertTrue(lastEndNode.includes(endTime));
    }
    Node lastEndNodeNext = lastEndNode.next();
    // handle start node
    SlewNode currentStartNode;
    if (startTime == lastStartNode.getStart()) {
        lastStartNode.setSlew(slew);
        currentStartNode = lastStartNode;
    } else {
        currentStartNode = nodeFactory.newSlewNode(slew,true,startTime,lastStartNode,null);
        lastStartNode.setNext(currentStartNode);
    }
    // handle end node
    SlewNode e;
    if (lastStartNode == lastEndNode && lastEndNode.getStart() == endTime)
        e = lastEndNode; // slew must be correct already
    else
        e = nodeFactory.newSlewNode(slew,lastEndNode.getMustRampToEnd(),endTime,null,lastEndNodeNext);

    // clean up loose ends
    e.setPrevious(currentStartNode);
    currentStartNode.setNext(e);
    currentStartNode.setMustRampToEnd(true);
    nodeArray.updateWith(currentStartNode);
    nodeArray.updateWith(e);
    lastStartNode = null;
    lastEndNode = null;
}
}

