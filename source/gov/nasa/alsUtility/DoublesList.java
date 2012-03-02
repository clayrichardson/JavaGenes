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
//  Created by Al Globus on Wed Jun 26 2002.

package gov.nasa.alsUtility;

import java.util.Vector;
import java.util.StringTokenizer;

public class DoublesList {
protected Vector list = new Vector();
final static public String separator = ",";

public DoublesList(String commaSeparatedList) {
    StringTokenizer tokenizer = new StringTokenizer(commaSeparatedList,separator);
    while(tokenizer.hasMoreTokens()) {
        String next = tokenizer.nextToken();
        list.addElement(new Double(next));
    }
}
public int size() {
    return list.size();
}
public double get(int i) {
    return ((Double)list.elementAt(i)).doubleValue();
}
}
