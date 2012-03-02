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
package gov.nasa.javaGenes.main;

import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.FieldRecordText;
import gov.nasa.alsUtility.Utility;

// depends on the output format used by Anna Pryor's program
public class FindMinimumInTsdFile {
static String separator = "\t";

public static void main(String[] arguments) {
    if (arguments.length < 3) {
        System.out.println("format: java gov.nasa.javaGenes.main startToken endToken filename.tsd ...");
        System.exit(-1);
    }
	String startToken = arguments[0];
	String endToken = arguments[1];
	for(int f = 2; f < arguments.length; f++) {
		boolean checking = false;
		FieldRecordText in = new FieldRecordText(arguments[f],separator);
		double minimum = Double.MAX_VALUE;
		while(true) {
			String[] line = in.readLine();
			if (line == null)
				break;
			for(int i = 0; i < line.length; i++) {
				if (line[i].equals(startToken))
					checking = true;
				else if (line[i].equals(endToken))
					checking = false;
				if (checking) {
					try {
						double value = Utility.string2double(line[i]);
						if (value < minimum)
							minimum = value;
					} catch (Exception e) {} // handles non-doubles in the file
				}
			}
		}
		System.out.println(minimum+"");
	}
}
}
