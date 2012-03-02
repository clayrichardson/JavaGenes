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
import gov.nasa.javaGenes.chemistry.Molecule;
import gov.nasa.javaGenes.chemistry.Atom;
import gov.nasa.alsUtility.IncrementIterator;
import gov.nasa.javaGenes.chemistry.xyzFormat;

public class MakeDimerPair {
public static void main(String[] arguments) {
  Error.assertTrue(arguments.length == 13);

  String outputFilename = arguments[0];

  String firstElement = arguments[1];
  String secondElement = arguments[2];
  String thirdElement = arguments[3];
  String fourthElement = arguments[4];

  double bondLength = Utility.string2double(arguments[5]);
  Error.assertTrue(bondLength > 0);
  double shortest = Utility.string2double(arguments[6]);
  Error.assertTrue(shortest >= 0);
  double longest = Utility.string2double(arguments[7]);
  Error.assertTrue(longest >= shortest);
  int numberOfLengths = Utility.string2integer(arguments[8]);
  Error.assertTrue(numberOfLengths >= 2);

  double dimerDistance = Utility.string2double(arguments[9]); // distance from 0,0,0 atom to center of other dimer
  Error.assertTrue(dimerDistance > 0);

  // angles are from 0,0,0 atom to center of dimer
  double shortestAngle = Utility.degrees2radians(Utility.string2double(arguments[10]));
  Error.assertTrue(shortestAngle >= 0);
  double longestAngle = Utility.degrees2radians(Utility.string2double(arguments[11]));
  Error.assertTrue(longestAngle >= shortestAngle);
  int numberOfAngles = Utility.string2integer(arguments[12]);
  Error.assertTrue(numberOfAngles >= 2);

  Molecule startingPoint = new Molecule();

  Atom a = new Atom(firstElement);
  a.setXyz(0,0,0);
  Atom b = new Atom(secondElement);
  b.setXyz(bondLength,0,0);
  Atom c = new Atom(thirdElement);
  Atom d = new Atom(fourthElement);
  Molecule molecule = new Molecule();
  molecule.add(a);
  molecule.add(b);
  molecule.add(c);
  molecule.add(d);

  boolean firstTime = true;
  for(IncrementIterator i = new IncrementIterator(shortest,longest,numberOfLengths); i.more(); i.increment()) {
    final double length = i.value();
    final double z = length/2;
    for(IncrementIterator j = new IncrementIterator(shortestAngle,longestAngle,numberOfAngles); j.more(); j.increment()) {
      final double angle = j.value();
      final double x = dimerDistance * Math.cos(angle);
      final double y = dimerDistance * Math.sin(angle);
      c.setXyz(x,y,z);
      d.setXyz(x,y,-z);
      if (firstTime) {
        firstTime = false;
        xyzFormat.writeToFile(molecule,outputFilename);
      } else
        xyzFormat.appendToFile(molecule,outputFilename);
    }
  }
}
}

