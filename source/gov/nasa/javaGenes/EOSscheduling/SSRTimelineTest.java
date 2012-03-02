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
//  Created by Al Globus on Thu Sep 19 2002.
package gov.nasa.javaGenes.EOSscheduling;

import junit.framework.TestCase;

public class SSRTimelineTest extends TestCase {
private SSRTimeline timeline;
private Horizon horizon;
private final int typicalTaskDuration = 2;
private final int SSRcapacity = 10;
private final int NOT_SCHEDULED = Timeline.NOT_SCHEDULED;
private SchedulingData scheduleData;
private String time[] = {
    "1 Jan 2002 00:00:00.00",
    "1 Jan 2002 00:00:10.00",
    "2 Jan 2002 00:00:00.00",
    "3 Jan 2002 00:00:00.00"
};

public SSRTimelineTest(String name) {super(name);}
protected void setUp() {
    horizon = new Horizon(time[0],time[1]);
    Satellite satellite = new Satellite("satellite");
    satellite.setSSR(SSRcapacity,horizon,typicalTaskDuration);
    timeline = satellite.getSSRtimeline();
    timeline.assertIsValid();
    scheduleData = new SchedulingData();
}

public void testGetNumberOfSSRsegments() {
    assertTrue("1",timeline.getNumberOfSSRsegments() == 1);
    timeline.insertDumpAt(3);
    assertTrue("2",timeline.getNumberOfSSRsegments() == 2);
    timeline.insertDumpAt(7);
    assertTrue("3",timeline.getNumberOfSSRsegments() == 3);
    timeline.insertDumpAt(9);
    assertTrue("4",timeline.getNumberOfSSRsegments() == 4);
}
public void testFits() {
    SchedulingData sd = new SchedulingData();

    sd.setSSRuse(5);
    assertTrue("1", timeline.fits(3,sd));
    sd.setSSRuse(10);
    assertTrue("2", timeline.fits(3,sd));
    sd.setSSRuse(11);
    assertTrue("3", !timeline.fits(3,sd));
    sd.setSSRuse(10);
    assertTrue("4", timeline.fits(3,sd));
    timeline.insertAt(3,sd); // see if crashes

    assertTrue("noneTest", new SSRTimelineNone().fits(3,sd));
}
public void testConstructor() {
    timeline.assertIsValid();
    int[] correctTimes = {0};
    double[] correctCapacity = {SSRcapacity};
    check(correctTimes,correctCapacity,"constructor");
}
public void testInsertDumpAt() {
    timeline.insertDumpAt(3);
    int[] correctTimes = {0,3};
    double[] correctCapacity = {SSRcapacity,SSRcapacity};
    check(correctTimes,correctCapacity,"1");

    timeline.insertDumpAt(8);
    int[] correctTimes2 = {0,3,8};
    double[] correctCapacity2 = {SSRcapacity,SSRcapacity,SSRcapacity};
    check(correctTimes2,correctCapacity2,"2");

    timeline.insertDumpAt(3);
    int[] correctTimes3 = {0,3,8};
    double[] correctCapacity3 = {SSRcapacity,SSRcapacity,SSRcapacity};
    check(correctTimes3,correctCapacity3,"3");

    timeline.insertDumpAt(2);
    int[] correctTimes4 = {0,2,3,8};
    double[] correctCapacity4 = {SSRcapacity,SSRcapacity,SSRcapacity,SSRcapacity};
    check(correctTimes4,correctCapacity4,"4");

    scheduleData.setSSRuse(3);
    correctTimes = correctTimes4;
    timeline.insertAt(5,scheduleData);
    double[] correctCapacity5 = {SSRcapacity,SSRcapacity,SSRcapacity-3,SSRcapacity};
    check(correctTimes,correctCapacity5,"5");

    timeline.insertAt(2,scheduleData);
    double[] correctCapacity6 = {SSRcapacity,SSRcapacity-3,SSRcapacity-3,SSRcapacity};
    check(correctTimes,correctCapacity6,"6");

    timeline.insertAt(0,scheduleData);
    double[] correctCapacity7 = {SSRcapacity-3,SSRcapacity-3,SSRcapacity-3,SSRcapacity};
    check(correctTimes,correctCapacity7,"7");

    scheduleData.setSSRuse(5);
    timeline.insertAt(9,scheduleData);
    double[] correctCapacity8 = {SSRcapacity-3,SSRcapacity-3,SSRcapacity-3,SSRcapacity-5};
    check(correctTimes,correctCapacity8,"8");
}
public void testInsertAt() {
    scheduleData.setSSRuse(3);
    int[] correctTimes = {0};

    timeline.insertAt(5,scheduleData);
    double[]c = {SSRcapacity - 3};
    check(correctTimes,c,"1");

    scheduleData.setSSRuse(1);
    timeline.insertAt(3,scheduleData);
    double[]c1 = {SSRcapacity - 3 - 1};
    check(correctTimes,c1,"2");

    scheduleData.setSSRuse(2);
    timeline.insertAt(8,scheduleData);
    double[]c2 = {SSRcapacity - 3 - 1 - 2};
    check(correctTimes,c2,"3");
    

    scheduleData.setSSRuse(1);
    timeline.insertAt(5,scheduleData);
    double[]c3 = {SSRcapacity - 3 - 1 - 2 - 1};
    check(correctTimes,c3,"4");
}

public void testFindEarliest() {
    scheduleData.setDuration(3);
    scheduleData.setSSRuse(SSRcapacity-3);
    assertTrue("1",timeline.findEarliest(0,5,scheduleData) == 0);
    assertTrue("2",timeline.findEarliest(3,5,scheduleData) == 3);
    assertTrue("3",timeline.findEarliest(9,9,scheduleData) == 9);
    assertTrue("3",timeline.findEarliest(9,13,scheduleData) == 9);

    scheduleData.setSSRuse(0);
    assertTrue("4",timeline.findEarliest(9,9,scheduleData) == 9);
    assertTrue("5",timeline.findEarliest(3,9,scheduleData) == 3);

    scheduleData.setSSRuse(SSRcapacity);
    assertTrue("7",timeline.findEarliest(9,9,scheduleData) == 9);
    assertTrue("6",timeline.findEarliest(3,9,scheduleData) == 3);

    scheduleData.setSSRuse(SSRcapacity+1);
    assertTrue("7",timeline.findEarliest(9,9,scheduleData) == NOT_SCHEDULED);
    assertTrue("6",timeline.findEarliest(3,9,scheduleData) == NOT_SCHEDULED);
}
private void check(int[] correctTimes,double[] correctCapacity,String name) {
    int[] timeLineTimes = timeline.getStartTimesArray();
    assertTrue("start times length", correctTimes.length == timeLineTimes.length);
    for(int i = 0; i < correctTimes.length; i++) 
        assertTrue(i+" " +name, correctTimes[i] == timeLineTimes[i]);

    double[] capacity = timeline.getCapacityArray();
    assertTrue("capacity length", capacity.length == correctCapacity.length);
    for(int i = 0; i < capacity.length; i++)
        assertTrue("capacity " + i  + " " + name, capacity[i] == correctCapacity[i]);
}
/*
public void testIncompatibleSlewInMiddle() {
    SlewNode node = (SlewNode)allTimeNode();
    SlewRequirement slew = new CrossTrackSlew(5.0001);
    SlewRequirement badSlew = new CrossTrackSlew(6);
    SlewMotor slewMotor = new SlewMotor(1,-5,10,horizon,typicalTaskDuration);
    assertTrue("1",!timeline.incompatibleSlewInMiddle(slew,node,node,slewMotor));
    assertTrue("2",!timeline.incompatibleSlewInMiddle(badSlew,node,node,slewMotor));

    timeline.setLastNodes(allTimeNode(),allTimeNode());
    timeline.insertAt(2,4,new CrossTrackSlew(5.0));
    SlewNode other = (SlewNode)timeline.getNodeAtTime(3);
    assertTrue("3",!timeline.incompatibleSlewInMiddle(slew,node,other,slewMotor));
    assertTrue("4",timeline.incompatibleSlewInMiddle(badSlew,node,other,slewMotor));

    other = (SlewNode)timeline.getNodeAtTime(7);
    assertTrue("5",!timeline.incompatibleSlewInMiddle(slew,node,other,slewMotor));
    assertTrue("6",timeline.incompatibleSlewInMiddle(badSlew,node,other,slewMotor));
}
public void testGetEarliestStartTimeInNode() {
    SlewNode node = (SlewNode)allTimeNode();
    SlewRequirement slew = new CrossTrackSlew(5);
    SlewMotor slewMotor = new SlewMotor(1,-5,10,horizon,typicalTaskDuration);
    assertTrue("1",0 == timeline.getEarliestStartTimeInNode(node,0,slew,slewMotor));
    assertTrue("2",4 == timeline.getEarliestStartTimeInNode(node,4,slew,slewMotor));
    assertTrue("3",9 == timeline.getEarliestStartTimeInNode(node,9,slew,slewMotor));
    timeline.setLastNodes(allTimeNode(),allTimeNode());
    timeline.insertAt(2,4,new CrossTrackSlew(5.0));
    SlewRequirement badSlew = new CrossTrackSlew(20);
    node = (SlewNode)timeline.getNodeAtTime(3);
    assertTrue("4",3 == timeline.getEarliestStartTimeInNode(node,3,slew,slewMotor));
    assertTrue("5",timeline.NOT_SCHEDULED == timeline.getEarliestStartTimeInNode(node,3,badSlew,slewMotor));
    node = (SlewNode)timeline.getNodeAtTime(5);
    SlewRequirement nearSlew = new CrossTrackSlew(7);
    assertTrue("6",6 == timeline.getEarliestStartTimeInNode(node,4,nearSlew,slewMotor));
    assertTrue("7",6 == timeline.getEarliestStartTimeInNode(node,5,nearSlew,slewMotor));
    assertTrue("8",6 == timeline.getEarliestStartTimeInNode(node,6,nearSlew,slewMotor));
    assertTrue("9",7 == timeline.getEarliestStartTimeInNode(node,7,nearSlew,slewMotor));
    assertTrue("9",6 == timeline.getEarliestStartTimeInNode(node,2,nearSlew,slewMotor));
    assertTrue("10",6 == timeline.getEarliestStartTimeInNode(node,1,nearSlew,slewMotor));
}
public void testEndTimeOK() {
    SlewNode node = (SlewNode)allTimeNode();
    SlewRequirement slew = new CrossTrackSlew(5);
    SlewMotor slewMotor = new SlewMotor(1,-5,10,horizon,typicalTaskDuration);
    assertTrue("1", timeline.endTimeOK(node,5,slew,slewMotor));
    assertTrue("2", timeline.endTimeOK(node,0,slew,slewMotor));
    assertTrue("3", timeline.endTimeOK(node,9,slew,slewMotor));
    // insertAt cases
    timeline.setLastNodes(allTimeNode(),allTimeNode());
    timeline.insertAt(5,8,new CrossTrackSlew(5.0));
    SlewRequirement badSlew = new CrossTrackSlew(20);
    assertTrue("4",timeline.endTimeOK((SlewNode)timeline.getNodeAtTime(4),4,slew,slewMotor));
    assertTrue("5",!timeline.endTimeOK((SlewNode)timeline.getNodeAtTime(4),4,badSlew,slewMotor));
    assertTrue("6",timeline.endTimeOK((SlewNode)timeline.getNodeAtTime(6),6,slew,slewMotor));
    assertTrue("7",!timeline.endTimeOK((SlewNode)timeline.getNodeAtTime(6),6,badSlew,slewMotor));
    SlewRequirement anotherSlew = new CrossTrackSlew(7);
    assertTrue("7.5",!timeline.endTimeOK((SlewNode)timeline.getNodeAtTime(4),4,anotherSlew,slewMotor));
    assertTrue("8",timeline.endTimeOK((SlewNode)timeline.getNodeAtTime(3),3,anotherSlew,slewMotor));
    assertTrue("9",timeline.endTimeOK((SlewNode)timeline.getNodeAtTime(2),2,anotherSlew,slewMotor));
}
public void testFindEarliest() {
    SlewRequirement slew = new CrossTrackSlew(5.0);
    SlewMotor slewMotor = new SlewMotor(1,-50,50,horizon,typicalTaskDuration);
    SlewNode node = (SlewNode)allTimeNode();
    assertTrue("1", 0 == timeline.findEarliest(0,10,5,slew,slewMotor));
    checkLastNodes("1 last",0,5);
    assertTrue("2", NOT_SCHEDULED == timeline.findEarliest(6,10,5,slew,slewMotor));
    assertTrue("3", NOT_SCHEDULED == timeline.findEarliest(2,6,5,slew,slewMotor));
    assertTrue("4", 5 == timeline.findEarliest(5,10,5,slew,slewMotor));
    checkLastNodes("4 last",5,10);
    assertTrue("5", 5 == timeline.findEarliest(5,8,3,slew,slewMotor));
    checkLastNodes("5 last",5,8);
    assertTrue("6", NOT_SCHEDULED == timeline.findEarliest(6,8,3,slew,slewMotor));
    
    timeline.setLastNodes(allTimeNode(),allTimeNode());
    timeline.insertAt(4,5,slew);
    SlewRequirement r = new CrossTrackSlew(7);
    assertTrue("7", 0 == timeline.findEarliest(0,10,2,r,slewMotor));
    checkLastNodes("7 last",0,2);
    assertTrue("8", 7 == timeline.findEarliest(0,10,3,r,slewMotor));
    checkLastNodes("8 last",7,10);
    assertTrue("9", NOT_SCHEDULED == timeline.findEarliest(0,10,4,r,slewMotor));
    assertTrue("10", 0 == timeline.findEarliest(0,10,7,slew,slewMotor));
    checkLastNodes("10 last",0,7);
}
private void checkLastNodes(String name, int start, int end) {
    assertTrue(name + " start",timeline.isLastStartNode((SlewNode)timeline.getNodeAtTime(start)));
    assertTrue(name + " end",timeline.isLastEndNode((SlewNode)timeline.getNodeAtTime(end)));
}
private Node allTimeNode() {
    return timeline.getFirstNode();
}
public void testInsertAt1() {
    int[] times = {0,2,8,10};
    double[] slews = {0,5,5,0};
    boolean[] ramps = {false,true,false,false};
    SlewRequirement r = new CrossTrackSlew(5.0);
    timeline.setLastNodes(allTimeNode(),allTimeNode());
    timeline.insertAt(2,8,r);
    timeline.assertIsValid();
    check(times,slews,ramps,"1");
}
public void testInsertAt2() {
    int[] times = {0,8,10};
    double[] slews = {5,5,0};
    boolean[] ramps = {true,false,false};
    SlewRequirement r = new CrossTrackSlew(5.0);
    timeline.setLastNodes(allTimeNode(),allTimeNode());
    timeline.insertAt(0,8,r);
    timeline.assertIsValid();
    check(times,slews,ramps,"2");
}
public void testInsertAt3() {
    int[] times = {0,2,10};
    double[] slews = {0,5,5};
    boolean[] ramps = {false,true,false};
    SlewRequirement r = new CrossTrackSlew(5.0);
    timeline.setLastNodes(allTimeNode(),allTimeNode().next());
    timeline.insertAt(2,10,r);
    timeline.assertIsValid();
    check(times,slews,ramps,"3");
}
public void testInsertAt4() {
    int[] times = {0,2,4,10};
    double[] slews = {0,5,5,0};
    boolean[] ramps = {false,true,false,false};
    SlewRequirement r = new CrossTrackSlew(5.0);
    timeline.setLastNodes(allTimeNode(),allTimeNode());
    timeline.insertAt(2,4,r);
    timeline.assertIsValid();
    check(times,slews,ramps,"4.1");
    
    int[] t = {0,2,4,6,8,10};
    double[] s = {0,5,5,7,7,0};
    boolean[] ra = {false,true,false,true,false,false};
    r = new CrossTrackSlew(7.0);
    timeline.setLastNodes(timeline.getNodeAtTime(6),timeline.getNodeAtTime(8));
    timeline.insertAt(6,8,r);
    timeline.assertIsValid();
    check(t,s,ra,"4.2");
    
    int[] tt = {0,1,9,10};
    double[] ss = {0,8,8,0};
    boolean[] rra = {false,true,false,false};
    r = new CrossTrackSlew(8.0);
    timeline.setLastNodes(timeline.getNodeAtTime(1),timeline.getNodeAtTime(9));
    timeline.insertAt(1,9,r);
    timeline.assertIsValid();
    check(tt,ss,rra,"4.3");
}
public void testInsertAt5() {
    int[] times = {0,4,7,10};
    double[] slews = {0,5,5,0};
    boolean[] ramps = {false,true,false,false};
    SlewRequirement r = new CrossTrackSlew(5.0);
    timeline.setLastNodes(allTimeNode(),allTimeNode());
    timeline.insertAt(4,7,r);
    timeline.assertIsValid();
    check(times,slews,ramps,"5.1");
    
    int[] t = {0,1,6,7,10};
    double[] s = {0,8,8,5,0};
    boolean[] ra = {false,true,true,false,false};
    r = new CrossTrackSlew(8.0); // wouldn't normaly actually be different, but better for testing
    timeline.setLastNodes(timeline.getNodeAtTime(1),timeline.getNodeAtTime(6));
    timeline.insertAt(1,6,r);
    timeline.assertIsValid();
    check(t,s,ra,"5.2");
}
public void testInsertAt6() {
    int[] times = {0,4,7,10};
    double[] slews = {0,5,5,0};
    boolean[] ramps = {false,true,false,false};
    SlewRequirement r = new CrossTrackSlew(5.0);
    timeline.setLastNodes(allTimeNode(),allTimeNode());
    timeline.insertAt(4,7,r);
    timeline.assertIsValid();
    check(times,slews,ramps,"6.1");
    
    int[] t = {0,1,7,10};
    double[] s = {0,8,8,0};
    boolean[] ra = {false,true,false,false};
    r = new CrossTrackSlew(8.0); // wouldn't normaly actually be different, but better for testing
    timeline.setLastNodes(timeline.getNodeAtTime(1),timeline.getNodeAtTime(7));
    timeline.insertAt(1,7,r);
    timeline.assertIsValid();
    check(t,s,ra,"6.2");
}
public void testInsertAt7() {
    int[] times = {0,4,7,10};
    double[] slews = {0,5,5,0};
    boolean[] ramps = {false,true,false,false};
    SlewRequirement r = new CrossTrackSlew(5.0);
    timeline.setLastNodes(allTimeNode(),allTimeNode());
    timeline.insertAt(4,7,r);
    timeline.assertIsValid();
    check(times,slews,ramps,"7.1");
    
    int[] t = {0,6,7,10};
    double[] s = {8,8,5,0};
    boolean[] ra = {true,true,false,false};
    r = new CrossTrackSlew(8.0); // wouldn't normaly actually be different, but better for testing
    timeline.setLastNodes(timeline.getNodeAtTime(0),timeline.getNodeAtTime(6));
    timeline.insertAt(0,6,r);
    timeline.assertIsValid();
    check(t,s,ra,"7.2");
}
public void testInsertAt8() {
    int[] times = {0,4,7,10};
    double[] slews = {0,5,5,0};
    boolean[] ramps = {false,true,false,false};
    SlewRequirement r = new CrossTrackSlew(5.0);
    timeline.setLastNodes(allTimeNode(),allTimeNode());
    timeline.insertAt(4,7,r);
    timeline.assertIsValid();
    check(times,slews,ramps,"9.1");
    
    int[] t = {0,7,10};
    double[] s = {8,8,0};
    boolean[] ra = {true,false,false};
    r = new CrossTrackSlew(8.0); // wouldn't normaly actually be different, but better for testing
    timeline.setLastNodes(timeline.getNodeAtTime(0),timeline.getNodeAtTime(7));
    timeline.insertAt(0,7,r);
    timeline.assertIsValid();
    check(t,s,ra,"9.2");
}
*/
}



