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

public class AvailableNode extends Node {
boolean isAvailable;
protected boolean isAvailable() {return isAvailable;}
protected boolean isUnAvailable() {return !isAvailable;}
protected void setAvailable(boolean inIsAvailable) {isAvailable = inIsAvailable;}
protected AvailableNode nextAvailableNode() { 
    AvailableNode n = (AvailableNode)next(); 
    while (n != null && n.isUnAvailable())
        n = (AvailableNode)n.next();
    return n;
}
protected AvailableNode previousAvailableNode() { 
    AvailableNode n = (AvailableNode)previous(); 
    while (n != null && n.isUnAvailable())
        n = (AvailableNode)n.previous();
    return n;
}
protected AvailableNode nextUnAvailableNode() { 
    AvailableNode n = (AvailableNode)next(); 
    while (n != null && n.isAvailable())
        n = (AvailableNode)n.next();
    return n;
}
protected AvailableNode previousUnAvailableNode() { 
    AvailableNode n = (AvailableNode)previous(); 
    while (n != null && n.isAvailable())
        n = (AvailableNode)n.previous();
    return n;
}
    
}
