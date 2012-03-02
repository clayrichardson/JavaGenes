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

//  Created by Al Globus on Mon Jun 24 2002.

import java.lang.Math;
import gov.nasa.javaGenes.graph.Vertex;

/**
a 'unit cell' that is not a unit cell at all.  Simplifies other code
where a UnitCell may or may not be in use.
*/
public class UnitCellNone extends UnitCell {

public boolean nearlyEquals(UnitCell uc) {
    return uc instanceof UnitCellNone;
}
public boolean isInside(Vertex a ) {
    return true;
}
public void moveInside(Vertex v) {
    return;
}
public UnitCell copy() {
    return this;
}
public String toString() {return "";}
public double getDistance(Vertex a, Vertex b) {
    return a.getDistanceTo(b);
}
public double getAngle(Vertex x, Vertex center, Vertex z) {
    return center.getAngleBetween(x,z);
}
public double getSeparation(int dimension, double a, double b) {
    return Math.abs(a - b);
}
public boolean isCutoffInBounds(double d) {
    return true;
}
        
}
