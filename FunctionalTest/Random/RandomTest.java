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
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.alsUtility.IntegerInterval;
import gov.nasa.alsUtility.Sample;
import java.lang.Integer;
import gov.nasa.javaGenes.core.RouletteWheel;
import gov.nasa.javaGenes.core.ChangingWeightsObject;
import gov.nasa.alsUtility.Error;

public class RandomTest {

public static void main(String[] arguments) {
    final int size = 100000;
    int booleanCount = 0;

    for(int i = 0; i < size; i++) 
	if (RandomNumber.getBoolean())
	    booleanCount++;
    System.out.println("boolean true (expect 0.5) = " + (double)booleanCount/(double)size);

    int[] indexCount = new int[4];
    for(int i = 0; i < size; i++)
		indexCount[RandomNumber.getIndex(indexCount.length)]++;
    System.out.println("index counts should be even:");
	for(int i = 0; i < indexCount.length;i++)
		System.out.println("\t" + i + "\t" + indexCount[i]);

    indexCount = new int[4];
	IntegerInterval integerInterval = new IntegerInterval(0,3);
    for(int i = 0; i < size; i++)
		indexCount[integerInterval.random()]++;
    System.out.println("index counts should be even:");
	for(int i = 0; i < indexCount.length;i++)
		System.out.println("\t" + i + "\t" + indexCount[i]);

    int probabilityCount = 0;
    for(int i = 0; i < size; i++) 
	if (RandomNumber.getProbability(.2) == true)
	    probabilityCount++;
    System.out.println("probabiity is true (expect 0.2) = " + (double)probabilityCount/(double)size);

    Sample sample = new Sample();
    for(int i = 0; i < size; i++)
	sample.addDatum(RandomNumber.getGaussian());
    System.out.println("Gaussian1: mean     (expect 0): " + sample.getMean());
    System.out.println("standard deviation (expect 1): " + sample.getStandardDeviation());

    sample = new Sample();
    int[] array = {3,4,5,6,7};
    for(int i = 0; i < size; i++)
	sample.addDatum(RandomNumber.getIntFromArray(array));
    System.out.println("getIntFromArray: mean     (expect 5): " + sample.getMean());

    sample = new Sample();
    final double sd = 10;
    for(int i = 0; i < size; i++)
	sample.addDatum(RandomNumber.getGaussian(sd));
    System.out.println("Gaussian2: mean     (expect 0): " + sample.getMean());
    System.out.println("standard deviation (expect "+sd+"): " + sample.getStandardDeviation());

    int[] histogram = new int[3];
    RouletteWheel wheel = new RouletteWheel();
    wheel.add(new ChangingWeightsObject(new Integer(3),3,0));
    wheel.add(new ChangingWeightsObject(new Integer(5),5,0));
    wheel.add(new ChangingWeightsObject(new Integer(2),2,0));
	      for(int i = 0; i < 10000; i++) {
		  switch(((Integer)wheel.spinWheel(1)).intValue()) {
		  case 2: histogram[0]++; break;
		  case 3: histogram[1]++; break;
		  case 5: histogram[2]++; break;
		  default: Error.fatal("bad case");
		  }
	      }
	      System.out.println("RouletteWheel");
	      System.out.println("Expect 2000: " + histogram[0]);
	      System.out.println("Expect 3000: " + histogram[1]);
	      System.out.println("Expect 5000: " + histogram[2]);
}
}
