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

import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.ExtendedVector;

/**
This class keeps a list of ChildMaker.  When asked, a ChildMakerProvider
will supply a random ChildMaker from this list.
@see ChildMaker
*/
public class ChildMakerProvider implements java.io.Serializable {
protected ExtendedVector childMakers = new ExtendedVector();

/**
add a ChildMaker to the list
*/
public void add(ChildMaker c) {
  Error.assertTrue(c != null);
  childMakers.addElement(c);
}
public ChildMaker get() { 
	Error.assertTrue(size() > 0);
	return (ChildMaker)childMakers.getRandomElement();
}
public void removeLastChildMaker() {
	if (size() > 0)
		childMakers.removeElementAt(size()-1);
}
/**
@arg totalNumberOfKidsProduced used by subclass
@return a random ChildMaker
*/
public ChildMaker getChildMaker(int totalNumberOfKidsProduced) { 
  return get();
}
public int size() {return childMakers.size();}
public ChildMaker get(int index) {return (ChildMaker)childMakers.elementAt(index);}
public void setFitnessFunction(FitnessFunction ff) {
    String[] fitnessNames = ff.getNameArray();
	setFitnessFunctionNames(fitnessNames);
}
public void setFitnessFunctionNames(String[] names) {
    for(int i = 0; i < childMakers.size(); i++)
        get(i).setFitnessFunctionNames(names);
}
public String toString() {
  String s = "ChildMakerProvider\n";
  for(int i = 0; i < childMakers.size(); i++){
    s += childMakers.elementAt(i).toString() + "\n";
  }
  s += "end ChildMakerProvider\n";
  return s;
}
}