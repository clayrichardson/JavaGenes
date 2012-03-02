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
package gov.nasa.javaGenes.graph;

import gov.nasa.alsUtility.Predicate;

/**
predicate that determines when vertices can accept an additional edge
*/
public class VertexAcceptsEdge implements Predicate {
/**
the edge to be accepted
*/
protected Edge edge = null;
/**
@param e the edge to be accepted. May be null to ask if any edge can be accepted.
*/
public VertexAcceptsEdge(Edge e) {edge = e;}
/**
used when asking if any edge may be accepted
*/
public VertexAcceptsEdge() {edge = null;}
/**
@return true if v except edge. If edge == null, true if v can accept any edge
*/
public boolean execute(Object v) {return ((Vertex)v).canAcceptEdge(edge);}
}
