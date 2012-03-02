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

import java.util.HashMap;
import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.EOSscheduling.EOSModel;
import gov.nasa.javaGenes.EOSscheduling.Satellite;
import gov.nasa.javaGenes.EOSscheduling.AccessWindow;
import gov.nasa.javaGenes.EOSscheduling.SSRTimeline;
import gov.nasa.javaGenes.EOSscheduling.Node;

public class AllSSRcontenders implements java.io.Serializable {
private final static boolean debug = gov.nasa.javaGenes.EOSscheduling.Debug.debug;
protected EOSModel model;
protected TaskList taskList;

protected SSRcontenders[][] theSSRcontenders;

/**
assumes that SSR nodes for ground station access is already set up in the SSSR timelines
*/
// UNIT_TEST?
public AllSSRcontenders(EOSModel inModel,TaskList inTaskList) {
    Error.assertTrue(inModel != null);
    Error.assertTrue(inTaskList != null);
    model = inModel;
    taskList = inTaskList;

    // create SSRcontenders
    theSSRcontenders = new SSRcontenders[model.getNumberOfSatellites()][];
    HashMap map = new HashMap();
    for(int i = 0; i < theSSRcontenders.length; i++) {
        Satellite satellite = model.getSatellite(i);
        SSRTimeline timeline = satellite.getSSRtimeline();
        theSSRcontenders[i] = new SSRcontenders[timeline.getNumberOfSSRsegments()];
        int j = 0;
        for(Node node = timeline.getFirstNode(); j < theSSRcontenders[i].length; node = node.next(),j++) {
            theSSRcontenders[i][j] = new SSRcontenders();
            theSSRcontenders[i][j].setSSRnode(node); // so initial need and contention can be calculated
            map.put(node,theSSRcontenders[i][j]);
        }
    }
    for(int i = 0; i < taskList.getInitialSize(); i++) {
        TaskWeight taskWeight = taskList.getTaskWeight(i);
        AccessWindowsList accessWindows = taskWeight.getContentionAWlist();
        for(int j = 0; j < accessWindows.getInitialSize(); j++) {
            AccessWindowWeight aw = accessWindows.getAccessWindowWeight(j);
            AccessWindow a = aw.getAccessWindow();
            int time = a.getSSRtime();
            SSRTimeline timeline = a.getSatellite().getSSRtimeline();
            Node node = timeline.getNodeAtTime(time);
            Error.assertTrue(node != null);
            Error.assertTrue(map.get(node) != null);
            SSRcontenders contenders = (SSRcontenders)map.get(node);
            contenders.addAccessWindowWeight(aw);
        }
    }
    for(int i = 0; i < theSSRcontenders.length; i++)
        for(int j = 0; j < theSSRcontenders[i].length; j++)
            theSSRcontenders[i][j].setInitialSSRneedSumAndContention();
}
public void reinitializeAndAttachContendersToTimelineNodes() {
    for(int i = 0; i < theSSRcontenders.length; i++) {
        SSRTimeline timeline = model.getSatellite(i).getSSRtimeline();
        int j = 0;
        for(Node node = timeline.getFirstNode(); j < theSSRcontenders[i].length; node = node.next(),j++) {
            theSSRcontenders[i][j].reinitialize();
            theSSRcontenders[i][j].setSSRnode(node);
            if (debug)
                theSSRcontenders[i][j].assertIsValid(model.getSatellite(i));
        }
    }
}
/** for testing */
public SSRcontenders getSSRcontenders(int satellite, int node) {
    return theSSRcontenders[satellite][node];
}
public void assertCorrect() {
    for(int i = 0; i < theSSRcontenders.length; i++)
    for(int j = 0; j < theSSRcontenders[i].length; j++)
        theSSRcontenders[i][j].assertCorrect();
}
}