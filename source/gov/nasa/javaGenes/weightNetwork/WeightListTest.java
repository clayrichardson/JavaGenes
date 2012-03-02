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
package gov.nasa.javaGenes.weightNetwork;

import junit.framework.TestCase;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.alsUtility.Error;

public class WeightListTest extends TestCase {
private WeightList list;
private Weight w1;
private Weight w2;
private Weight w3;

public WeightListTest(String name) {super(name);}

public void setUp() {
    list = new WeightList();

    w1 = new Weight();
    w1.setWeight(1);
    list.add(w1);

    w2 = new Weight();
    w2.setWeight(2);
    list.add(w2);

    w3 = new Weight();
    w3.setWeight(3);
    list.add(w3);

    list.initializeWeightSum();
    list.reinitialize();
}
public void testAdd() {
    list = new WeightList();
   
    assertTrue("1", !list.currentlyContains(w1));
    assertTrue("2", !list.more());
    list.initializeWeightSum();
    list.reinitialize();
    assertTrue("3", !list.currentlyContains(w1));
    assertTrue("4", !list.more());
    assertTrue("4.01", list.getFirst() == null);
    assertTrue("4.02", Utility.nearlyEqual(list.getWeightSum(),0));
    assertTrue("4.03", list.getInitialSize() == 0);
    assertTrue("4.04", !list.everContains(w1));
    
    list.add(w1);
    assertTrue("3.1", !list.currentlyContains(w1));
    assertTrue("3.2", list.everContains(w1));
    assertTrue("4.1", !list.more());
    list.initializeWeightSum();
    list.reinitialize();
    assertTrue("5", list.currentlyContains(w1));
    assertTrue("5.1", list.everContains(w1));
    assertTrue("6", list.more());
    assertTrue("7", !list.currentlyContains(w2));
    assertTrue("8", w1.getWeightList() == list);
    assertTrue("9", list.getFirst() == w1);
    assertTrue("10", Utility.nearlyEqual(list.getWeightSum(),1));
    assertTrue("11", w1.getNext() == null);
    assertTrue("12", w1.getPrevious() == null);
    assertTrue("12.1", list.getInitialSize() == 1);
    
    list.add(w2);
    assertTrue("13", !list.currentlyContains(w2));
    list.initializeWeightSum();
    list.reinitialize();
    assertTrue("14", list.currentlyContains(w1));
    assertTrue("15", list.currentlyContains(w2));
    assertTrue("15.1", list.everContains(w2));
    assertTrue("16", list.more());
    assertTrue("17", !list.currentlyContains(w3));
    assertTrue("18", w1.getWeightList() == list);
    assertTrue("18.1", w2.getWeightList() == list);
    assertTrue("19", list.getFirst() == w1);
    assertTrue("20", Utility.nearlyEqual(list.getWeightSum(),3));
    assertTrue("21", w1.getNext() == w2);
    assertTrue("22", w1.getPrevious() == null);
    assertTrue("23", w2.getNext() == null);
    assertTrue("24", w2.getPrevious() == w1);
    assertTrue("24.1", list.getInitialSize() == 2);
    
    list.add(w3);
    assertTrue("23", !list.currentlyContains(w3));
    list.initializeWeightSum();
    list.reinitialize();
    assertTrue("24", list.currentlyContains(w1));
    assertTrue("25", list.currentlyContains(w2));
    assertTrue("25.1", list.currentlyContains(w3));
    assertTrue("26", list.more());
    assertTrue("27", w3.getWeightList() == list);
    assertTrue("28", w1.getWeightList() == list);
    assertTrue("28.1", w2.getWeightList() == list);
    assertTrue("29", list.getFirst() == w1);
    assertTrue("30", Utility.nearlyEqual(list.getWeightSum(),6));
    assertTrue("31", w1.getNext() == w2);
    assertTrue("32", w1.getPrevious() == null);
    assertTrue("33", w2.getNext() == w3);
    assertTrue("34", w2.getPrevious() == w1);
    assertTrue("35", w3.getNext() == null);
    assertTrue("36", w3.getPrevious() == w2);
    assertTrue("36.1", list.getInitialSize() == 3);
}
public void testWeightChanged() {
    assertTrue("1",list.getWeightSum() == 6);
    w1.setWeightAndPropagate(3);
    assertTrue("2",list.getWeightSum() == 8);
    w2.setWeightAndPropagate(1);
    assertTrue("3",list.getWeightSum() == 7);
    w3.setWeightAndPropagate(3);
    assertTrue("4",list.getWeightSum() == 7);
    for(int i = 0; i < 100; i++) {
        float r = (float)new DoubleInterval(1,5).random();
        switch(i%3) {
            case 0: w1.setWeightAndPropagate(r); break;
            case 1: w2.setWeightAndPropagate(r); break;
            case 2: w3.setWeightAndPropagate(r); break;
            default: Error.fatal("");
        }
        // can fail occasionally, probably due to floating point inaccuracies
        assertTrue("i="+i, Utility.nearlyEqual(list.getWeightSum(),w1.getWeight()+w2.getWeight()+w3.getWeight()));
    }
}
public void testRemove() {
    assertTrue("1",list.getMinCurrentWeight() == 1);
    assertTrue("2",list.getMaxCurrentWeight() == 3);

    w1.removeFromWeightList();
    assertTrue("3",list.getWeightSum() == 5);
    assertTrue("4",list.getMinCurrentWeight() == 2);
    assertTrue("5",list.getMaxCurrentWeight() == 3);
    assertTrue("6", !list.currentlyContains(w1));
    assertTrue("7", list.currentlyContains(w2));
    assertTrue("8", list.currentlyContains(w3));
    assertTrue("9", list.currentSize() == 2);

    list.reinitialize();
    w2.removeFromWeightList();
    assertTrue("13",list.getWeightSum() == 4);
    assertTrue("14",list.getMinCurrentWeight() == 1);
    assertTrue("15",list.getMaxCurrentWeight() == 3);
    assertTrue("16", list.currentlyContains(w1));
    assertTrue("17", !list.currentlyContains(w2));
    assertTrue("18", list.currentlyContains(w3));
    assertTrue("19", list.currentSize() == 2);
    
    list.reinitialize();
    w3.removeFromWeightList();
    assertTrue("23",list.getWeightSum() == 3);
    assertTrue("24",list.getMinCurrentWeight() == 1);
    assertTrue("25",list.getMaxCurrentWeight() == 2);
    assertTrue("26", list.currentlyContains(w1));
    assertTrue("27", list.currentlyContains(w2));
    assertTrue("28", !list.currentlyContains(w3));
    assertTrue("29", list.currentSize() == 2);

    list.reinitialize();
    w1.removeFromWeightList();
    w3.removeFromWeightList();
    assertTrue("33",list.getWeightSum() == 2);
    assertTrue("34",list.getMinCurrentWeight() == 2);
    assertTrue("35",list.getMaxCurrentWeight() == 2);
    assertTrue("36", !list.currentlyContains(w1));
    assertTrue("37", list.currentlyContains(w2));
    assertTrue("38", !list.currentlyContains(w3));
    assertTrue("39", list.currentSize() == 1);

    list.reinitialize();
    w1.removeFromWeightList();
    w2.removeFromWeightList();
    w3.removeFromWeightList();
    assertTrue("43",list.getWeightSum() == 0);
    assertTrue("46", !list.currentlyContains(w1));
    assertTrue("47", !list.currentlyContains(w2));
    assertTrue("48", !list.currentlyContains(w3));
    assertTrue("49", list.currentSize() == 0);
}

}


