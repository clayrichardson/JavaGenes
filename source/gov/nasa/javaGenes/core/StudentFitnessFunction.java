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

import gov.nasa.alsUtility.Utility;

// ala Jason Lohn multiobjective GA
public class StudentFitnessFunction extends FitnessFunctionMultiObjective {
protected TeacherPopulation _teachers;
public void setTeachers(TeacherPopulation teachers) {_teachers = teachers;}

public void testStudent(StudentFitness studentFitness) {
  double[] studentArray = studentFitness.getFitnessArray();
  int teachersPassed = 0;
  for(int i = 0; i < _teachers.getSize(); i++)
    if (Utility.lessThanOrEqual(studentArray,_teachers.getArray(i)))
      teachersPassed++;
  studentFitness.setGrade(_teachers.getSize() - teachersPassed);
}
public Fitness evaluateFitness (Evolvable evolvable) {
  StudentFitness f = (StudentFitness)super.evaluateFitness(evolvable);
  if (_teachers != null) // not true when initializing student population
    testStudent(f);
  return f;
}
public FitnessMultiObjective getNewFitness() {
  return new StudentFitness(this);
}

}