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
package gov.nasa.javaGenes.EOSscheduling.HBSS;

import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.javaGenes.EOSscheduling.EOSschedulingEvolvable;
import gov.nasa.javaGenes.permutation.PermutationEvolvable;
import gov.nasa.alsUtility.Error;

/**
NOTE: it doesn't really matter what childmaker is used because the HBSS algorithm
will rearrange the permutation to reflect the order tasks were chosen by the
TaskList (a RouletteWheel of one sort or another
*/
public class ChildMaker extends gov.nasa.javaGenes.core.ChildMaker {
public int numberOfParents() {return 1;}
public Evolvable[] makeChildren(Evolvable[] parents) {
    Error.assertTrue(parents.length == 1);
    PermutationEvolvable[] p = new PermutationEvolvable[1];
    p[0] = (PermutationEvolvable)new EOSschedulingEvolvable(parents[0].getSize(),"ascending");
    return p;
}
public String toString() {
	return "EOSscheduling.HBSS.ChildMaker";
}
}

