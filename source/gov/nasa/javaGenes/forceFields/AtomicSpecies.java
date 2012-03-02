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

import gov.nasa.javaGenes.chemistry.Element;
import gov.nasa.javaGenes.chemistry.Atom;
import gov.nasa.alsUtility.Error;

/**
For a multibody atomic potential, this class is used to indicate what
atomic element a body has.  Also contains two cutoffs (in angstroms).
@see Body
*/
public class AtomicSpecies extends Species {
protected Element element;
protected double cutoff = 0; // outer cutoff
protected double toInnerCutoff = 0; // distance to inner cutoff

public AtomicSpecies(Atom a) {
  this(new Element(a.getAtomicNumber()));
}
public AtomicSpecies(Element e) {
  element = e;
}
public AtomicSpecies(String symbol) {
  this(Element.getElement(symbol));
}
public AtomicSpecies(String symbol, double inCutoff, double inToInnerCutoff) {
    this(symbol);
    cutoff = inCutoff;
    toInnerCutoff = inToInnerCutoff;
    Error.assertTrue(cutoff > 0);
    Error.assertTrue(toInnerCutoff >= 0);
}
/**
@arg inCutoff set both cutoffs to this value
*/
public AtomicSpecies(String symbol, double inCutoff) {
    this(symbol,inCutoff,0);
}

public String toString() {return element.toString();}
public String getElementName() {return element.toString();}

public double getCutoff() {return cutoff;}
public double getToInnerCuttof() {return toInnerCutoff;}
/**
@return true if the atomic numbers are the same
*/
public boolean equals(Object object) {
  return element.getAtomicNumber() == ((AtomicSpecies)object).element.getAtomicNumber();
}
public int hashCode() {
  return element.getAtomicNumber();
}
}

