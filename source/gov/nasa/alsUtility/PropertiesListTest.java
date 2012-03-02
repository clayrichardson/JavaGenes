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
import junit.framework.TestCase;

public class PropertiesListTest extends TestCase {

public PropertiesListTest(String name) {super(name);}

public void testProperties() {
    PropertiesList list = new PropertiesList("energy = 1.5");
    assertTrue("0", list.size() == 1);
    assertTrue("1", list.hasProperty("energy"));
    assertTrue("2", list.getProperty("energy").equals("1.5"));
    
    list = new PropertiesList("energy = 1.5 unitCell = 1,2,3,4,5,6 ");
    assertTrue("2", list.hasProperty("energy"));
    assertTrue("3", list.getProperty("energy").equals("1.5"));
    assertTrue("4", list.hasProperty("unitCell"));
    assertTrue("5", list.getProperty("unitCell").equals("1,2,3,4,5,6"));
    assertTrue("5.5", list.size() == 2);
    
    list = new PropertiesList("  unitCell = 1,2,3,4,5,6    energy = 1.5 ");
    assertTrue("6", list.hasProperty("energy"));
    assertTrue("7", list.getProperty("energy").equals("1.5"));
    assertTrue("8", list.hasProperty("unitCell"));
    assertTrue("9", list.getProperty("unitCell").equals("1,2,3,4,5,6"));
    
    list = new PropertiesList("  unitCell  = 1,2,3,4,5,6  dimerForce =  -5.123e3  energy = 1.5 ");
    assertTrue("10", list.hasProperty("energy"));
    assertTrue("11", list.getProperty("energy").equals("1.5"));
    assertTrue("12", list.hasProperty("unitCell"));
    assertTrue("13", list.getProperty("unitCell").equals("1,2,3,4,5,6"));
    assertTrue("14", list.hasProperty("dimerForce"));
    assertTrue("15", list.getProperty("dimerForce").equals("-5.123e3"));
    assertTrue("16", !list.hasProperty("foo"));
    assertTrue("17", list.size() == 3);
}
}

