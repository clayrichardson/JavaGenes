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
import gov.nasa.javaGenes.chemistry.UnitCell;
import gov.nasa.alsUtility.Utility;

public class ThreeBody extends MultiBody{

protected double _jiDistance;
protected double _jkDistance;
protected double _angle; // around center atom
protected boolean requiresSWextraParameters = false;
/**
middle atom is center of the three body.  Order of others is forced to be alphabetic (for comparisons).
*/
// for first and third atom, name must always have the earliest string in the alphabet first to simplify finding and comparing bodies
public ThreeBody(String atomi, String atomj, String atomk, double jiDistance, double jkDistance, double angleAroundj) {
  super(atomi.compareTo(atomk) <= 0 ? atomi + atomj + atomk : atomk + atomj + atomi);
  _jiDistance = atomi.compareTo(atomk) <= 0 ? jiDistance : jkDistance; Error.assertTrue(_jiDistance > 0);
  _jkDistance = atomi.compareTo(atomk) <= 0 ? jkDistance : jiDistance; Error.assertTrue(_jkDistance > 0);
  _angle = angleAroundj;
  
  if (isSWspecial(atomi) && isSWspecial(atomj) && isSWspecial(atomk))
    requiresSWextraParameters = true;
  
  Error.assertTrue(isReasonable());
}
public boolean requiresStillingWeberFFFform(){
    return requiresSWextraParameters;
}
public ThreeBody(String atomi, String atomj, String atomk) {
    this(atomi,atomj,atomk,1,1,1);
}
//BUG: should really use Atom.java for this and look for valence 1
protected boolean isSWspecial(String s) {
    String names[] = {"H","Li","Na","K","Rb","Cs","Fr","F","Cl","Br","I","At"};
    for(int i = 0; i < names.length; i++)
        if (names[i].equals(s))
            return true;
    return false;
}
/**
middle atom is center of the three body
*/
public ThreeBody(Atom atomi, Atom atomj, Atom atomk) {
  this(atomi.toString(), atomj.toString(), atomk.toString(),
       atomj.getDistanceTo(atomi),
       atomj.getDistanceTo(atomk),
       atomj.getAngleBetween(atomi,atomk));
}
/**
middle atom is center of the three body
*/
public ThreeBody(Atom atomi, Atom center, Atom atomk, UnitCell unitCell) {
  this(atomi.toString(), center.toString(), atomk.toString(),
       unitCell.getDistance(center,atomi),
       unitCell.getDistance(center,atomk),
       unitCell.getAngle(atomi,center,atomk));
}

public double getRJI() {return _jiDistance;}
public double getRJK() {return _jkDistance;}
public double getAngle() {return _angle;}
public void setRJI(double v) {_jiDistance = v;}
public void setRJK(double v) {_jkDistance = v;}
public void setAngle(double v) {_angle = v;}
public void scaleLengthsBy(double scaleFactor) {
  _jiDistance *= scaleFactor;
  _jkDistance *= scaleFactor;
  Error.assertTrue(isReasonable());
}
public boolean withinCutoff(Potential potential) {
    if (potential == null)
        return true;
    final double cutoff = potential.getCutoff(this);
    return withinCutoff(cutoff);
}
public boolean withinCutoff(double cutoff) {
    return getRJI() < cutoff && getRJK() < cutoff;
}

public boolean isReasonable() {return _jiDistance >= 0 && _jkDistance >= 0;}

public String toString() {
	return  "(" + getName() + " " + _jiDistance + " " + _jkDistance + " " + _angle + "\t";
}
public String tabSeparatedString() {
	return getName() + "\t" + getHowMany() + "\t" + _jiDistance + "\t" + _jkDistance + "\t" + _angle;
}

public boolean nearlyEqual(ThreeBody other) {
    return getName().equals(other.getName()) 
            && Utility.nearlyEqual(getAngle(),other.getAngle())
            && Utility.nearlyEqual(getRJI(),other.getRJI())
            && Utility.nearlyEqual(getRJK(),other.getRJK());
}
}


