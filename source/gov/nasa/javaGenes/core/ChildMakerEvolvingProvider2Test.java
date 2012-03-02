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
//  Created by Al Globus on Fri Feb 21 2003.
package gov.nasa.javaGenes.core;

import junit.framework.TestCase;
import gov.nasa.alsUtility.Error;

public class ChildMakerEvolvingProvider2Test extends TestCase {
public ChildMakerEvolvingProvider2Test(String name) {super(name);}

public void testEvolve() {
	final int numberOfChildMakers = 10;
	final int numberToKill = 5;
    ChildMakerEvolvingProvider2 provider = new ChildMakerEvolvingProvider2(15,numberOfChildMakers,numberToKill,0.0,new gov.nasa.javaGenes.evolvableDoubleList.ChildMakerProviderRandom(0,10));
	String[] names = {"foo"};
    provider.setFitnessFunctionNames(names);

	Error.assertTrue("size", provider.size() == numberOfChildMakers);
    for(int i = 0; i < provider.size(); i++)
		provider.get(i).forEvolution.setResults(0,1,1,1);
	provider.evolve();
    for(int i = 0; i < provider.size(); i++)
		if (i < numberOfChildMakers - numberToKill)
			Error.assertTrue("1-"+i, provider.get(i).forEvolution.checkResults(0,1,1,1));
		else
			Error.assertTrue("1-"+i, provider.get(i).forEvolution.checkResults(0,0,0,0));
}
public void testGet() {
	final int numberOfChildMakers = 10;
	final int numberToKill = 5;
	final int numberBetweenEvolution = 15;
    ChildMakerEvolvingProvider2 provider = new ChildMakerEvolvingProvider2(numberBetweenEvolution,numberOfChildMakers,numberToKill,0.0,new gov.nasa.javaGenes.evolvableDoubleList.ChildMakerProviderRandom(0,10));
	String[] names = {"foo"};
    provider.setFitnessFunctionNames(names);

    for(int i = 0; i < provider.size(); i++)
		provider.get(i).forEvolution.setResults(0,1,1,1);
	for(int i = 0; i < numberBetweenEvolution; i++)
		provider.get();
	Error.assertTrue("1", provider.numberOfGetsSoFar == numberBetweenEvolution);
    for(int i = 0; i < provider.size(); i++)
		Error.assertTrue("1-"+i, provider.get(i).forEvolution.checkResults(0,1,1,1));
	provider.get();
	Error.assertTrue("2", provider.numberOfGetsSoFar == 1);
    for(int i = 0; i < provider.size(); i++)
		Error.assertTrue("2-"+i, provider.get(i).forEvolution.checkResults(0,0,0,0));
}		
}



