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
mutate an edge
*/
public class MutateEdge extends GraphMutation {
/**
provides the edges to mutate to
*/
protected VertexAndEdgeProvider provider;
public MutateEdge(VertexAndEdgeProvider p) {provider = p;}

public Graph makeChild(Graph child) {
  Edge newEdge = provider.getEdge();
  Edge oldEdge = child.getRandomEdge(new CanReplace(newEdge));
  if (oldEdge == null) return null;
  child.replaceEdge(oldEdge,newEdge);
  return child;
}
private class CanReplace implements Predicate {
  protected Edge replaceWith;
  public CanReplace(Edge e) {replaceWith = e;}
  public boolean execute (Object object) {
    Edge e = (Edge) object;
    return !replaceWith.isSame (e) && replaceWith.canAcceptVerticesOf (e);
}
}
}

