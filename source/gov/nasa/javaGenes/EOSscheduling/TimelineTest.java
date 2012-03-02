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
//  Created by Al Globus on Mon Sep 16 2002.
package gov.nasa.javaGenes.EOSscheduling;

import junit.framework.TestCase;
import gov.nasa.alsUtility.RandomNumber;

public class TimelineTest extends TestCase {
private AvailabilityTimeline timeline;
private Horizon horizon;
private final int typicalTaskDuration = 2;
private String time[] = {
    "1 Jan 2002 00:00:00.00",
    "1 Jan 2002 00:00:10.00",
    "2 Jan 2002 00:00:00.00",
    "3 Jan 2002 00:00:00.00"
};

protected void setUp() {
    horizon = new Horizon(time[0],time[1]);
    timeline = new AvailabilityTimeline(horizon,typicalTaskDuration);
    timeline.assertIsValid();
}

public TimelineTest(String name) {super(name);}

public void testGetNodeAtTime() {
    Node node = timeline.getFirstNode();
    assertTrue("1", null == timeline.getNodeAtTime(-1));
    assertTrue("2", null != timeline.getNodeAtTime(10)); // finds termination nodes at end of AvailabilityTimeline
    assertTrue("3", node == timeline.getNodeAtTime(0));
    assertTrue("4", node == timeline.getNodeAtTime(9));
    assertTrue("5", node == timeline.getNodeAtTime(4));
    assertTrue("6", null == timeline.getNodeAtTime(20)); 
}
public void testIncludes() {
    Node node = new Node();
    Node next = new Node();
    node.setNext(next);
    node.setStart(0);
    node.setEnd(10);
    assertTrue("1", timeline.includes(node));
    node.setEnd(11);
    assertTrue("2", !timeline.includes(node));
    node.setEnd(9);
    assertTrue("3", timeline.includes(node));
    node.setStart(-1);
    assertTrue("4", !timeline.includes(node));
    node.setStart(3);
    assertTrue("5", timeline.includes(node));
}
}
