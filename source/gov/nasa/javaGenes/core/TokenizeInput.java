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

import gov.nasa.alsUtility.IO;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;

/**
used to read files created by TokenizeOutput.  Format is ASCII with one token per line.
A token is a number, a string, or a boolean. Used for checkpointing by JavaGenes.

@see TokenizeOutput
*/
public class TokenizeInput {
protected java.io.BufferedReader input;
protected String filename;
/**
@param f filename
*/
public TokenizeInput(String f) {
	filename = f;
  input = IO.getBufferedReader(filename);
}
public boolean getBoolean() {return Utility.string2boolean(getLine());}
public int getInteger() {return Utility.string2integer(getLine());}
public long getLong() {return Utility.string2long(getLine());}
public double getDouble() {return Utility.string2double(getLine());}
public String getString() {return getLine();}
public void close() {
	try {
		if (input != null)
  		input.close();
  } catch (Exception exception) {Error.fatal("can't close file " + filename);}
  input = null;
}
protected String getLine () {
	String string = null;
	try {
 		string = input.readLine();
  } catch (java.io.IOException exception) {Error.fatal("input error reading from file " + filename);}
  return string;
 }
/**
close the file if necessary
*/
protected void finalize() throws Throwable {super.finalize(); close();}
} 