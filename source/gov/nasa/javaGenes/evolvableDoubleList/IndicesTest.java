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

import junit.framework.TestCase;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.DoubleInterval;


public class IndicesTest extends TestCase {

public IndicesTest(String name) {super(name);}

public void testAddAll() {
    EvolvableDoubleList list = new EvolvableDoubleList(7);
    Indices indices = new Indices();
    indices.addAll(list.getSize());
    int[] output = indices.getArray();
    assertTrue("length", output.length == 7);
    for(int i = 0; i < output.length; i++)
        assertTrue(i+"", output[i] == i);
}
public void testAddIndex() {
    int[] in1 = {1,2,3};
    int[] out1 = {1,2,3};
    addIndexTest("1",in1,out1);
    int[] in2 = {2,1,3};
    addIndexTest("2",in2,out1);
    int[] in3 = {4,4,4};
    int[] out3 = {4};
    addIndexTest("3",in3,out3);
    int[] in4 = {4,4,1};
    int[] out4 = {1,4};
    addIndexTest("4",in4,out4);
}
protected void addIndexTest(String name, int[] in, int[] out) {
    Indices indices = new Indices();
    for(int i = 0; i < in.length; i++)
        indices.addIndex(in[i]);
    int[] output = indices.getArray();
    assertTrue(name+" length", out.length == output.length);
    for(int i = 0; i < out.length; i++)
        assertTrue(name+" "+i+" "+output, out[i] == output[i]);
}
}