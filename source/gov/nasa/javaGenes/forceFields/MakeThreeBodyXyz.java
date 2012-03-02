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

public class MakeThreeBodyXyz {
public static void main(String[] arguments) {
  String format = "java MakeThreeBodyXyz outputFilename atom centerAtom atom shortest longest narrowest widest squareRootOfNumberOfMolecules";
  if (arguments.length != 9)
    Error.fatal(format);
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
  Atom c = new Atom(thirdElement);

  Molecule molecule = new Molecule();
  molecule.add(a);
  molecule.add(b);
  molecule.add(c);

  boolean firstTime = true;
  IncrementIterator lengthIterator = new IncrementIterator(shortest,longest,numberOfMolecules);
  for(;lengthIterator.more(); lengthIterator.increment()) {
    double length = lengthIterator.value();
    a.setXyz(lengthIterator.value(),0,0);
    IncrementIterator angleIterator = new IncrementIterator(narrowest,widest,numberOfMolecules);
    for(;angleIterator.more(); angleIterator.increment()) {
      double angle = angleIterator.value();
      double radians = Utility.degrees2radians(angle);
      double x = length * Math.sin(radians);
      double y = length * Math.cos(radians);
      c.setXyz(x,y,0);
      if (firstTime) {
        xyzFormat.writeToFile(molecule,outputFilename);
        firstTime = false;
      }
      else
        xyzFormat.appendToFile(molecule,outputFilename);
    }
  }
}
}
