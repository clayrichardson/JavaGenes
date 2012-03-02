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

import gov.nasa.javaGenes.core.*;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.alsUtility.RandomNumber;
import gov.nasa.javaGenes.chemistry.UnitCell;
import gov.nasa.alsUtility.Error;

public class ChromosomeParameters extends Parameters {

static {
   RandomNumber.setSeed(990639400906L); // used to generate repeatable runs. See seed.txt for seed of last run
}
public AlleleTemplate alleles;
public String lengthsAndAnglesFilename = "lengthsAndAngles.tsd";
public AssumedParameters assumed;
public Immigrants immigrants;

public DoubleInterval exponentInterval = new DoubleInterval(-6,24);
public DoubleInterval factorInterval = new DoubleInterval(-100,150);
public DoubleInterval cutoffInterval = new DoubleInterval(0,8);

public ManyMultiBodiesForOneEnergy energiesToExamineBestIndividual;

public String Si8UnitCellFilename = "../input/Si8UnitCell.xyz";
public String otherSiFilename = "../input/Si5_51_SEenergies.xyz";

public Potential potential;
//public int whichPotential = Potential.STILLINGER_WEBER;
public int whichPotential = Potential.TERSOFF;

private void setParameters() {
    populationSize = 10;
    kidsPerGeneration = 20;
    maximumGenerations = 5;
    frequencyOfASCIIPopulations = 1;
    stopAtPerfection = false;
    
    AtomicSpecies[] species = {new AtomicSpecies("Si",3.0,0.3)};
    switch(whichPotential) {
        case Potential.STILLINGER_WEBER: 
            potential = new StillingerWeber(species,factorInterval,exponentInterval,cutoffInterval);
            break;
        case Potential.TERSOFF:
            potential = new Tersoff(species);
            //((Tersoff)potential).evolveSiOnly();
            break;
        default:
            Error.assertTrue(true);
    }
    UnitCell unitCell = new UnitCell(5.43);
    ManyMultiBodiesForOneEnergy Si8 = new ManyMultiBodiesForOneEnergy(potential,Si8UnitCellFilename);
    potential.mustModel(Si8);    
    Si8.removeBodiesAboveCutoff(potential);
    ManyMultiBodiesForOneEnergy otherSi = new ManyMultiBodiesForOneEnergy(potential,otherSiFilename);
    potential.mustModel(otherSi);    
    otherSi.removeBodiesAboveCutoff(potential);
        
    WeightedSumFitness fitness = new WeightedSumFitness();
    FitnessFunction f;
    f = new ManyMoleculesEnergyFitness(potential,Si8);
    f.setName("Si8");
    fitness.add(1.0,f);
    f = new ManyMoleculesEnergyFitness(potential,otherSi);
    f.setName("otherSi");
    fitness.add(1.0,f);

    fitnessFunction = fitness;
    
    breeder = new BreederSteadyState(this);
    alleles = potential.getAlleles();
    ChromosomeXoverWithMutation c = new ChromosomeXoverWithMutation(alleles,0.5,0.5,0.5);
    childMakerProvider.add(c);
}

/**
Sets up all parameters
*/
public ChromosomeParameters() {
  setParameters();
}
public void makeFiles() {
   fitnessFunction.makeFiles();
   alleles.makeFiles();
}

}
