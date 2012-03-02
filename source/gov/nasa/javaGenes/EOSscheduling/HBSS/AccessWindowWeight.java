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

import gov.nasa.javaGenes.weightNetwork.Weight;
import gov.nasa.javaGenes.EOSscheduling.AccessWindow;
import gov.nasa.javaGenes.EOSscheduling.SlewRequirement;

public class AccessWindowWeight extends Weight {

protected AccessWindow accessWindow;

protected AccessWindowWeight(){}
/**
assumes the associated sensor is cross track slewable.  The off nadir pointing is
used for the weight
*/
public AccessWindowWeight(TaskWeight inTaskWeight,AccessWindow inAccessWindow) {
    accessWindow = inAccessWindow;
    SlewRequirement sr = accessWindow.getSlewRequirement(); 
    setWeight(1+(float)java.lang.Math.abs(sr.getParameter(0))); //NOTE: only works for 1D slews, 1 is to avoid 0 weights
}
public AccessWindow getAccessWindow() {
    return accessWindow;
}
}
