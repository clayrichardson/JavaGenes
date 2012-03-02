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
import java.util.Comparator;

class ChildMakerDownFractionComparator implements Comparator,java.io.Serializable {
public int compare(Object o1, Object o2) {
	Error.assertTrue(o1 instanceof ChildMaker && o2 instanceof ChildMaker);
	ChildMaker c1 = (ChildMaker)o1;
	ChildMaker c2 = (ChildMaker)o2;

	if (c1.forEvolution.getN() == 0 && c2.forEvolution.getN() == 0)
		return 0;
	if (c1.forEvolution.getN() == 0)
		return -1;
	if (c2.forEvolution.getN() == 0)
		return 1;

	double score1 = c1.forEvolution.getDownFranction();
	double score2 = c2.forEvolution.getDownFranction();
	if (score1 > score2)
		return -1; // these go first when childMakers is sorted
	else if (score1 == score2)
		return 0;
	else
		return 1;
}
}
