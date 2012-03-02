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
import java.util.Hashtable;
import java.io.Serializable;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.javaGenes.chemistry.Atom;
import gov.nasa.javaGenes.chemistry.Constants;
import java.lang.Math;

/**

Implements Thomas A. Weber, Frank H. Stillinger, "Dynamical branching during
fluorination of the dimerized Si(100) surface: A molecular dynamics study,"
Journal of Chemical Physics, 92 (10) 15 May 1990.

The paper was hard to translate into a functional form.  They collapsed things
into constants in places we don't want to for the search.  Here's a summary
of my translation of the paper into a functional form:

two body form:
f2(r) = A(Br^-p - r^-q)exp(C/(r-a)); // cuttoff at a

three body form:
f3(rij,rjk,theta) =
  (alpha + lambda((cos(theta) - cos(theta0))^2)))
  * exp(gamma(1/(rij - a1) + 1/(rjk - a1)) // cuttoff at a1

plus an additional term for FFF not added on (see StillingerWeberSiF.java)
delta(rij*rjk)^-m * exp(beta(1/(rij - a2) + 1/(rjk - a2))) // cutoff at a2

*/
public class StillingerWeber extends Potential implements Serializable {
protected AlleleSetup alleleSetup = new AlleleSetup();
protected Chromosome chromosome;
protected AlleleTemplate alleles;
protected boolean setupComplete = false;

protected DoubleInterval bondLengthInterval = new DoubleInterval(1.0,4.0);
protected DoubleInterval exponentInterval = new DoubleInterval(0,100);
protected DoubleInterval factorInterval = new DoubleInterval(0,200);
protected DoubleInterval angleInterval = new DoubleInterval(0,2*Math.PI);
protected DoubleInterval cutoffInterval = new DoubleInterval(0,3);

protected final int A_index = 0;
protected final int B_index = 1;
protected final int C_index = 2;
protected final int p_index = 3;
protected final int q_index = 4;
protected final int a_index = 5;
protected final int numberOfTwoBodyParameters = 6;

protected final int alpha_index = 0;
protected final int lambda_index = 1;
protected final int theta0_index = 2;
protected final int gamma_index = 3;
protected final int a1_index = 4;
protected final int numberOfThreeBodyParameters = 5;

protected final int flourineThreeBodyParametersIncrement = 4;
protected final int specialThreeBodyParametersIncrement = flourineThreeBodyParametersIncrement;
protected final int delta_index = 5;
protected final int m_index = 6;
protected final int beta_index = 7;
protected final int a2_index = 8;


protected final double NotApplicable = 0.0;

protected Hashtable cutoffs = new java.util.Hashtable();

public StillingerWeber(){}
public StillingerWeber( DoubleInterval factorInterval,DoubleInterval exponentInterval,DoubleInterval cutoffInterval) {
  setFactorInterval(factorInterval);
  setExponentInterval(exponentInterval);
  setCutoffInterval(cutoffInterval);
}
public StillingerWeber(AtomicSpecies[] elements, DoubleInterval factorInterval,DoubleInterval exponentInterval,DoubleInterval cutoffInterval) {
    this(factorInterval, exponentInterval, cutoffInterval);
    // add two bodies
    for(int i = 0; i < elements.length; i++)
        for(int j = i; j < elements.length; j++) {
            TwoBody t = new TwoBody(elements[i].getElementName(),elements[j].getElementName());
            mustModel(t,Math.max(elements[i].getCutoff(),elements[j].getCutoff()));
        }
    // three bodies
    for(int i = 0; i < elements.length; i++) {
        String a = elements[i].getElementName();
        ThreeBody t = new ThreeBody(a,a,a);
        mustModel(t,elements[i].getCutoff());
        for(int j = i+1; j < elements.length; j++) {
            String b = elements[j].getElementName();
            double cutoff = Math.max(elements[i].getCutoff(),elements[j].getCutoff());
            mustModel(new ThreeBody(a,a,b),cutoff);
            mustModel(new ThreeBody(a,b,b),cutoff);
            mustModel(new ThreeBody(a,b,a),cutoff);
            mustModel(new ThreeBody(b,a,b),cutoff);
            for(int k = j + 1; k < elements.length; k++) {
                double cutoff2 = Math.max(cutoff,elements[k].getCutoff());
                String c = elements[k].getElementName();
                mustModel(new ThreeBody(c,a,b),cutoff2);
                mustModel(new ThreeBody(a,b,c),cutoff2);
                mustModel(new ThreeBody(b,c,a),cutoff2);
            }
	}
    }
}
public void mustModel(ManyMultiBodiesForOneEnergy many) {
    for(int i = 0; i < many.size(); i++) {
        MultiBodiesForOneEnergy bodies = many.getMultiBodies(i);
        for(int j = 0; j < bodies.size(); j++) {
            MultiBody m = bodies.get(j);
            mustModel(m);
        }
    }    
}
protected void mustModel(MultiBody m, double cutoff) {
    mustModel(m);
    setCutoff(m,cutoff);
}
public void mustModel(MultiBody m) {
    setupAllelesAndChromosome();
    if (!canModel(m))
        need(m,alleles.numberOfArrays());
}
protected boolean canModel(MultiBody m) {
    int index = getSpeciesIndex(m.getName());
    return index != AlleleSetup.NOT_HERE;
}
protected void need(MultiBody m,int index) {
    String name = m.getName();
    if (m instanceof TwoBody)
        addTwoBody(m.getName(),index);
    else if (m instanceof ThreeBody) {
        if (((ThreeBody)m).requiresStillingWeberFFFform())
            addSpecialThreeBody(name,index);
        else
            addThreeBody(name,index);
    }
    else
        Error.assertTrue(false);
}
public void addTwoBody(String name, int index) {
    addToArraySizes(numberOfTwoBodyParameters);
    alleleSetup.add(name,index);
    ChromosomeParametersData c = new ChromosomeParametersData(name);
    c.add("A",0);
    c.add("B",0);
    c.add("C",0); 
    c.add("p",0);
    c.add("q",0); 
    c.addNoEvolution("a",3.77); // good for Si and F
}
public void addThreeBody(String name, int index) {
    addToArraySizes(numberOfThreeBodyParameters);
    alleleSetup.add(name,index);
    ChromosomeParametersData c = new ChromosomeParametersData(name);
    c.add("alpha",0);
    c.add("lambda",0);
    c.addNoEvolution("theta0",Constants.TetrahedronAngle);
    c.add("gamma",0);
    c.addNoEvolution("a1",3.77); // good for Si and F
}
public void addSpecialThreeBody(String name,int index) {
    addToArraySizes(numberOfThreeBodyParameters + specialThreeBodyParametersIncrement);
    alleleSetup.add(name,index);
    ChromosomeParametersData c = new ChromosomeParametersData(name);
    c.add("alpha",0);
    c.add("lambda",0);
    c.addNoEvolution("theta0",Utility.degrees2radians(90));
    c.add("gamma",0);
    c.addNoEvolution("a1", 3.77); // Good for Si and F
    c.add("delta",0);
    c.add("m",0);
    c.add("beta",0);
    c.addNoEvolution("a2",4.37); // good for FFF
}
protected void addToArraySizes(int next) {
  Error.assertTrue(next > 0);
  chromosome.addArray(next);
  alleles.addArray(next);
}

public boolean hasTwoBody(Atom a, Atom b) {
  return alleles.hasArray(a.toString() + b.toString()) || alleles.hasArray(b.toString() + a.toString());
}
public void setCutoff(MultiBody m, double value) {
    if (m instanceof TwoBody)
        setCutoff((TwoBody)m,value);
    else if (m instanceof ThreeBody)
        setCutoff((ThreeBody)m,value);
    else
        Error.assertTrue(false);
}
/**
value is in angstoms
*/
public void setCutoff(TwoBody pair, double value) {
    final double cutoff = value;
    final int speciesIndex = getSpeciesIndex(pair.getName());
    setValue(cutoff,speciesIndex,a_index);
    ChromosomeParametersData c = new ChromosomeParametersData(pair.getName());
    c.addNoEvolution("a",cutoff);
}
/**
value is in angstoms
*/
public void setCutoff(ThreeBody threeBody, double value) {
    final double cutoff = value;
    final int speciesIndex = getSpeciesIndex(threeBody.getName());
    setValue(cutoff,speciesIndex,a1_index);
    ChromosomeParametersData c = new ChromosomeParametersData(threeBody.getName());
    c.addNoEvolution("a1",cutoff);
}
/**
values are in angstoms
*/
public void setCutoff(ThreeBody threeBody, double a1_value, double a2_value) {
    final double a1_cutoff = a1_value;
    final double a2_cutoff = a2_value;
    final String name = threeBody.getName();
    Error.assertTrue(threeBody.requiresStillingWeberFFFform());
    final int speciesIndex = getSpeciesIndex(name);
    setValue(a1_cutoff,speciesIndex,a1_index);
    setValue(a2_cutoff,speciesIndex,a2_index);
    ChromosomeParametersData c = new ChromosomeParametersData(name);
    c.addNoEvolution("a1",a1_cutoff);
    c.addNoEvolution("a2",a2_cutoff);
}
public double getCutoff(TwoBody pair) {
  final int speciesIndex = getSpeciesIndex(pair.getName());
  final double a = getValue(speciesIndex,a_index);
  return a;
}
public double getCutoff(ThreeBody threeBody) {
  final int speciesIndex = getSpeciesIndex(threeBody.getName());
  final double a1 = getValue(speciesIndex,a1_index);
  if (threeBody.requiresStillingWeberFFFform()) {
    final double a2 = getValue(speciesIndex,a2_index);
    return Math.max(a1,a2);
  }
  return a1;
}

// BUG: only works for one kind of Atom, this code should be replaced
// by something that returns the atoms we have
public boolean hasThreeBody(Atom a, Atom b, Atom c) {
  return alleles.hasArray(a.toString() + b.toString() + c.toString());
}

public AlleleTemplate getAlleles() {
  setupAllelesAndChromosome();
  return alleles;

}
public Chromosome getChromosome() {
  setupAllelesAndChromosome();
  return chromosome;
}

protected void setupAllelesAndChromosome() {
  if (setupComplete) return;
  setupComplete = true;

  int sizes[] = getChromosomeArraySizes();
  alleles = new AlleleTemplate(sizes);
  chromosome = new Chromosome(alleles);


  formSetup();
  setupChromosomeFromPaper();
}
protected int[] getChromosomeArraySizes() {
    return new int[0];
}

protected void formSetup() {
  setIndices();
}
protected void setIndices() {
    alleleSetup.add("A", A_index, factorInterval);
    alleleSetup.add("B", B_index, factorInterval);
    alleleSetup.add("C", C_index, cutoffInterval);
    alleleSetup.add("p", p_index, exponentInterval);
    alleleSetup.add("q", q_index, exponentInterval);
    alleleSetup.add("a", a_index, bondLengthInterval);
    
    alleleSetup.add("alpha", alpha_index, factorInterval);
    alleleSetup.add("lambda", lambda_index, factorInterval);
    alleleSetup.add("theta0", theta0_index, angleInterval);
    alleleSetup.add("gamma", gamma_index, cutoffInterval);
    alleleSetup.add("a1", a1_index, bondLengthInterval);
    
    alleleSetup.add("delta", delta_index, factorInterval);
    alleleSetup.add("m", m_index, exponentInterval);
    alleleSetup.add("beta", beta_index, cutoffInterval);
    alleleSetup.add("a2", a2_index, bondLengthInterval);

}
protected void setupChromosomeFromPaper() {}

protected void noThreeTermParameters(String name) {
  ChromosomeParametersData c = new ChromosomeParametersData(name);
  c.add("alpha",NotApplicable);
  c.add("lambda",NotApplicable);
  c.add("theta0",NotApplicable);
  c.add("gamma",NotApplicable);
  c.add("a1",NotApplicable);
}

public void setExponentInterval(DoubleInterval interval) {exponentInterval = new DoubleInterval(interval);}
public void setFactorInterval(DoubleInterval interval) {factorInterval = new DoubleInterval(interval);}
public void setBondLengthInterval(DoubleInterval interval) {bondLengthInterval = new DoubleInterval(interval);}
public void setCutoffInterval(DoubleInterval interval) {cutoffInterval = new DoubleInterval(interval);}

protected double getValue(int speciesIndex, int parameterIndex) {
  return chromosome.getValue(speciesIndex,parameterIndex);
}
protected void setValue(double value,int speciesIndex, int parameterIndex) {
  chromosome.setValue(value,speciesIndex,parameterIndex);
}
protected int getSpeciesIndex(String name) {
  return alleleSetup.getIndex(name);
}
public class ChromosomeParametersData implements Serializable {
  protected String name;
  
  public ChromosomeParametersData(String n) {
    name = n;
  }
  public void add(String parameterName, double value) {
    DoubleInterval interval = alleleSetup.getInterval(parameterName);
    add(parameterName, value, interval);
  }
  public void addNoEvolution(String parameterName, double value) {
    DoubleInterval interval = new DoubleInterval(value,value);
    add(parameterName, value, interval);
  }
  private void add(String parameterName, double value, DoubleInterval interval) {
    Error.assertTrue(interval != null);
    Error.assertTrue(parameterName != null);
    int parameterIndex = alleleSetup.getIndex(parameterName);
    int speciesIndex = alleleSetup.getIndex(name);
    alleles.setAllele(new Allele(name+"_"+parameterName, interval),speciesIndex,parameterIndex);
    chromosome.setValue(value,speciesIndex,parameterIndex);
  }
}

public void setChromosome(Chromosome c) {
  chromosome = c;
}
// the rest of these functions follow the form of the equations in the paper
public double getEnergy(MultiBodiesForOneEnergy bodies) {
  double energy = 0;
  for(int i = 0; i < bodies.size(); i++)
      energy += getEnergy(bodies.get(i));
  return energy;
}
public double getEnergy(MultiBody body) {
  if (body instanceof TwoBody)
    return getEnergy((TwoBody)body);
  if (body instanceof ThreeBody)
    return getEnergy((ThreeBody)body);
  Error.assertTrue(false);
  return 0;
}
protected double getEnergy(TwoBody pair) {
  final int speciesIndex = getSpeciesIndex(pair.getName());
  final double a = getValue(speciesIndex,a_index);
  if (cutoff(pair.getR(),getValue(speciesIndex,a_index)))
    return 0;
  final double A = getValue(speciesIndex,A_index);
  final double B = getValue(speciesIndex,B_index);
  final double C = getValue(speciesIndex,C_index);
  final double p = getValue(speciesIndex,p_index);
  final double q = getValue(speciesIndex,q_index);
  final double r = pair.getR();
  final double cutoffValue = getCutoffTerm(Math.exp(C/(r-a)));

  final double energy = calculateTwoBodyEnergy(A,B,p,q,r) * cutoffValue;
  if (!Utility.normalNumber(energy))
    return 0;
  return  energy * pair.getHowMany();
}
protected double calculateTwoBodyEnergy(double A, double B, double p, double q, double r) {
  return A * (B * Math.pow(r,-p) - Math.pow(r,-q));
}
public double getForce(TwoBody pair) {
  final int speciesIndex = getSpeciesIndex(pair.getName());
  final double a = getValue(speciesIndex,a_index);
  if (cutoff(pair.getR(),getValue(speciesIndex,a_index)))
    return 0;
  final double A = getValue(speciesIndex,A_index);
  final double B = getValue(speciesIndex,B_index);
  final double C = getValue(speciesIndex,C_index);
  final double p = getValue(speciesIndex,p_index);
  final double q = getValue(speciesIndex,q_index);
  final double r = pair.getR();

  final double force = calculateForce(A,B,C,p,q,r,a);
  if (!Utility.normalNumber(force))
    return 0;
  return -force;
}
protected double calculateForce(double A, double B, double C, double p, double q, double r, double a) {
  return A*Math.exp(C/(-a + r))*
     (-(B*p*Math.pow(r,-1 - p)) + q*Math.pow(r,-1 - q)) - 
    (A*C*Math.exp(C/(-a + r))*(B/Math.pow(r,p) - Math.pow(r,-q)))/Math.pow(-a + r,2);
}

protected double getEnergy(ThreeBody threeBody) {
  double energy = 0;

  final double rji = threeBody.getRJI();
  final double rjk = threeBody.getRJK();
  final double theta = threeBody.getAngle();

  final int speciesIndex = getSpeciesIndex(threeBody.getName());
  final double alpha = getValue(speciesIndex,alpha_index);
  final double lambda = getValue(speciesIndex,lambda_index);
  final double theta0 = getValue(speciesIndex,theta0_index);
  final double gamma = getValue(speciesIndex,gamma_index);
  final double a1 = getValue(speciesIndex,a1_index);
  if (!cutoff(rji,a1) && !cutoff(rjk,a1)) {
    double cosTerm = Math.cos(theta) - Math.cos(theta0);
    cosTerm *= cosTerm;
    final double cutoffTerm = getCutoffTerm(Math.exp(gamma * lengthTerm(rji,rjk,a1)));
    energy = (alpha + lambda * cosTerm)* cutoffTerm;
  }

  if (threeBody.requiresStillingWeberFFFform()) {
    final double delta = getValue(speciesIndex,delta_index);
    final double m = getValue(speciesIndex,m_index);
    final double beta = getValue(speciesIndex,beta_index);
    final double a2 = getValue(speciesIndex,a2_index);
    if (!cutoff(rji,a2) && !cutoff(rjk,a2)) {
      final double cutoffTerm = getCutoffTerm(Math.exp(beta * lengthTerm(rji,rjk,a2)));
      energy += delta * Math.pow(rji*rjk,-m) * cutoffTerm;
    }
  }

  if (!Utility.normalNumber(energy))
    return 0;
  return energy * threeBody.getHowMany();
}
protected double getCutoffTerm(double value) {
  if (!Utility.normalNumber(value))
    return 0;
  else
    return value;
}
protected double lengthTerm(double rij, double rjk, double cutoff) {
  final double value = (1/(rij-cutoff)) + (1/(rjk-cutoff));
  if (Utility.normalNumber(value))
    return value;
  else
    return 0;
}
protected boolean cutoff(double r,double cutoff) {
  return r >= cutoff;
}
}