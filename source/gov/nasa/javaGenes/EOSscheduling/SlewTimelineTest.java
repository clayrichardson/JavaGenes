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

public class SlewTimelineTest extends TestCase {
private SlewTimeline timeline;
private Horizon horizon;
private final int typicalTaskDuration = 2;
private final int NOT_SCHEDULED = SlewTimeline.NOT_SCHEDULED;
private String time[] = {
    "1 Jan 2002 00:00:00.00",
    "1 Jan 2002 00:00:10.00",
    "2 Jan 2002 00:00:00.00",
    "3 Jan 2002 00:00:00.00"
};

public SlewTimelineTest(String name) {super(name);}
protected void setUp() {
    horizon = new Horizon(time[0],time[1]);
    timeline = new SlewTimeline(horizon,typicalTaskDuration);
    timeline.assertIsValid();
}
public void testFits() {
    SchedulingData sd = new SchedulingData();
    Slewable slewable = new SlewMotor(1,-180, 180,horizon,24);
    sd.setSlewable(slewable);
    sd.setDuration(2);

    SlewRequirement r = new CrossTrackSlew(0);
    timeline.setLastNodes(allTimeNode(),allTimeNode());
    timeline.insertAt(4,6,r);

    sd.setSlewRequirement(new CrossTrackSlew(0));
    assertTrue("1", timeline.fits(0,sd));
    assertTrue("2", timeline.fits(4,sd));
    assertTrue("3", timeline.fits(5,sd));
    assertTrue("4", timeline.fits(6,sd));
    assertTrue("5", timeline.fits(8,sd));
 
    sd.setSlewRequirement(new CrossTrackSlew(1));
    assertTrue("11", timeline.fits(0,sd));
    assertTrue("11.1", timeline.fits(1,sd));
    assertTrue("12", !timeline.fits(2,sd));
    assertTrue("13", !timeline.fits(5,sd));
    assertTrue("14", !timeline.fits(6,sd));
    assertTrue("15", timeline.fits(7,sd));
    assertTrue("16", timeline.fits(8,sd));
  
    sd.setSlewRequirement(new CrossTrackSlew(2));
    assertTrue("21", timeline.fits(0,sd));
    assertTrue("21.1", !timeline.fits(1,sd));
    assertTrue("22", !timeline.fits(2,sd));
    assertTrue("23", !timeline.fits(5,sd));
    assertTrue("24", !timeline.fits(6,sd));
    assertTrue("25", !timeline.fits(7,sd));
    assertTrue("26", timeline.fits(8,sd));
    timeline.insertAt(8,sd);
    timeline.assertIsValid();
    
    assertTrue("27", new SlewTimelineNone().fits(5,sd));
}
public void testGetTotalSlewTime() {
    Slewable slewable = new SlewMotor(1, -180, 180,horizon,24);
    assertTrue("1",timeline.getTotalSlewTime(slewable) == 0);
    SlewRequirement r = new CrossTrackSlew(5.0);
    timeline.setLastNodes(allTimeNode(),allTimeNode());
    timeline.insertAt(3,4,r);
    timeline.assertIsValid();
    assertTrue("2",timeline.getTotalSlewTime(slewable) == 0);

    r = new CrossTrackSlew(4.0);
    int time = timeline.findEarliest(4,9, 1, r,slewable);
    timeline.insertAt(time,time+1,r);
    assertTrue("3",timeline.getTotalSlewTime(slewable) == 1);

    time = timeline.findEarliest(0,2, 1, r,slewable);
    timeline.insertAt(time,time+1,r);
    assertTrue("4",timeline.getTotalSlewTime(slewable) == 2);
    
}

public void testConstructor() {
    timeline.assertIsValid();
    int[] correctTimes = {0,10};
    double[] correctSlew = {0,0};
    boolean[] ramp = {false,false};
    check(correctTimes,correctSlew,ramp,"constructor");
}
private void check(int[] correctTimes,double[] correctSlew,boolean correctRamp[],String name) {
    int[] timeLineTimes = timeline.getStartTimesArray();
    assertTrue("length", correctTimes.length == timeLineTimes.length);
    for(int i = 0; i < correctTimes.length; i++) 
        assertTrue(i+" " +name, correctTimes[i] == timeLineTimes[i]);

    double[][] slews = timeline.getSlewArray();
    assertTrue("slews length", slews.length == correctSlew.length && slews[0].length == 1);
    for(int i = 0; i < slews.length; i++)
        assertTrue("slews " + i  + " " + name, slews[i][0] == correctSlew[i]);

    boolean ramps[] = timeline.getRampArray();
    assertTrue("ramps", ramps.length == correctRamp.length);
    for(int i = 0; i < ramps.length; i++)
        assertTrue( "ramps " + i + " " + name, ramps[i] == correctRamp[i]);
}
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
}


