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

import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.RandomNumber;

public class randomizeXYZ {
static final String formatString =
    "java randomizeXYZ numberOfVariations distanceToRandomize minimumAtomSpacing (gaussian|flat) inputFile.xyz outputFile.xyz [numberOfAtomsToMoveIncrement] [randomNumberSeed]";

public static void main(String[] arguments) {
  if (arguments.length < 6 || 8 < arguments.length)
    userError("wrong number of arguments");
  if (arguments.length == 8)
    RandomNumber.setSeed(Utility.string2long(arguments[7]));

  int numberOfVariations = Utility.string2integer(arguments[0]);
  Error.assertTrue(numberOfVariations > 0);
  double distanceToRandomize = Utility.string2double(arguments[1]);
  Error.assertTrue(distanceToRandomize > 0);
  double minimumDistance = Utility.string2double(arguments[2]);
  boolean gaussianDistribution = false;
  if (arguments[3].equals("gaussian"))
    gaussianDistribution = true;
  else if (arguments[3].equals("flat"))
    gaussianDistribution = false;
  else
    userError("fourth argument must be 'gaussian' or 'flat'");
  String inputXyzFilename = arguments[4];
  String outputXyzFilename = arguments[5];
  boolean changeNumberOfMovingAtoms = false;
  int movingAtomsIncrement = 0;
  if (arguments.length >= 7) {
    changeNumberOfMovingAtoms = true;
    movingAtomsIncrement = Utility.string2integer(arguments[6]);
  }
  Molecule startingPoint = xyzFormat.read(inputXyzFilename);
  xyzFormat.writeToFile(startingPoint,outputXyzFilename);

  int moveAtoms = movingAtomsIncrement;
  for(int i = 1; i <= numberOfVariations; i++) {
    Molecule next = startingPoint.copy();
    next.setComment(startingPoint.getComment()); // needed for any unit cell
    if (changeNumberOfMovingAtoms)
      next.randomizeAtomicPositionsBy(distanceToRandomize,gaussianDistribution,moveAtoms);
    else
      next.randomizeAtomicPositionsBy(distanceToRandomize,gaussianDistribution);
    if (next.hasVertexPairCloserThan(minimumDistance)) {
      i--;
      continue;
    }
    xyzFormat.appendToFile(next,outputXyzFilename);
    moveAtoms += movingAtomsIncrement;
  }
}

private static void userError(String error) {
  System.out.println(error);
  System.out.println("Format: " + formatString);
  System.exit(1);
}
} 