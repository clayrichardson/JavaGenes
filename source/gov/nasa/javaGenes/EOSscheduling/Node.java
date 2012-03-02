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

import gov.nasa.alsUtility.Error;
import java.lang.Math;

public class Node extends TimeInterval {
protected int startTime;
protected Node previous;
protected Node next;
private static final boolean debug = Debug.debug;
/**
should only be called from NodeFactory and subclasses (via NodeFactory)
*/
protected Node() {} 
protected void setTimeAndNeighbors(int inStartTime, Node previous, Node next) {
    setStart(inStartTime);
    setPrevious(previous);
    setNext(next);
}
public int getStart() {return startTime;}
protected void setStart(int inStartTime) {startTime = inStartTime;}
public int getEnd() {return next.startTime;}
protected void setEnd(int inEndTime) {next().setStart(inEndTime);}
public Node next() {return next;}
protected void setNext(Node node) {next = node;}
public Node previous() {return previous;}
protected void setPrevious(Node node) {previous = node;}
/**
@return true if can start in this node and finish by the end of it
*/
protected boolean isDurationAvailable(int inStartTime,int inDuration) {
    if (debug) Error.assertTrue(inDuration > 0);
    return startTime <= inStartTime && Math.max(inStartTime,startTime) + inDuration <= getEnd();
}

}
