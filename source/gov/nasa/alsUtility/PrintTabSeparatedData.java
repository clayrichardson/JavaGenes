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
import java.io.OutputStream;
import gov.nasa.alsUtility.Error;

/**
print things out in ASCII with tabs separating each print on a line
*/
public class PrintTabSeparatedData extends PrintWriter{
private boolean needsTab = false;


public PrintTabSeparatedData(OutputStream out) {
	super(out);
  Error.assertTrue(out != null);
}
public void print(String argument) {
	if (needsTab)
		super.print("\t");
	super.print(argument);
	needsTab = true;
}
public void print(boolean argument) {print(String.valueOf(argument));}
public void print(int argument) {print(String.valueOf(argument));}
public void print(long argument) {print(String.valueOf(argument));}
public void print(float argument) {print(String.valueOf(argument));}
public void print(double argument) {print(String.valueOf(argument));}
public void print(Object argument) {print(String.valueOf(argument));}
public void print(char argument) {print(String.valueOf(argument));}
public void print(char[] argument) {print(String.valueOf(argument));}

public void println() {
	super.println();
	needsTab = false;
}
public void println(String argument) {
	print(argument);
	println();
}
public void println(boolean argument) {println(String.valueOf(argument));}
public void println(int argument) {println(String.valueOf(argument));}
public void println(long argument) {println(String.valueOf(argument));}
public void println(float argument) {println(String.valueOf(argument));}
public void println(double argument) {println(String.valueOf(argument));}
public void println(Object argument) {println(String.valueOf(argument));}
public void println(char argument) {println(String.valueOf(argument));}
public void println(char[] argument) {println(String.valueOf(argument));}
}
