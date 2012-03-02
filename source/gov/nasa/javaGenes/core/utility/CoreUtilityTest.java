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
package gov.nasa.javaGenes.core.utility;

import junit.framework.TestCase;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.Utility;
import gov.nasa.javaGenes.core.Parameters;

public class CoreUtilityTest extends TestCase {

public CoreUtilityTest(String name) {super(name);}

public void testGetParameterValues() {
	final String filename = "TTEEEMMPPPP876.txt";
	Parameters p = new Parameters();
	p.frequencyOfASCIIPopulations = 2;
	p.kidsPerGeneration = 17;
	p.populationSize = 100;
	Utility.makeFile(filename,p.toString());
	String[] desiredParameters = {"frequencyOfASCIIPopulations","kidsPerGeneration","populationSize"};
	String[] parameterValues = CoreUtility.getParameterValues(desiredParameters,filename);
	Error.assertTrue("1", parameterValues[0].equals("2"));
	Error.assertTrue("2", parameterValues[1].equals("17"));
	Error.assertTrue("3", parameterValues[2].equals("100"));
	Utility.removeFile(filename);
}
}