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

import gov.nasa.alsUtility.Error;

class NodeArray implements java.io.Serializable {
protected Node[] array;
protected int timeIncrement;
protected Horizon horizon;
static protected final boolean debug = Debug.debug;

public NodeArray(Horizon inHorizon, int inTimeIncrement) {
    horizon = inHorizon;
    timeIncrement = inTimeIncrement;
    Error.assertTrue(timeIncrement > 0);
    int numberOfArrayElements = horizon.getDuration() / timeIncrement;
    if (horizon.getDuration() % timeIncrement > 0)
        numberOfArrayElements++;
    Error.assertTrue(numberOfArrayElements >= 1);
    array = new Node[numberOfArrayElements];
}
public void initializeTo(Node allTime) {
    for(int i = 0; i < array.length; i++)
        array[i] = allTime;
    if (debug) 
        assertIsValid();
}
public void assertIsValid() {
    for(int i = 0; i < array.length; i++) {
        Node node = array[i];
        Error.assertTrue(node != null, "node is null at " + i);
        Error.assertTrue(node.next() != null, "next node is null at " + i);
        int time = i * timeIncrement;
        Error.assertTrue(node.getStart() <= time && time <= node.getEnd(),
            "bad times at index = " + i + "startTime = " + node.getStart() + ", endTime = " + node.getEnd());
    }
}
/**
@arg node has been added to list and may need array pointers
only call after Node list has been updated
*/
public void updateWith(Node node) {
    updateWith(node.getStart(),node.getEnd(),node);
}
/**
the time between startTime and endTime is now occupied by pointTo.  Update array accordingly.
Assumes that 0 is the earliest possible time.
<br>
only call after nodeList has been updated
*/
public void updateWith(int startTime, int endTime, Node pointTo) {
    if (debug) {
        Error.assertTrue(horizon.includes(startTime) || horizon.getEnd() == startTime);
        Error.assertTrue(pointTo.includes(startTime));
        Error.assertTrue(pointTo.getEnd() == endTime || pointTo.includes(endTime));
    }
    int first = startTime % timeIncrement == 0 ? getIndex(startTime) : getIndex(startTime)+1;
    int last = endTime % timeIncrement == 0 && endTime != 0 ? getIndex(endTime-1) : getIndex(endTime);
    if (last > array.length-1)
        last = array.length-1;
    for(int i = first; i <= last; i++)
        array[i] = pointTo;
}
public Node getNode(int atTime) {
    if (!horizon.includes(atTime)) {
        if (atTime == horizon.getEnd())
            return array[array.length-1];
        return null;
    }
    int index = getIndex(atTime);
    return array[index];
}
public int getIndex(int atTime) {
    int where = atTime - horizon.getStart();
    int index = where / timeIncrement;
    if (debug) 
        Error.assertTrue(index >= 0);
    return index;
}
}

