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

import junit.framework.TestCase;
import gov.nasa.alsUtility.Utility;

public class AccessWindowTest extends TestCase {
private String time[] = {
    "1 Jan 2002 00:00:00.00",
    "1 Jan 2002 00:00:10.00",
    "2 Jan 2002 00:00:00.00",
    "3 Jan 2002 00:00:00.00"
};
public AccessWindowTest(String name) {super(name);}
public void testOverlapInTimeWith() {
    AccessWindow a = new AccessWindow(100,200);
    assertTrue("1",a.overlapInTimeWith(new AccessWindow(110,130)));
    assertTrue("2",a.overlapInTimeWith(new AccessWindow(90,130)));
    assertTrue("3",a.overlapInTimeWith(new AccessWindow(110,230)));
    assertTrue("4",a.overlapInTimeWith(new AccessWindow(100,130)));
    assertTrue("5",a.overlapInTimeWith(new AccessWindow(110,200)));
    assertTrue("6",a.overlapInTimeWith(new AccessWindow(90,230)));
    assertTrue("7",!a.overlapInTimeWith(new AccessWindow(10,100)));
    assertTrue("8",!a.overlapInTimeWith(new AccessWindow(200,299)));
    assertTrue("9",a.overlapInTimeWith(new AccessWindow(100,200)));
    assertTrue("10",!a.overlapInTimeWith(new AccessWindow(110,110)));
    assertTrue("11",!a.overlapInTimeWith(new AccessWindow(200,300)));
    assertTrue("12",!a.overlapInTimeWith(new AccessWindow(50,100)));
    assertTrue("13",a.overlapInTimeWith(new AccessWindow(199,300)));
    assertTrue("14",a.overlapInTimeWith(new AccessWindow(50,101)));
}
public void testCoulbBeScheduledWith() {
    Horizon horizon = new Horizon(time[0],time[1]);
    Satellite satellite = new Satellite("S");
    SensorType sensorType = new SensorType("foo");
    Sensor sensor1 = new Sensor(satellite,sensorType,horizon,5);
    Sensor sensor2 = new Sensor(satellite,sensorType,horizon,5);
    SlewMotor slewMotor = new SlewMotor(1, -100, 100,new Horizon(time[0],time[1]),3); 
    sensor1.setSlewMotor(slewMotor);

    AccessWindow a1 = new AccessWindow(100,200);
    AccessWindow a2 = new AccessWindow(100,200);
    AccessWindow a3 = new AccessWindow(210,300);
    AccessWindow a4 = new AccessWindow(10,90);
    AccessWindow a5 = new AccessWindow(200,210);
    
    a1.setSensor(sensor1);
    a2.setSensor(sensor1);
    assertTrue("1",!a1.couldBeScheduledWith(a2));
    a2.setSensor(sensor2);
    assertTrue("2",a1.couldBeScheduledWith(a2));
    a2.setSensor(sensor2);
    
    a3.setSensor(sensor1);
    a4.setSensor(sensor1);

    a1.setSlewRequirement(new CrossTrackSlew(0));
    a3.setSlewRequirement(new CrossTrackSlew(5));
    assertTrue("3",a1.couldBeScheduledWith(a3));
    a3.setSlewRequirement(new CrossTrackSlew(15));
    assertTrue("4",!a1.couldBeScheduledWith(a3));

    a4.setSlewRequirement(new CrossTrackSlew(5));
    assertTrue("5",a1.couldBeScheduledWith(a4));
    a4.setSlewRequirement(new CrossTrackSlew(15));
    assertTrue("6",!a1.couldBeScheduledWith(a4));
    
    a2.setSensor(sensor1);
    a3.setSensor(sensor1);
    a5.setSensor(sensor1);
    a2.setSlewRequirement(new CrossTrackSlew(0));
    a3.setSlewRequirement(new CrossTrackSlew(0));
    a5.setSlewRequirement(new CrossTrackSlew(0));
    assertTrue("7",a2.couldBeScheduledWith(a5));
    assertTrue("7.5",a5.couldBeScheduledWith(a2));
    assertTrue("8",a3.couldBeScheduledWith(a5));
    assertTrue("8.5",a5.couldBeScheduledWith(a3));
    a5.setSlewRequirement(new CrossTrackSlew(1));
    assertTrue("9",!a2.couldBeScheduledWith(a5));
    assertTrue("9.5",!a5.couldBeScheduledWith(a2));
    assertTrue("10",!a3.couldBeScheduledWith(a5));
    assertTrue("10.5",!a5.couldBeScheduledWith(a3));
}
public void testDuration() {
    AccessWindow w = new AccessWindow(5,10);
    assertTrue("1",w.getDuration() == 5);
    w.setStart(1);
    assertTrue("2",w.getDuration() == 9);
    w.setEnd(1);
    assertTrue("3",w.getDuration() == 0);
}
public void testIsWithin() {
    AccessWindow w = new AccessWindow(3,6);
    assertTrue("1", w.isWithin(5));
    assertTrue("2", w.isWithin(6));
    assertTrue("3", w.isWithin(3));
    assertTrue("4", !w.isWithin(2));
    assertTrue("5", !w.isWithin(7));
}
public void testGetTimeAtMiddle() {
    AccessWindow w = new AccessWindow(2,4);
    assertTrue("1",w.getTimeAtMiddle() == 3);
    w = new AccessWindow(2,6);
    assertTrue("2",w.getTimeAtMiddle() == 4);
    w = new AccessWindow(2,5);
    assertTrue("3",w.getTimeAtMiddle() == 3 || w.getTimeAtMiddle() == 4);
    w = new AccessWindow(2,7);
    assertTrue("4",w.getTimeAtMiddle() == 4 || w.getTimeAtMiddle() == 5);
}
public void testShrinkAroundMiddle() {
    AccessWindow w = new AccessWindow(3,10);
    w.setStart(3);
    w.setEnd(10);
    w.shrinkAroundMiddle(2);
    assertTrue("1",w.getStart() == 5 || w.getStart() == 6);
    assertTrue("2",w.getDuration() == 2);
    
    w.setStart(4);
    w.setEnd(10);
    w.shrinkAroundMiddle(2);
    assertTrue("3",w.getStart() == 6);
    assertTrue("3",w.getEnd() == 8);
    
    w.setStart(3);
    w.setEnd(10);
    w.shrinkAroundMiddle(3);
    assertTrue("4",w.getStart() == 5);
    assertTrue("5",w.getEnd() == 8);
    
    w.setStart(4);
    w.setEnd(10);
    w.shrinkAroundMiddle(3);
    assertTrue("7",w.getStart() == 5 || w.getStart() == 6);
    assertTrue("8",w.getDuration() == 3);
    
    w.setStart(3);
    w.setEnd(10);
    w.shrinkAroundMiddle(24);
    assertTrue("9",w.getStart() == 3);
    assertTrue("10",w.getEnd() == 10);
}
}
