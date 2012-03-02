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

import java.io.Serializable;
import gov.nasa.alsUtility.IntegerInterval;
import gov.nasa.javaGenes.core.Parameters;

/**
Parameters and setup for genetic graph runs.

@see GraphIndividual
@see GraphPopulation
@see GraphReporter
*/
public class GraphParameters extends Parameters implements Serializable{

/**
an interval to choose a random number of Graph vertices from. Used
for generation of a random GraphPopulation.

@see GraphPopulation
*/
public IntegerInterval verticesInterval = new IntegerInterval(2,10);
/**
an interval to choose a random number of Graph cycles from. Used
for generation of a random GraphPopulation.

@see GraphPopulation
*/
public IntegerInterval cyclesInterval = new IntegerInterval(0,2);
/**
will provide random vertices and edges for random GraphPopulation
construction.

@see GraphPopulation
*/
public VertexAndEdgeProvider provider = new VertexAndEdgeProvider();


public boolean layoutGraph2d = true;
public GraphLayout layout = new GraphLayout();
}
