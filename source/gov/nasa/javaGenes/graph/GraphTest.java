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
package gov.nasa.javaGenes.graph;import junit.framework.TestCase;public class GraphTest extends TestCase {public GraphTest(String name) {super(name);}public void setUp() {}public void testHasVertexPairCloserThan() {  Vertex vertex1 = new Vertex();  vertex1.setXyz(0,0,0);  Vertex vertex2 = new Vertex();  vertex2.setXyz(2,0,0);  Vertex vertex3 = new Vertex();  vertex3.setXyz(1,0,0);  Graph graph = new Graph();  graph.add(vertex1);  graph.add(vertex2);  graph.add(vertex3);  assertTrue(graph.hasVertexPairCloserThan(1.5));  assertTrue(!graph.hasVertexPairCloserThan(1));  assertTrue(!graph.hasVertexPairCloserThan(0.5));}}