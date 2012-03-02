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
//  Created by Al Globus on Wed Dec 04 2002.
package gov.nasa.javaGenes.EOSscheduling;
import gov.nasa.javaGenes.permutation.PermutationChildMaker;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.alsUtility.Error;

public class RandomChild extends PermutationChildMaker {
public int numberOfParents() {return 1;} // need to know size, don't actually use parent
public Evolvable[] makeChildren(Evolvable[] parents) {
    Error.assertTrue(parents.length == 1);
    Error.assertTrue(parents[0] instanceof EOSschedulingEvolvable);
    EOSschedulingEvolvable kid = new EOSschedulingEvolvable(parents[0].getSize());
    Evolvable[] child = new Evolvable[1];
    child[0] = kid;
    return child;
}
public String toString() {
	return "RandomChild";
}

}
