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

public class AmericanWireGauge {
private static final double[] gaugeDiameter = { // in mm.  From http://www.reade.com/Conversion/wire_gauge.html
	8.252, 7.348, 6.543, 5.827, 5.189,
	4.621, 4.115, 3.665, 3.264, 2.906,
	2.588, 2.304, 2.052, 1.829, 1.628,
	1.450, 1.291, 1.150, 1.024, 0.9119,
	0.8128, 0.7239, 0.6426, 0.574, 0.5106,
	0.4547, 0.4038, 0.3606, 0.3200, 0.2870
};

public static double getRadius(int gauge) {
	Error.assertTrue("unimplemented guage = " + gauge, 0 <= gauge && gauge < gaugeDiameter.length);
	return gaugeDiameter[gauge] / 2000;
}
}