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

import java.util.Hashtable;
import java.util.Vector;
import java.io.Serializable;
import java.io.PrintWriter;
import gov.nasa.alsUtility.Utility;

/**
handles named Vectors of double data. Can save this data in tab separated
ASCII files suitable for MS Excel and other data analysis packages.
*/
public class DataTable extends Hashtable implements Serializable{
public DataTable() {}
/*
public DataTable(TokenizeInput tokenizer) {
  int namesLength = tokenizer.getInteger();
  for(int i = 0; i < namesLength; i++){
  	String name = tokenizer.getString();
    int dataLength = tokenizer.getInteger();
    for(int j = 0; j < dataLength; j++)
    	putDatum(name,j,tokenizer.getDouble());
	}
}
public void stateSave(TokenizeOutput tokenizer) {
	String[] names = Utility.getStringKeys (this);
  tokenizer.putInteger(names.length);
  for(int i = 0; i < names.length; i++){
  	tokenizer.putString(names[i]);
    Vector data = (Vector)get(names[i]);
    tokenizer.putInteger(data.size());
    for(int j = 0; j < data.size(); j++)
    	tokenizer.putDouble(((Double)(data.elementAt(j))).doubleValue());
	}
}
*/
public void sort() {
  String[] names = Utility.getStringKeys (this);
  for(int i = 0; i < names.length; i++){
    Vector data = getData(names[i]);
    Utility.sortVectorOfDouble(data);
  }
}
/**
@param name name of the data to put a double into
@param place index into the Vector to place the data
@param datum the data to save
*/
public void putDatum (String name, int place, double datum) {
    Vector data = getData (name);
    while (data.size() <= place)
        data.addElement (new Double(Double.NaN));
    data.setElementAt(new Double(datum),place);
}
/**
adds datum to end of vector
*/
public void putDatum (String name, double datum) {
    Vector data = getData (name);
    putDatum(name,data.size(),datum);
}

/**
get the data associated with a name. Create a new Vector if the data
doesn't already exist.

@return A Vector of data (possibly empty)
*/
public Vector getData (String name) {
    Vector data = (Vector) get (name);
    if (data == null) {
        data = new Vector();
        put (name, data);
    }
    return data;
}

/**
write all the data to a tab separated ASCII file. Place the names in the
first column and all data associated with a name in the same row.
*/
public void write (String filename) {
    PrintWriter out = Utility.outputFile (filename);
    String[] names = Utility.getStringKeys (this);
    for(int i = 0; i < names.length; i++){
        out.print (names [i] + "");
		Vector data = (Vector) get (names [i]);
		for(int j = 0; j < data.size(); j++){
	     	out.print ("\t" + data.elementAt (j));
		}
		out.println();
    }
    out.close();
}
/**
write all the data to a tab separated ASCII file. Place the names in the
first row and all data associated with a name in the same column.
*/
public void writeColumns (String filename) {
    PrintWriter out = Utility.outputFile (filename);
    String[] names = Utility.getStringKeys (this);
    Vector data = new Vector(); // vector of vectors
    int size = 0;
    for(int i = 0; i < names.length; i++){
		data.addElement (get (names [i]));
		if (((Vector)data.elementAt(i)).size() > size)
			size = ((Vector)data.elementAt(i)).size();
    }
    for(int i = 0; i < names.length; i++)
        out.print (names [i] + "\t");
	out.println();
	for(int i = 0; i < size; i++){
		for(int j = 0; j < names.length; j++){
			Vector d = (Vector)data.elementAt(j);
			if (d.size() > i)
	     		out.print (d.elementAt(i).toString());
	     	out.print ("\t");
	    }
		out.println();
    }
    out.close();
}

/*
public void read (String filename) {
    TabSeparatedFile in = new TabSeparatedInputFile(filename);
    while (!in.eof()) {
        String name = in.readString();
	Vector data = getData (name);
	in.readDoubleln (data);
    }
    in.close();
}
*/
}
