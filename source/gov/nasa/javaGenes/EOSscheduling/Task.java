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
//  Created by Al Globus on Fri Jul 05 2002.

package gov.nasa.javaGenes.EOSscheduling;

import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.ExtendedVector;
import java.io.PrintWriter;

public class Task implements java.io.Serializable {
protected int duration;
protected ExtendedVector accessWindows = new ExtendedVector();
protected AccessWindow[] accessWindowsArray;
protected double priority = 1;
protected int number = -1;

public Task(int inDuration) {
    duration = inDuration;
}
public void setNumbers(int inNumber) {
    number = inNumber;
    Error.assertTrue(arrayCreated());
    for(int i = 0; i < accessWindowsArray.length; i++)
        accessWindowsArray[i].setNumber(i);
}
public boolean arrayCreated() {
    return accessWindowsArray != null && accessWindows.size() == accessWindowsArray.length;
}
public int getNumber() {return number;}
public static String getReportHeader() {return "Duration\tPriority\tNumberOfAccessWindows";}
public void reportTo(PrintWriter p) {
    p.println(duration + "\t" + getPriority() + "\t" + accessWindowsArray.length);
}
public void reportAccessWindowsWindowsTo(int taskNumber,PrintWriter p) {
    for(int i = 0; i < accessWindowsArray.length; i++)
        accessWindowsArray[i].reportTo(taskNumber,p);
}
public double getPriority() {return priority;} // higher priorities more important
public void setPriority(double inPriority) {priority = inPriority;}
public int getDuration() {return duration;}
public void addAccessWindow(AccessWindow w) {
    Error.assertTrue(accessWindowsArray == null);
    Error.assertNotNull(w);
    accessWindows.addElement(w);
}
public void hasAllAccessWindowsNow() {
    accessWindowsArray = new AccessWindow[accessWindows.size()];
    accessWindows.copyInto(accessWindowsArray);
    sortWindowsArrayByEarliestStartTime();
}
public boolean isExecutable() {
    if (accessWindowsArray != null)
        return accessWindowsArray.length > 0;
    if (accessWindows != null)
        return accessWindows.size() > 0;
    return false;
}
public boolean areAccessWindowsInEarliestFirstOrder() {return accessWindowsArray != null;}
private void sortWindowsArrayByEarliestStartTime() { // bubble sort, but arrays should be short
    boolean sorted = false;
    while (!sorted) {
	sorted = true;
	for (int i = 0; i + 1 < accessWindowsArray.length; i++){
	    if (accessWindowsArray[i].getStart() > accessWindowsArray[i + 1].getStart()) {
		    AccessWindow temporary = accessWindowsArray[i];
		    accessWindowsArray[i] = accessWindowsArray[i + 1];
		    accessWindowsArray[i + 1] = temporary;
		    sorted = false;
	    }
	}
    }
}
/**
@return don't modify.
*/
public AccessWindow[] getAccessWindows() {
    Error.assertNotNull(accessWindowsArray);
    return accessWindowsArray;
}
public int numberOfAccessWindows() {return accessWindowsArray.length;}
public AccessWindow getAccessWindow(int i) {return accessWindowsArray[i];}
}
