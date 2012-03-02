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
//  Created by Al Globus on Fri Sep 06 2002.

package gov.nasa.javaGenes.EOSscheduling;

import junit.framework.TestCase;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.FieldRecordText;
import gov.nasa.alsUtility.EasyFile;

public class CrossTrackSlewTest extends TestCase {
private String StkAerGeneratedPointingDataFile = "testData/pointingTestData.csv";

public CrossTrackSlewTest(String name) {super(name);}
private FieldRecordText pointingData;
//private EasyFile checkPointing;

/*
public void testCalculateCrossTrackSlew() {
    // for checking values, not normally used
    pointingData = new FieldRecordText(StkAerGeneratedPointingDataFile);
    //checkPointing = new EasyFile("checkPointing.tsd");
    //checkPointing.println("Azimuth\tElevation\nRange\nCrossTrackPoint");
     
    pointingData = new FieldRecordText(StkAerGeneratedPointingDataFile);
    pointingData.readLine();
    pointingData.readLine();
    
    
    assertTrue("1", Utility.nearlyEqual(0,getNextCrossTrackSlew(),0.01));

    double first = getNextCrossTrackSlew();
    double second = getNextCrossTrackSlew();
    double third = getNextCrossTrackSlew();
    assertTrue("10", Math.abs(second) > Math.abs(first));
    assertTrue("11", Utility.signOf(second) == Utility.signOf(first));
    assertTrue("12", Math.abs(third) > Math.abs(second));
    assertTrue("13", Utility.signOf(third) == Utility.signOf(second));

    first = getNextCrossTrackSlew();

    assertTrue("15", Utility.signOf(first) != Utility.signOf(second));
    second = getNextCrossTrackSlew();
    third = getNextCrossTrackSlew();
    assertTrue("20", Math.abs(second) > Math.abs(first));
    assertTrue("21", Utility.signOf(second) == Utility.signOf(first));
    assertTrue("22", Math.abs(third) > Math.abs(second));
    assertTrue("23", Utility.signOf(third) == Utility.signOf(second));

    pointingData.close();
    //checkPointing.close();    
}
*/
protected double getNextCrossTrackSlew() {
    pointingData.readLine();
    String[] line = pointingData.readLine();
    double minElevation = 1;
    PointingRequirement best = null;
    //checkPointing.println("Azimuth\tElevation\tRange\tCrossTrackPoint");
    while(line.length > 0) {
        PointingRequirement p = new PointingRequirement(0,Utility.string2double(line[1]),Utility.string2double(line[2]),Utility.string2double(line[3]));
        //checkPointing.println(p.getAzimuth() + "\t" + p.getElevation() + "\t" + p.getRange() + "\t" + CrossTrackSlew.calculateCrossTrackSlew(p));
        if (minElevation > p.getElevation()) {
            minElevation = p.getElevation();
            best = p;
        }
        line = pointingData.readLine();
    }
    double r = CrossTrackSlew.calculateCrossTrackSlew(best);
    return r;
}
}
