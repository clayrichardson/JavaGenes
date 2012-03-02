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

import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.Utility;

public class Parameters2tsd  {

final static String format = "format: java gov.nasa.javaGenes.core.utility.Parameters2tsd outputFilename number_of_parameters [names_of_parameters ...] directories_with_JavaGenes_output ...";
public static String outputFilename;
public static String[] parameterNames;
public static String[] directoryNames;

public static void main(String[] arguments) {
	processArguments(arguments);
	CoreUtility.printData2tsd(outputFilename,parameterNames,directoryNames,null);
}
public static void processArguments(String[] arguments) {
	assertTrue(3 <= arguments.length);

	outputFilename = arguments[0];
	parameterNames = new String[Utility.string2integer(arguments[1])];
	assertTrue(parameterNames.length+2 <= arguments.length);
	for(int i = 0; i < parameterNames.length; i++)
		parameterNames[i] = arguments[i+2];
	directoryNames = new String[arguments.length - parameterNames.length - 2];
	for(int i = 0; i < directoryNames.length; i++)
		directoryNames[i] = arguments[i + 2 + parameterNames.length];
}
protected static void assertTrue(boolean b) {
	if (!b)
		Error.fatal(format);
}
}