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

import java.util.Vector;
import gov.nasa.alsUtility.ObjectCache;

/**
used to reuse and avoid garbage collecting Nodes. Number of nodes grows monotonically for life of the object.
*/
public class NodeFactory implements java.io.Serializable {    
private ObjectCache availableNodeCache = new ObjectCache("gov.nasa.javaGenes.EOSscheduling.AvailableNode");
private ObjectCache slewNodeCache = new ObjectCache("gov.nasa.javaGenes.EOSscheduling.SlewNode");
private ObjectCache SSRNodeCache = new ObjectCache("gov.nasa.javaGenes.EOSscheduling.SSRNode");

/**
only call when all Nodes are no longer needed (or suffer a horrible fate)
*/
protected void initialize() {
    availableNodeCache.reinitialize();
    slewNodeCache.reinitialize();
    SSRNodeCache.reinitialize();
}

protected SSRNode newSSRNode(int capacity, int startTime, Node previous, Node next) {
    SSRNode n = (SSRNode)SSRNodeCache.getObject();
    n.setTimeAndNeighbors(startTime, previous, next);
    n.setCapacity(capacity);
    return n;
}
private AvailableNode createNewAvailableNode(int startTime, Node previous, Node next) {
    AvailableNode n = (AvailableNode)availableNodeCache.getObject();
    n.setTimeAndNeighbors(startTime, previous, next);
    return n;
}
protected AvailableNode newAvailableNode(int startTime, Node previous, Node next) {
    AvailableNode node = createNewAvailableNode(startTime,previous,next);
    node.setAvailable(true);
    return node;
}
protected AvailableNode newUnAvailableNode(int startTime, Node previous, Node next) {
    AvailableNode node = createNewAvailableNode(startTime,previous,next);
    node.setAvailable(false);
    return node;
}
protected SlewNode newSlewNode(SlewRequirement slew, boolean mustRampToEnd, int startTime, Node previous, Node next) {
    SlewNode s = (SlewNode)slewNodeCache.getObject();
    s.setTimeAndNeighbors(startTime,previous,next);
    s.setSlew(slew);
    s.setMustRampToEnd(mustRampToEnd);
    return s;
}
}        
