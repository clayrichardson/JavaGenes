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
 
import java.io.Serializable;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.EasyFile;
import gov.nasa.alsUtility.DoubleInterval;

/**
a two-dimensional array of alleles.  Each one-dimensional array also has a name.
Two-dimensional arrays are used for convenience when searching for molecular mechanics parameters
for multi-species potentials.  Each chemical species gets one array and a name.

By convention, any parameters that refer to two different species are found at the end of the array
for each species.  The first multi-species parameter refers to the zeroeth array,
the second to Allele[1], etc. opt to the current species minus 1.

@see allele
*/


public class AlleleTemplate implements Serializable {
protected Allele[][] alleles;
/**
These names refer to the first dimension in alleles
*/
protected String[] names;

/**
@param size the sizes of the arrays
*/
public AlleleTemplate(int[] size) {
	alleles = new Allele[size.length][];
	names = new String[size.length];
  for(int i = 0; i < size.length; i++) {
  	alleles[i] = new Allele[size[i]];
    names[i] = "none";
  }
}
public AlleleTemplate(int onlyArraySize) {
  alleles = new Allele[1][];
  names = new String[1];
  alleles[0] = new Allele[onlyArraySize];
}
public void addArray(int size) {
  Allele[][] array = new Allele[alleles.length+1][];
  for(int i = 0; i < alleles.length; i++)
    array[i] = alleles[i];
  array[array.length-1] = new Allele[size];
  alleles = array;

  String[] strings = new String[names.length+1];
  for(int i = 0; i < names.length; i++)
    strings[i] = names[i];
  strings[strings.length-1] = "none";
  names = strings;
}
public boolean hasArray(String n) {
  for(int i = 0; i < names.length; i++)
    if (alleles[i][0].getName().startsWith(n))
      return true;
  return false;
}
/**
@return the number of allele arrays (alleles.length)
*/
public int numberOfArrays() {return alleles.length;}
/**
@return the length of the index allele array
*/
public int getSize(int index) {return alleles[index].length;}
public Allele getAllele(int i, int j) {return alleles[i][j];}
public Allele getAllele(String name) {
  for(int i = 0; i < alleles.length; i++)
  for(int j = 0; j < alleles[i].length; j++)
    if (name.equals(getAllele(i,j).getName()))
      return alleles[i][j];
  Error.assertTrue(false);
  return null;
}
public int[] getIndices(String name) {
  for(int i = 0; i < alleles.length; i++)
  for(int j = 0; j < alleles[i].length; j++)
    if (name.equals(getAllele(i,j).getName())) {
      int[] r = {i,j};
      return r;
    }
  Error.assertTrue(false);
  return null;

}
/**
@return true if an allele exists at the indicated location
*/
public boolean hasAllele(int i, int j) {return alleles[i][j] != null;}
public void setAllele(Allele allele, int i, int j) {alleles[i][j] = allele;}
public void setName(String name,int index) {names[index] = name;}
public String getName(int index) {return names[index];}
public String toString() {
	Error.assertTrue(names.length == alleles.length);
	String string = "";
  for(int i = 0; i < alleles.length; i++) {
  	string += names[i] + ": ";
  	for(int j = 0; j < alleles[i].length; j++)
    	string += "(" + alleles[i][j] + ")";
  }
  return string;
}
public String getHeader() {
  String header = "";
  for(int i = 0; i < alleles.length; i++) {
    String bodies = names[i];
  	for(int j = 0; j < alleles[i].length; j++)
    	header += alleles[i][j].getName() + "\t";
  }
  return header;
}
/**
create a file "alleles.tsd" with the allele information in it
*/
public void makeFiles() {
	Error.assertTrue(names.length == alleles.length);
	EasyFile file = new EasyFile("alleles.tsd");

  for(int i = 0; i < names.length; i++) {
  	file.print(names[i]);
  	for(int j = 0; j < alleles[i].length; j++)
    	file.print("\t");
  }
  file.println();

  for(int i = 0; i < alleles.length; i++) {
  	for(int j = 0; j < alleles[i].length; j++)
    	file.print(alleles[i][j].getName() + "\t");
  }
  file.println("");

  for(int i = 0; i < alleles.length; i++) {
  	for(int j = 0; j < alleles[i].length; j++) {
    	DoubleInterval interval = alleles[i][j].getInterval();
    	file.print(interval.low() + "\t");
    }
  }
  file.println("");

  for(int i = 0; i < alleles.length; i++) {
  	for(int j = 0; j < alleles[i].length; j++) {
    	DoubleInterval interval = alleles[i][j].getInterval();
    	file.print(interval.high() + "\t");
    }
  }
  file.println("");

  file.close();
}
}
