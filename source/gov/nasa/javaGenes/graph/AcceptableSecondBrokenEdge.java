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
The execute() method will determine whether a BrokenEdge may be merged
with the BrokenEdge argument to the constructor. 
*/
public class AcceptableSecondBrokenEdge implements Predicate {
protected BrokenEdge first;
protected boolean mustBeCompatible;
private static final boolean debug = false;
/**
@param b BrokenEdge seeking a compatible BrokenEdge
@param compatible determines whether the BrokenEdges must be compatible (isCompatible()
returns true) to
be merged (for example, both be double bonds). If "compatible" is false, the
merger can take place so long as the vertex attached to the execute() argument
can accept any edge and the merger won't create a length one loop.
*/
public AcceptableSecondBrokenEdge(BrokenEdge b, boolean compatible) {
	first = b;
	mustBeCompatible = compatible;
}
/**
@param object BrokenEdge object to be checked for compatibility
*/        
public boolean execute(Object object){
    BrokenEdge b = (BrokenEdge)object;
    Vertex v = b.vertex;
    if (debug) {
    	boolean compatible =  first.isCompatible(b);
    	boolean accept =  v.canAcceptEdge();
    	boolean has =  !v.hasEdgeTo(first.vertex);
    }
    return (mustBeCompatible ? first.isCompatible(b):true)
           && v.canAcceptEdge() && !v.hasEdgeTo(first.vertex);
}
}

