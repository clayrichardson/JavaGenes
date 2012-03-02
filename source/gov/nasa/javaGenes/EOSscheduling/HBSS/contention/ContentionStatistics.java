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
package gov.nasa.javaGenes.EOSscheduling.HBSS.contention;

import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.Sample;
import gov.nasa.alsUtility.IO;
import java.io.PrintWriter;

public class ContentionStatistics implements java.io.Serializable {
protected TaskList taskList;
protected Sample taskWeight = new Sample();
protected Sample sensorAvailNeed = new Sample();
protected Sample SSRneed = new Sample();
protected Sample accessWindowWeight = new Sample();
protected Sample sensorAvailContention = new Sample();
protected Sample sensorAvailContenders = new Sample();
protected Sample SSRcontention = new Sample();
protected Sample sensorAvailDifficulty = new Sample();
protected Sample SSRdifficulty = new Sample();
protected Sample SSRCtaskCount = new Sample();
protected Sample SSRCaccessWindowCount = new Sample();
protected Sample SSRCneedSum = new Sample();
protected Sample SSRCcontention = new Sample();

public ContentionStatistics(TaskList inTaskList) {
    Error.assertTrue(inTaskList != null);
    taskList = inTaskList;
    gatherData();
}
public void gatherData() {
    for(int i = 0; i < taskList.getInitialSize(); i++) {
        TaskWeight t = taskList.getTaskWeight(i);
        taskWeight.addDatum(t.getWeight());
        sensorAvailNeed.addDatum(t.getSensorAvailNeed());
        SSRneed.addDatum(t.getSSRneed());
        sensorAvailDifficulty.addDatum(t.getSensorAvailDifficulty());
        SSRdifficulty.addDatum(t.getSSRdifficulty());
        for(int j = 0; j < t.getNumberOfAccessWindows(); j++) {
            AccessWindowWeight aw = taskList.getAccessWindowWeight(i,j);
            accessWindowWeight.addDatum(aw.getWeight());
            sensorAvailContention.addDatum(aw.getSensorAvailContention());
            sensorAvailContenders.addDatum(aw.getSensorAvailContenders().size());
            SSRcontention.addDatum(aw.getSSRcontention());
        }
    }
    SSRcontenders[][] ssr = taskList.allSSRcontenders.theSSRcontenders;
    for(int i = 0; i < ssr.length; i++)
    for(int j = 0; j < ssr[i].length; j++) {
        SSRcontenders s = ssr[i][j];
        SSRCtaskCount.addDatum(s.getCurrentTaskWeightCount());
        SSRCaccessWindowCount.addDatum(s.getCurrentAccessWeightCount());
	SSRCneedSum.addDatum(s.getSSRneedSum());
	SSRCcontention.addDatum(s.getSSRcontention());
    }
}
public void printStatistics(String filename) {
    PrintWriter out = IO.getPrintWriter(filename);
    out.println("value\tmean\tvariance\tstdev\tmin\tmax\tcount");
    out.println("taskWeight\t" + taskWeight.statisticsString());
    out.println("sensorAvailNeed\t" + sensorAvailNeed.statisticsString());
    out.println("SSRneed\t" + SSRneed.statisticsString());
    out.println("accessWindowWeight\t" + accessWindowWeight.statisticsString());
    out.println("sensorAvailContention\t" + sensorAvailContention.statisticsString());
    out.println("sensorAvailContender count\t" + sensorAvailContenders.statisticsString());
    out.println("SSRcontention\t" + SSRcontention.statisticsString());
    out.println("sensorAvailDifficulty\t" + sensorAvailDifficulty.statisticsString());
    out.println("SSRdifficulty\t" + SSRdifficulty.statisticsString());
    out.println("SSRcontenders task count\t" + SSRCtaskCount.statisticsString());
    out.println("SSRcontenders access window count\t" + SSRCaccessWindowCount.statisticsString());
    out.println("SSRcontenders SSRneedSum\t" + SSRCneedSum.statisticsString());
    out.println("SSRcontenders SSRCcontention\t" + SSRCcontention.statisticsString());
    out.close();
}
}
