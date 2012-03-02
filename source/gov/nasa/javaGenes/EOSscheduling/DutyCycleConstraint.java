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
//  Created by Al Globus on Thu May 01 2003.
package gov.nasa.javaGenes.EOSscheduling;

import gov.nasa.alsUtility.Error;

/**
Implements constraints of the form:
resource must be Available for no more than onTime in any totalTime period.
DEFICIENCIES: Does not consider instrument warmup time and allows instument to be turned off
for very short periods (e.g., 1 second) to meet constraint.
*/
public class DutyCycleConstraint implements java.io.Serializable {
protected int totalTime;
protected int maxOnTime;
protected int onTimeArrayElementDuration;
protected int[] onTimes; // holds onTime for a time interval
static protected final boolean debug = Debug.debug;

/**
holds data for a constraint where something must be off for at least inOffTime in every period of inTotalTime
*/
public DutyCycleConstraint(Horizon horizon, int inTotalTime, int inMaxOnTime) {
    totalTime = inTotalTime;
    maxOnTime = inMaxOnTime;
    Error.assertTrue(totalTime >= maxOnTime);
    Error.assertTrue(inMaxOnTime >= 0);
    Error.assertTrue(horizon != null);
    setQuickCheckArrays(horizon);
}

/* 
BEGIN QUICKFIT CHECK CODE.  Used to quickly check if a time duration will probably fit.
Maintains arrays of buckets with the time already used in a particularly time period. Checks
these to see if it's likely that a new duration will fit.

This stuff has a Error.fatal() BUG!  Run in debug mode in PerformanceTest/DutyCycleTest.
*/
public void setQuickCheckArrays(Horizon horizon) {
    onTimeArrayElementDuration = totalTime;
    onTimes = new int[(horizon.getDuration()/onTimeArrayElementDuration) + 1];
    initialize();
}
public void initialize() {
    for(int i = 0; i < onTimes.length; i++)
        onTimes[i] = 0;
}
/** 
used for a quick screening check. Must only be called if fit() true for other availability constraints.
@return true if dutyCylce is definitely not violated using a quick check.  May return false
even if there isn't a violation, so when false is returned a more precise check must be made.
Designed to quickly decide for performance.
*/
public boolean fastFitCheck(int startTime, int duration) {
    if (duration > maxOnTime)
        return false;
    if (debug) Error.assertTrue(duration >= 0);
    int endTime = startTime + duration;
    int iStart = getIndex(startTime);
    int iEnd = getIndex(endTime);
    if (debug) Error.assertTrue(iEnd - iStart <= 1);
    for(int i = iStart-1; i <= iEnd; i++)
        if (getOnTimes(i) + getOnTimes(i+1) + duration > maxOnTime)
            return false;
    return true;
}
public void insertAt(int startTime, int endTime) {
    int iStart = getIndex(startTime);
    int iEnd = getIndex(endTime);
    int duration = endTime - startTime;
    if (debug) Error.assertTrue(duration >= 0);
    if (iStart == iEnd) 
        onTimes[iStart] += duration;
    else {
        onTimes[iStart] += durationInStartBucket(startTime);
        onTimes[iEnd] += durationInEndBucket(endTime);
        for(int i = iStart + 1; i < iEnd; i++) { // just for completeness, should never happen
            onTimes[i] += onTimeArrayElementDuration;
        }
        if (debug)
            Error.assertTrue(durationInStartBucket(startTime)+durationInEndBucket(endTime) == duration);
    }
    if (debug)
        for(int i = iStart; i <= iEnd; i++) {
            Error.assertTrue(onTimes[i] <= maxOnTime, "startTime=" + startTime + " endTime=" + endTime + " iStart=" + iStart + " iEnd=" + iEnd + " i=" + i + " onTimes[i]=" + onTimes[i] + " maxOnTime=" + maxOnTime);
        }
}
protected int getIndex(int time) {
    int index = time / onTimeArrayElementDuration;
    if (debug)
        Error.assertTrue(index < onTimes.length);
    return index;
}
public int durationInStartBucket(int startTime) {
    return onTimeArrayElementDuration - (startTime % onTimeArrayElementDuration);
}
public int durationInEndBucket(int endTime) {
    return endTime % onTimeArrayElementDuration;
}
/** @return 0 if index 1 too big or small.  Simplifies definitelyFits() */
public int getOnTimes(int index) {
    if (index == -1 || index == onTimes.length)
        return 0;
    return onTimes[index];
}
// for testing only
protected void checkOnTimes(int[] correct) {
    Error.assertTrue(correct.length <= onTimes.length);
    for(int i = 0; i < correct.length; i++)
        Error.assertTrue(correct[i] == onTimes[i]);
}
// END QUICKFIT CODE

// used for testing
public boolean fits(AvailabilityTimeline timeline) {
    for(AvailableNode node = (AvailableNode)timeline.getFirstNode(); timeline.includes(node); node = (AvailableNode)node.next()) {
        if (node.isUnAvailable()) {
            if (!okForward(timeline,node,node.getStart(),maxOnTime))
                return false;
            if (!okBackward(timeline,node,node.getEnd(),maxOnTime))
                return false;
        }
    }
    return true;  
}

public boolean fits(AvailabilityTimeline timeline, AvailableNode node, int startTime, int duration) {
    boolean fastResults = fastFits(timeline,node,startTime,duration);
    if (debug) {
        boolean slowResults = slowFits(timeline,node,startTime,duration);
        Error.assertTrue(fastResults == slowResults, "fast = " + fastResults + " slow = " + slowResults);
    }
    return fastResults;
}

/**
if a period of 'duration' length starting at startTime became UnAvailable, would the timeline
be consistant with the dutycycle constraint?

@arg node where startTime through endTime is, for optimization since caller already should know it.  most be isAvailable()  
*/
public boolean fastFits(AvailabilityTimeline timeline, AvailableNode node, int startTime, int duration) {
    if (duration <= 0)
        return true;
    final int endTime = startTime + duration;
    if (debug) {
        Error.assertTrue(node.isAvailable());
        Error.assertTrue(node.includes(startTime));
        Error.assertTrue(node.includes(endTime) || node.getEnd() == endTime);
    }
    final int maxOnTimeForSearch = maxOnTime - duration;
    if (maxOnTimeForSearch < 0)
        return false;
    final int checkStart = Math.max(timeline.getStart(),endTime - totalTime);
    final int checkEnd = Math.min(timeline.getEnd(),startTime + totalTime);

    // set up variables for first time through loop
    final AvailableNode nodeAtCheckStart = (AvailableNode)timeline.getNodeAtTime(checkStart);
    if (debug)
        Error.assertTrue(nodeAtCheckStart != null);
    if (anyUnAvailableNodeTooLong(nodeAtCheckStart,maxOnTimeForSearch,startTime,endTime))
        return false;
    AvailableNode beginNode = nodeAtCheckStart.nextAvailableNode(); // beginNode.isAvailable()
    if (beginNode == null)
        return true;

    AvailableNode endNode = (AvailableNode)beginNode.previous();
    if (debug) 
        checkEndNode(endNode,startTime,endTime);
    int lastUnAvailableTime = 0;
    if (endNode != null) {
        if (endNode.getStart() > checkEnd)
            return true;
        lastUnAvailableTime = Math.min(checkEnd,endNode.getEnd()) - Math.max(endNode.getStart(),checkStart);
    }
    int cumulativeOnTime = lastUnAvailableTime;
    int endSearchAt = Math.min(Math.max(checkStart,endNode.getEnd()-lastUnAvailableTime) + totalTime,checkEnd);
    endNode = beginNode.nextUnAvailableNode(); // always !endNode.isAvailable()
        
    while(true) { // first extend endNode
        while(true) {
            if (debug) 
                checkEndNode(endNode,startTime,endTime);
            if (cumulativeOnTime > maxOnTimeForSearch) 
                return false;
            if (endNode == null || (endNode.getStart() > checkEnd)) 
                return true;
            if (endNode.getStart() >= endSearchAt) 
                break;
            cumulativeOnTime += Math.min(endNode.getEnd(),endSearchAt) - endNode.getStart();
            endNode = endNode.nextUnAvailableNode();
        }
        // then extend startNode
        cumulativeOnTime -= lastUnAvailableTime;
        beginNode = beginNode.nextAvailableNode();
        if (beginNode == null)
            endSearchAt = checkEnd;
        else
            endSearchAt = Math.min(beginNode.previous().getStart() + totalTime,checkEnd);
        if (beginNode == null || beginNode.getEnd() >= checkEnd) {
            if (cumulativeOnTime + Math.min(endNode.getEnd(),endSearchAt) - endNode.getStart() > maxOnTimeForSearch)
                return false;
            return true;
        }
        lastUnAvailableTime = beginNode.previous().getDuration();
        if (debug) 
            Error.assertTrue(beginNode.isAvailable());
    }
}

protected boolean anyUnAvailableNodeTooLong(AvailableNode nodeAtCheckStart,int maxOnTimeForSearch,int startTime,int endTime) {
    if (debug) {
        Error.assertTrue(nodeAtCheckStart != null);
        Error.assertTrue(endTime >= startTime);
    }
    if (endTime - startTime < maxOnTimeForSearch)
        return false;
    AvailableNode unAvailable = nodeAtCheckStart.isAvailable() ? nodeAtCheckStart.nextUnAvailableNode() : nodeAtCheckStart; 
    while(true) {
        if (unAvailable == null)
            return false;
        else if (unAvailable.getStart() >= endTime)
            return false;
        else if (Math.min(endTime,unAvailable.getEnd()) - Math.max(startTime,unAvailable.getStart()) > maxOnTimeForSearch)
            return true;
        unAvailable = unAvailable.nextUnAvailableNode();
    }
    // can't get here
}
protected void checkEndNode(AvailableNode endNode,int startTime,int endTime){
    if (endNode == null)
        return;
    Error.assertTrue(!endNode.isAvailable());
    if (endNode.getDuration() == 0)
        return;
    Error.assertTrue(!(startTime <= endNode.getStart() && endNode.getStart() < endTime));
    Error.assertTrue(!(startTime < endNode.getEnd() && endNode.getEnd() <= endTime));
    Error.assertTrue(!(startTime <= endNode.getStart() && endNode.getEnd() <= endTime));
    Error.assertTrue(!(endNode.getStart() <= startTime && endTime < endNode.getEnd()));
}

/**
if a period of 'duration' length starting at startTime became UnAvailable, would the timeline
be consistant with the dutycycle constraint?

Algorithm:
- subtract duration to maxOnTime to get the maximum ontime acceptable
- check from the beginning of the duration forward
- check from the end of the duration backward
- for UnAvailable nodes before the duration, check from it's beginning forward
- for UnAvailable nodes after the duration, check from it's end backward

@arg node where startTime is, for optimization since caller already should know it.
*/
public boolean slowFits(AvailabilityTimeline timeline, AvailableNode node, int startTime, int duration) {
    int endTime = startTime + duration;
    if (debug) {
        Error.assertTrue(node.isAvailable());
        Error.assertTrue(node.includes(startTime));
        Error.assertTrue(node.includes(endTime) || node.getEnd() == endTime);
    }
    int maxOnTimeForSearch = maxOnTime - duration;
    if (maxOnTimeForSearch < 0)
        return false;
    int checkStart = Math.max(timeline.getStart(),endTime - totalTime);
    int checkEnd = Math.min(timeline.getEnd(),startTime + totalTime);

    if (!okForward(timeline,node.nextUnAvailableNode(),startTime,maxOnTimeForSearch) || 	!okBackward(timeline,node.previousUnAvailableNode(),endTime,maxOnTimeForSearch))
        return false;
    boolean checkForward = true;
    for(AvailableNode n = (AvailableNode)timeline.getNodeAtTime(checkStart); n.getStart() < checkEnd; n = (AvailableNode)n.next()) {
        if (n == node) {
            checkForward = false;
            continue;
        }
        if (n.isUnAvailable()) {
            if (checkForward) {
                if (!okForward(timeline,n,Math.max(n.getStart(),checkStart),maxOnTimeForSearch)) 
                    return false;
            } else {
                if (!okBackward(timeline,n,Math.min(n.getEnd(),checkEnd),maxOnTimeForSearch))
                    return false;
            }
        }
    }
    return true;
}
/**
starting at 'startTime', check forward 'totalTime' to see if there is no more than 'maxOnTimeForSearch' UnAvailable time
*/
protected boolean okForward(AvailabilityTimeline timeline, AvailableNode node, int startTime, int maxOnTimeForSearch) {
    if (debug) Error.assertTrue(node.isUnAvailable());
    int cumulativeOnTime = 0;
    int endSearchAt = startTime+totalTime;
    for(AvailableNode n = node; timeline.includes(n) && n.getStart() <= endSearchAt; n = n.nextUnAvailableNode()) {
        cumulativeOnTime += Math.min(n.getEnd(),endSearchAt) - Math.max(n.getStart(),startTime);
        if (cumulativeOnTime > maxOnTimeForSearch)
            return false;
    }
    return true;
}
/**
starting at 'endTime', check backward 'totalTime' to see if there is no more than 'maxOnTimeForSearch' UnAvailable time
*/
protected boolean okBackward(AvailabilityTimeline timeline, AvailableNode node, int endTime, int maxOnTimeForSearch) {
    if (debug) Error.assertTrue(node.isUnAvailable());
    int cumulativeOnTime = 0;
    int endSearchAt = endTime - totalTime;
    for(AvailableNode n = node; timeline.includes(n) && n.getEnd() >= endSearchAt; n = n.previousUnAvailableNode()) {
        cumulativeOnTime += Math.min(n.getEnd(),endTime) - Math.max(n.getStart(),endSearchAt);
        if (cumulativeOnTime > maxOnTimeForSearch)
            return false;
    }
    return true;
}
}
