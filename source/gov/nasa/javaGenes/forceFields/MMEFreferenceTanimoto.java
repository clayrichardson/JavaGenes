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
//  Created by Al Globus on Fri Apr 25 2003.

package gov.nasa.javaGenes.forceFields;

import gov.nasa.alsUtility.Utility;

public class MMEFreferenceTanimoto  extends MMEFreferenceRMS {

public MMEFreferenceTanimoto(Potential p, ManyMultiBodiesForOneEnergy m, double inEnergyRangeDivideBy, double inReferenceEnergy, double referenceEnergyLowerBound, double referenceEnergyUpperBound,boolean inDoPerAtomEnergies) {
    super(p,m,inEnergyRangeDivideBy,inReferenceEnergy,referenceEnergyLowerBound,referenceEnergyUpperBound,inDoPerAtomEnergies);
}
public MMEFreferenceTanimoto(Potential p, ManyMultiBodiesForOneEnergy m, double inEnergyRangeDivideBy, double referenceEnergy) {
    this(p,m,inEnergyRangeDivideBy,referenceEnergy,referenceEnergy,referenceEnergy,false);
}
protected double calculateDelta(double energy,double target) {
    return Utility.TanimotoDistance(energy,target);
}
}

