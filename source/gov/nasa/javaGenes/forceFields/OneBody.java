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
import gov.nasa.alsUtility.ExtendedVector;

/**
represents a single body in a multibody potential.  The body maintains
a list of TwoBody(s) that it is involved with.
*/
public class OneBody extends Body {
// these two contain the same info after a createOneBodyArray
// the Vector used to add easily, the array to access quickly
protected ExtendedVector vector = new ExtendedVector();
/**
the twoBody(s) this is involved with in a public array for fast access.
Will not contain anything until createArrays() is called
*/
public SecondBody[] secondBody = new SecondBody[0];
public SecondBody[] getSecondBodies() {return secondBody;}

/**
@param s the type of body this is
*/
public OneBody(Species s) {super(s);}
/*public void add(TwoBody body) {Error.fatal("temporary, remove me");} // REMOVE*/
public void removeInternalElementsOutsideOfCutoff(Potential form) {
    Tersoff potential = (Tersoff)form;
    ExtendedVector shortened = new ExtendedVector();
    for(int i = 0; i < vector.size(); i++) {
        SecondBody secondBody = (SecondBody)vector.elementAt(i);
        if (secondBody.radialDistance >= potential.cutOffDistance(speciesIndex,secondBody.speciesIndex))
            continue;
        secondBody.removeInternalElementsOutsideOfCutoff(form,speciesIndex);
        shortened.add(secondBody);
    }
    vector = shortened;
}
public boolean withinCutoff(Potential form) {
    Tersoff potential = (Tersoff)form;
    for(int i = 0; i < vector.size(); i++) {
        SecondBody secondBody = (SecondBody)vector.elementAt(i);
        if (secondBody.radialDistance < potential.cutOffDistance(speciesIndex,secondBody.speciesIndex))
            return true;
    }
    return false;
}
public void scaleLengthsBy(double scaleFactor) {
    for(int i = 0; i < vector.size(); i++)
        ((SecondBody)vector.elementAt(i)).scaleLengthsBy(scaleFactor);
}

public void add(SecondBody body) {vector.addElement(body);}
/**
moves the TwoBody(s) from any ExtendedVector to an array for fast access
*/
public void createArrays() {
  secondBody = new SecondBody[vector.size()];
  vector.copyInto(secondBody);
  for(int i = 0; i < secondBody.length; i++)
    secondBody[i].createArrays();
}
/**
converts the species of this and the SecondBody(s) to an integer for fast access
*/
public void setSpeciesIndices(Species2IndexMap map) {
  setSpeciesIndex(map);
  for(int i = 0; i < secondBody.length; i++)
    secondBody[i].setSpeciesIndices(map);
}
public String toString() {
	String string = "1b\t" + species.toString() + "\t";
  for(int i = 0; i < vector.size(); i++)
  	string += ((SecondBody)vector.elementAt(i)).toString();
	return string;
}
}

