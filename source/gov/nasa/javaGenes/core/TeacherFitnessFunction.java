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
import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.forceFields.Chromosome;

public class TeacherFitnessFunction extends FitnessFunction {
protected Population _students;
public void setStudents(Population students) {_students = students;}

public Fitness evaluateFitness (Evolvable evolvable) {
  Error.assertTrue(_students != null);
  Chromosome teacher = (Chromosome)evolvable;
  int studentsFailed = 0;
  for(int i = 0; i < _students.getSize(); i++)
    if (!Utility.lessThanOrEqual(_students.getFitness(i).getFitnessArray(),teacher.getArray(0)))
      studentsFailed++;
  if (studentsFailed == _students.getSize())
    return new FitnessDouble(_students.getSize() + 1); // excessively strict teachers are the least fit
  return new FitnessDouble(_students.getSize() - studentsFailed); // but strict teachers are generally more fit
}
}