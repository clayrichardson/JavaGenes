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

public class MakeTrimer {
public static void main(String[] arguments) {
  Error.assertTrue(arguments.length == 9);
  String outputFilename = arguments[0];
  String firstElement = arguments[1];
  String secondElement = arguments[2];
  String thirdElement = arguments[3];

  double shortest = Utility.string2double(arguments[4]);
  Error.assertTrue(shortest >= 0);
  double longest = Utility.string2double(arguments[5]);
  Error.assertTrue(longest >= shortest);

  double narrowest = Utility.string2double(arguments[6]);
  Error.assertTrue(shortest >= 0);
  double widest = Utility.string2double(arguments[7]);
  Error.assertTrue(longest >= shortest);

  int numberOfMolecules = Utility.string2integer(arguments[8]);
  Error.assertTrue(numberOfMolecules >= 1);

  Molecule startingPoint = new Molecule();

  Atom a = new Atom(firstElement);
  Atom b = new Atom(secondElement);
  b.setXyz(0,0,0);
  Atom c = new Atom(secondElement);

  Molecule molecule = new Molecule();
  molecule.add(a);
  molecule.add(b);
  molecule.add(c);

  if (numberOfMolecules == 1) {
    double length = (shortest + longest)/2;
    setLength(a,length);
    setAngle(c,length,(narrowest + widest)/2);
    xyzFormat.writeToFile(molecule,outputFilename);
    return;
  }

  double increment = (longest - shortest) / (numberOfMolecules-1);
  for(int i = 0; i < numberOfMolecules; i++) {
    setLength(b,shortest + (i * increment));
    if (i == numberOfMolecules-1)
      setLength(b,longest);
    if ( i == 0)
      xyzFormat.writeToFile(molecule,outputFilename);
    else
      xyzFormat.appendToFile(molecule,outputFilename);
  }
}

private static void setLength(Atom a, double distance) {
  a.setXyz(distance,0,0);
}
private static void setAngle(Atom a, double distance, double angle) {
  double radians = Utility.degrees2radians(angle);
  double x = distance * Math.sin(radians);
  double y = distance * Math.cos(radians);
  a.setXyz(x,y,0);
}
}
