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
import java.io.PrintWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import gov.nasa.alsUtility.IO;


/**
used to write files that will be read by TokenizeInput.  Format is ASCII with one token per line.
A token is a number, a string, or a boolean. Used for checkpointing by JavaGenes.

@see TokenizeInput
*/
public class TokenizeOutput {
protected String filename;
protected PrintWriter file;

/**
@param f filename
*/
public TokenizeOutput (String f) {
  filename = f;
  file = IO.getPrintWriter(filename);
}

public void putBoolean(boolean b) {file.println(b? "true" : "false");}
public void putInteger(int integer) {file.println(integer);}
public void putLong(long integer) {file.println(integer);}
public void putDouble(double real) {file.println(real);}
public void putString(String string) {file.println(string);}
public void close() {if (file != null) file.close(); file = null;}
/**
close the file if necessary
*/
protected void finalize() throws Throwable {super.finalize(); close();}
}