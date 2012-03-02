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

public class ChildMakerEvolvingProvider extends ChildMakerProvider {
public ChildMaker get() {
  ChildMaker c1 = super.get();
  ChildMaker c2 = super.get();
  return best(c1,c2);
}
protected ChildMaker best(ChildMaker c1, ChildMaker c2) {
    if (c1.neverUsed()) return c1;
    if (c2.neverUsed()) return c2;
    if (c1.proportionDown() > c2.proportionDown())
        return c1;
    return c2;
}

public String toString() {return "Evolving " + super.toString();}

}
