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
import gov.nasa.alsUtility.EasyFile;

public class PotentialEnergyTestCase {
/**
the ideal energies for bodies
*/
protected double[] energy;
protected Bodies[] bodies;

/**
@param e the energies, a copy is not made
@param b the bodies, a copy is not made
*/
public PotentialEnergyTestCase(double[] e, Bodies[] b) {
	Error.assertTrue(e.length == b.length);
  energy = e;
	bodies = b;
}
/**
@return the energies calculated with the input potential for this.bodies
minus the ideal energies stored in this.energy.
*/
public double[] getDifference(Potential potential) {
	double[] other = potential.getEnergy(bodies);
  for (int i = 0; i < other.length; i++)
  	other[i] -= energy[i];
  return other;
}
public void printTo(EasyFile file) {
	for (int i = 0; i < energy.length; i++)
  	file.println(energy[i] + ": " + bodies[i]);
}
}