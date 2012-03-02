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

import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.javaGenes.chemistry.Constants;
import gov.nasa.alsUtility.DoubleInterval;

public class StillingerWeberSiF extends StillingerWeber {
// units the energy is returned in; from paper
protected double energyUnitKcalPerMol = 50.0;
protected double energyUnitErgPerAtom = 3.4723e-12;

static public double getLengthScale() {return 1/2.0951;} // angstroms
protected double getEnergy(TwoBody pair) {
  return energyUnitKcalPerMol * super.getEnergy(pair);
}
public double getForce(TwoBody pair) {
  return super.getForce(pair) * energyUnitKcalPerMol * getLengthScale();
}
protected double getEnergy(ThreeBody threeBody) {
  return energyUnitKcalPerMol * super.getEnergy(threeBody);
}
public void setCutoff(TwoBody pair, double value) {
    super.setCutoff(pair,value*getLengthScale());
}

public void setCutoff(ThreeBody threeBody, double value) {
    super.setCutoff(threeBody,value*getLengthScale());
}
public void setCutoff(ThreeBody threeBody, double a1_value, double a2_value) {
    super.setCutoff(threeBody,a1_value * getLengthScale(),a2_value * getLengthScale());
}

protected StillingerWeberSiF() {}
public StillingerWeberSiF(
  DoubleInterval factorInterval,DoubleInterval exponentInterval,DoubleInterval cutoffInterval) {
    setFactorInterval(factorInterval);
    setExponentInterval(exponentInterval);
    setCutoffInterval(cutoffInterval);
    setupAllelesAndChromosome();
}

protected void formSetup() {
  super.formSetup();
  need("SiSi", 0);
  need("SiF", 1);
  alleleSetup.add("FSi",1);
  need("FF", 2);

  need("SiSiSi", 3);
  need("SiSiF", 4);
  alleleSetup.add("FSiSi", 4);
  need("SiFSi", 5);
  need("SiFF", 6);
  alleleSetup.add("FFSi", 6);
  need("FSiF", 7);
  need("FFF", 8);
}

protected void need(String name,int index) {
  if (name.equals("SiSi") || name.equals("FF") || name.equals("SiF") || name.equals("FSi")) {
    addTwoBody(name,index);
  } else if (name.equals("SiSiSi") ||
            name.equals("SiSiF") ||
            name.equals("SiFSi") ||
            name.equals("SiFF") ||
            name.equals("FSiSi") ||
            name.equals("FSiF") ||
            name.equals("FFSi"))
    addThreeBody(name, index);
  else if (name.equals("FFF"))
    addFFF(index);
  else
    Error.assertTrue(false);
}
public void addTwoBody(String name, int index) {
  addToArraySizes(numberOfTwoBodyParameters);
  alleleSetup.add(name,index);
  setupChromosomeFromPaper(name);
}
public void addThreeBody(String name, int index) {
  addToArraySizes(numberOfThreeBodyParameters);
  alleleSetup.add(name,index);
  setupChromosomeFromPaper(name);
}
public void addFFF(int index) {
  addToArraySizes(numberOfThreeBodyParameters + flourineThreeBodyParametersIncrement);
  alleleSetup.add("FFF",index);
  alleleSetup.add("delta", delta_index, factorInterval);
  alleleSetup.add("m", m_index, exponentInterval);
  alleleSetup.add("beta", beta_index, cutoffInterval);
  alleleSetup.add("a2", a2_index, bondLengthInterval);
  setupChromosomeFromPaper("FFF");
}
protected void setupChromosomeFromPaper(String name) {
  if (name.equals("SiSi")) {
    ChromosomeParametersData SiSi = new ChromosomeParametersData(name);
    SiSi.add("A",7.049556277);
    SiSi.add("B",0.6022245584);
    SiSi.add("C",1); // factored out in the paper
    SiSi.add("p",4);
    SiSi.add("q",0); // factored out in the paper
    SiSi.addNoEvolution("a",1.8);
  } else if (name.equals("FF")) {
    ChromosomeParametersData FF = new ChromosomeParametersData(name);
    FF.add("A",0.52276);
    FF.add("B",0.11277);
    FF.add("C",0.579495);
    FF.add("p",8);
    FF.add("q",4);
    FF.addNoEvolution("a",2.086182);
  } else if (name.equals("SiF") || name.equals("FSi")) {
    ChromosomeParametersData SiF = new ChromosomeParametersData(name);
    SiF.add("A",21.23414138);
    SiF.add("B",0.5695476433);
    SiF.add("C",1.3);
    SiF.add("p",3);
    SiF.add("q",2);
    SiF.addNoEvolution("a",1.8);
  } else if (name.equals("SiSiSi")) {
    ChromosomeParametersData SiSiSi = new ChromosomeParametersData(name);
    SiSiSi.add("alpha",0);
    SiSiSi.add("lambda",21);
    SiSiSi.addNoEvolution("theta0",Constants.TetrahedronAngle);
    SiSiSi.add("gamma",1.2);
    SiSiSi.addNoEvolution("a1",1.8);
  } else if (name.equals("SiFF") || name.equals("FFSi")) {
    ChromosomeParametersData SiFF = new ChromosomeParametersData(name);
    SiFF.add("alpha",3.5);
    SiFF.add("lambda",0);
    SiFF.addNoEvolution("theta0",NotApplicable);
    SiFF.add("gamma",1);
    SiFF.addNoEvolution("a1",1.8);
  } else if (name.equals("SiSiF") || name.equals("FSiSi")) {
    ChromosomeParametersData SiSiF = new ChromosomeParametersData(name);
    SiSiF.add("alpha",0);
    SiSiF.add("lambda",15);
    SiSiF.addNoEvolution("theta0",Constants.TetrahedronAngle);
    SiSiF.add("gamma",1);
    SiSiF.addNoEvolution("a1",1.8);
  } else if (name.equals("SiFSi")) {
    ChromosomeParametersData SiFSi = new ChromosomeParametersData(name);
    SiFSi.add("alpha",50);
    SiFSi.add("lambda",0);
    SiFSi.addNoEvolution("theta0",NotApplicable);
    SiFSi.add("gamma",1.3);
    SiFSi.addNoEvolution("a1",1.8);
  } else if (name.equals("FSiF")) {
    ChromosomeParametersData FSiF = new ChromosomeParametersData(name);
    FSiF.add("alpha",-3.2);
    FSiF.add("lambda",24);
    FSiF.addNoEvolution("theta0",Utility.degrees2radians(103));
    FSiF.add("gamma",1);
    FSiF.addNoEvolution("a1",1.8);
  } else if (name.equals("FFF")) {
    ChromosomeParametersData FFF = new ChromosomeParametersData(name);
    FFF.add("alpha",38.295);
    FFF.add("lambda",-19.1475);
    FFF.addNoEvolution("theta0",Utility.degrees2radians(90));
    FFF.add("gamma",1.738485);
    FFF.addNoEvolution("a1",1.622586);
    FFF.add("delta",0.0818182);
    FFF.add("m",4);
    FFF.add("beta",0.579495);
    FFF.addNoEvolution("a2",2.086182);
  } else
    Error.assertTrue(false);
}
}