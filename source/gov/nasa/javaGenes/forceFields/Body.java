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

/**
This is used as a superclass by OneBody, TwoBody, and ThreeBody
*/
public class Body extends MultiBody {
/**
the species of this body (second body in TwoBody, third body in ThreeBody)
*/
public Species species;

/**
the species index.  Used for speed.
*/
public int speciesIndex = -1;

public Body(Species s) {
    super(s.toString());
    species = s;
}
/**
used to set the species index
*/
protected void setSpeciesIndex(Species2IndexMap map) {
  speciesIndex = map.getIndex(species);
}
public void scaleLengthsBy(double scaleFactor) {Error.notImplemented();}
public boolean withinCutoff(Potential form) {Error.notImplemented(); return true;}
public String tabSeparatedString() {Error.notImplemented(); return "";}

}

