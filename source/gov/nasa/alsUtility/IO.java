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
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;

/*
central place for IO functions. Originally created so that one can
easily use Condor CHIRP remote IO or normal IO.  CHIRP has been
removed.
*/
public class IO {

public static PrintWriter getPrintWriter(String filename) {
  return getPrintWriter(filename, false);
}
public static PrintWriter getPrintWriter(String filename, boolean append) {
    PrintWriter file = null;
    try {
      FileOutputStream stream =  new FileOutputStream (filename,append);
    	file = new PrintWriter(stream);
    } catch (IOException e) {Error.fatal ("Couldn't create " + filename);}            
    return file;
}

public static BufferedReader getBufferedReader(String filename) {
  BufferedReader input = null;
  try {
      FileInputStream stream =  new FileInputStream (filename);
      input = new BufferedReader(new InputStreamReader(stream));
  } catch (FileNotFoundException e) {
    	Error.fatal("Can't find file " + filename);
  }
  return input;
}

public static void renameFile(String currentName, String newName) {
      File n = new File(newName);
      File c = new File(currentName);
      n.delete();
	    c.renameTo(n);
      c.delete();
}
public static void makeDirectory(String name) {
    File f = new File (name);
    if (f.isDirectory())
    	return;
    if (f.exists())
    	f.delete();
    f.mkdir();
}
public static void removeFile(String name) {
    java.io.File file = new java.io.File(name);
    file.delete();
}
} 