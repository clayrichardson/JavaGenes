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
//  Created by Al Globus on Mon Jun 24 2002.
package gov.nasa.alsUtility;

import java.util.Hashtable;
import java.util.StringTokenizer;

public class PropertiesList {
protected Hashtable table = new Hashtable();
final static protected String assignmentString = "=";

public PropertiesList(String list) {
    StringTokenizer tokenizer = new StringTokenizer(list);
    while(tokenizer.countTokens() >= 3) {
        String key = tokenizer.nextToken();
        tokenizer.nextToken();// should be = //Error.assertTrue(tokenizer.nextToken().equals(assignmentString),"no assignment " + assignmentString + " for property in " + list);
        String value = tokenizer.nextToken();
        putProperty(key,value);
    }
}
public void putProperty(String key, String value) {
    table.put(key,value);
}
public boolean hasProperty(String name) {
    return table.containsKey(name);
}
public String getProperty(String name) {
    return (String)table.get(name);
}
public int size() {return table.size();}
static public String separator() {return "\t";}
static public String assignmentString() {return " " + assignmentString + " ";}

}
