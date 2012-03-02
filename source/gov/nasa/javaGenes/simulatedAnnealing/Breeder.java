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
//  Created by Al Globus on Thu Dec 05 2002.
package gov.nasa.javaGenes.simulatedAnnealing;

import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.core.Individual;
import gov.nasa.javaGenes.core.Evolvable;
import gov.nasa.javaGenes.core.Population;
import gov.nasa.javaGenes.core.Parameters;
import gov.nasa.javaGenes.core.ChildMaker;
import gov.nasa.javaGenes.core.Checkpointer;

/**
implements simulated annealing.  Population size must be 2.  One anneals and the other holds the best so far.
*/
public class Breeder extends gov.nasa.javaGenes.hillClimbing.Breeder {

public Breeder(Parameters p,gov.nasa.javaGenes.simulatedAnnealing.Accepter inAccepter) {
    super(p,inAccepter,new gov.nasa.javaGenes.hillClimbing.RestartNever(),null);
}
public String toString() {return "Simulated Annealing: " + accepter.toString();}
}


