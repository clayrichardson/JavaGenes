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

import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.Compatible;
import gov.nasa.javaGenes.graph.Edge;

/**
Represent a chemical bond.

Note: class Molecule takes care of saving Bond state during a checkpoint and restoring it
during a restore.
*/
public class Bond extends Edge {
protected int valence = 1; // the amount of valence this bond consumes
/**
create a single bond
*/
public Bond() {}
/**
create a bond. Argument v indicates single, double, or triple bond.
*/
public Bond(int v) {valence = v;}
/**
Create a bond between the two atoms. 
@param v indicates single, double,
or triple bond.
*/
public Bond(Atom a1, Atom a2, int v) {
    valence = v;
    Error.assertTrue(valence <= getMaximumBondTypes());
    setVertices(a1,a2);
    makeCompatibleWithVertices();
}
//UGLY: no Bond(tokenizer), stateSave(tokenizer) because use makeBond() in Molecule

public Atom getAtom(int i) {return (Atom)vertices[i];}
/**
Force the type (single, double, or triple) of this bond to be compatible with 
the valence of its atoms. Fail with an Error.assertTrue grants if this is impossible.
*/
public void makeCompatibleWithVertices() {makeValenceCompatible();}
/**
Force the type (single, double, or triple) of this bond to be compatible with 
the valence of its atoms. Fail with an Error.assertTrue grants if this is impossible.
*/
public void makeValenceCompatible() {
    boolean changed = false;
    for(int i = 0; i < vertices.length; i++){
        while(valence > ((Atom)vertices[i]).remainingValence()) {
            valence = valence - 1;
            changed = true;
        }
    }
    if (changed) {
        vertices[0].stateChange();
        vertices[1].stateChange();
    }
    Error.assertTrue(valence >= 1);
}
/**
Is the argument a bond with the same order?
*/
public boolean isCompatible(Compatible c) {
    return getClass() == c.getClass() && valence == ((Bond)c).valence;
}
/**
is the type and valence of these bonds the same?
*/
public boolean isSame(Edge e) {
    return getClass() == e.getClass() && valence == ((Bond)e).valence;
}
/**
can the atoms of the argument be connected by this bond (e)?
@param e must be a Bond
*/
public boolean canAcceptVerticesOf(Edge e) {
  Bond b = (Bond)e;
  int d = valence - b.valence;
  return d <= b.getAtom(0).remainingValence() && d <= b.getAtom(1).remainingValence();
}
/**
@param e must be a Bond
*/
public boolean dissimilarEdgeCompatibleWithVertices(Edge e) {
  Bond b = (Bond)e;
  if (valence == b.valence) return false;
  if (valence < b.valence) return true;
  int increase = valence - b.valence;
  return (getAtom(0).remainingValence() >= increase)
      && (getAtom(1).remainingValence() >= increase);
}
/**
Return the highest number that can represent a bond type (3 for triple presently)
*/
public static int getMaximumBondTypes(){return 3;} // single, double, and triple bonds
/**
Return a value to be used as an array index (starting at 0).
*/
public int getBondTypeIntegerCode() {return getValence() - 1;}
/**
Return the amount of valence this bond uses
*/
public int getValence() {return valence;}
public String toString() {return valence + "";}
}
