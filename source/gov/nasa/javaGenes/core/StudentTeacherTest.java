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

import junit.framework.TestCase;
import gov.nasa.javaGenes.forceFields.Chromosome;
import gov.nasa.javaGenes.graph.GraphPopulation;
import gov.nasa.javaGenes.graph.Graph;

public class StudentTeacherTest extends TestCase {
public StudentTeacherTest(String name) {super(name);}

private TeacherFitnessFunction teacherFF;
private StudentFitnessFunction studentFF;
private final int studentsSize = 4;
private final int teacherSize = 5;
private Population students;
private TeacherPopulation teachers;

public void setUp() {
  students = new GraphPopulation(studentsSize);
  addStudent(0,1,1,1);
  addStudent(1,2,2,2);
  addStudent(2,3,3,3);
  addStudent(3,1,2,3);

  teacherFF = new TeacherFitnessFunction();
  teacherFF.setStudents(students);
  teachers = new TeacherPopulation(teacherSize,BreederWithTeachers.createAlleleTemplate(students),teacherFF);
  fixTeacher(0,0,0,0); // too strict
  fixTeacher(1,1,1,1); // passes one
  fixTeacher(2,2,2,2); // passes two
  fixTeacher(3,2,2,3); // passes three
  fixTeacher(4,4,4,4); // too lenient

  studentFF = new StudentFitnessFunction();
  studentFF.add(new FitnessFunctionFixed(0));
  studentFF.add(new FitnessFunctionFixed(1));
  studentFF.add(new FitnessFunctionFixed(1));
  studentFF.setTeachers(teachers);
}
public void testStudents() {
  students.retestStudents(studentFF);
  assertTrue("student 0", students.getFitness(0).asDouble() == 1);
  assertTrue("student 1", students.getFitness(1).asDouble() == 2);
  assertTrue("student 2", students.getFitness(2).asDouble() == 4);
  assertTrue("student 3", students.getFitness(3).asDouble() == 3);
}
public void testTeachers() {
  teachers.evaluateFitness(teacherFF);
  assertTrue("teacher 0", teachers.getFitness(0).asDouble() == 5);
  assertTrue("teacher 1", teachers.getFitness(1).asDouble() == 1);
  assertTrue("teacher 2", teachers.getFitness(2).asDouble() == 2);
  assertTrue("teacher 3", teachers.getFitness(3).asDouble() == 3);
  assertTrue("teacher 4", teachers.getFitness(4).asDouble() == 4);
}
private void fixTeacher(int location, int fitness1, int fitness2, int fitness3) {
  Chromosome t = (Chromosome)teachers.getEvolvable(location);
  t.setValue(fitness1,0,0);
  t.setValue(fitness2,0,1);
  t.setValue(fitness3,0,2);
}
private void addStudent(int location, int fitness1, int fitness2, int fitness3) {
  Individual i = new Individual(new Graph(), getStudentFitnessFunction(fitness1,fitness2,fitness3));
  students.setIndividual(location,i);
}
private FitnessFunctionMultiObjective getStudentFitnessFunction(int fitness1, int fitness2, int fitness3) {
  StudentFitnessFunction r = new StudentFitnessFunction();
  r.add(new FitnessFunctionFixed(fitness1));
  r.add(new FitnessFunctionFixed(fitness2));
  r.add(new FitnessFunctionFixed(fitness3));
  return r;
}

}
