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
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.alsUtility.IntegerInterval;
import gov.nasa.javaGenes.core.ChildMakerProviderWeighted;
import gov.nasa.javaGenes.core.ChildMakerRandomCreator;

public class ChildMakerProviderRandom extends ChildMakerProviderWeighted implements ChildMakerRandomCreator {

static protected  DoubleInterval defaultCrossoverIntervalRange = new DoubleInterval(0.5,1.5);
static protected  DoubleInterval defaultMutationStandardDeviationRange = new DoubleInterval(0.01,0.1);
static protected  DoubleInterval defaultSelectionProbabilityRange = new DoubleInterval(0.1,0.9);
static protected  DoubleInterval defaultWeightRange = new DoubleInterval(1,10);

protected  DoubleInterval crossoverIntervalRange = defaultCrossoverIntervalRange;
protected  DoubleInterval mutationStandardDeviationRange = defaultMutationStandardDeviationRange;
protected  DoubleInterval selectionProbabilityRange = defaultSelectionProbabilityRange;
protected  DoubleInterval weightRange = defaultWeightRange;
protected int myModulo = 1;
protected int myOffset = 0;
protected boolean randomlyChooseDefaultModulo;
protected int minEvolvableSize;
protected int maxEvolvableSize;
protected boolean fixedLength;
protected int[] deleteSizes;
protected int[] insertSizes;

// for subclasses only
protected ChildMakerProviderRandom() {}

/** for fixed length representations with no modulo/offset */
public ChildMakerProviderRandom(int numberOfChildMakers, int evolvableSize) {
	this(numberOfChildMakers, 1, 0, true, false, new int[0], new int[0], evolvableSize, evolvableSize);
}
/** for fixed length representations */
public ChildMakerProviderRandom(int numberOfChildMakers, int modulo, int offset, boolean randomlyChooseDefaultModulo, int evolvableSize) {
	this(numberOfChildMakers, modulo, offset, true, randomlyChooseDefaultModulo, new int[0], new int[0], evolvableSize, evolvableSize);
}
public ChildMakerProviderRandom(int numberOfChildMakers, int modulo, int offset, boolean fixedLength, boolean randomlyChooseDefaultModulo, int[] deleteSizes, int[] insertSizes,int minEvolvableSize, int maxEvolvableSize) {
	Error.assertTrue(numberOfChildMakers >= 0);
	Error.assertTrue(minEvolvableSize > 0);
	Error.assertTrue(maxEvolvableSize >= minEvolvableSize);
	Error.assertTrue(modulo > 0);
	Error.assertTrue(offset >= 0);
	myModulo = modulo;
	myOffset = offset;
	this.randomlyChooseDefaultModulo = randomlyChooseDefaultModulo;
	this.minEvolvableSize = minEvolvableSize;
	this.maxEvolvableSize = maxEvolvableSize;
	this.fixedLength = fixedLength;
	this.deleteSizes = Utility.copy(deleteSizes);
	this.insertSizes = Utility.copy(insertSizes);
	for(int i = 0; i < numberOfChildMakers; i++) {
		double weight = weightRange.random();
		add(weight,create());
	}
}

public gov.nasa.javaGenes.core.ChildMaker create() {
	int modulo = -1;
	int offset = -1;
	if (randomlyChooseDefaultModulo && RandomNumber.getBoolean()) {
		modulo = 1;
		offset = 0;
	} else {
		modulo = myModulo;
		offset = myOffset;
	}
	Selector basicSelector = getRandomSelector(modulo,offset,minEvolvableSize,maxEvolvableSize);
	SelectModulo selector = new SelectModulo(basicSelector,modulo,offset);
	int maxOperator = fixedLength ? 6 : 10;
	int childMaker = new IntegerInterval(0,maxOperator).random();
	switch(childMaker) {
	case 0: return new CrossoverInterval(selector,crossoverIntervalRange.random());
	case 1: return new CrossoverOnePoint(new SelectOneNeighboringPair(modulo,offset));
	case 2: return new CrossoverTwoPoints(new SelectTwoNeighboringPairs(modulo,offset));
	case 3: return RandomNumber.getBoolean() ? new CrossoverPickOne() : new CrossoverPickOne(selector);
	case 4: return RandomNumber.getBoolean() ? new CrossoverAdewaya() : new CrossoverAdewaya(selector);
	case 5: return new Mutation3parents(selector);
	case 6: return new MutationFixedStdDev(selector,mutationStandardDeviationRange.random());
	case 7: return new CrossoverOnePointEach(new SelectOneNeighboringPair(modulo,offset));
	case 8: return new CrossoverTwoPointsEach(new SelectTwoNeighboringPairs(modulo,offset));
	case 9: return new MutationDelete(new SelectChunk(deleteSizes,modulo,offset),minEvolvableSize);
	case 10: return new MutationInsert(selector,insertSizes);
	default: Error.fatal("bad case choosing child maker = " + childMaker);
	}
	Error.fatal("should never get here");
	return null; // should never happen
}
protected Selector getRandomSelector(int modulo, int offset, int minEvolvableSize, int maxEvolvableSize) {
        int selectorCase = new IntegerInterval(0,4).random();
        int[] numberToChoose = {modulo}; // chunk size.  This is a weakness.
        switch(selectorCase) {
                case 0: 
                        return new SelectAll();
                case 1: {
                        double probability = selectionProbabilityRange.random();
                        int minSize = new IntegerInterval(1,Math.max(1,(int)Math.round(minEvolvableSize*probability))).random();
                        return new SelectByProbability(probability, minEvolvableSize);
                }
                case 2: 
                        return new SelectFixedNumber(new IntegerInterval(1,maxEvolvableSize-1).random());
                case 3: {
                        double probability = new DoubleInterval(0.1,1.0/numberToChoose[0]).random();
                        Selector selector =  new SelectByProbability(probability, 1);
                        return new SelectChunk(selector, numberToChoose, modulo, offset);
                }
                case 4:
                        Selector selector =  new SelectFixedNumber(new IntegerInterval(1,(maxEvolvableSize/numberToChoose[0])-1).random());
                        return new SelectChunk(selector, numberToChoose, modulo, offset);
                default: 
                        Error.fatal("bad case choosing selector = " + selectorCase); 
                        break;
        }
        Error.fatal("should never get here");
        return null;
}
// UNIT TEST
static public void setDefaultCrossoverIntervalRange(DoubleInterval range) {
	DoubleInterval crossoverIntervalRange = new DoubleInterval(range);
	defaultCrossoverIntervalRange.limitTo(new DoubleInterval(0,1));
}
// UNIT TEST
static public void setDefaultMutationStandardDeviationRange(DoubleInterval range) {
	DoubleInterval mutationStandardDeviationRange = new DoubleInterval(range);
	defaultMutationStandardDeviationRange.limitTo(new DoubleInterval(0,Double.MAX_VALUE));
}
// UNIT TEST
static public void setDefaultSelectionProbabilityRange(DoubleInterval range) {
	DoubleInterval selectionProbabilityRange = new DoubleInterval(range);
	defaultSelectionProbabilityRange.limitTo(new DoubleInterval(0,1));
}
// UNIT TEST
static public void setDefaultWeightRange(DoubleInterval range) {
	DoubleInterval weightRange = new DoubleInterval(range);
	defaultWeightRange.limitTo(new DoubleInterval(0,Double.MAX_VALUE));
}

public String toString() {
	return "ChildMakerProviderRandom " + super.toString();
}
}