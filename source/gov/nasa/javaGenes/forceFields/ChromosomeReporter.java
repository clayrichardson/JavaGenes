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
import gov.nasa.alsUtility.Utility;
import gov.nasa.javaGenes.core.Reporter;
import gov.nasa.javaGenes.core.Parameters;
import gov.nasa.javaGenes.core.TokenizeInput;
import gov.nasa.javaGenes.core.TokenizeOutput;
import gov.nasa.javaGenes.core.Population;
import gov.nasa.javaGenes.core.Individual;
import gov.nasa.javaGenes.chemistry.Atom;

public class ChromosomeReporter extends Reporter{
protected String EnergiesFilename = "bestEnergies.tsd";

public ChromosomeReporter(Parameters p) {
  super(p);
}
public ChromosomeReporter(Parameters p, TokenizeInput tokenizer) {
  super(p,tokenizer);
}
// Assumes StillingerWeber
public void done(Population population) {
  super.done(population);
  ChromosomeParameters p = (ChromosomeParameters)parameters;
  if (! (p.potential instanceof StillingerWeber))
    return;
  StillingerWeber potential = (StillingerWeber)p.potential;
  Individual best = population.bestIndividual();
  Chromosome chromosome = (Chromosome)best.getEvolvable();
  PutPotentialEnergiesAndForces out = new PutPotentialEnergiesAndForces(potential,chromosome);
  Atom Si = new Atom("Si");
  Atom F = new Atom("F");
  int numberOfValues = 100;
  if (potential.hasTwoBody(Si,F))
    out.putTwoBody("SiFEnergiesAndForces.tsd",Si,F,numberOfValues);
  if (potential.hasTwoBody(Si,Si))
    out.putTwoBody("SiSiEnergiesAndForces.tsd",Si,Si,numberOfValues);
  if (potential.hasTwoBody(F,F))
    out.putTwoBody("FFEnergiesAndForces.tsd",F,F,numberOfValues);
  if (potential.hasThreeBody(Si,Si,Si))
    out.putThreeBody("SiSiSiEnergies.tsd",Si,Si,Si,20);

  ManyMultiBodiesForOneEnergy m = p.energiesToExamineBestIndividual;
  if (m != null) {
    PrintWriter out2 = Utility.outputFile(EnergiesFilename);
    potential.setChromosome(chromosome);
	  for(int i = 0; i < m.size(); i++) {
  	  MultiBodiesForOneEnergy test = m.getMultiBodies(i);
      double energy = potential.getEnergy(test);
      out2.println(energy + "");
    }
    out2.close();
  }
}

} 