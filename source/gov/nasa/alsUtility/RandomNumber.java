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
package gov.nasa.alsUtility;
import java.util.Random;
import gov.nasa.alsUtility.IO;
import java.io.PrintWriter;
import gov.nasa.alsUtility.Error;
import cern.jet.random.engine.RandomEngine;
import cern.jet.random.engine.MersenneTwister64;
import java.util.Date;
import gov.nasa.alsUtility.Vector3d;

/** 
single random number generator for the whole program. All distributions are flat unless
name indicates otherwise.

Modified 13 Dec 2002 to use colt MersenneTwister64
*/
public class RandomNumber {
protected static long seed = System.currentTimeMillis();
private static RandomEngine randomizer;

static {initialize();}

public synchronized static void initialize() {
     randomizer = new MersenneTwister64 ( new Date(seed) );
     PrintWriter out = IO.getPrintWriter("seed.txt");
     out.println("Random number seed = " + seed);
     out.close();
}

/**
@return a random doubled between 0 and 1
*/
public synchronized static double getDouble() {
	return randomizer.nextDouble();
}
/**
@return a random doubled between 0 and high
*/
public synchronized static double getDouble(double high) {
    return randomizer.uniform(0,high);
}
public synchronized static boolean getProbability(double probability) {
  if (probability <= 0) return false;
  if (probability >= 1) return true;
  return getDouble() <= probability;
}
/**
@return a random double within DoubleInterval i, inclusive
*/
public synchronized static double getDouble ( DoubleInterval i) {
    return randomizer.uniform(i.low(),i.high());
}
public synchronized static Vector3d getVector3d(DoubleInterval i) {
	return new Vector3d(getDouble(i),getDouble(i),getDouble(i));
}
/**
@return a random doubled from a Gaussian distribution with a mean of 0
and a standard deviation of 1.  

BUG?: 0 and 1 still valid with new generator?
*/
public synchronized static double getGaussian() {
  return getGaussian(1.0);
}
public synchronized static double getGaussian(double standardDeviation) {
    return  randomizer.gaussian(standardDeviation);
}
/**
@return a random double within IntegerInterval i, inclusive
*/
public synchronized static int getInteger ( IntegerInterval i) {
    return randomizer.choose(i.low(),i.high());
}
/**
fill results with random numbers in integer i inclusive.  May repeat numbers.
*/
public synchronized static void fillRandomly(IntegerInterval interval,int[] results) {
    for(int i = 0; i < results.length; i++)
        results[i] = getInteger(interval);
}

/**
flip a virtual coin
*/
public synchronized static boolean getBoolean() {
	return getDouble() < 0.5;
}
/**
set the random number generator seed. Not necessary unless you must
control the seed for repeatability. If never called, clock is used
to set the seed.
*/
public synchronized static  void setSeed(long value) {seed = value; initialize();}
public static long getSeed(){return seed;}

public synchronized static int getIntFromArray(int[] array) {
    Error.assertTrue(array.length > 0);
    return array[getIndex(array.length)];
}
public synchronized static Object getObjectFromArray(Object[] array) {
    Error.assertTrue(array.length > 0);
    return array[getIndex(array.length)];
}
public synchronized static String getStringFromArray(String[] array) {
	return (String)getObjectFromArray(array);
}
/**
@return a random value between 0 and limit - 1
*/
public synchronized static int getIndex (int limit){
    Error.assertTrue(limit >= 1);
    return randomizer.choose(0,limit-1);
}
public synchronized static int getUniqueIndex (int limit, int avoid){
  Error.assertTrue(limit >= 2);
  int r;
  do {
     r = getIndex(limit);
  } while (r == avoid);
  return r;
}

/**
for testing run: 
<code>java RandomNumber buckets trials</code>
*/
public static void test () {
/*
    int count[] = new int[Integer.valueOf(arguments[0]).intValue()];
    for(int i = 0; i < count.length; i++) count[i] = 0;
    int trials = Integer.valueOf(arguments[1]).intValue();
    for(int i = 0; i < trials; i++)
        count[getIndex (count.length)]++;
    for (int i = 0; i < count.length; i++)
        System.out.println (i + "\t" + count[i]);
*/
    IntegerInterval interval = new IntegerInterval(0,6);
    int[] array = new int[7];
    for(int i = 0; i < 10000; i++)
    	array[interval.random()]++;
    for(int i = 0; i < array.length; i++)
    	System.out.println(array[i]);
}
}
