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
//  Created by Al Globus on Fri Jul 19 2002.
package gov.nasa.javaGenes.EOSscheduling;
import junit.framework.TestCase;
import gov.nasa.alsUtility.Utility;
import java.util.Date;

public class HorizonTest extends TestCase {
private String time[] = {
    "1 Jan 2002 00:00:00.00",
    "1 Jan 2002 00:00:10.00",
    "2 Jan 2002 00:00:00.00",
    "3 Jan 2002 00:00:00.00"
};
private Horizon horizon10seconds;
private Horizon horizon1day;

public HorizonTest(String name) {super(name);}

protected void setUp() {
    horizon10seconds = new Horizon(time[0],time[1]);
    horizon1day = new Horizon(time[2],time[3]);
}
public void testConstructor() {
    Horizon horizon = horizon10seconds;
    assertTrue("1",horizon.getDuration() == 10);
    assertTrue("2",horizon.getStart() == 0);
    assertTrue("3",horizon.getEnd() == 10);
    assertTrue("4",horizon.isValid());

    horizon = horizon1day;
    int seconds = 24 * 60 * 60;
    assertTrue("1",horizon.getDuration() == seconds);
    assertTrue("2",horizon.getStart() == 0);
    assertTrue("3",horizon.getEnd() == seconds);
    assertTrue("4",horizon.isValid());
}
public void testTheSame() {
    Horizon h = horizon10seconds;
    Horizon same = horizon10seconds.copy();
    Horizon different = horizon1day.copy();
    assertTrue("1", same.theSame(h));
    assertTrue("2", h.theSame(same));
    assertTrue("3", !h.theSame(different));
}
public void testCopy() {
    Horizon horizon = horizon10seconds.copy();
    assertTrue("1",horizon.getStart() == horizon10seconds.getStart());
    assertTrue("2",horizon.getEnd() == horizon10seconds.getEnd());
    assertTrue("3",horizon.startDate.equals(horizon10seconds.startDate));
    assertTrue("4",horizon.endDate.equals(horizon10seconds.endDate));
}
public void testIncludes() {
    assertTrue("1",horizon1day.includes(horizon1day));
    assertTrue("2",horizon1day.includes(horizon1day.copy()));
    assertTrue("3",!horizon10seconds.includes(horizon1day));
    assertTrue("4",!horizon1day.includes(horizon10seconds));

    Horizon h1 = new Horizon(time[0],time[3]);
    assertTrue("5",h1.includes(horizon10seconds));
    assertTrue("6",!horizon1day.includes(h1));
    assertTrue("7",!horizon10seconds.includes(h1));
    assertTrue("8",h1.includes(horizon1day));

    Horizon h2 = new Horizon(time[1],time[2]);
    assertTrue("9",h1.includes(h2));
    assertTrue("10",!h2.includes(h1));
    
    Horizon h3 = new Horizon(time[1],time[3]);
    assertTrue("11",!h2.includes(h3));
    assertTrue("12",h3.includes(h2));
    
    Horizon h4 = new Horizon(time[0],time[2]);
    assertTrue("13",!h4.includes(h3));
    assertTrue("14",!h3.includes(h4));
}
public void testWindowIncludes() {
    Horizon h = horizon10seconds;
    AccessWindow w = new AccessWindow(0,10);
    assertTrue("13",h.includes(w));
    w.setStart(2);
    assertTrue("14",h.includes(w));
    w.setEnd(6);
    assertTrue("15",h.includes(w));
    w.setStart(-1);
    assertTrue("16",!h.includes(w));
    w.setStart(2);
    w.setEnd(11);
    assertTrue("17",!h.includes(w));
}
public void testGetIntegerTimeAt() {
    Date d = Utility.stkDateString2Date(time[0]);
    assertTrue("1",horizon10seconds.getIntegerTimeAt(d) == 0);
    d = Utility.stkDateString2Date(time[1]);
    assertTrue("2",horizon10seconds.getIntegerTimeAt(d) == 10);
    d = Utility.stkDateString2Date("1 Jan 2002 00:00:05.00");
    assertTrue("3",horizon10seconds.getIntegerTimeAt(d) == 5);
}
public void testGetDurationOf() {
    assertTrue("1",Horizon.getDurationOf(time[0],time[0]) == 0);
    assertTrue("2",Horizon.getDurationOf(time[0],time[1]) == 10);
    assertTrue("3",Horizon.getDurationOf(time[2],time[3]) == 24 * 60 * 60);
}
}
