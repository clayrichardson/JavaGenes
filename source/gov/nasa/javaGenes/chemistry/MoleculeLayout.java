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
package gov.nasa.javaGenes.chemistry;

import gov.nasa.javaGenes.graph.GraphLayout;

/**
would usually have one static method called main (it's called tasks here to get
around a development environment problem).  This would implement and application
that read a mol file and layed it out in two dimensions for viewing.
*/
public class MoleculeLayout {
public static void test(String[] arguments) {
  if (arguments.length < 2 || 3 < arguments.length) {
    System.out.println("format: java MoleculeLayout infile.mol outfile.mol [iterations]");
    System.exit(1);
  }
  Molecule graph = molFormat.read(arguments[0]);
  GraphLayout layout = new GraphLayout();
  if (arguments.length == 3) {
    Integer i = new java.lang.Integer(arguments[2]);
    layout.iterations = i.intValue();
  }
  layout.layout2d (graph);
  molFormat format = new molFormat();
  format.writeFile(graph, arguments[1]);
}
} 