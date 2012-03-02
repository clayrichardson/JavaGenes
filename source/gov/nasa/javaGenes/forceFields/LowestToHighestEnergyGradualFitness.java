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
package gov.nasa.javaGenes.forceFields;

import gov.nasa.alsUtility.RootMeanSquares;
import gov.nasa.alsUtility.Error;

public class LowestToHighestEnergyGradualFitness extends LowestToHighestEnergyFitness {
protected double outOfOrderPenalty = 1;

/** for testing only */
protected LowestToHighestEnergyGradualFitness() {}
public LowestToHighestEnergyGradualFitness(Potential p, ManyMultiBodiesForOneEnergy m) {
    super(p,m);
}
protected double outOfOrder(double[] energy) {
    double numberOutOfOrder = super.outOfOrder(energy);
    RootMeanSquares rms = new RootMeanSquares();
    int fitness = 0;
    for(int i = 0; i < energy.length; i++)
        for(int j = i + 1; j < energy.length; j++)
            if (energy[i] >= energy[j])
                rms.addDatum(energy[i] - energy[j]);
    return numberOutOfOrder*outOfOrderPenalty + rms.rms();
}
/**
@arg inOutOfOrderPenalty multiply the number of out-of-order energies by this and add to rms Default = 1. Must be >= 0
*/
public void setOutOfOrderPenalty(double inOutOfOrderPenalty) {
    Error.assertTrue(inOutOfOrderPenalty >= 0);
    outOfOrderPenalty = inOutOfOrderPenalty;
}
}



