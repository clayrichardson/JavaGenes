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

import java.lang.Math;
import gov.nasa.javaGenes.chemistry.Constants;

public class StillingerWeberSi extends StillingerWeber {

protected int[] getChromosomeArraySizes() {
  int[] arraySizes = {numberOfTwoBodyParameters,numberOfThreeBodyParameters};
  return arraySizes;
}
protected void formSetup() {
  super.formSetup();
  alleleSetup.add("SiSi", 0);
  alleleSetup.add("SiSiSi", 1);
}
protected void setupChromosomeFromPaper() {
  ChromosomeParametersData SiSi = new ChromosomeParametersData("SiSi");
  SiSi.add("A",7.049556277);
  SiSi.add("B",0.6022245584);
  SiSi.add("C",1); // factored out in the paper
  SiSi.add("p",4);
  SiSi.add("q",0);
  SiSi.addNoEvolution("a",1.8);

  ChromosomeParametersData SiSiSi = new ChromosomeParametersData("SiSiSi");
  SiSiSi.add("alpha",0);
  SiSiSi.add("lambda",21);
  SiSiSi.addNoEvolution("theta0",Constants.TetrahedronAngle);
  SiSiSi.add("gamma",1.2);
  SiSiSi.addNoEvolution("a1",1.8);
}
}
