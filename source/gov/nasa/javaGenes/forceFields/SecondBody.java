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
import gov.nasa.alsUtility.Distribution;
import gov.nasa.alsUtility.ExtendedVector;

/**
represents the second body and relevant parameters of a three-body.
*/
public class SecondBody extends Body {
/**
distance to the first body
*/
public double radialDistance = 1.4;
/**
used to turn of calculation off two body energies so the three body terms will be more important.
*/
public boolean twoBodyTermMatters = true;
// these two contain the same info after a createOneBodyArray
// the Vector used to add easily, the array to access quickly
protected Vector vector = new Vector();
/**
don't access until after createArrays has been called
*/
public ThirdBody[] thirdBody = new ThirdBody[0];
public ThirdBody[] getThirdBodies() {return thirdBody;}
/**
@param s the species of the second body
@param distance the distance to the first body
*/
public SecondBody(Species s, double distance) {
  super(s);
  radialDistance = distance;
}
public void removeInternalElementsOutsideOfCutoff(Potential form,int iSpeciesIndex) {
    Tersoff potential = (Tersoff)form;
    Vector shortened = new Vector();
    for(int i = 0; i < vector.size(); i++) {
        ThirdBody thirdBody = (ThirdBody)vector.elementAt(i);
        if (thirdBody.radialDistance >= potential.cutOffDistance(iSpeciesIndex,thirdBody.speciesIndex))
            continue;
        shortened.add(thirdBody);
    }
    vector = shortened;
}
/*
public void scaleLengthsBy(double scaleFactor) {
    radialDistance *= scaleFactor;
    for(int i = 0; i < vector.size(); i++)
        ((ThirdBody)vector.elementAt(i)).scaleLengthsBy(scaleFactor);
}
*/
/**
@param s the species of the second body
@param distance the distance to the first body
@param twoBodiesCount flag to turn of calculation of two body energies
*/
public SecondBody(Species s, double distance, boolean twoBodiesCount) {
  super(s);
  radialDistance = distance;
  twoBodyTermMatters = twoBodiesCount;
}
public void add(ThirdBody body) {vector.addElement(body);}
/**
moves the ThreeBody(s) from the vector to the array
*/
public void createArrays() {
  thirdBody = new ThirdBody[vector.size()];
  vector.copyInto(thirdBody);
}
public String toString() {
	String string = "2b\t" + species.toString() + "\t" + radialDistance + "\t";
  for(int i = 0; i < vector.size(); i++)
  	string += ((ThirdBody)vector.elementAt(i)).toString();
	return string;
}
/**
make a set of Bodies to use in a fitness function.
@param species all the species to use
@param distribution provides lengths
*/
public static Bodies[] makeArray(Species[] species, Distribution distribution) {
  ExtendedVector allBodies = new ExtendedVector();
  for(int i = 0; i < species.length; i++)
  for(int j = i; j < species.length; j++)
  for(int k = 0; k < distribution.size(); k++) {
    Bodies bodies = new Bodies();
    allBodies.addElement(bodies);
    OneBody oneBody = new OneBody(species[i]);
    bodies.add(oneBody);
    SecondBody secondBody = new SecondBody(species[j],distribution.get(k));
    oneBody.add(secondBody);
  }
  return Bodies.createArrays(allBodies);
}
/**
sets the array index for the species for each body for fast access
*/
public void setSpeciesIndices(Species2IndexMap map) {
  setSpeciesIndex(map);
  for(int i = 0; i < thirdBody.length; i++)
    thirdBody[i].setSpeciesIndex(map);
}
}
