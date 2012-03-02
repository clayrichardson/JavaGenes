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
package gov.nasa.alsUtility;


import java.io.Serializable;
import java.util.Date;

/**
time things using wallclock time. Will also keep track of time allocated
for a task and let you know when time is up.
*/
public class Timer implements Serializable {
protected long timeAllocated = 0;
protected long timeUsed = 0;
protected long lastStart = 0;
protected boolean running = false;

public String toString() {
    String s = "timeAllocated = " + timeAllocated + "\n";
    s += "timeUsed = " + timeUsed + "\n";
    s += "running = " + running + "\n";
    s += "timeElapsed = " + timeElapsed() + "\n";
    return s;
}

public Timer () {}
/**
@param t sets the time allocated
*/
public Timer (long t) {timeAllocated = t;}
/**
resets the timer and stops
@param t sets the time allocated
*/
public void reset (long t) {
    reset(); 
    timeAllocated = t;
}
/**
resets the timer and stops
*/
public void reset() {
     stop();
     timeUsed = 0;
}
/**
stops the clock and remembers the time used
*/
public void stop() { 
    if (running)
       timeUsed += System.currentTimeMillis() - lastStart; 
    running = false;
}
/**
starts the clock
*/
public void start() { 
    if (!running)
        lastStart = System.currentTimeMillis();
    running = true;
}
/**
has the allocated time been used?
*/
public boolean isDone() { return (timeAllocated - timeElapsed()) < 0;}
public long timeElapsed() {
    return timeUsed + (running ?  System.currentTimeMillis() - lastStart : 0);
}
/**
how much allocated time is still available?
*/
public long timeAvailable() {return timeAllocated - timeElapsed();}

public static void test (String[] arguments) {
    Timer timer = new Timer (1000);
    System.out.println (timer.toString());
    timer.start();
    System.out.println (timer.toString());
    wasteTime();
    timer.stop();
    System.out.println (timer.toString());
    timer.reset(10);    
    System.out.println (timer.toString());
    timer.start();
    wasteTime();
    System.out.println (timer.toString());
    timer.stop();
}

private static void wasteTime() {
    int j;
    for(int i = 0; i < 1000000; i++)
         j = i*i;
}
}
