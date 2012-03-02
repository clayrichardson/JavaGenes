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
package gov.nasa.javaGenes.core;

import java.util.Date;
import gov.nasa.alsUtility.Error;

/**
causes another thread to checkpoint periodically
*/
public class PeriodicCheckpoints extends Thread {
protected Checkpointer checkpointer;
protected long time;
protected long lastTime;
private final long minimumSleepTime = 600000L;//10 minutes

PeriodicCheckpoints(Checkpointer c, long periodicity) {
	Error.assertTrue(periodicity > 0);
	checkpointer = c;
	time = periodicity;
	setDaemon(true);
	setPriority(MAX_PRIORITY);
}

public void run() {
	lastTime = System.currentTimeMillis();
	while(true) {
		Date d = checkpointer.getLastCheckpointDate();
		if (d != null && d.getTime() > lastTime)
			lastTime = d.getTime();
		if (System.currentTimeMillis() - lastTime >= time) {
			checkpointer.checkpointWhenPossible();
			lastTime = System.currentTimeMillis();
		}
    else
    	checkpointer.dontCheckpoint();
		try {
    	long timeToSleep = time - (System.currentTimeMillis() - lastTime);
      timeToSleep = java.lang.Math.max(timeToSleep,minimumSleepTime);
			sleep(timeToSleep);
		} catch (InterruptedException e) {} // do nothing
	}
}
}
