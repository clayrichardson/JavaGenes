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

public class NodeArrayTest extends TestCase {
private NodeFactory nodeFactory;
private NodeArray nodeArray;
private AvailableNode allNode;
private Horizon horizon;
private NodeArray nodeArray2;
private AvailableNode allNode2;
private Horizon horizon2;
private String time[] = {
    "1 Jan 2002 00:00:00.00",
    "1 Jan 2002 00:00:10.00",
    "1 Jan 2002 00:00:09.00"
};
public NodeArrayTest(String name) {super(name);}

protected void setUp() {
    final int timeInterval = 3;
    
    nodeFactory = new NodeFactory();
    horizon = new Horizon(time[0],time[1]);
    nodeArray = new NodeArray(horizon,timeInterval);
    allNode = makeNode(horizon.getStart(),horizon.getEnd());
    nodeArray.initializeTo(allNode);

    // case where duration % timeInterval == 0
    horizon2 = new Horizon(time[0],time[2]);
    nodeArray2 = new NodeArray(horizon2,timeInterval);
    allNode2 = makeNode(horizon2.getStart(),horizon2.getEnd());
    nodeArray2.initializeTo(allNode2);
}
private AvailableNode makeNode(int start, int end) {
    AvailableNode endNode = nodeFactory.newUnAvailableNode(end,null,null);
    AvailableNode node = nodeFactory.newAvailableNode(start,null,endNode);
    endNode.setPrevious(node);
    return node;
}    
public void testInitializeTo() {
    nodeArray.assertIsValid();
    nodeArray2.assertIsValid();
}
public void testUpdateWith1() {
    AvailableNode node = makeNode(2,8);
    nodeArray.updateWith(node);
    nodeArray.assertIsValid();
    nodeArray2.updateWith(node);
    nodeArray2.assertIsValid();
    for(int i = horizon.getStart(); i <= horizon.getEnd(); i++)
        if (3 <= i && i <= 8)
            assertTrue(i+"",nodeArray.getNode(i) == node);
        else
            assertTrue(i+"",nodeArray.getNode(i) == allNode);
    for(int i = horizon2.getStart(); i <= horizon2.getEnd(); i++)
        if (3 <= i && i <= 9)
            assertTrue(i+"e",nodeArray2.getNode(i) == node);
        else
            assertTrue(i+"e",nodeArray2.getNode(i) == allNode2);
}
public void testUpdateWith2() {
    AvailableNode node = makeNode(0,5);
    nodeArray.updateWith(node);
    nodeArray.assertIsValid();
    nodeArray2.updateWith(node);
    nodeArray2.assertIsValid();
    for(int i = horizon.getStart(); i <= horizon.getEnd(); i++)
        if (i <= 5)
            assertTrue(i+"",nodeArray.getNode(i) == node);
        else
            assertTrue(i+"",nodeArray.getNode(i) == allNode);
    for(int i = horizon2.getStart(); i <= horizon2.getEnd(); i++)
        if (i <= 5)
            assertTrue(i+"e",nodeArray2.getNode(i) == node);
        else
            assertTrue(i+"e",nodeArray2.getNode(i) == allNode2);
}
public void testUpdateWith3() {
    AvailableNode node = makeNode(7,10);
    nodeArray.updateWith(node);
    nodeArray.assertIsValid();
    AvailableNode node2 = makeNode(7,9);
    nodeArray2.updateWith(node2);
    nodeArray2.assertIsValid();
    for(int i = horizon.getStart(); i <= horizon.getEnd(); i++)
        if (9 <= i)
            assertTrue(i+"",nodeArray.getNode(i) == node);
        else
            assertTrue(i+"",nodeArray.getNode(i) == allNode);
    for(int i = horizon2.getStart(); i <= horizon2.getEnd(); i++)
        assertTrue(i+"e",nodeArray2.getNode(i) == allNode2);
}
public void testUpdateWith4() {
    AvailableNode node = makeNode(4,5);
    nodeArray.updateWith(node);
    nodeArray.assertIsValid();
    nodeArray2.updateWith(node);
    nodeArray2.assertIsValid();
    for(int i = horizon.getStart(); i <= horizon.getEnd(); i++)
            assertTrue(i+"",nodeArray.getNode(i) == allNode);
    for(int i = horizon2.getStart(); i <= horizon2.getEnd(); i++)
            assertTrue(i+"e",nodeArray2.getNode(i) == allNode2);
}
public void testUpdateWith5() {
    AvailableNode node = makeNode(3,4);
    nodeArray.updateWith(node);
    nodeArray.assertIsValid();
    nodeArray2.updateWith(node);
    nodeArray2.assertIsValid();
    for(int i = horizon.getStart(); i <= horizon.getEnd(); i++)
        if (3 <= i && i <= 5)
            assertTrue(i+"",nodeArray.getNode(i) == node);
        else
            assertTrue(i+"",nodeArray.getNode(i) == allNode);
    for(int i = horizon2.getStart(); i <= horizon2.getEnd(); i++)
        if (3 <= i && i <= 5)
            assertTrue(i+"e",nodeArray2.getNode(i) == node);
        else
            assertTrue(i+"e",nodeArray2.getNode(i) == allNode2);
}
public void testUpdateWith6() {
    AvailableNode node = makeNode(1,2);
    nodeArray.updateWith(node);
    nodeArray.assertIsValid();
    nodeArray2.updateWith(node);
    nodeArray2.assertIsValid();
    for(int i = horizon.getStart(); i <= horizon.getEnd(); i++)
        assertTrue(i+"",nodeArray.getNode(i) == allNode);
    for(int i = horizon2.getStart(); i <= horizon2.getEnd(); i++)
        assertTrue(i+"e",nodeArray2.getNode(i) == allNode2);
}
}