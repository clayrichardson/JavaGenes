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
import java.io.Serializable;

import gov.nasa.alsUtility.Utility;
import java.util.Vector;
import gov.nasa.alsUtility.DataTable;
import gov.nasa.javaGenes.chemistry.Molecule;
import gov.nasa.javaGenes.chemistry.Molecules;
import gov.nasa.javaGenes.chemistry.xyzFormat;
import gov.nasa.alsUtility.ExtendedVector;
import gov.nasa.alsUtility.Error;

public class ManyMultiBodiesForOneEnergy implements Serializable {
protected Vector molecules = new Vector();  // these should be optional
protected Vector multiBodies = new Vector();

public ManyMultiBodiesForOneEnergy(){}            
public ManyMultiBodiesForOneEnergy(String xyzFilename) {
    this(null,xyzFilename);
}
public ManyMultiBodiesForOneEnergy(Potential potential,String xyzFilename) {
  Molecules input = xyzFormat.readMolecules(xyzFilename);
  for(int i = 0; i < input.size(); i++) {
    Molecule next = input.get(i);
    next.setPropertiesFromComment();
    MultiBodiesForOneEnergy m = null;
    if (potential instanceof StillingerWeber)
        m = new MultiBodiesForOneEnergy(next,potential);
   else if (potential instanceof Tersoff) {
        m = new Bodies(next);
        ((Bodies)m).setSpeciesIndices(((Tersoff)potential).getSpecies2IndexMap());
    } else
        m = new MultiBodiesForOneEnergy(next,null);
    multiBodies.addElement(m);
    molecules.addElement(next);
  }
}
public ManyMultiBodiesForOneEnergy(String xyzFilename, double lengthScale) {
  this(null,xyzFilename);
  scaleLengthsBy(lengthScale);
}
/**
only works if a proper (xyz) molecule with an energy value is there!
*/
public double getEnergy(int i) {
    Molecule next = getMolecule(i);
    Error.assertTrue(next != null);
    return next.getEnergy();
}
public void add(MultiBodiesForOneEnergy m) {
  multiBodies.addElement(m);   // NOTE: no associated molecule!
}
public void removeBodiesAboveCutoff(Potential form) {
  for (int i = 0; i < size(); i++) {
    final MultiBodiesForOneEnergy m = getMultiBodies(i);
    m.removeBodiesAboveCutoff(form);
  }
  createArrays();
}
public void createArrays() {}
public void scaleLengthsBy(double lengthScale) {
  for (int i = 0; i < size(); i++) {
    final MultiBodiesForOneEnergy m = getMultiBodies(i);
    m.scaleLengthsBy(lengthScale);
  }
}
public DataTable getLengthsAndAngles() {
  final DataTable table = new DataTable();
  for (int i = 0; i < size(); i++) {
    final MultiBodiesForOneEnergy m = getMultiBodies(i);
    for(int j = 0; j < m.size(); j++) {
      final MultiBody body = m.get(j);
      final String name = body.getName();
      String prefix = "error";
      double value = -1;
      final String separator = "-";
      if (body instanceof TwoBody) {
        value = ((TwoBody)body).getR();
        prefix = "distance";
      }
      if (body instanceof ThreeBody) {
        value = Utility.radians2degrees(((ThreeBody)body).getAngle());
        prefix = "angle";
      }
      table.putDatum(prefix,value);
      table.putDatum(prefix+separator+name,value);
    }
  }
  return table;
}
public String[] getBodiesNames() {
  ExtendedVector names = new ExtendedVector();
  for (int i = 0; i < size(); i++)
    names.addVector(getMultiBodies(i).getBodiesNames());
  String[] strings = new String[names.size()];
  names.copyInto((Object[])strings);
  return strings;
}
public MultiBodiesForOneEnergy getMultiBodies(int i) {
  return  (MultiBodiesForOneEnergy)multiBodies.elementAt(i);
}
public Molecule getMolecule(int i) {
  return  (Molecule)molecules.elementAt(i);
}
public int size() {return multiBodies.size();}
} 