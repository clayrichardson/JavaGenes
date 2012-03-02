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
//  Created by Al Globus on Fri Jun 14 2002.

import java.lang.Math;
import gov.nasa.alsUtility.DoublesList;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.graph.Vertex;

import gov.nasa.javaGenes.graph.Vertex;

public class UnitCell implements java.io.Serializable {

public UnitCell(){}

protected double cell[][] = {{0,1},{0,1},{0,1}}; // 1,1,1 unit cell is default
protected double halfDistance[] = {0.5,0.5,0.5};
protected double fullDistance[] = {1,1,1};
final static String separator = ",";

public UnitCell(double size) {
    this(0,size,0,size,0,size);
}
public UnitCell(double x, double y, double z) {
   this(0,x,0,y,0,z);
}
public UnitCell(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
    setAll(xmin,xmax,ymin,ymax,zmin,zmax);
}
public UnitCell copy() {
    UnitCell u = new UnitCell(cell[0][0],cell[0][1],cell[1][0],cell[1][1],cell[2][0],cell[2][1]);
    return u;
}
public void scaleBy(double factor) {
    for(int i = 0; i < cell.length; i++)
        setDimension(i,cell[i][0]*factor,cell[i][1]*factor);
}
public void setAll(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
    setDimension(0,xmin,xmax);
    setDimension(1,ymin,ymax);
    setDimension(2,zmin,zmax);
}
/**
@arg unitCellString xmin xmax ymin ymax zmin zmax
*/
public UnitCell(String unitCellString) {
    DoublesList v = new DoublesList(unitCellString);
    Error.assertTrue(v.size() == 6);
    setAll(v.get(0),v.get(1),v.get(2),v.get(3),v.get(4),v.get(5));
}
public String toString() {
    String r = "";
    for(int i = 0; i < cell.length; i++)
    for(int j = 0; j < cell[i].length; j++)
        r += cell[i][j] + separator;
    return r.substring(0,r.length()-1);
}
public void setDimension(int index, double min, double max) {
    Error.assertTrue(min <= max);
    cell[index][0] = min;
    cell[index][1] = max;
    fullDistance[index] = max - min;
    halfDistance[index] = fullDistance[index]/2;
}
public boolean nearlyEquals(UnitCell uc) {
    UnitCell u = (UnitCell)uc;
    for(int i = 0; i < cell.length; i++)
    for(int j = 0; j < 2; j++)
        if (!Utility.nearlyEqual(u.cell[i][j],cell[i][j]))
            return false;
    return true;
}
public boolean isInside(Vertex a ) {
    double xyz[] = a.getXyz();
    for(int i = 0; i < cell.length; i++)
        if (xyz[i] < cell[i][0] || cell[i][1] < xyz[i])
            return false;
    return true;
}
public void moveInside(Vertex v ) {
    for(int i = 0; i < cell.length; i++)
        v.moveInsideInterval(i,cell[i][0],cell[i][1]);
}    
    
public double getDistance(Vertex a, Vertex b) {
    Error.assertTrue(isInside(a));
    Error.assertTrue(isInside(b));
    double[] x = a.getXyz();
    double[] y = b.getXyz();
    double xDistance = getSeparation(0,x[0],y[0]);
    double yDistance = getSeparation(1,x[1],y[1]);
    double zDistance = getSeparation(2,x[2],y[2]);
    return Math.sqrt(xDistance*xDistance + yDistance*yDistance + zDistance*zDistance);
}
public double getAngle(Vertex x, Vertex center, Vertex z) {
  Error.assertTrue(x != center);
  Error.assertTrue(x != z);
  Error.assertTrue(center != z);
  // use triangle equality: a^2 = b^2 + c^2 = 2bc cos(alpha)
  double a = getDistance(x,z);
  double b = getDistance(center,x);
  double c = getDistance(center,z);
  if (b == 0 || c == 0)
    return java.lang.Double.NaN;
  double number = (a*a - b*b - c*c) / (-2*b*c);
  if (number < -1) number = -1;
  if (number >  1) number =  1;
  return Math.acos(number);
}
public double getSeparation(int dimension, double a, double b) {
    final double normalDistance = Math.abs(a - b);
    if (normalDistance <= halfDistance[dimension])
        return normalDistance;
    if (normalDistance >= fullDistance[dimension])
        return 0;
    double fraction = normalDistance / halfDistance[dimension];
    int intPart = fraction >= 1 ? 2 : 0;
    double x = halfDistance[dimension] * (fraction - intPart);
    return Math.abs(x);
}
public boolean isCutoffInBounds(double d) {
    for(int i = 0; i < halfDistance.length; i++)
        if (halfDistance[i] < d)
            return false;
    return true;
}
        
}
