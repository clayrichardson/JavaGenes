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
//  Created by Al Globus on Wed Jan 29 2003.
package gov.nasa.javaGenes.hillClimbing;

import junit.framework.TestCase;
import gov.nasa.javaGenes.core.FitnessFunctionFixed;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.javaGenes.permutation.PermutationEvolvable;
import gov.nasa.javaGenes.core.Individual;

public class RestartAfterNChildrenWithoutImprovementTest extends TestCase {

public RestartAfterNChildrenWithoutImprovementTest(String name) {super(name);}

public void testAll() {
    final int size = 10;
    RestartAfterNChildrenWithoutImprovement r = new RestartAfterNChildrenWithoutImprovement(size);
    Individual worse = new Individual(new PermutationEvolvable(2),new FitnessFunctionFixed(3));
    Individual better = new Individual(new PermutationEvolvable(2),new FitnessFunctionFixed(2));
    for(int i = 0; i < 10 * size; i++) {
        if (i > 0 && (i+5 % size == 0)) {
            assertTrue(i+"", r.shouldRestart());
            r.restarting();
        } else
            assertTrue(i+"", !r.shouldRestart());
        if (i >= 5) 
            r.childCreated(better,worse); 
        else {
            if (RandomNumber.getBoolean())
                r.childCreated(better,better);
            else
                r.childCreated(worse,better);
        }
        r.lastChildAccepted();
    }
}
}

