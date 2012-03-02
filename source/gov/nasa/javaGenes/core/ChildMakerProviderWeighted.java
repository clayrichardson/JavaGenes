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

public class ChildMakerProviderWeighted extends ChildMakerProvider {
protected RouletteWheel rouletteWheel = new RouletteWheel();

public void add(ChildMaker c) {
    rouletteWheel.add(new ChangingWeightsObject(c));
}
public void add(double weight, ChildMaker c) {
    add(new ChangingWeightsObject(c,weight,0));
}
public void add(ChangingWeightsObject w) {
    super.add((ChildMaker)w.getObject());
    rouletteWheel.add(w);
}
/**
return a ChildMaker based on a weighted roulette wheel. get() returns random childmaker
*/
public ChildMaker getChildMaker(int totalNumberOfKidsProduced) { 
    return (ChildMaker)rouletteWheel.spinWheel(totalNumberOfKidsProduced);
}
public String toString() {
  return "ChildMakerProviderWeighted " + rouletteWheel.toString();
}
}