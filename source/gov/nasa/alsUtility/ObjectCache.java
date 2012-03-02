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
//  Created by Al Globus on Wed Sep 11 2002.
package gov.nasa.alsUtility;

import java.util.Vector;
import gov.nasa.alsUtility.Error;
import java.lang.Class;

/**
 cache objects to avoid object re-allocation.  Objects created will not be garbage collected until
 the ObjectCache is garbage collected.  Object class must have a constructor that takes no arguments.
 This constructor will be used to create the objects as needed.
*/
public class ObjectCache implements java.io.Serializable {
private Vector cache = new Vector();
private int nextIndex = 0;
private Class theClass;

public ObjectCache(String inClass) {
    try {
        theClass = Class.forName(inClass);
    } catch (Exception e) {
        Error.fatal(e);
    }
}
public Object getObject() {
    Object object = null;
    if (nextIndex >= cache.size()) {
        try {
            object = theClass.newInstance();
            cache.addElement(object);
        } catch (Exception e) {
            Error.fatal(e);
        }
    } else
        object = cache.elementAt(nextIndex);
    nextIndex++;
    return object;
}
/**
Call when it's ok to reuse all of the objects in the cache.
*/
public void reinitialize() {
    nextIndex = 0;
}
}
