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

import java.io.PrintStream;
import java.io.PrintWriter;
import gov.nasa.alsUtility.ExtendedVector;
import gov.nasa.alsUtility.IO;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.Utility;
import gov.nasa.javaGenes.core.Parameters;

public class CoreUtility {

/** @return array of string found after desiredParameterNamesArray + " + " up to end-of-line in same order. */
public static String[] getParameterValues(String[] desiredParameterNamesArray, String parametersFilename) {
	ExtendedVector desiredParameterNames = new ExtendedVector(desiredParameterNamesArray); // UNNECESSARY. REMOVE SOME DAY
	String[] parameterLines = Utility.file2StringArray(parametersFilename);
	String[] desiredValues = new String[desiredParameterNames.size()];
	for(int i = 0; i < parameterLines.length; i++) {
		for(int j = 0; j < desiredParameterNames.size(); j++) {
			String name = (String)desiredParameterNames.get(j);
			if (parameterLines[i].startsWith(name + Parameters.SEPARATOR)) {
				Error.assertTrue(desiredValues[j] == null);
				desiredValues[j] = parameterLines[i].substring(name.length() + gov.nasa.javaGenes.core.Parameters.SEPARATOR.length());
				break;
			}
		}
	}
	for(int i = 0; i < desiredValues.length; i++)
		Error.assertTrue("value missing: " + desiredParameterNamesArray[i], desiredValues[i] != null);
	return desiredValues;
}

/** @param dataFilename if not null this file must be in every directory */
public static void printData2tsd(String filename,String[] parameterNames, String[] directoryNames, String dataFilename) {
	PrintWriter out = IO.getPrintWriter(filename);
	printData2tsd(out,parameterNames,directoryNames,dataFilename);
	out.close();
}
/** @param dataFilename if not null this file must be in every directory */
public static void printData2tsd(PrintWriter out,String[] parameterNames, String[] directoryNames, String dataFilename) {
	Error.assertNotNull(out);
	Error.assertNotNull(parameterNames);
	Error.assertNotNull(directoryNames);

	// print out header
	out.print("name");
	for(int j = 0; j < parameterNames.length; j++)
		out.print("\t" + parameterNames[j]);
	if (dataFilename != null && directoryNames.length > 0) {
		final String filename = directoryNames[0] + Utility.fileSeparator() + dataFilename;
		final String header = Utility.getFirstLineOfFile(filename);
		if (header != null)
			out.println("\t" + header);
		else
			out.println();
	} else
		out.println();

	// print out data
	for(int i = 0; i < directoryNames.length; i++) {
		final String parametersFilename = directoryNames[i] + Utility.fileSeparator() + gov.nasa.javaGenes.core.Reporter.parametersFilename;
		final String[] parameterValues = CoreUtility.getParameterValues(parameterNames,parametersFilename);
		
		out.print(directoryNames[i]);
		for(int j = 0; j < parameterValues.length; j++)
			out.print("\t" + parameterValues[j]);
		if (dataFilename != null) {
			final String filename = directoryNames[i] + Utility.fileSeparator() + dataFilename;
			final String data = Utility.getLastLineOfFile(filename);
			if (data != null)
				out.println("\t" + data);
			else
				out.println();
		} else
			out.println();
	}
}
}
	
