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
package gov.nasa.javaGenes.chemistry;

import gov.nasa.javaGenes.core.Run;

/**
run a genetic algorithm on a Molecule represented as a graph
*/
public class RunMolecule extends Run {
public RunMolecule() {}

/**
create MoleculeParameters and MoleculePopulation objects

@param arguments should usually have a filename of a .mol file. This is the molecular target.
It can be empty, but then a small default molecule will be the target.
*/
protected void startSpecialized(String[] arguments) {
    if (arguments.length == 1)
        parameters = new MoleculeParameters(arguments[0]);
    else
        parameters = new MoleculeParameters();

    generationTimer.start();
    population = new MoleculePopulation((MoleculeParameters)parameters);
    generationTimer.stop();
}
}