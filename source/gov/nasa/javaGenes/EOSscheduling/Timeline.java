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
//  Created by Al Globus on Fri Jul 12 2002.
package gov.nasa.javaGenes.EOSscheduling;

import java.util.Vector;
import gov.nasa.alsUtility.Error;

/**
This class must be very fast.  Therefor, object generation is minimized here and in
all subclasses.
*/
abstract public class Timeline implements java.io.Serializable {
static protected NodeFactory nodeFactory = new NodeFactory(); 
static private Vector allTimelines = new Vector();

static public final int NOT_SCHEDULED = Scheduler.NOT_SCHEDULED;
static protected final boolean debug = Debug.debug;

// these are used together
protected Node nodeList; // double linked list for fast insert and delete
protected NodeArray nodeArray; // used to find the right node quickly

protected Horizon horizon = new Horizon();

public Horizon getHorizon() {return horizon;}
public int getStart() {return horizon.getStart();}
public int getEnd() {return horizon.getEnd();}
public boolean includes(int time) {return horizon.includes(time);}
/**
is the time covered by 'node' inside this?
*/
public boolean includes(Node node) {
    if (node == null)
        return false;
    return getStart() <= node.getStart() && node.getEnd() <= getEnd();
}

public void initialize() {
    // if you change this function check SSRTimeline carefully!
    setUpNodeList();
    if (nodeArray != null)
        nodeArray.initializeTo(getInitialNodeForNodeArray());
}
public Node getInitialNodeForNodeArray() {
    return nodeList;
}
public void setUpNodeList() {
    Error.notImplemented();
}
/**
must be called by all Timeline and subclass constructors reinitialize
timelines for next scheduling execution.
*/
public Timeline() {
    allTimelines.addElement(this);
}
public Timeline(Horizon inHorizon) {
    this();
    horizon = inHorizon.copy();
    Error.assertTrue(horizonIsValid());
}

abstract public int findEarliest(int start, int end, SchedulingData schedulingData);
/*
does the task implied in schedulingData fit in the timeline?
*/
abstract public boolean fits(int start, SchedulingData schedulingData);
abstract public void insertAt(int start, SchedulingData schedulingData);

public Node getFirstNode() {
    return nodeList;
}
public boolean horizonIsValid() {return horizon.isValid();}
static public void initializeAllTimelines() {
    nodeFactory.initialize();
    for(int i = 0; i < allTimelines.size(); i++)
        ((Timeline)allTimelines.elementAt(i)).initialize();
}
public void assertIsValid() {
    Error.assertTrue(horizonIsValid());
    int last = horizon.getStart() - 10; // 10 somewhat arbitrary
    for(Node node = nodeList; node != null; node = node.next()) {
        Error.assertTrue(last < node.getStart(),"last = " + last + ", startTime = " + node.getStart());
        last = node.getStart();
    }
}
public Node getNodeAtTime(int atTime) {
    Node node = nodeArray.getNode(atTime);
    while(node != null && !node.includes(atTime))
        node = node.next();
    return node;
}

}
