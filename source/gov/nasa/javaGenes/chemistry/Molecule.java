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


import java.lang.Math;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.PropertiesList;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.ExtendedVector;
import gov.nasa.javaGenes.graph.Graph;
import gov.nasa.javaGenes.graph.VertexAndEdgeProvider;
import gov.nasa.javaGenes.graph.VertexIterator;

/**
note: during evolution Graph objects will be created from Molecule populations.  The nature
of vertices and edges indicates that the Graph is actually a Molecule.
*/
public class Molecule extends Graph {
private String comment = "";
private double _energy = 0;
private boolean hasDimerForce = false;
private double dimerForce = 0;
private UnitCell unitCell = new UnitCellNone();
final static public String energyPropertyName = "energy";
final static public String unitCellPropertyName = "unitCell";
final static public String dimerForcePropertyName = "dimerForce";

public void scaleBy(double factor) {
    unitCell.scaleBy(factor);
    for(VertexIterator v = getVertexIterator(); v.more(); v.next())
        v.vertex().scaleBy(factor);
}
public Molecule copy() {
    Molecule molecule =  (Molecule)deepCopyGraph();
    molecule.unitCell = unitCell.copy();
    return molecule;
}
public String getComment() {return comment;}
public void setComment(String c) {comment = c;}

public boolean hasUnitCell() {return !(unitCell instanceof UnitCellNone);}
public UnitCell getUnitCell() {return unitCell;}
public void setUnitCell(UnitCell inUnitCell) {unitCell = inUnitCell;}
public double getEnergy() {return _energy;}
public double getDimerForce() {
    Error.assertTrue(hasDimerForce);
    return dimerForce;
}
public void setDimerForce(double inForce) {
    hasDimerForce = true;
    dimerForce = inForce;
}
public void setEnergy(double energy) {_energy = energy;}
public void setPropertiesFromComment() {
    PropertiesList properties = new PropertiesList(comment);
    Error.assertTrue(properties.size() <= 3);
    if (properties.hasProperty(energyPropertyName))
        _energy = Utility.string2double(properties.getProperty(energyPropertyName));
    if (properties.hasProperty(dimerForcePropertyName))
        setDimerForce(Utility.string2double(properties.getProperty(dimerForcePropertyName)));
    if (properties.hasProperty(unitCellPropertyName)) {
        String unitCellString = properties.getProperty(unitCellPropertyName);
        unitCell = new UnitCell(unitCellString);
    }    
}
public void setCommentFromProperties(){
    comment = "";
    comment = energyPropertyName + PropertiesList.assignmentString() + _energy;
    if (!(unitCell instanceof UnitCellNone)) 
        comment += PropertiesList.separator() 
            + unitCellPropertyName 
            + PropertiesList.assignmentString() 
            + unitCell.toString();
    if (hasDimerForce)
        comment += PropertiesList.separator() + dimerForcePropertyName + PropertiesList.assignmentString() + dimerForce;
}

public Molecule() {}
public Molecule(VertexAndEdgeProvider provider, int numberOfVertices, int numberOfCycles) {
	super(provider, numberOfVertices, numberOfCycles);
}
public int getNumberOfAtoms() {return getVerticesSize();}
public Atom getAtom(int index) {return (Atom)getVertex(index);}
/**
make a bond between two atoms numbered starting at 1 (the way chemists count)
and add the bond to the molecule.
*/
public Bond makeBond(int vertex1, int vertex2, int valence) {
	Atom a1 = (Atom)getVertex(vertex1-1);
	Atom a2 = (Atom)getVertex(vertex2-1);
  Bond bond = new Bond(a1, a2, valence);
  a1.add(bond);
  a2.add(bond);
	add(bond);
  return bond;
}
/**
set the positions of atoms to random numbers
*/
public void randomizeAtomicPositions() {
	DoubleInterval range = new DoubleInterval(0,Math.ceil(Math.sqrt(vertices.size())));
	for(VertexIterator v = getVertexIterator(); v.more(); v.next()) {
		Atom a = (Atom)v.vertex();
		a.setXyz(range.random(),range.random(),range.random());
	}
        moveAtomsIntoUnitCell();
}
/**
set the positions of atoms to random numbers
*/
public void randomizeAtomicPositionsBy(double distance) {
  randomizeAtomicPositionsBy(distance, false, getNumberOfAtoms());
}
public void randomizeAtomicPositionsBy(double distance,boolean gaussianDistribution) {
  randomizeAtomicPositionsBy(distance, gaussianDistribution, getNumberOfAtoms());
}
public void randomizeAtomicPositionsBy(double distance,boolean gaussianDistribution,int numberOfAtomsToMove) {
    DoubleInterval range = new DoubleInterval(-distance,distance);
    int i = 0;
    for(VertexIterator v = getVertexIterator(); v.more(); v.next(), i++) {
        if (i >= numberOfAtomsToMove)
            return;
        Atom a = (Atom)v.vertex();
        double[] xyz = a.getXyz();
        if (gaussianDistribution)
            a.setXyz(xyz[0] + RandomNumber.getGaussian(distance), xyz[1] + RandomNumber.getGaussian(distance), xyz[2] + RandomNumber.getGaussian(distance));
        else
            a.setXyz(xyz[0] + range.random(), xyz[1] + range.random(), xyz[2] + range.random());
    }
    moveAtomsIntoUnitCell();
}

public void moveAtomsIntoUnitCell() {
    for(VertexIterator v = getVertexIterator(); v.more(); v.next())
        unitCell.moveInside(v.vertex());
}

public Atom getAtom(String element) {
	for(VertexIterator v = getVertexIterator(); v.more(); v.next()) {
		Atom a = (Atom)v.vertex();
    if (a.isElement(element))
      return a;
  }
  return null;
}

public void removeLastNAtoms(int n) {
    removeLastNVertices(n);
}
public Atom[] getAllAtoms(String element) {
  ExtendedVector molecules = new ExtendedVector();
	for(VertexIterator v = getVertexIterator(); v.more(); v.next()) {
		Atom a = (Atom)v.vertex();
    if (a.isElement(element))
      molecules.addElement(a);
  }
  Atom[] array = new Atom[molecules.size()];
  molecules.copyInto(array);
  return array;
}
public boolean isLegal() {return isConnected();}
}
