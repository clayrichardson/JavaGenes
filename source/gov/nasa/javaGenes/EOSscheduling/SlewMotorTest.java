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
import gov.nasa.alsUtility.Utility;

public class SlewMotorTest extends TestCase {
private String time[] = {
    "1 Jan 2002 00:00:00.00",
    "1 Jan 2002 00:00:20.00",
    "2 Jan 2002 00:00:00.00",
    "3 Jan 2002 00:00:00.00"
};
private Horizon horizon = new Horizon(time[0],time[1]);

public SlewMotorTest(String name) {super(name);}

public void testGetMaxAbsoluteSlew() {
    SlewMotor slewMotor = new SlewMotor(1, -10, 15,new Horizon(time[0],time[1]),3); 
    assertTrue("1", Utility.nearlyEqual(slewMotor.getMaxAbsoluteSlew(),15));
    slewMotor = new SlewMotor(1, -24, 24,new Horizon(time[0],time[1]),3); 
    assertTrue("1", Utility.nearlyEqual(slewMotor.getMaxAbsoluteSlew(),24));
}
public void testGetMaxSlewTimeFrom() {
    SlewMotor slewMotor = new SlewMotor(1, -10, 15,new Horizon(time[0],time[1]),3); 
    assertTrue("1", slewMotor.getMaxSlewTimeFrom(new CrossTrackSlew(-10)) == 25);
    assertTrue("2", slewMotor.getMaxSlewTimeFrom(new CrossTrackSlew(0)) == 15);
    assertTrue("3", slewMotor.getMaxSlewTimeFrom(new CrossTrackSlew(10)) == 20);
}
public void testEquivalent() {
    SlewMotor slewMotor = new SlewMotor(1,-50,10,horizon,2);
    assertTrue("1",slewMotor.equivalent(new CrossTrackSlew(5),new CrossTrackSlew(5.009)));
    assertTrue("2",!slewMotor.equivalent(new CrossTrackSlew(5),new CrossTrackSlew(5.011)));
    slewMotor.setSlop(0.02);
    assertTrue("3",slewMotor.equivalent(new CrossTrackSlew(5),new CrossTrackSlew(4.9811)));
    assertTrue("4",!slewMotor.equivalent(new CrossTrackSlew(5),new CrossTrackSlew(5.021)));
    assertTrue("5",!slewMotor.equivalent(new CrossTrackSlew(5),new CrossTrackSlew(4.979)));
}
public void testIsWithinLimits() {
    SlewMotor slewMotor = new SlewMotor(1,-5,10,horizon,2);
    CrossTrackSlew r = new CrossTrackSlew(7);
    assertTrue("1",slewMotor.isWithinLimits(r));
    r = new CrossTrackSlew(-5);
    assertTrue("2",slewMotor.isWithinLimits(r));
    r = new CrossTrackSlew(10);
    assertTrue("3",slewMotor.isWithinLimits(r));

    r = new CrossTrackSlew(-5.1);
    assertTrue("4",!slewMotor.isWithinLimits(r));
    r = new CrossTrackSlew(10.7);
    assertTrue("5",!slewMotor.isWithinLimits(r));
}

public void testSlewTime() {
    SlewMotor slewMotor = new SlewMotor(2,-5,10,horizon,2);
    CrossTrackSlew f = new CrossTrackSlew(7);
    CrossTrackSlew t = new CrossTrackSlew(3);
    assertTrue("1",Utility.nearlyEqual(slewMotor.slewTime(f,t),2));
    assertTrue("2",Utility.nearlyEqual(slewMotor.slewTime(f,t),slewMotor.slewTime(t,f)));
    f = new CrossTrackSlew(-5);
    t = new CrossTrackSlew(3);
    assertTrue("3",Utility.nearlyEqual(slewMotor.slewTime(f,t),4));
    assertTrue("4",Utility.nearlyEqual(slewMotor.slewTime(f,t),slewMotor.slewTime(t,f)));
}
}
