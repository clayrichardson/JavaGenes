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
//  Created by Al Globus on Tue May 06 2003.
package gov.nasa.javaGenes.EOSscheduling;

import junit.framework.TestCase;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.Error;

/**
RESTRICTION: the dutyCylce constraint assumes that the task duration is equal to the access window -- i.e, there is no point in trying to move the task around in time.  This was done to simplify implementation since the more general case may never be an issue for this project.
*/
public class DutyCycleConstraintTest extends TestCase {
private AvailabilityTimeline timeline;
private Horizon horizon;
private final int typicalTaskDuration = 2;
private String time[] = {
    "1 Jan 2002 00:00:00.00",
    "1 Jan 2002 00:00:10.00",
    "2 Jan 2002 00:00:00.00",
    "3 Jan 2002 00:00:00.00",
    "4 Jan 2002 00:00:00.00"
};

public DutyCycleConstraintTest(String name) {super(name);}
protected void setUp() {
    horizon = new Horizon(time[0],time[2]);
    timeline = new AvailabilityTimeline(horizon,typicalTaskDuration);
    timeline.assertIsValid();
    timeline.insertAt(timeline.getAvailableNodeAtOrAfter(400),400,500);
    timeline.insertAt(timeline.getAvailableNodeAtOrAfter(700),700,800);
    timeline.insertAt(timeline.getAvailableNodeAtOrAfter(900),900,950);
    timeline.insertAt(timeline.getAvailableNodeAtOrAfter(975),975,1000);
    timeline.insertAt(timeline.getAvailableNodeAtOrAfter(1100),1100,1200);
    timeline.insertAt(timeline.getAvailableNodeAtOrAfter(1400),1400,1600);
    timeline.assertIsValid();
}
public void testFastVsSlowBug() {
    horizon = new Horizon(time[0],time[3]);
    timeline = new AvailabilityTimeline(horizon,typicalTaskDuration);
    timeline.assertIsValid();
    timeline.insertAt(timeline.getAvailableNodeAtOrAfter(164105),164105,164106);
    timeline.insertAt(timeline.getAvailableNodeAtOrAfter(170232),170232,170301);
    timeline.insertAt(timeline.getAvailableNodeAtOrAfter(170614),170614,170615);
    timeline.insertAt(timeline.getAvailableNodeAtOrAfter(171750),171750,171813);
    timeline.insertAt(timeline.getAvailableNodeAtOrAfter(172299),172299,172303);
    timeline.assertIsValid();
    assertTrue("0", !fits(171291,54,1000,100));
}    
public void testDefinitelyFits() {
    DutyCycleConstraint dutyCycle = new DutyCycleConstraint(horizon,100,10);
    assertTrue("0", dutyCycle.fastFitCheck(0,9));
    assertTrue("1", dutyCycle.fastFitCheck(190,10));
    assertTrue("2", !dutyCycle.fastFitCheck(0,11));
    dutyCycle.insertAt(50,55);
    assertTrue("3", dutyCycle.fastFitCheck(50,5));
    assertTrue("4", dutyCycle.fastFitCheck(0,4));
    assertTrue("5", !dutyCycle.fastFitCheck(0,6));
    assertTrue("6", !dutyCycle.fastFitCheck(100,6));
    assertTrue("7", dutyCycle.fastFitCheck(200,6));
    dutyCycle.insertAt(97,101);
    assertTrue("8", !dutyCycle.fastFitCheck(100,6));
    assertTrue("9", dutyCycle.fastFitCheck(100,1));
    assertTrue("10", !dutyCycle.fastFitCheck(30,6));
    assertTrue("11", dutyCycle.fastFitCheck(200,9));
    assertTrue("12", !dutyCycle.fastFitCheck(250,10));
    assertTrue("13", dutyCycle.fastFitCheck(300,10));
}
public void testInsertAtAndInitialize() {
    DutyCycleConstraint dutyCycle = new DutyCycleConstraint(horizon,100,60);
    int[] zeros = {0,0,0,0,0,0,0,0,0};
    dutyCycle.checkOnTimes(zeros);

    dutyCycle.insertAt(90,110);
    int[] a = {10,10,0,0,0};
    dutyCycle.checkOnTimes(a);

    dutyCycle.insertAt(220,240);
    a[2] = 20;
    dutyCycle.checkOnTimes(a);
    
    dutyCycle.insertAt(20,50);
    a[0] = 40;
    dutyCycle.checkOnTimes(a);
    
    dutyCycle.initialize();
    dutyCycle.checkOnTimes(zeros);
}
public void testDurationBucketCalcs() {
    DutyCycleConstraint dutyCycle = new DutyCycleConstraint(horizon,100,10);
    assertTrue("0", dutyCycle.durationInStartBucket(2) == 98);
    assertTrue("1", dutyCycle.durationInEndBucket(2) == 2);
    assertTrue("2", dutyCycle.durationInStartBucket(150) == 50);
    assertTrue("3", dutyCycle.durationInEndBucket(150) == 50);
    assertTrue("2", dutyCycle.durationInStartBucket(290) == 10);
    assertTrue("3", dutyCycle.durationInEndBucket(290) == 90);
}
public void testGetIndex() {
    DutyCycleConstraint dutyCycle = new DutyCycleConstraint(horizon,100,10);
    assertTrue("0", dutyCycle.getIndex(0) == 0);
    assertTrue("1", dutyCycle.getIndex(3) == 0);
    assertTrue("2", dutyCycle.getIndex(103) == 1);
    assertTrue("3", dutyCycle.getIndex(100) == 1);
    assertTrue("4", dutyCycle.getIndex(199) == 1);
    assertTrue("5", dutyCycle.getIndex(250) == 2);
}
public void testFits() {
    assertTrue("0",  fits(100,20,200,25));
    assertTrue("0.1",!fits(100,20,400,25));

    assertTrue("1",  fits(300,30,100,40));
    assertTrue("2", !fits(370,30,50,40));
    assertTrue("3", !fits(361,30,50,40));
    assertTrue("4",  fits(360,30,50,40));
    
    assertTrue("5",  fits(600,50,250,150));
    assertTrue("6", !fits(600,50,250,149));
    assertTrue("7", !fits(650,50,280,150));
    
    assertTrue("8",  fits(1700,100,300,200));
    assertTrue("8.1",!fits(1700,100,300,199));
    assertTrue("9",  fits(2700,100,300,200));
    assertTrue("10", fits(2700,101,300,200));
    assertTrue("11",!fits(1699,100,300,200));

    assertTrue("12", fits(1700,100,700,400));
    assertTrue("13",!fits(1700,100,700,399));

    assertTrue("14", fits(300,50,500,250));
    assertTrue("15",!fits(300,51,500,250));
    assertTrue("16",!fits(300,50,500,249));
    
    assertTrue("17", fits(2000,50,100,51));
    assertTrue("17",!fits(2000,50,100,49));
}
private boolean fits(int start, int duration, int totalTime, int maxOnTime) {
    AvailableNode node = (AvailableNode)timeline.getNodeAtTime(start);
    Error.assertTrue(node.isAvailable());
    DutyCycleConstraint d = new DutyCycleConstraint(horizon,totalTime,maxOnTime);
    return d.fits(timeline,node,start,duration);
}
public void testOkBackward() {
    assertTrue("1",  okBackward(1700,40,10));
    assertTrue("2",  okBackward(5,40,10));
    assertTrue("3", !okBackward(1610,40,10));
    assertTrue("4",  okBackward(1630,40,10));
    assertTrue("5",  okBackward(1650,100,50));
    assertTrue("6", !okBackward(1650,100,49));
    assertTrue("7",  okBackward(1650,300,200));
    assertTrue("8", !okBackward(1650,300,199));
    assertTrue("9",  okBackward(1650,500,250));
    assertTrue("10",!okBackward(1650,500,249));
    assertTrue("11", okBackward(1650,1650,575));
    assertTrue("12",!okBackward(1650,1650,574));
    assertTrue("13", okBackward(1650,16500,575));
    assertTrue("14",!okBackward(1650,16500,574));
    assertTrue("15", okBackward(550,40,10));
    assertTrue("16",!okBackward(505,40,10));
}
public void testOkForward() {
    assertTrue("0",  okForward(300,40,10));
    assertTrue("1", !okForward(400,125,10));
    assertTrue("2",  okForward(399,200,101));
    assertTrue("3",  okForward(399,200,100));
    assertTrue("4", !okForward(399,200,99));
    assertTrue("5", !okForward(399,125,99));
    assertTrue("5.1", okForward(350,100,99));
    assertTrue("5.2", okForward(350,100,50));
    assertTrue("5.3",!okForward(350,100,49));
    assertTrue("5.4",!okForward(350,400,149));
    assertTrue("5.5", okForward(350,400,150));

    assertTrue("6", !okForward(650,300,125));
    assertTrue("7", !okForward(650,300,149));
    assertTrue("8",  okForward(650,300,150));
    assertTrue("9",  okForward(650,300,160));

    assertTrue("10",  okForward(100,1700,575));
    assertTrue("11",  okForward(100,1700,580));
    assertTrue("12", !okForward(100,1700,574));
    assertTrue("13", !okForward(100,1700,500));

    assertTrue("14",  okForward(100,1000000,575));
    assertTrue("15", !okForward(100,1000000,574));

    assertTrue("15", okForward(1700,1000000,1));
    assertTrue("16", okForward(1700,1000000,0));
}
private boolean okForward(int start, int totalTime, int maxOnTime) {
    AvailableNode node = (AvailableNode)timeline.getNodeAtTime(start);
    if (node.isAvailable())
        node = node.nextUnAvailableNode();
    DutyCycleConstraint d = new DutyCycleConstraint(horizon,totalTime,maxOnTime);
    return d.okForward(timeline, node, start, maxOnTime);
}
private boolean okBackward(int end, int totalTime, int maxOnTime) {
    AvailableNode node = (AvailableNode)timeline.getNodeAtTime(end);
    if (node.isAvailable())
        node = node.previousUnAvailableNode();
    DutyCycleConstraint d = new DutyCycleConstraint(horizon,totalTime,maxOnTime);
    return d.okBackward(timeline, node, end, maxOnTime);
}
}
    
