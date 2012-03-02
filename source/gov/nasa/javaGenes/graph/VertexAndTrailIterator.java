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

import gov.nasa.alsUtility.ExtendedVector;
import gov.nasa.alsUtility.SetMark;

/**
This iterator will return each vertex in g starting with the neighbors of v
using a breadth first search. Along with each vertex will be the shortest trail
to it from v. In cases where there are more than one shortest trail to a vertex,
that vertex will be returned once for each shortest trail along with the appropriate
trail.

@see Graph
@see Trail
*/
public class VertexAndTrailIterator {
ExtendedVector currentTrails;
int place;
/**
@param g the graph to find trails in
@param v the vertex to start from (starts every trail)
*/
public VertexAndTrailIterator(Graph g, Vertex v) {
    g.setVertexMarks(false);
    v.setMark(true);

    // set up state so next() will do the right thing on first call,then call next()
    currentTrails = new ExtendedVector();
    currentTrails.addElement(new Trail(v));
    place = 1;
    next();
}
/**
@return true if there are any more trails to iterate over
*/
public boolean more() {return place < currentTrails.size();}
/**
move to the next trail
*/
public void next() {
    place++;
    if (place < currentTrails.size())
        return;
    ExtendedVector nextTrails = new ExtendedVector();
    ExtendedVector vertices = new ExtendedVector();
    for(int i = 0; i < currentTrails.size(); i++){
        Vertex v = ((Trail)currentTrails.elementAt(i)).getEndVertex();
        for(EdgeIterator e = v.getEdgeIterator(); e.more(); e.next()) {
            Vertex candidate = e.edge().otherVertex(v);
            if (candidate.isMarked(false)){ 
                vertices.addElement(candidate);
                Trail t = ((Trail)currentTrails.elementAt(i)).mediumCopyTrail();
                t.add(e.edge());
                nextTrails.addElement(t);
            }
        }
    }
    vertices.executeOnAll(new SetMark(true));
    currentTrails = nextTrails;
    place = 0;
}
/**
@return the current vertex
*/
public Vertex vertex() {return ((Trail)currentTrails.elementAt(place)).getEndVertex();}
/**
@return the current trail
*/
public Trail trail() {return (Trail)currentTrails.elementAt(place);}
}
