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

import gov.nasa.alsUtility.IntegerInterval;
import gov.nasa.javaGenes.core.ChildMaker;
import gov.nasa.javaGenes.core.Evolvable;
/*
Used to generate a random graph as a sort of strange form of mutation.  Useful
for proving that the search algorithm is better than random search.
*/
public class GraphGenerator extends ChildMaker {
protected GraphParameters parameters;

public GraphGenerator(GraphParameters p) {
	parameters = p;
}
public int numberOfParents() {return 0;}
public Evolvable[] makeChildren(Evolvable[] parents) {
  IntegerInterval vertices = parameters.verticesInterval;
  IntegerInterval cycles = parameters.cyclesInterval;
  Evolvable[] kids = new Evolvable[1];
  kids[0] = new Graph(parameters.provider, vertices.random(), cycles.random());
  return kids;
}
}

