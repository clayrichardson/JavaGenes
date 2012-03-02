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
import gov.nasa.alsUtility.Vector3d;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import java.util.Vector;
import gov.nasa.javaGenes.chemistry.Molecule;
import gov.nasa.javaGenes.chemistry.Atom;
import gov.nasa.javaGenes.chemistry.molFormat;
import gov.nasa.javaGenes.chemistry.xyzFormat;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.alsUtility.ExtendedVector;

/**
requires SiF4.mol, Si2F6.mol, and Si3F8.mol to be in current directory.
*/
public class MakeSiFMolecules {
public static void main(String[] arguments) {
  if (arguments.length != 5)
    Error.fatal("format: java MakeSiFMolecules numberSiF numberSiSi numberSiSiSi randomizeBy minimumDistance");
  ExtendedVector molecules;
  molecules = forSiF(molFormat.read("SiF4.mol"), Utility.string2integer(arguments[0]));
  xyzFormat.writeMolecules(molecules,"SiF.xyz");
  molecules = forSiSi(molFormat.read("Si2F6.mol"), Utility.string2integer(arguments[1]));
  xyzFormat.writeMolecules(molecules,"SiSi.xyz");
  molecules = forSiSiSi(molFormat.read("Si3F8.mol"),Utility.string2integer(arguments[2])-1,Utility.string2double(arguments[3]),Utility.string2double(arguments[4]));
  xyzFormat.writeMolecules(molecules,"SiSiSi.xyz");
}
private static ExtendedVector forSiF(Molecule molecule, int number) {
  Atom Si = molecule.getAtom("Si"); Error.assertTrue(Si != null);
  Atom F = molecule.getAtom("F"); Error.assertTrue(F != null);
  return doTranslation(molecule,Si,F,0.5,3.77117,number);
}
private static ExtendedVector forSiSi(Molecule molecule, int number) {
  Atom Si[] = molecule.getAllAtoms("Si");
  Error.assertTrue(Si.length == 2);
  return doTranslation(molecule,Si[0],Si[1],0.5,3.77117,number);
}
private static ExtendedVector forSiSiSi(Molecule molecule, int number, double distanceToRandomize, double minimumDistance) {
  ExtendedVector molecules = new ExtendedVector();
  molecules.addElement(molecule);
  final DoubleInterval interval = new DoubleInterval(0,distanceToRandomize);
  for(int i = 1; i <= number; i++) {
    Molecule next = molecule.copy();
    next.setComment("variant #" + i);
    Atom Si[] = next.getAllAtoms("Si");
    for(int j = 0; j < Si.length; j++) {
      Vector3d motion = Vector3d.factoryRandom(interval);
      Si[j].translateWithNeighbors(motion,Si);
    }
    if (next.hasVertexPairCloserThan(minimumDistance)) {
      i--;
      continue;
    }
    molecules.addElement(next);
  }
  return molecules;
}
private static ExtendedVector doTranslation(Molecule molecule,Atom stay,Atom move,double start,double end,int number) {
  ExtendedVector molecules = new ExtendedVector();
  Vector3d[] translations = Vector3d.calculateTranslations(stay.getLocationVector(),move.getLocationVector(),start,end,number);
  Atom[] keepStationary = {stay};
  for(int i = 0; i < translations.length; i++) {
    move.translateWithNeighbors(translations[i], keepStationary);
    molecules.addElement(molecule.copy());
    Vector3d back = Vector3d.multiply(translations[i],-1);;
    move.translateWithNeighbors(back, keepStationary);
  }
  return molecules;
}

}
