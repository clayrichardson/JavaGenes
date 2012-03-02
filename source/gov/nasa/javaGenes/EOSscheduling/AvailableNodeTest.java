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
//  Created by Al Globus on Thu May 01 2003.
package gov.nasa.javaGenes.EOSscheduling;

import junit.framework.TestCase;

public class AvailableNodeTest extends TestCase {

public AvailableNodeTest(String name) {super(name);}
public void testNextPreviousAvailableNode() {
    AvailableNode n = new AvailableNode();
    n.setAvailable(true);
    assertTrue("1", n.nextAvailableNode() == null);
    assertTrue("2", n.previousAvailableNode() == null);

    AvailableNode a = new AvailableNode();
    a.setAvailable(false);
    AvailableNode b = new AvailableNode();
    b.setAvailable(false);
    n.setPrevious(a); a.setNext(n);
    n.setNext(b); b.setPrevious(n);
    assertTrue("3", n.nextAvailableNode() == null);
    assertTrue("4", n.previousAvailableNode() == null);
    assertTrue("5", a.nextAvailableNode() == n);
    assertTrue("6", a.previousAvailableNode() == null);
    assertTrue("7", b.nextAvailableNode() == null);
    assertTrue("8", b.previousAvailableNode() == n);

    AvailableNode c = new AvailableNode();
    c.setAvailable(true);
    AvailableNode e = new AvailableNode();
    e.setAvailable(true);
    a.setPrevious(c); c.setNext(a);
    b.setNext(e); e.setPrevious(b);
    assertTrue("9", n.nextAvailableNode() == e);
    assertTrue("10", n.previousAvailableNode() == c);
    assertTrue("11", a.nextAvailableNode() == n);
    assertTrue("12", a.previousAvailableNode() == c);
    assertTrue("13", b.nextAvailableNode() == e);
    assertTrue("14", b.previousAvailableNode() == n);
    assertTrue("15", c.nextAvailableNode() == n);
    assertTrue("16", c.previousAvailableNode() == null);
    assertTrue("17", e.nextAvailableNode() == null);
    assertTrue("18", e.previousAvailableNode() == n);
}
}
