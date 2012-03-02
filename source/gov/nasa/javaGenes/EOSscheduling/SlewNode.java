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
//  Created by Al Globus on Wed Sep 11 2002.
package gov.nasa.javaGenes.EOSscheduling;

import gov.nasa.alsUtility.Error;

public class SlewNode extends Node {
protected SlewRequirement slewRequirement;
protected boolean mustRampToEnd = false;
public void setSlew(SlewRequirement inSlew) {
    Error.assertNotNull(inSlew);
    slewRequirement = inSlew;
}
public SlewRequirement getSlew() {return slewRequirement;}
public void setMustRampToEnd(boolean value) {mustRampToEnd = value;}
public boolean getMustRampToEnd() {return mustRampToEnd;}
public int numberOfParameters() {return slewRequirement.numberOfParameters();}
public double getParameter(int index) {return slewRequirement.getParameter(index);}
public double[] getParameters() {return slewRequirement.getParameters();}
}
