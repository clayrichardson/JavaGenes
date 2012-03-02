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
import java.lang.Cloneable;
import gov.nasa.alsUtility.Error;

public abstract class MultiBody implements Cloneable,Serializable {
protected String _name = "none";
protected double howMany = 1; // used by subclasses in ForceFields.crystals for multiples of the same 2/3 body

public MultiBody(String name) {setName(name);}
public MultiBody copy() {
    try {
        return (MultiBody)clone();
    } catch (CloneNotSupportedException e) {
        Error.fatal("can't clone object: " + e);
        return null;
    }
}
public void setName(String name) {
  _name = name;
  Error.assertTrue(_name != null);
}
public String getName() {return _name;}
public abstract void scaleLengthsBy(double scaleFactor);
public abstract boolean withinCutoff(Potential form);
public void removeInternalElementsOutsideOfCutoff(Potential form) {}
/**
@return factor to multiple energy by when there are many instances of the same MultiBody in a crystal (or other molecule)
*/
public double getHowMany() {return howMany;} 
public void setHowMany(double inHowMany) {howMany = inHowMany;}
public void incrementHowMany() {howMany++;}
public void divideHowManyBy(double number) {howMany /= number;}

public abstract String tabSeparatedString();
} 