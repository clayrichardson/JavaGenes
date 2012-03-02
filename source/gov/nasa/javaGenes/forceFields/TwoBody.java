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
import gov.nasa.alsUtility.Utility;
import gov.nasa.javaGenes.chemistry.Atom;
import gov.nasa.javaGenes.chemistry.UnitCell;

public class TwoBody extends MultiBody {

protected double _radialDistance = 1.4;

// name must always have the earliest string in the alphabet first to simplify finding and comparing bodies
public TwoBody(String atomi, String atomj, double distance) {
  super(atomi.compareTo(atomj) <= 0 ? atomi + atomj : atomj + atomi);
  _radialDistance = distance;
  Error.assertTrue(isReasonable());
}
public TwoBody(String atomi, String atomj) {
    this(atomi,atomj,1);
}
public TwoBody(Atom atomi, Atom atomj) {
  this(atomi.toString(),atomj.toString(),atomi.getDistanceTo(atomj));
}
public TwoBody(Atom atomi, Atom atomj,UnitCell unitCell) {
  this(atomi.toString(),atomj.toString(),unitCell.getDistance(atomi,atomj));
}
public double getR() {return _radialDistance;}
public void setR(double r) {_radialDistance = r;}
public void scaleLengthsBy(double scaleFactor) {
  _radialDistance *= scaleFactor;
  Error.assertTrue(isReasonable());
}
public boolean withinCutoff(Potential potential) {
    if (potential == null)
        return true;
    final double cutoff = potential.getCutoff(this);
    return withinCutoff(cutoff);
}
public boolean withinCutoff(double cutoff) {
    return getR() < cutoff;
}
public boolean isReasonable() {return _radialDistance >= 0;}
public String toString() {
	return "(" + getName() + " " + getR() + ")";
}
public String tabSeparatedString() {
	return getName() + "\t" + getHowMany() + "\t" + getR();
}

public boolean nearlyEqual(TwoBody other) {
    return getName().equals(other.getName()) && gov.nasa.alsUtility.Utility.nearlyEqual(getR(),other.getR());
}
}

