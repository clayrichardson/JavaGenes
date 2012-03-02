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
//  Created by Al Globus on Mon Jul 22 2002.
package gov.nasa.javaGenes.EOSscheduling;

import junit.framework.TestCase;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.IntegerInterval;

/**
RESTRICTION: the dutyCylce constraint assumes that the task duration is equal to the access window -- i.e, there is
no point in trying to move the task around in time.  This was done to simplify implementation since the more
general case may never be an issue for this project.
*/
public class AvailabilityTimelineTest extends TestCase {
private AvailabilityTimeline timeline;
private Horizon horizon;
private Horizon horizonLong;
private final int typicalTaskDuration = 2;
private String time[] = {
    "1 Jan 2002 00:00:00.00",
    "1 Jan 2002 00:00:10.00",
    "2 Jan 2002 00:00:00.00",
    "3 Jan 2002 00:00:00.00"
};

public AvailabilityTimelineTest(String name) {super(name);}
protected void setUp() {
    horizon = new Horizon(time[0],time[1]);
    horizonLong = new Horizon(time[0],time[3]);
    timeline = new AvailabilityTimeline(horizon,typicalTaskDuration);
    timeline.assertIsValid();
}
public void testGetAvailableNodeAtOrAfter() {
    Node node = timeline.getAvailableNodeAtOrAfter(1);
    assertTrue("1",node.getStart() == 0);
    node = timeline.getAvailableNodeAtOrAfter(0);
    assertTrue("2",node.getStart() == 0);
    node = timeline.getAvailableNodeAtOrAfter(9);
    assertTrue("3",node.getStart() == 0);

    timeline.insertAt(allTimeNode(),3,7);
    node = timeline.getAvailableNodeAtOrAfter(1);
    assertTrue("4",node.getStart() == 0);
    node = timeline.getAvailableNodeAtOrAfter(3);
    assertTrue("5",node.getStart() == 7);
    node = timeline.getAvailableNodeAtOrAfter(5);
    assertTrue("6",node.getStart() == 7);
    node = timeline.getAvailableNodeAtOrAfter(7);
    assertTrue("7",node.getStart() == 7);
    node = timeline.getAvailableNodeAtOrAfter(8);
    assertTrue("8",node.getStart() == 7);

    timeline.insertAt(node,7,10);
    node = timeline.getAvailableNodeAtOrAfter(1);
    assertTrue("9",node.getStart() == 0);
    node = timeline.getAvailableNodeAtOrAfter(4);
    assertTrue("10",node == null);
    node = timeline.getAvailableNodeAtOrAfter(7);
    assertTrue("11",node == null);
    node = timeline.getAvailableNodeAtOrAfter(9);
    assertTrue("12",node == null);
}    
public void testConstructor() {
    timeline.assertIsValid();
    int[] correctTimes = {0,10};
    checkTimes(correctTimes);
}
public void testFits() {
    Node node = timeline.getAvailableNodeAtOrAfter(1);
    timeline.insertAt(node,4,6);
    SchedulingData sd = new SchedulingData();
    sd.setDuration(2);
    
    assertTrue("1", timeline.fits(0,sd));
    assertTrue("2", timeline.fits(2,sd));
    assertTrue("3", timeline.fits(6,sd));
    assertTrue("4", timeline.fits(7,sd));
    assertTrue("5", !timeline.fits(5,sd));
    assertTrue("6", !timeline.fits(4,sd));

    sd.setDuration(5);
    assertTrue("7", !timeline.fits(3,sd));

    sd.setDuration(2);
    assertTrue("7", timeline.fits(7,sd));
    timeline.insertAt(7,sd);
    timeline.assertIsValid();
}
private void checkTimes(int[] correctTimes) {checkTimes(correctTimes,"");}
private void checkTimes(int[] correctTimes,String name) {
    int[] timeLineTimes = timeline.getStartTimesArray();
    assertTrue("length " + name, correctTimes.length + 2 == timeLineTimes.length);
    assertTrue("before " + name,timeLineTimes[0] == horizon.getStart()-1);
    assertTrue("after " + name,timeLineTimes[correctTimes.length+1] == horizon.getEnd()+1);
    for(int i = 0; i < correctTimes.length; i++)
        assertTrue(i+" " +name,correctTimes[i] == timeLineTimes[i+1]);
}

public void testInsertAt1() {
    int[] times = {0,2,8,10};
    timeline.insertAt(allTimeNode(),2,8);
    timeline.assertIsValid();
    checkTimes(times,"1");
    
    timeline.insertAt(timeline.getFirstNode(),0,2);
    timeline.assertIsValid();
    int t[] = {8,10};
    checkTimes(t,"2");
    
    timeline.insertAt(timeline.getFirstNode(),8,10);
    timeline.assertIsValid();
    int tt[] = {};
    checkTimes(tt,"3");
}
public void testInsertAt2() {
    int[] times = {0,3,7,10};
    timeline.insertAt(allTimeNode(),3,7);
    timeline.assertIsValid();
    checkTimes(times,"1");

    timeline.insertAt(timeline.getFirstNode().next().next(),7,10);
    timeline.assertIsValid();
    int tt[] = {0,3};
    checkTimes(tt,"2");
}
public void testInsertAt3() {
    int[] times = {7,10};
    timeline.insertAt(allTimeNode(),0,7);
    timeline.assertIsValid();
    checkTimes(times);
}
public void testInsertAt4() {
    int[] times = {9,10};
    timeline.insertAt(allTimeNode(),0,9);
    timeline.assertIsValid();
    checkTimes(times);
}
public void testInsertAt5() {
    int[] times = {8,10};
    timeline.insertAt(allTimeNode(),0,8);
    timeline.assertIsValid();
    checkTimes(times);
}
public void testInsertAt6() {
    int[] times = {0,5};
    timeline.insertAt(allTimeNode(),5,10);
    timeline.assertIsValid();
    checkTimes(times);
}
public void testInsertAt7() {
    int[] times = {0,1};
    timeline.insertAt(allTimeNode(),1,10);
    timeline.assertIsValid();
    checkTimes(times);
}
public void testInsertAt8() {
    int[] times = {0,6};
    timeline.insertAt(allTimeNode(),6,10);
    timeline.assertIsValid();
    checkTimes(times);
}
public void testInsertAt9() {
    int[] times = {};
    timeline.insertAt(allTimeNode(),0,10);
    timeline.assertIsValid();
    checkTimes(times);
}

private Node allTimeNode() {
    return timeline.getFirstNode();
}
public void testDutyCycles() {
    timeline.insertEarliest(5,7,2);
    assertTrue("1",7 == timeline.findEarliest(7,8,1));
    DutyCycleConstraint[] dc = {new DutyCycleConstraint(horizon,3,2)};
    dc[0].insertAt(5,7);
    timeline.setDutyCycles(dc);
    assertTrue("2",Timeline.NOT_SCHEDULED == timeline.findEarliest(7,8,1));
    assertTrue("2.2",Timeline.NOT_SCHEDULED == timeline.findEarliest(4,5,1));
    assertTrue("3",8 == timeline.findEarliest(8,9,1));
    assertTrue("4",3 == timeline.findEarliest(3,4,1));
    
    dc[0] = new DutyCycleConstraint(horizon,10,3);
    assertTrue("5",7 == timeline.findEarliest(7,8,1));
    assertTrue("5.2",4 == timeline.findEarliest(4,5,1));
    assertTrue("6",8 == timeline.findEarliest(8,9,1));
    assertTrue("7",3 == timeline.findEarliest(3,4,1));
    
    DutyCycleConstraint[] dc2 = {new DutyCycleConstraint(horizon,10,3),new DutyCycleConstraint(horizon,3,2)};
    dc2[0].insertAt(5,7);
    dc2[1].insertAt(5,7);
    timeline.setDutyCycles(dc2);
    assertTrue("8",Timeline.NOT_SCHEDULED == timeline.findEarliest(7,8,1));
    assertTrue("8.2",Timeline.NOT_SCHEDULED == timeline.findEarliest(4,5,1));
    assertTrue("9",8 == timeline.findEarliest(8,9,1));
    assertTrue("10",3 == timeline.findEarliest(3,4,1));
}

public void testInsertEarliest0() {
    timeline.insertEarliest(5,10,5);
    int[] t = {0,5};
    timeline.assertIsValid();
    checkTimes(t,"0");
}
public void testInsertEarliest01() {
    timeline.insertEarliest(0,10,10);
    int[] t = {};
    timeline.assertIsValid();
    checkTimes(t,"01");
}
public void testInsertEarliest1() {
    timeline.insertEarliest(2,8,6);
    int[] t = {0,2,8,10};
    timeline.assertIsValid();
    checkTimes(t,"1");
    
    timeline.insertEarliest(2,8,3);
    timeline.assertIsValid();
    checkTimes(t,"2");
    
    timeline.insertEarliest(4,9,2);
    timeline.assertIsValid();
    checkTimes(t,"3");
    
    timeline.insertEarliest(0,8,3);
    timeline.assertIsValid();
    checkTimes(t,"4");
    
    timeline.insertEarliest(0,10,3);
    timeline.assertIsValid();
    checkTimes(t,"5");

    timeline.insertEarliest(0,10,2);
    int[] tt = {8,10};
    timeline.assertIsValid();
    checkTimes(tt,"6");

    timeline.insertEarliest(0,10,2);
    int[] ttt = {};
    timeline.assertIsValid();
    checkTimes(ttt,"7");
}
public void testInsertEarliest2() {
    timeline.insertEarliest(2,10,2);
    int[] t = {0,2,4,10};
    timeline.assertIsValid();
    checkTimes(t,"1");

    timeline.insertEarliest(4,10,2);
    int[] tt = {0,2,6,10};
    timeline.assertIsValid();
    checkTimes(tt,"2");

    timeline.insertEarliest(6,10,2);
    int[] ttt = {0,2,8,10};
    timeline.assertIsValid();
    checkTimes(ttt,"3");

    timeline.insertEarliest(0,10,3);
    timeline.assertIsValid();
    checkTimes(ttt,"4");

    timeline.insertEarliest(0,10,2);
    int[] tttt = {8,10};
    timeline.assertIsValid();
    checkTimes(tttt,"5");
}
public void testInsertEarliestRandomized() { // randomized stress test
    RandomNumber.initialize();
    //RandomNumber.setSeed(1062790623091L); // used for repeatability
    randomized("1",true,null);
    randomized("2",false,null);

    DutyCycleConstraint[] d1 = {new DutyCycleConstraint(horizonLong,1000,100)};
    randomized("3",false,d1);
    DutyCycleConstraint[] d2 = {new DutyCycleConstraint(horizonLong,200,100)};
    randomized("4",false,d2);
    DutyCycleConstraint[] d3 = {new DutyCycleConstraint(horizonLong,200,10),new DutyCycleConstraint(horizonLong,1000,100)};
    randomized("5",false,d3);
}
protected void randomized(String name,boolean checkEveryTime, DutyCycleConstraint[] dutyCycles) {
    Horizon horizon1 = new Horizon(time[0],time[3]);
    AvailabilityTimeline timeline1 = new AvailabilityTimeline(horizon1,24);
    timeline1.setDutyCycles(dutyCycles);
    timeline1.assertIsValid();
    assertTrue(name+"-1",timeline1.fitsDutyCycles());

    int horizonDuration = horizon1.getDuration();
    int count = checkEveryTime ? 10 : 1000;
    for(int i = 0; i < count; i++) {
        int start = RandomNumber.getIndex(horizonDuration);
        int end = start + RandomNumber.getIndex(horizonDuration - start);
        int duration = Math.max(1,RandomNumber.getIndex(end - start + 1));
        timeline1.insertEarliest(start,end,duration);
        if (checkEveryTime) {
            timeline1.assertIsValid();
            assertTrue(name+"-2-"+i,timeline1.fitsDutyCycles());;
        }
    }
    timeline1.assertIsValid();
    assertTrue(name+"-3",timeline1.fitsDutyCycles());
}
public void testDutyCyclesSpeedCheck() { 
    RandomNumber.initialize();
    //RandomNumber.setSeed(1065641163208L); // used for repeatability
    System.out.println("testDutyCyclesSpeedCheck() seed = " + RandomNumber.getSeed());
    for(int i = 0; i < 10; i++){
        DutyCycleConstraint[] dutyCycles = new DutyCycleConstraint[new IntegerInterval(1,5).random()];
        for(int j = 0; j < dutyCycles.length; j++) {
            int totalTime = new IntegerInterval(500,1000).random();
            int maxOnTime = new IntegerInterval(20,450).random();
            dutyCycles[j] = new DutyCycleConstraint(horizonLong,totalTime,maxOnTime);
        }
        randomizedDC1(dutyCycles);
    }
}
protected void randomizedDC1(DutyCycleConstraint[] dutyCycles) {
    int insertCount = 10;
    int fastCheckTestCount = 100;
        
    AvailabilityTimeline timeline1 = new AvailabilityTimeline(horizonLong,24);
    timeline1.setDutyCycles(dutyCycles);
    timeline1.assertIsValid();
    int minDuration = horizonLong.getDuration();
    for(int i = 0; i < dutyCycles.length; i++)
        if (dutyCycles[i].maxOnTime < minDuration)
            minDuration = dutyCycles[i].maxOnTime;

    for(int i = 0; i < insertCount; i++) {
        int start = RandomNumber.getIndex(horizonLong.getDuration());
        int end = start + RandomNumber.getIndex(horizonLong.getDuration() - start);
        int duration = Math.max(1,RandomNumber.getIndex(end - start + 1));
        if (duration > minDuration)
            duration = new IntegerInterval(1,minDuration).random();
        timeline1.insertEarliest(start,end,duration);
        for(int j = 0; j < fastCheckTestCount; j++) {
            int startdc = RandomNumber.getIndex(horizonLong.getDuration());
            AvailableNode node = ((AvailableNode)timeline1.getNodeAtTime(startdc));
            if (!node.isAvailable() || node.getEnd() == startdc)
                continue;
            int durationdc = new IntegerInterval(1,node.getEnd() - startdc).random();
            if (durationdc > minDuration)
                durationdc = new IntegerInterval(1,minDuration).random();

            // check for fit without duty cycle constraint
            timeline1.setDutyCycles(null);
            if (!timeline1.fits(startdc,duration)) { 
                timeline1.setDutyCycles(dutyCycles);
                continue;
            }
            timeline1.setDutyCycles(dutyCycles);
            
            boolean fitsAll = true;
            for(int k = 0; k < dutyCycles.length; k++) {
                boolean fits = dutyCycles[k].fits(timeline1,node,startdc,durationdc);
                if (!fits)
                    fitsAll = false;
                if (dutyCycles[k].fastFitCheck(startdc,durationdc)) 
                    assertTrue("3-" + i + "," + j + "," + k, fits);
            }
            if (fitsAll)
                assertTrue("4-" + i + "," + j, timeline1.fits(startdc,durationdc));
        }
    }
    timeline1.assertIsValid();
    assertTrue("3",timeline1.fitsDutyCycles());
}

public void testInsertEarliest4() {
    for(int i = 1; i <= 9; i++) {
        timeline.insertEarliest(0,10,1);
        int[] t = {i,10};
        timeline.assertIsValid();
        checkTimes(t,i+"");
    }
}
public void testInsertEarliest5() {
    for(int i = 9; i <= 1; i++) {
        timeline.insertEarliest(i,10,1);
        int[] t = {0,i,10};
        timeline.assertIsValid();
        checkTimes(t,i+"");
    }
}
public void testInsertEarliest6() {
    timeline.insertEarliest(5,10,1);
    int[] t = {0,5,6,10};
    timeline.assertIsValid();
    checkTimes(t);
}
public void testInsertEarliest7() {
    timeline.insertEarliest(0,10,10);
    int[] t = {}; //{0,10};
    timeline.assertIsValid();
    checkTimes(t);
}
}
