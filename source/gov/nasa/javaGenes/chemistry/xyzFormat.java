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

/*
Written in part by W. Todd Wipke and Richard McClellan, Molecular
Engineering Laboratory, Department of Chemistry, University
of California, Santa Cruz, CA  95064
*/

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.EOFException;
import java.io.*;
import java.util.StringTokenizer;
import gov.nasa.alsUtility.IO;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.ExtendedVector;
import gov.nasa.javaGenes.graph.VertexIterator;

/**
read and write mol files representing molecules
*/
public class xyzFormat {

public static void appendToFile(Molecule molecule, String filename) {
  PrintWriter out = IO.getPrintWriter (filename,true);
  write(molecule, out);
  out.close();
}
public static void writeToFile(Molecule molecule, String filename) {
  PrintWriter out = IO.getPrintWriter(filename,false);
  write(molecule, out);
  out.close();
}
public static void writeMolecules(ExtendedVector molecules,String filename) {
  Utility.makeFile(filename,"");
  writeToFile((Molecule)molecules.elementAt(0),filename);
  for(int i = 1; i < molecules.size(); i++)
    appendToFile((Molecule)molecules.elementAt(i),filename);
}
/**
write the file to PrintWriter out
*/
public static void write(Molecule molecule, PrintWriter out) {
  out.println(molecule.getVerticesSize() + "");
  out.println(molecule.getComment());

	// atoms
	for(VertexIterator v = molecule.getVertexIterator(); v.more(); v.next()){
		Atom a = (Atom)v.vertex();
		double[] xyz = a.getXyz();
    out.print(a.toString() + "\t");
		for(int i = 0; i < xyz.length; i++)
			out.print(xyz[i] + "\t");
		out.println();
	}
}

 /**
 read an xyz file
 @param filename the file to read
 @return the molecule
 */
 public static Molecule read(String filename) {
  Molecule molecule = null;
  BufferedReader input = IO.getBufferedReader(filename);
  try {
    molecule = read(input);
  } catch (EOFException e) {
    	Error.fatal("xyzFormat.read(): unexpected EOF " + e);
  } catch (IOException e) {
    	Error.fatal("xyzFormat.read(): IO error " + e);
  }
	return molecule;
 }
/**
read many molecules from a xyz file
*/
public static Molecules readMolecules(String filename) {
  Molecules molecules = new Molecules();
  try {
      BufferedReader input = IO.getBufferedReader(filename);
      while(true) {
        Molecule molecule = read(input);
        molecules.add(molecule);
      }
  } catch (EOFException e) {
    	return molecules; // normal return
  } catch (IOException e) {
    	Error.fatal("xyzFormat.readMolecules(): IO error " + e);
  }
  Error.assertTrue(false);
	return null;
 }
/**
 read an xyz file
 @return the molecule
 */
public static Molecule read(BufferedReader input) throws IOException, EOFException {
  	Molecule molecule = new Molecule();

    int atoms = Utility.string2integer(readLine(input));
    molecule.setComment(input.readLine()); // allows empty line

    for (int i = 0; i < atoms; i++) {
    StringTokenizer tokenizer = new StringTokenizer(readLine(input));
      Error.assertTrue(tokenizer.countTokens() == 4);
      Atom a = new Atom(tokenizer.nextToken());
      a.setXyz(
        Utility.string2double(tokenizer.nextToken()),
        Utility.string2double(tokenizer.nextToken()),
        Utility.string2double(tokenizer.nextToken()));
      molecule.add(a);
    }
	return molecule;
}
protected static String readLine(BufferedReader input) throws IOException, EOFException {
  String line = input.readLine();
  if (line == null || line.equals(""))
    throw new EOFException();
  return line;
}
}
