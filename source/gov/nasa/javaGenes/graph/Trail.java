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


import java.lang.CloneNotSupportedException;
import java.lang.Cloneable;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.ExtendedVector;
import gov.nasa.alsUtility.Predicate;


/**
A trail of edges between ends[0] and ends[1]. The edges must be in order, each sharing a common
vertex. The first edge must connect ends[0] and the last edge connect ends[1].
 <p>
The edges and vertices are never copied or cloned, they are identical to those in the graph.
*/
public class Trail implements Cloneable{
protected ExtendedVector edges = new ExtendedVector();
protected Vertex[] ends = new Vertex[2];

/* 
handle the degenerate case of a trail from a vertex to itself by
setting both ends to to v
*/
public Trail(Vertex v) {ends[0] = v; ends[1] = v;}
/**
keeps original edges and vertices
*/
public Trail mediumCopyTrail() {
    try {
        return (Trail)clone();
    } catch (CloneNotSupportedException e) {
        Error.fatal("can't clone object: " + e);
        return null;
    }
}
/**
keeps original edges and vertices
*/
public Object clone() throws CloneNotSupportedException {
    Trail trail = (Trail)super.clone();
    trail.edges = (ExtendedVector)edges.clone();
    trail.ends = new Vertex[2];
    trail.ends[0] = ends[0];
    trail.ends[1] = ends[1];
    return trail;
}
/**
add a new edge to the trail and update ends[1]
*/
public void add(Edge e) {
    Error.assertTrue(e.doesConnect(ends[1]));
    edges.addElement(e);
    ends[1] = e.otherVertex(ends[1]);
}
/**
@return the vertex of e closest to end
*/
public Vertex getVertexInEdgeClosestToEnd(Edge e, Vertex end) {
    if (e.doesConnect(end)) return end;
    int index = edges.indexOf(e); 
    Error.assertTrue(index != -1);
    
    int otherOffset = 0;
    if (end.equals(ends[0]))
        otherOffset = -1;
    else if (end.equals(ends[1]))
        otherOffset = 1;
    else
        Error.fatal("argument end must be at beginning or end of trail");
    Edge other = (Edge)edges.elementAt(index + otherOffset);
    Vertex vertex = other.commonVertex(e);
    Error.assertTrue(vertex != null);
    return vertex;
}
/**
@return a string suitable for use as a hashtable key for this trail
*/
public String getStringKey(){ // PERFORMANCE cache this
    final String to = " to ";
    String s1 = ends[0].getExtendedTypeString();
    String s2 = ends[1].getExtendedTypeString();
    StringBuffer r = new StringBuffer();
    if (s1.compareTo(s2) < 0) {
    	r.append(s1);
    	r.append(to);
    	r.append(s2);
    } else { 
        r.append(s2);
    	r.append(to);
    	r.append(s1);
	}
	r.append(" distance ");
	r.append(edges.size());
    return r.toString();
}

public Edge getRandomEdge() {return (Edge)edges.getRandomElement();}
/**
@return a random edge that satisfies p
*/
public Edge getRandomEdge(Predicate p) {return (Edge)edges.getRandomElement(p);}
public Vertex getStartVertex() {return ends[0];}
public Vertex getEndVertex() {return ends[1];}
}
