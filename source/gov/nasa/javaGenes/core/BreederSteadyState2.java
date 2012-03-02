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
package gov.nasa.javaGenes.core;

import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;

public class BreederSteadyState2 extends Breeder {
protected ChooseParents parentChooser;
protected ChooseForDeath grimReaper;

public BreederSteadyState2(Parameters p) {
	this(p,new Tournament(2),new AntiTournament(2));
}
public BreederSteadyState2(Parameters p,ChooseParents parentChooser,ChooseForDeath grimReaper) {
	super(p);
	Error.assertNotNull(parentChooser);
	Error.assertNotNull(grimReaper);
	this.parentChooser = parentChooser;
	this.grimReaper = grimReaper;
}

public Population breed (Population parents, int kidsPerGeneration) {
	// to parallelize, make threads here -- do in subclass.
	generationJustStarting();
	while (generationNotComplete(kidsPerGeneration)){
		breedOnce(parents);
		Checkpointer.ok();
	}
	generationIsComplete();
	return parents;
}
public void breedOnce(Population population) {
	ChildMaker maker = childMakerProvider.getChildMaker(getTotalNumberOfKidsProduced());
	int[] parentIndices = parentChooser.getParentIndices(maker.numberOfParents(),population);
	makeChildren(maker,parentIndices,population);
}
protected void makeChildren(ChildMaker maker, int[] parentIndices, Population population) {
	Evolvable[] c = maker.makeChildren(population.makeIndividualArray(parentIndices));
	for(int i = 0; i < c.length; i++){
		Individual individual = population.makeIndividual(c[i],getFitnessFunction());
		// to parallelize, make next three lines synchorized
		newChild(individual);
		maker.results(individual,population.makeIndividualArray(parentIndices));
		int indexToKill = grimReaper.getDeathRowIndex(parentIndices,population);
		population.setIndividual(indexToKill,individual);
	}
}
public String toString() {
	return "BreederSteadyState2 parentChooser=" + parentChooser + " grimReaper=" + grimReaper;
}
}