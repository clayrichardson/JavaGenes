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
package gov.nasa.javaGenes.forceFields;

import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.chemistry.Atom;
import gov.nasa.javaGenes.chemistry.Molecule;
import gov.nasa.javaGenes.chemistry.xyzFormat;
import gov.nasa.alsUtility.IncrementIterator;

public class MakeDimer {
public static void main(String[] arguments) {
  Error.assertTrue(arguments.length == 6);
  String outputFilename = arguments[0];
  String firstElement = arguments[1];
  String secondElement = arguments[2];

  double shortest = Utility.string2double(arguments[3]);
  Error.assertTrue(shortest >= 0);
  double longest = Utility.string2double(arguments[4]);
  Error.assertTrue(longest >= shortest);

  int numberOfMolecules = Utility.string2integer(arguments[5]);
  Error.assertTrue(numberOfMolecules >= 1);

  Molecule startingPoint = new Molecule();

  Atom a = new Atom(firstElement);
  a.setXyz(0,0,0);
  Atom b = new Atom(secondElement);
  Molecule molecule = new Molecule();
  molecule.add(a);
  molecule.add(b);

  if (numberOfMolecules == 1) {
    setLength(b,(shortest + longest)/2);
    xyzFormat.writeToFile(molecule,outputFilename);
    return;
  }

  boolean firstTime = true;
  for(IncrementIterator i = new IncrementIterator(shortest,longest,numberOfMolecules); i.more(); i.increment()) {
    final double length = i.value();
    setLength(b,length);
    if (firstTime) {
      firstTime = false;
      xyzFormat.writeToFile(molecule,outputFilename);
    } else
      xyzFormat.appendToFile(molecule,outputFilename);
  }
}

private static void setLength(Atom a, double distance) {
  a.setXyz(distance,0,0);
}
}
