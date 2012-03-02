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
package gov.nasa.javaGenes.core.HFC;

import gov.nasa.alsUtility.Error;

import gov.nasa.javaGenes.core.ChildMakerProvider;
import gov.nasa.javaGenes.core.FitnessFunction;
import gov.nasa.javaGenes.core.Population;
import gov.nasa.javaGenes.core.Individual;

public class MiddleSubBreeder extends SubBreeder {
public boolean killParents = false;

public MiddleSubBreeder(ChildMakerProvider childMakerProvider, FitnessFunction fitnessFunction) {
	this(childMakerProvider,fitnessFunction,false);
}
public MiddleSubBreeder(ChildMakerProvider childMakerProvider, FitnessFunction fitnessFunction, boolean killParents) {
	super(childMakerProvider,fitnessFunction);
	this.killParents = killParents;
}
public void parentsOfPromoted(int[] parentIndices, Population population) {
	if (killParents) {
		Error.assertTrue(areValidIndices(parentIndices));
		toDeathRow(parentIndices);
	}
}
public String toString() {return "MiddleSubBreeder killParents=" + killParents + " " + super.toString();}

}