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
import gov.nasa.alsUtility.FieldRecordText;
import java.io.Serializable;
import gov.nasa.alsUtility.Error;
import java.util.Vector;

public class ChromosomeParameterValues implements Serializable {
protected Vector names = new Vector();
protected Vector values = new Vector();

public final static String END_TOKEN = "end";
public ChromosomeParameterValues(String filename) {
    this(new FieldRecordText(filename,"\t"));
}
public ChromosomeParameterValues(FieldRecordText file) {
    String[] fields;
    while((fields = file.readLine()) != null) {
      if (fields.length == 1 && fields[0].equals(END_TOKEN))
        break;
      Error.assertTrue(fields.length == 2);
      names.addElement(fields[0]);
      values.addElement(new Double(fields[1]));
    }
}
public int size() {return names.size();}
public String getName(int index) {return (String)names.elementAt(index);}
public double getValue(int index) {return ((Double)values.elementAt(index)).doubleValue();}
public String toString() {
  String string = new String(this.getClass().toString() + ": ");
  for(int i = 0; i < names.size(); i++)
    string += "(" + getName(i) + "=" + getValue(i) + ")";
  return string;
}
}