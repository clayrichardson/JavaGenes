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
package gov.nasa.alsUtility;


import java.io.PrintWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.IO;

/**
create a file and add text to it from time to time
*/
public class LogFile {
protected String filename;
protected PrintWriter file;

/**
@param f the filename
@param append if true, add to existing file. Otherwise create new file.
*/
public LogFile (String f, boolean append) {
    filename = f;
    file = IO.getPrintWriter(filename,append);
}
public LogFile(String filename) {this(filename,true);}
public boolean containsData(){
	File it = new File(filename);
	return it.exists() && it.length() > 0;
}
public void print (String s) {file.print (s); flush();}
public void println (String s) {file.println (s); flush();}
public void println () {file.println (); flush();}
public void close() {file.close(); file = null;}
public void flush() {file.flush();}

private boolean first = true;
/**
print d with a comma between it and all other entries
*/
public void printComaSeparated (double d) {
    if (first) first = false;
    else       print (",");
    print (d + "");
}
/**
print a with the form x,y
*/
public void printComaSeparatedXYLine (double x, double y) {
    println (x + "," + y);
}
/**
close the file if necessary
*/
protected void finalize() throws Throwable {super.finalize(); if (file != null) close();}
}
