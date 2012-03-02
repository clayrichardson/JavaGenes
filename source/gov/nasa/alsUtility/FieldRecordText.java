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

import gov.nasa.alsUtility.Error;
import java.util.StringTokenizer;
import java.util.Vector;

public class FieldRecordText {
  protected java.io.BufferedReader in;
  protected String separator = ",";
  private int currentLineNumber = 0;
  private String filename;

  public FieldRecordText(String inFilename)  {
    filename = inFilename;
    in = IO.getBufferedReader(filename);
  }
  public FieldRecordText(String filename, String theDelimiters) {
    this(filename);
    Error.assertTrue(theDelimiters != null);
    Error.assertTrue(!theDelimiters.equals(""));
    separator = theDelimiters;
  }

public static String[][] readTsdFile(String filename) {return readFile(filename,"\t");}
public static String[][] readFile(String filename, String theDelimiters) {
	FieldRecordText file = new FieldRecordText(filename,theDelimiters);
	Vector lines = new Vector();
	while(true) {
		String[] line = file.readLine();
		if (line == null)
			break;
		lines.add(line);
	}
	String[][] r = new String[lines.size()][];
	for(int i = 0; i < r.length; i++)
		r[i] = (String[])lines.get(i);
	file.close();
	return r;
}
  public String[] readLine() {
    java.util.Vector fields = new java.util.Vector();
    String[] fieldsString = new String[0];
    try {
      String line = in.readLine();
      currentLineNumber++;
      if (line == null) return null;
      if (line.equals("")) return fieldsString;

      int index = 0;
      int separatorAt = line.indexOf(separator,index);
      while(separatorAt != -1) {
	fields.addElement(line.substring(index,separatorAt));
	index = separatorAt + 1;
	separatorAt = line.indexOf(separator,index);
      }
      fields.addElement(line.substring(index));
      fieldsString = new String[fields.size()];
      fields.copyInto((Object[])fieldsString);
    }   catch (java.io.IOException e) {Error.fatal(e);}
  return fieldsString;
  }
  public void close() {
    try {
      in.close();
    } catch (java.io.IOException e) {Error.fatal(e);}
  }
public int getCurrentLineNumber() {return currentLineNumber;}
public String getFilename() {return filename;}
}