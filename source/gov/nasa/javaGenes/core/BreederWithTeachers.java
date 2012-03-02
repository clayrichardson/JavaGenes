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
package gov.nasa.javaGenes.core;
//TeacherFitnessFunction/StudentFitnessFunction be merged?

import gov.nasa.javaGenes.forceFields.AlleleTemplate;
import gov.nasa.javaGenes.forceFields.Allele;
import gov.nasa.alsUtility.DoubleInterval;
import gov.nasa.alsUtility.MinMaxes;

public class BreederWithTeachers extends BreederSteadyState {
protected int _teacherIndex = 0;
protected int _numberOfTeachers = 0;
protected int _newTeachersPerGeneration = 0;
protected TeacherPopulation _teacherPopulation = null;
protected TeacherFitnessFunction _teacherFitnessFunction = null;
protected ChildMakerProvider _teacherChildMakers = null;

public BreederWithTeachers(Parameters p, int numberOfTeachers, int newTeachersPerGeneration, ChildMakerProvider teacherChildMakers) {
  super(p);
  _numberOfTeachers = numberOfTeachers;
  _newTeachersPerGeneration = newTeachersPerGeneration;
  _teacherFitnessFunction = new TeacherFitnessFunction();
  _teacherChildMakers = teacherChildMakers;
}
public TeacherPopulation getTeachers() {return _teacherPopulation;}
/**
return each interval is from best to worst possible fitness for each objective
*/
public static AlleleTemplate createAlleleTemplate(FitnessFunction studentFitness) {
  StudentFitnessFunction sf = (StudentFitnessFunction)studentFitness;
  int size = sf.numberOfObjectives();
  AlleleTemplate alleles = new AlleleTemplate(size);
  for(int i = 0; i < size; i++) {
    FitnessFunction f = sf.getFitnessFunction(i);
    DoubleInterval interval = new DoubleInterval(f.bestDouble(),f.worstDouble());
    alleles.setAllele(new Allele(interval),0,i);
  }
  return alleles;
}
/**
return based on the population given.  Intervals will be from lowest to highest
fitness for each objective
*/
public static AlleleTemplate createAlleleTemplate(Population students) {
  StudentFitness sf = (StudentFitness)students.getFitness(0);
  int size = sf.getFitnessArray().length;
  AlleleTemplate alleles = new AlleleTemplate(size);
  MinMaxes mm = new MinMaxes();
  for(int i = 0; i < students.getSize(); i++)
    mm.add(students.getFitness(i).getFitnessArray());
  for(int i = 0; i < size; i++) {
    DoubleInterval interval = mm.getDoubleInterval(i);
    alleles.setAllele(new Allele(interval),0,i);
  }
  return alleles;
} 
public Population breed (Population students, int kidsPerGeneration) {
  _teacherFitnessFunction.setStudents(students);
  if (_teacherPopulation == null)  // first time only
    _teacherPopulation = new TeacherPopulation(_numberOfTeachers, createAlleleTemplate(students), _teacherFitnessFunction);
  else
    _teacherPopulation.evaluateFitness(_teacherFitnessFunction);

  while (needNewTeachers()){
    ChildMaker maker = _teacherChildMakers.get();
    Evolvable[] p = new Evolvable[maker.numberOfParents()];
    for(int i = 0; i < p.length; i++)
      if (useTournament())   // Deficiency: uses same tournament probability as students
        p[i] = tournament(_teacherPopulation,null).getEvolvable();
      else
        p[i] = pickOne(_teacherPopulation).getEvolvable();
    Evolvable[] c = maker.makeChildren(p);
    for(int i = 0; i < c.length; i++){
      if (needNewTeachers()){
        Individual individual = _teacherPopulation.makeIndividual(c[i],_teacherFitnessFunction);
        _teacherPopulation.setIndividual(antiTournamentGetIndex(_teacherPopulation),individual);
        _teacherIndex++;
      }
    }
    Checkpointer.ok();
  }
  // now evolve the student (primary) population
  StudentFitnessFunction studentFF = (StudentFitnessFunction)getFitnessFunction();
  studentFF.setTeachers(_teacherPopulation);
  students.retestStudents(studentFF);
  Population r = super.breed(students,kidsPerGeneration);
  _teacherIndex = 0;
  return r;
}
private boolean needNewTeachers() {
  return _teacherIndex < _newTeachersPerGeneration;
}

}
