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

import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.chemistry.Atom;

public class StillingerWeberPrintEnergiesAndForces {

public static void main(String[] arguments) {
  Error.assertTrue(arguments.length == 0);
  StillingerWeber potential = new StillingerWeberSiF();
  Chromosome chromosome = potential.getChromosome();
  PutPotentialEnergiesAndForces out = new PutPotentialEnergiesAndForces(potential,chromosome);
  out.putTwoBody("SiSiForcesAndEnergies.tsd", new Atom("Si"), new Atom("Si"), 100);
  out.putTwoBody("SiFForcesAndEnergies.tsd", new Atom("Si"), new Atom("F"), 100);
  out.putTwoBody("FFForcesAndEnergies.tsd", new Atom("F"), new Atom("F"), 100);
  Atom Si = new Atom("Si");
  out.putThreeBody("SiSiSiEnergies.tsd",Si,Si,Si,20);
}
}