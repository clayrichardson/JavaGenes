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
//  Created by Al Globus on Mon Sep 16 2002.
package gov.nasa.alsUtility;

import junit.framework.TestCase;

public class ObjectCacheTest extends TestCase {

public ObjectCacheTest(String name) {super(name);}

public void testOjbect() {
    ObjectCache cache = new ObjectCache("gov.nasa.alsUtility.integer");
    testIt(cache,0,5,1);
    testIt(cache,5,10,2);
    testIt(cache,10,111,3);
    testIt(cache,111,2045,4);
}
private void testIt(ObjectCache cache, int lastLimit, int limit, int name) {
    for(int i = 0; i < limit; i++) {
        integer theInt = (integer)cache.getObject();
        if (i < lastLimit) 
            assertTrue(name + " old value not there " + i, theInt.getValue() == i);
        else
            assertTrue(name + " should have been new object " + i, theInt.getValue() == 0);
        theInt.setValue(i);
    }
    cache.reinitialize();
}
}
