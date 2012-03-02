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


import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.*;
import java.util.StringTokenizer.*;
import java.lang.Class;
import java.lang.reflect.Field;  
import java.lang.String;
import java.util.*;
import gov.nasa.alsUtility.IO;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.graph.Graph;
import gov.nasa.javaGenes.graph.VertexIterator;
import gov.nasa.javaGenes.graph.EdgeIterator;


/**
read and write mol files representing molecules
*/
public class molFormat {
protected final static int integerSize = 3;
protected final static int symbolSize = 3;
protected final static int doubleSize = 10;

public static void writeFile(Molecule molecule, String filename) {
  PrintWriter out = IO.getPrintWriter(filename);
  write(molecule, out);
  out.close();
}
/**
write the file to PrintWriter out
*/
public static void write(Molecule molecule, PrintWriter out) {
	if (molecule.getVerticesSize() > 999 || molecule.getEdgesSize() > 999) {
		Error.warning("molFormat.write()", "molecule too large for mol file format");
		return;
	}
	out.write(toString(molecule));
}
public static String toString(Graph molecule) {
	if (molecule.getVerticesSize() > 999 || molecule.getEdgesSize() > 999) {
		Error.warning("molFormat.toString()", "molecule too large for mol file format");
		return "";
	}
	molecule.numberVertices(1);
	StringBuffer s = new StringBuffer();
	// header
	s.append("none");
	s.append(Utility.lineSeparator());
	s.append(Utility.lineSeparator());
	s.append(Utility.lineSeparator());
	s.append(integerString(molecule.getVerticesSize()));
	s.append(integerString(molecule.getEdgesSize()));
	s.append("  0  0  0");
	s.append(Utility.lineSeparator());
	
	// atoms
	for(VertexIterator v = molecule.getVertexIterator(); v.more(); v.next()){
		Atom a = (Atom)v.vertex();
		double[] xyz = a.getXyz();
		for(int i = 0; i < xyz.length; i++)
			s.append(doubleString(xyz[i]));
		s.append(" ");
		s.append(symbolString(a.toString()));
		s.append(" 0  0  0  0  0");
		s.append(Utility.lineSeparator());
	}
	
	// bonds
	for(EdgeIterator e = molecule.getEdgeIterator(); e.more(); e.next()){
		Bond b = (Bond)e.edge();
		s.append(integerString(b.getVertex(0).getNumber()));
		s.append(integerString(b.getVertex(1).getNumber()));
		s.append(integerString(b.getValence()));
		s.append("  0  0  0");
		s.append(Utility.lineSeparator());
	}

	return s.toString();
}
private static String symbolString(String symbol){
	StringBuffer s = new StringBuffer(symbol);
	while(s.length() < symbolSize)
		s.append(' ');
	return s.toString();
}
private static String integerString(int i){
	String s = new String(i + "");
	while(s.length() < integerSize)
		s =  " " + s;
	return s;
}
private static String doubleString(double d){
	String s = new String(d + "");
	int index = s.indexOf('.');
	if (index == -1)
		s = s + ".";
	while (s.indexOf('.') < 5)
		s = " " + s;
	while(s.length() < doubleSize)
		s = s + "0";
	StringBuffer buffer = new StringBuffer(s);
	buffer.setLength(doubleSize);
	return buffer.toString();
}


 /**
 read a mol file
 @param filename the file to read
 @return the molecule in filename
 */
 public static Molecule read(String filename) {
  	Molecule molecule = new Molecule();
    try {
      BufferedReader input = IO.getBufferedReader(filename);

 	    // header
      getLine(input);
      getLine(input);
      getLine(input);
      int atoms = getInteger(input);
      int bonds = getInteger(input);
      getLine(input);
	
	    // atoms
      for (int i = 0; i < atoms; i++) {
        double x = getDouble(input);
        double y = getDouble(input);
        double z = getDouble(input);
        String symbol = getChemicalSymbol(input);
        getLine(input);

        Atom a = new Atom(symbol);
        a.setXyz(x, y, z);
        molecule.add(a);
      }
	
	    // bonds
      for (int i = 0; i < bonds; i++) {
        int first = getInteger(input);
        int second = getInteger(input);
        int valence = getInteger(input);
        getLine(input);

        molecule.makeBond(first, second, valence);
      }
    } catch (FileNotFoundException e) {
    	Error.fatal("molFormat.read(): can find file " + e);
    } catch (IOException e) {
    	Error.fatal("molFormat.read(): IO error " + e);
  }
	return molecule;
 }
 static protected String getLine (BufferedReader input) throws IOException {
 	return input.readLine();
 }
 static protected String getChemicalSymbol (BufferedReader input) throws IOException {
 	char buffer[] = new char[symbolSize];
  Error.assertTrue(symbolSize == input.read(buffer, 0, symbolSize));
  String s = new String(buffer);
  return s.trim();
 }
static protected int getInteger (BufferedReader input) throws IOException {
 	char buffer[] = new char[integerSize];
  Error.assertTrue(integerSize == input.read(buffer, 0, integerSize));
  String s = new String(buffer);
  java.lang.Integer integer = new java.lang.Integer(s.trim());
  return integer.intValue();
 }
static protected double getDouble (BufferedReader input) throws IOException {
 	char buffer[] = new char[doubleSize];
  Error.assertTrue(doubleSize == input.read(buffer, 0, doubleSize));
  String s = new String(buffer);
  java.lang.Double real = new java.lang.Double(s.trim());
  return real.doubleValue();
 }


}


