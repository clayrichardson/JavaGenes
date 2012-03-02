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
import gov.nasa.alsUtility.Distribution;
import gov.nasa.alsUtility.ExtendedVector;

/**
represents the last body and relevant parameters of a three-body.
*/
public class ThirdBody extends Body {
/**
the distance of the last body to the first body
*/
public double radialDistance; // to third body
/**
the angle between the lines joining the second and third bodies and the first body
*/
public double angle;

/**
@param s the species of the third body
@param distance the distance between the third and first body
@param a the angle
*/
public ThirdBody(Species s, double distance, double a) {
  super(s);
  radialDistance = distance;
  angle = a;
}
/*
public void scaleLengthsBy(double scaleFactor) {
    radialDistance *= scaleFactor;
}
*/
public String toString() {
	String string =  "3b\t" + species.toString() + "\t" + radialDistance + "\t" + angle + "\t";
	return string;
}
/**
create an array of Bodies to use in the fitness function

@param species1 the species of the first body
@param species2 the species of the second body
@param species3 the species of the third body
@param twoBodyLengths will provide values for the distance between the first and second bodies
@param twoBodyLengths will provide values for the distance between the first and third bodies
@param twoBodyLengths will provide values for the angles
@param twoBodiesCount set to false so that only three energies will be calculated.  This is
needed because the two body energies are so much larger than the three body energies that
the three parameters don't evolve very well.
*/
public static Bodies[] makeArray(Species species1,Species species2,Species species3,
		Distribution twoBodyLengths,
  	Distribution thirdBodyLengths,
  	Distribution angles,
    boolean twoBodiesCount){
  Error.assertTrue(thirdBodyLengths.size() == angles.size());
  ExtendedVector allBodies = new ExtendedVector();

  for(int a = 0; a < twoBodyLengths.size(); a++) {
  	Bodies bodies = new Bodies();
    allBodies.addElement(bodies);
    OneBody oneBody = new OneBody(species1);
     bodies.add(oneBody);

    SecondBody secondBody = new SecondBody(species2,twoBodyLengths.get(a),twoBodiesCount);
    oneBody.add(secondBody);

  	for(int b = 0; b < angles.size(); b++) {
    	ThirdBody thirdBody = new ThirdBody(species3,thirdBodyLengths.get(b),angles.get(b));
    	secondBody.add(thirdBody);
    }
  }
  return Bodies.createArrays(allBodies);
}
}
