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
import java.io.PrintWriter;
import java.io.Serializable;
import gov.nasa.alsUtility.Utility;
import gov.nasa.javaGenes.chemistry.Atom;
import gov.nasa.alsUtility.IncrementIterator;

public class PutPotentialEnergiesAndForces implements Serializable {
protected Potential _potential;
protected Chromosome _chromosome;
protected double _minimumDistance = 0.5;
protected double _maximumDistance = 5.0;
protected double _minimumAngle = Utility.degrees2radians(10);
protected double _maximumAngle = Utility.degrees2radians(250);
protected double _lengthScale = 1;

public PutPotentialEnergiesAndForces(Potential potential, Chromosome chromosome) {
  _potential = potential;
  _chromosome = chromosome;
  if (_potential instanceof StillingerWeberSiF)
    _lengthScale = StillingerWeberSiF.getLengthScale();
}
public void putTwoBody(String filename, Atom a, Atom b, int numberOfValues) {
  PrintWriter out = Utility.outputFile(filename);
  out.println(a + "-" + b + "distance\tenergy\tforce");
  TwoBody body = new TwoBody(a,b);
  _potential.setChromosome(_chromosome);
  IncrementIterator i = new IncrementIterator(_minimumDistance,_maximumDistance,numberOfValues);
  while(i.more()) {
    body.setR(i.value()*_lengthScale);
    double energy = _potential.getEnergy(body);
    double force = _potential.getForce(body);
    out.println(i.value() + "\t" + energy + "\t" + force);
    i.increment();
  }
  out.close();
}
public void putTwoBodyEnergies(String filename, Atom a, Atom b, int numberOfValues) {
  PrintWriter out = Utility.outputFile(filename);
  out.println(a + "-" + b + "distance\tenergy");
  TwoBody body = new TwoBody(a,b);
  _potential.setChromosome(_chromosome);
  IncrementIterator i = new IncrementIterator(_minimumDistance,_maximumDistance,numberOfValues);
  while(i.more()) {
    body.setR(i.value()*_lengthScale);
    double energy = _potential.getEnergy(body);
    out.println(i.value() + "\t" + energy);
    i.increment();
  }
  out.close();
}
public void putThreeBody(String filename, Atom a, Atom b, Atom c, int squareRootOfNumberOfValues) {
  PrintWriter out = Utility.outputFile(filename);
  out.println(a + "-" + b + "distance\tangle\tenergy");
  ThreeBody body = new ThreeBody(a.toString(),b.toString(),c.toString(),1.0,1.0,1.0);
  _potential.setChromosome(_chromosome);
  IncrementIterator i = new IncrementIterator(_minimumDistance,_maximumDistance,squareRootOfNumberOfValues);
  for(;i.more();i.increment()) {
    body.setRJI(i.value()*_lengthScale);
    body.setRJK(i.value()*_lengthScale);
    IncrementIterator j = new IncrementIterator(_minimumAngle,_maximumAngle,squareRootOfNumberOfValues);
    for(;j.more();j.increment()) {
      body.setAngle(j.value());
      double energy = _potential.getEnergy(body);
      out.println(i.value() + "\t" + j.value() + "\t" + energy);
    }
  }
  out.close();
}

}
