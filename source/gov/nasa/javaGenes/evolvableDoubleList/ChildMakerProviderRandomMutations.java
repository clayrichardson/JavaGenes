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
package gov.nasa.javaGenes.evolvableDoubleList;

import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.alsUtility.IntegerInterval;

/** for fixed length representations */
public class ChildMakerProviderRandomMutations extends ChildMakerProviderRandom {
protected double minStandardDeviaion;
protected double maxStandardDeviaion;
protected int evolvableSize;

public ChildMakerProviderRandomMutations(int numberOfChildMakers, int evolvableSize, double minStandardDeviaion, double maxStandardDeviaion) {
	Error.assertTrue(numberOfChildMakers >= 0);
	Error.assertTrue(evolvableSize > 0);
	this.evolvableSize = evolvableSize;
	Error.assertTrue(0 <= minStandardDeviaion);
	Error.assertTrue(minStandardDeviaion <= maxStandardDeviaion);
	this.minStandardDeviaion = minStandardDeviaion;
	this.maxStandardDeviaion = maxStandardDeviaion;
	for(int i = 0; i < numberOfChildMakers; i++) {
		double weight = new DoubleInterval(1,10).random();
		add(weight,create());
	}
}
public gov.nasa.javaGenes.core.ChildMaker create() {
	Selector selector = getRandomSelector(1,0,evolvableSize,evolvableSize);
	return new MutationFixedStdDev(selector,new DoubleInterval(minStandardDeviaion,maxStandardDeviaion).random());
}
public String toString() {
	return "ChildMakerProviderRandomMutations " + super.toString();
}
}