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

import junit.framework.TestCase;
import gov.nasa.alsUtility.Utility;

public class FieldRecordTextTest extends TestCase {
  private String emptyFile = "TEMP1";
  private String filename = "TEMP2";
  private java.io.PrintWriter file;

  public FieldRecordTextTest(String name) {super(name);}
  protected void setUp() {
    Utility.makeFile(emptyFile,"");
    file = Utility.outputFile(filename);
    file.println("1,22,333,4444");
    file.println("");
    file.println(",,");
    file.println(",,1");
    file.println("5,6,,7,8");
    file.close();
  }
  protected void tearDown() {
    java.io.File file = new java.io.File(emptyFile);
    file.delete();
    file = new java.io.File(filename);
    file.delete();
  }
  public void testReadFile() throws java.io.FileNotFoundException {
	String[][] lines = FieldRecordText.readFile(emptyFile, ",");
    assertTrue(lines.length == 1);
    assertTrue(lines[0].length == 0);
    file.close();

	lines = FieldRecordText.readFile(filename, ",");
	assertTrue(lines.length == 5);
    String[] fields = lines[0];
    assertTrue(fields.length == 4);
    assertTrue(fields[0].equals("1"));
    assertTrue(fields[1].equals("22"));
    assertTrue(fields[2].equals("333"));
    assertTrue(fields[3].equals("4444"));

    fields = lines[1];
    assertTrue(fields.length == 0);

    fields = lines[2];
    assertTrue(fields.length == 3);
    assertTrue(fields[0].equals(""));
    assertTrue(fields[1].equals(""));
    assertTrue(fields[2].equals(""));

    fields = lines[3];
    assertTrue(fields.length == 3);
    assertTrue(fields[0].equals(""));
    assertTrue(fields[1].equals(""));
    assertTrue(fields[2].equals("1"));

    fields = lines[4];
    assertTrue(fields.length == 5);
    assertTrue(fields[0].equals("5"));
    assertTrue(fields[1].equals("6"));
    assertTrue(fields[2].equals(""));
    assertTrue(fields[3].equals("7"));
    assertTrue(fields[4].equals("8"));

    file.close();
  }
  public void testReadLine() throws java.io.FileNotFoundException {
    FieldRecordText file = new FieldRecordText(emptyFile);
    String[] fields = file.readLine();
    assertTrue(fields.length == 0);
    assertTrue(file.readLine() == null);
    file.close();

    file = new FieldRecordText(filename);
    fields = file.readLine();
    assertTrue(fields.length == 4);
    assertTrue(fields[0].equals("1"));
    assertTrue(fields[1].equals("22"));
    assertTrue(fields[2].equals("333"));
    assertTrue(fields[3].equals("4444"));

    fields = file.readLine();
    assertTrue(fields.length == 0);

    fields = file.readLine();
    assertTrue(fields.length == 3);
    assertTrue(fields[0].equals(""));
    assertTrue(fields[1].equals(""));
    assertTrue(fields[2].equals(""));

    fields = file.readLine();
    assertTrue(fields.length == 3);
    assertTrue(fields[0].equals(""));
    assertTrue(fields[1].equals(""));
    assertTrue(fields[2].equals("1"));

    fields = file.readLine();
    assertTrue(fields.length == 5);
    assertTrue(fields[0].equals("5"));
    assertTrue(fields[1].equals("6"));
    assertTrue(fields[2].equals(""));
    assertTrue(fields[3].equals("7"));
    assertTrue(fields[4].equals("8"));
  }
}