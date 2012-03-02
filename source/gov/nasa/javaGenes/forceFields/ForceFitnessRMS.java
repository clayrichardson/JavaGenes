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
import gov.nasa.alsUtility.FieldRecordText;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;

// uses energy fitness framework.  Term 'energies' should read 'forces' for this class
public class ForceFitnessRMS extends ManyMoleculesEnergyFitness {
protected static final int distanceColumn = 0;
protected static final int forceColumn = 1;
protected static final String distanceColumnHeading = "Si-SiDistance";
protected static final String forceColumnHeading = "force";
protected static final String atom = "Si"; // LIMITATION: only do Si dimers for now

public ForceFitnessRMS(Potential p, double lengthScale, String forcesFilename) {
  potential = p;
  molecules = new ManyMultiBodiesForOneEnergy();

  FieldRecordText input = new FieldRecordText(forcesFilename,"\t");
  String[] fields = input.readLine();
  Error.assertTrue(fields.length == 2);
  Error.assertTrue(fields[0].equals(distanceColumnHeading));
  Error.assertTrue(fields[1].equals(forceColumnHeading));

  while((fields = input.readLine()) != null) {
    Error.assertTrue(fields.length == 2);
    double distance = Utility.string2double(fields[distanceColumn]);
    double force = Utility.string2double(fields[forceColumn]);
    MultiBodiesForOneEnergy m = new MultiBodiesForOneEnergy();
    m.add(new TwoBody(atom,atom,distance));
    add(m,force);
  }
  input.close();

  molecules.scaleLengthsBy(lengthScale);
}
protected double calculateEnergy(MultiBodiesForOneEnergy test) {
  MultiBody twoBody = test.get(0);
  Error.assertTrue(twoBody instanceof TwoBody);
  return potential.getForce((TwoBody)twoBody);
}
}