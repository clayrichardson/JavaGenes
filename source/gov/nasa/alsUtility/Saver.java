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


import java.io.Serializable;
import java.util.Vector;
import gov.nasa.alsUtility.Utility;

/**
This class is intended to buffer up many timesteps or
generations of data and then save them as an array
using java serialization. The file name is appended
with an ASCII number that should be in alphabetical order.
Every save call creates an additional file in a specified directory.
*/
public class Saver implements java.io.Serializable {

/**
directory to save data in
*/
protected String directoryName;
protected int numberOfSaves = 0;
protected Vector data = new Vector();

/**
@param s directory name to save data in. Directory will be created
*/
public Saver (String s) {
    directoryName = s;
    Utility.makeDirectory (directoryName);
}

/**
 add object to the buffer for later saving
*/
public void buffer (Object object){
    data.addElement (object);
}

/**
save an array of objects that were buffered
*/
public void save() {
    if (data.size() <= 0) return;
    Object[] objects = new Object[data.size()];
    for (int i = 0; i < objects.length; i++)
        objects[i] = data.elementAt(i);
    Utility.serialize ((Serializable)objects, 
        directoryName + "/" + Utility.makeSequentialNumber(numberOfSaves++));
    data.removeAllElements();
}

/**
save an single object
*/
public void save(Serializable object) {
    Utility.serialize (object, 
        directoryName + "/" + Utility.makeSequentialNumber(numberOfSaves++));
}
/**
set the number used to name files
*/
public void setNumber(int number) {numberOfSaves = number;}
}
