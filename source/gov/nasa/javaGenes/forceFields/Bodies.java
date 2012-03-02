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
import gov.nasa.javaGenes.chemistry.Molecule;
import gov.nasa.javaGenes.graph.VertexIterator;
import gov.nasa.javaGenes.chemistry.Atom;
import gov.nasa.alsUtility.ExtendedVector;
import gov.nasa.javaGenes.chemistry.UnitCell;

// OPTIMIZE: doesn't currently sort by length to avoid looking at many lengths
/**
Holds a set of bodies for a multibody potential to find the energy of
@see Potential
*/
public class Bodies extends MultiBodiesForOneEnergy {
/**
this is public for fast access.  Access is often in an inner loop.
multiBodies is converted to this.
*/
public OneBody[] oneBody = new OneBody[0];
public OneBody[] getOneBodyArray() {return oneBody;}

/**
set the species indices in all the bodies for fast access
*/
public void setSpeciesIndices(Species2IndexMap map) {
  for(int i = 0; i < oneBody.length; i++)
    oneBody[i].setSpeciesIndices(map);
}
/**
Converts Molecules into Bodies
untested
@return an array of Bodies corresponding to molecules in the argument
*/
public static Bodies[] makeMultipleMolecules(Molecule[] molecules){
  Bodies[] bodies = new Bodies[molecules.length];
  for(int i = 0; i < molecules.length; i++)
    bodies[i] = new Bodies(molecules[i]);
  return bodies;
}
public Bodies(){}
public Bodies(Molecule molecule) {
    UnitCell unitCell = molecule.getUnitCell();
    for(VertexIterator i = molecule.getVertexIterator(); i.more(); i.next()) {
        Atom a = (Atom)i.vertex();
        OneBody oneBody = new OneBody(new AtomicSpecies(a));
        add(oneBody);
        for(VertexIterator j = molecule.getVertexIterator(); j.more(); j.next()) {
            Atom b = (Atom)j.vertex();
            if (b == a)
                continue;
            SecondBody twoBody = new SecondBody(new AtomicSpecies(b),unitCell.getDistance(a,b));
            oneBody.add(twoBody);
            for(VertexIterator k = molecule.getVertexIterator(); k.more(); k.next()) {
                Atom c = (Atom)k.vertex();
                if (c == b || c == a)
                    continue;
                ThirdBody threeBody = new ThirdBody(new AtomicSpecies(c),unitCell.getDistance(a,c),unitCell.getAngle(b,a,c));
                twoBody.add(threeBody);
            }
        }
    }
    createArrays();
}
/*
public void append(Bodies bodies) {
  multiBodies.append(bodies.multiBodies);
  createOneBodyArray();
}
*/
/**
Must be called before this is handed to a potential.  Converts Vectors to arrays
for fast access.
*/
public void createArrays() {
  createOneBodyArray();
  for(int i = 0; i < oneBody.length; i++)
    oneBody[i].createArrays();
}
private void createOneBodyArray() {
  oneBody = new OneBody[multiBodies.size()];
  multiBodies.copyInto(oneBody);
}
/**
converts a vector of bodies into an array of bodies and has all
of the bodies convert the internal vectors into arrays for fast access
*/
public static Bodies[] createArrays(Vector allBodies) {
  Bodies[] array = new Bodies[allBodies.size()];
  allBodies.copyInto(array);
  for(int i = 0; i < array.length; i++)
    array[i].createArrays();
  return array;
}
public String toString() {
	String string = "body\t";
  for(int i = 0; i < multiBodies.size(); i++)
  	string += ((OneBody)multiBodies.elementAt(i)).toString() + "\t";
	return string;
}
}

