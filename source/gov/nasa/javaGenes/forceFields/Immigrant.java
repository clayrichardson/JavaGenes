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
import gov.nasa.alsUtility.FieldRecordText;
import gov.nasa.alsUtility.Error;


public class Immigrant extends ChromosomeParameterValues {
protected boolean infinitNumber = false;
protected int number = 1; // not set when reading file. Set by Immigrants when reading multiple immigrants
public Immigrant(FieldRecordText file) {super(file);}
public Immigrant(String filename) {super(filename);}
public void setNumberInfinit(boolean value) {infinitNumber = value;}
public boolean isInfinit() {return infinitNumber;}
public boolean hasMore() {return isInfinit() || getNumber() > 0;}
public int getNumber() {return number;}
public void setNumber(int n) {
  Error.assertTrue(n >= 0);
  number = n;
}
public void setupChromosome(Chromosome chromosome,AlleleTemplate alleles) {
  for(int i = 0; i < size(); i++) {
    int[] indices = alleles.getIndices(getName(i));  
    chromosome.setValue(getValue(i),indices[0],indices[1]);
  }
}
}

