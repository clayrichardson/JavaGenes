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

import java.util.Vector;
import java.io.Serializable;
import gov.nasa.javaGenes.chemistry.Molecule;
import gov.nasa.javaGenes.chemistry.UnitCell;
import gov.nasa.javaGenes.chemistry.Atom;
import gov.nasa.javaGenes.chemistry.xyzFormat;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.EasyFile;
import java.lang.Cloneable;

public class MultiBodiesForOneEnergy implements Cloneable,Serializable {
protected Vector multiBodies = new Vector();
protected Molecule molecule;

/**
copy the graph and make copies of all edges and vertices
*/
public MultiBodiesForOneEnergy deepCopyMultiBodiesForOneEnergy() {
    try {
        return (MultiBodiesForOneEnergy)clone();
    } catch (CloneNotSupportedException e) {
        Error.fatal("can't clone object: " + e);
        return null;
    }
}
/**
copy the MultiBodiesForOneEnergy and make copies molecule and the multiBodies

@exception CloneNotSupportedException
*/
public Object clone() throws CloneNotSupportedException {
    MultiBodiesForOneEnergy mb = (MultiBodiesForOneEnergy)super.clone();
    mb.molecule = molecule.copy();
    mb.multiBodies = new Vector();
    for (int i = 0; i < size(); i++)
        mb.add(get(i).copy());
    return mb;
}

public MultiBodiesForOneEnergy() {}
public MultiBodiesForOneEnergy(Molecule inMolecule) {this(inMolecule,null);}

public MultiBodiesForOneEnergy(Molecule inMolecule,Potential potential) {
    molecule = inMolecule;
    UnitCell unitCell = molecule.getUnitCell();
    for (int i = 0;   i < molecule.getVerticesSize(); i++)
    for (int j = i+1; j < molecule.getNumberOfAtoms(); j++) {
        TwoBody twoBody = new TwoBody(molecule.getAtom(i),molecule.getAtom(j),unitCell);
        if (twoBody.withinCutoff(potential)) 
            add(twoBody);
        for (int k = j+1; k < molecule.getVerticesSize(); k++) {
            Atom ii = molecule.getAtom(i);
            Atom jj = molecule.getAtom(j);
            Atom kk = molecule.getAtom(k);
            ThreeBody threeBody = new ThreeBody(ii,jj,kk,unitCell);
            if (threeBody.withinCutoff(potential)) 
                add(threeBody);
            threeBody = new ThreeBody(jj,kk,ii,unitCell);
            if (threeBody.withinCutoff(potential)) 
                add(threeBody);
            threeBody = new ThreeBody(kk,ii,jj,unitCell);
            if (threeBody.withinCutoff(potential)) 
                add(threeBody);
        }
    }
}
public MultiBodiesForOneEnergy(Molecule inMolecule,double cutoff) {
    molecule = inMolecule;
    UnitCell unitCell = molecule.getUnitCell();
    for (int i = 0;   i < molecule.getVerticesSize(); i++)
    for (int j = i+1; j < molecule.getNumberOfAtoms(); j++) {
        TwoBody twoBody = new TwoBody(molecule.getAtom(i),molecule.getAtom(j),unitCell);
        if (twoBody.withinCutoff(cutoff))
            add(twoBody);
        for (int k = j+1; k < molecule.getVerticesSize(); k++) {
            Atom ii = molecule.getAtom(i);
            Atom jj = molecule.getAtom(j);
            Atom kk = molecule.getAtom(k);
            ThreeBody threeBody = new ThreeBody(ii,jj,kk,unitCell);
            if (threeBody.withinCutoff(cutoff)) 
                add(threeBody);
            threeBody = new ThreeBody(jj,kk,ii,unitCell);
            if (threeBody.withinCutoff(cutoff)) 
                add(threeBody);
            threeBody = new ThreeBody(kk,ii,jj,unitCell);
            if (threeBody.withinCutoff(cutoff)) 
                add(threeBody);
        }
    }
}
public int getNumberOfAtoms() {
    Error.assertTrue(molecule != null);
    return molecule.getNumberOfAtoms();
}
public void createArrays() {}
public Vector getBodiesNames() {
  Vector names = new Vector();
  for (int i = 0; i < multiBodies.size(); i++)
    names.addElement(get(i).getName());
  return names;
}
public void add(MultiBody m) {
    multiBodies.addElement(m);
}
public Molecule getMolecule() {return molecule;}
public int size() {return multiBodies.size();}
public MultiBody get(int index) {return (MultiBody)multiBodies.elementAt(index);}
public void scaleLengthsBy(double scaleFactor) {
  for (int i = 0; i < size(); i++)
    get(i).scaleLengthsBy(scaleFactor);
}
public MultiBodiesForOneEnergy scaleLengthsByAndReturnThis(double scaleFactor) {
    this.scaleLengthsBy(scaleFactor);
    return this;
}
public void removeBodiesAboveCutoff(Potential form) {
  Vector shortened = new Vector();
  for (int i = 0; i < size(); i++) {
    final MultiBody m = get(i);
    m.removeInternalElementsOutsideOfCutoff(form);
    if (m.withinCutoff(form))
      shortened.addElement(m);
  }
  multiBodies = shortened;
  createArrays();
}
public void makeMoleculeAndBodiesFiles(String baseName) {
    Error.assertTrue(molecule != null);
    Error.assertTrue(multiBodies != null);
    xyzFormat.writeToFile(molecule,baseName + ".xyz");
    writeThreeBodiesToTsdFile(baseName + "Threebodies.tsd");
    writeTwoBodiesToTsdFile(baseName + "Twobodies.tsd");
}
public void writeTwoBodiesToTsdFile(String filename) {
    EasyFile out = new EasyFile(filename);
    out.println("name\thowMany\tdistance");
    for (int i = 0; i < size(); i++)
        if (get(i) instanceof TwoBody)
            out.println(get(i).tabSeparatedString());
    out.close();
}
public void writeThreeBodiesToTsdFile(String filename) {
    EasyFile out = new EasyFile(filename);
    out.println("name\thowMany\tJIdistance\tJKdistance\tangle");
    for (int i = 0; i < size(); i++)
        if (get(i) instanceof ThreeBody)
            out.println(get(i).tabSeparatedString());
    out.close();
}
} 