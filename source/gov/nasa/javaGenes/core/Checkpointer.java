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


import java.lang.InterruptedException;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.lang.Thread;
import java.lang.Math;
import java.lang.Double;
import java.io.Serializable;
import gov.nasa.alsUtility.IO;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.LogFile;
import gov.nasa.alsUtility.Timer;


/**
Runs a Checkpointable job.

Modify
static final private Checkpointer read(String f, String className, String[] arguments)
when you want to evolve a new kind of data structure and have implemented the relevant classes:
	RunFoo
  FooParameters
  FooPopulation
  FooIndividual
and any mutator or crossover operators necessary

As a general rule, the static methods are used for checkpointing calls since these
are accessible everywhere without passing the Checkpointer object around as an argument.
*/
public class Checkpointer implements Serializable {
protected static volatile Checkpointer currentCheckpointer;

protected volatile Checkpointable objectToCheckpoint;
protected static volatile String filename = "checkpoint.serialized";

protected volatile int excessiveTimeBetweenCheckpointOKs = 0;
protected long timeBetweenCheckpoints = 0;

protected volatile Date lastCheckpointDate;
protected volatile long lastOkToCheckpoint = System.currentTimeMillis();
protected volatile boolean checkpointASAP = false;
protected volatile boolean mustPrepareToDie = false;
protected volatile boolean readyToDieNow = false;

/**
used only to test the simple things during debugging
*/
private static void test() {
}
/**
Will restart if a checkpoint file exists, will
start if not.
*/
public static void main(String[] a) {
    test();//used for debugging only
    LogFile readTimeFile = new LogFile("readTime.tsd", true);
    final int numberOfCheckpointerParameters = 3;
    if (a.length < numberOfCheckpointerParameters) {
        System.out.println("format: java Checkpointer subclass_of_JavaGenes.Run checkPointTimeInHours (true|false)");
        System.exit(-1);
    }
    String className = a[0];
    double checkPointTime = Utility.string2double(a[1]);
    long checkPointTimeInMilliseconds = (long)(checkPointTime <= 0 ? 0 : checkPointTime * 60 * 60 * 1000);
    
    boolean tryRestart = Utility.string2boolean(a[2]);
    // set up arguments for Run class
    String[] arguments = new String[a.length - numberOfCheckpointerParameters];
    for (int i = numberOfCheckpointerParameters; i < a.length; i++)
  	arguments[i-numberOfCheckpointerParameters] = a[i];

    try {
        File restartFile = new File(filename);
        if (tryRestart && restartFile.exists()) { // is this run a restart?
            Timer timeRead = new Timer();
            timeRead.start();
            // java serialization
            currentCheckpointer = read(filename);
            timeRead.stop();
            readTimeFile.println(Utility.date() + "\t" + timeRead.timeElapsed());
            
            currentCheckpointer.timeBetweenCheckpoints = checkPointTimeInMilliseconds;
            startPeriodicCheckpoint(currentCheckpointer,checkPointTimeInMilliseconds);
            currentCheckpointer.lastOkToCheckpoint = System.currentTimeMillis();
            currentCheckpointer.restart();
        } else {
            if (!readTimeFile.containsData())
                readTimeFile.println("time\tlength(ms)");
            currentCheckpointer = new Checkpointer((gov.nasa.javaGenes.core.Run)Utility.newInstance(className));
            currentCheckpointer.timeBetweenCheckpoints = checkPointTimeInMilliseconds;
            startPeriodicCheckpoint(currentCheckpointer,checkPointTimeInMilliseconds);
            currentCheckpointer.lastOkToCheckpoint = System.currentTimeMillis();
            currentCheckpointer.start(arguments);
        }
        currentCheckpointer.printCheckpointStatistics();
    } catch (Exception e) {
        e.printStackTrace(System.out); // put breakpoint here to catch all exceptions
				Error.fatal("something wrong in Checkpointer");
    }
    System.out.println("done");
    System.out.flush();
}		
/**
starts a thread that will execute periodic checkpoints
*/
protected static void startPeriodicCheckpoint(Checkpointer current, long interval) {
	try {
		if (interval > 0) {
			PeriodicCheckpoints periodic = new PeriodicCheckpoints(current,interval);
			periodic.start();
		}
    } catch (Exception e) {
        Error.fatal(e);
    }
}
private Checkpointer(Checkpointable object) {
	objectToCheckpoint = object;
	objectToCheckpoint.setCheckpointer(this);
	currentCheckpointer = this;
}
static final private Checkpointer read(String f) {
	Checkpointer c = getSerialized(f);
	c.objectToCheckpoint.setCheckpointer(c);
	c.filename = f;
	currentCheckpointer = c;
	return c;
}
// to be used exclusively by objectToCheckpoint
/**
called by a Checkpointable.
it's okay to checkpoint now.
*/
final static public void ok() {
    if (currentCheckpointer != null)
        currentCheckpointer.okLocal();
}
/**
called by a Checkpointable.
checkpoint immediately.
*/
final static public void checkpoint() {currentCheckpointer.checkpointLocal();}
/**
called by a Checkpointable.
checkpoint immediately to the indicated file.
*/
final static public void checkpoint(String f) {currentCheckpointer.checkpointLocal(f);}

// to be used by external agency that wishes to checkpoint
/**
Called by an external agency.
*/
final static public void checkpointWhenPossible() {currentCheckpointer.checkpointWhenPossibleLocal();}
/**
Called by an external agency.
*/
final static public void dontCheckpoint() {currentCheckpointer.dontCheckpointLocal();}
/**
Called by an external agency.
*/
final static public void prepareToDie() {currentCheckpointer.prepareToDieLocal();}
/**
Called by an external agency.
*/
final static public boolean areYouPreparingToDie() {return currentCheckpointer.areYouPreparingToDieLocal();}
/**
Called by an external agency.
*/
final static public boolean areYouReadyToDie() {return currentCheckpointer.areYouReadyToDieLocal();}
/**
Called by an external agency.
You're not going to die after all.
*/
final static public void cancelDeath() {currentCheckpointer.cancelDeathLocal();}
/**
@return date of last checkpoint, null if no checkpoint has been done since construction
*/
final static public Date getLastCheckpointDate() {return currentCheckpointer.getLastCheckpointDateLocal();}

/**
Not normally not called directly
*/
public void start(String[] arguments) {objectToCheckpoint.start(arguments);}
/**
Not normally not called directly
*/
public void restart() {objectToCheckpoint.restart();}
/**
Not normally not called directly
*/
final synchronized public void okLocal() {
	Thread.yield(); // allow environment (for example, Condor) to execute
	long currentTime = System.currentTimeMillis();
  if (timeBetweenCheckpoints > 0 && currentTime - lastOkToCheckpoint > timeBetweenCheckpoints)
  	excessiveTimeBetweenCheckpointOKs++;
	lastOkToCheckpoint = currentTime;
	if (mustPrepareToDie || checkpointASAP)
		checkpointLocal();
}
/**
Not normally not called directly
*/
final synchronized public void checkpointLocal(String f) {filename = f; checkpoint();}
/**
Not normally not called directly
*/

final synchronized public void checkpointLocal() {
		objectToCheckpoint.beforeCheckpoint();
		checkpointASAP = false;
		String temporaryFilename = filename + "_temporary";
    //standard java serialization
	try {
	    FileOutputStream f = new FileOutputStream(temporaryFilename);
	    ObjectOutputStream  s  =  new  ObjectOutputStream(f);
	    s.writeObject(this);
	    s.flush();
	    s.close();
    } catch (IOException e) {
        Error.fatal ("serialization error in file " + filename + ": " + e);
    }

    /*special-purpose state save
    TokenizeOutput tokenizer = new TokenizeOutput(temporaryFilename);
    objectToCheckpoint.stateSave(tokenizer);
    tokenizer.close();
    */
      gov.nasa.alsUtility.IO.renameFile(temporaryFilename,filename); // minimizes time process failure creates corrupt file
	    objectToCheckpoint.afterCheckpoint();
    lastCheckpointDate = new Date();
    if (mustPrepareToDie) {
    	mustPrepareToDie = false;
		printCheckpointStatistics();
		readyToDieNow = true;
		while(readyToDieNow) {
			try {
				wait();	// wait for death
			} catch (InterruptedException e) {}
		}
    }
}
/**
Not normally not called directly
*/
final synchronized public void cancelDeathLocal() {
	readyToDieNow = false;
	notifyAll();
}
/**
Not normally not called directly
*/
final synchronized public void checkpointWhenPossibleLocal() {checkpointASAP = true;}
/**
Not normally not called directly
*/
final synchronized public void dontCheckpointLocal() {checkpointASAP = false;}
/**
Not normally not called directly
*/
final synchronized public void prepareToDieLocal() {mustPrepareToDie = true;}
/**
Not normally not called directly
*/
final synchronized public boolean areYouPreparingToDieLocal() {return mustPrepareToDie;}
/**
Not normally not called directly
*/
final synchronized public boolean areYouReadyToDieLocal() {return readyToDieNow;}
/**
Not normally not called directly
*/
final synchronized public Date getLastCheckpointDateLocal() {return lastCheckpointDate;}
/**
creates checkpoint_filename.statistics.txt and puts mean, standard deviation,
highest, and lowest time between checkpoints.  These data may be used to determine
if a Checkpointable is calling Checkpointer.ok() often enough.
*/
synchronized public void printCheckpointStatistics(){
    PrintWriter file = IO.getPrintWriter (filename + ".statistics.txt");
		file.println("ok() called less often than time between periodic checkpoints " +
  		excessiveTimeBetweenCheckpointOKs + " times");
    if (excessiveTimeBetweenCheckpointOKs > 0)
    	file.println("ok() should be called more often to insure checkpointing");
    file.close();
}

/**
@return the object serialized in filename
*/
synchronized protected static Checkpointer getSerialized (String filename) {
try { 
    FileInputStream f = new FileInputStream(filename);
    ObjectInputStream  s  =  new  ObjectInputStream(f);
    Checkpointer o = (Checkpointer)s.readObject();
    s.close();
    return o;
    } catch (IOException e){
        Error.fatal ("serialization error in file " + filename + ": " + e);
    } catch (ClassNotFoundException e){
        Error.fatal ("serialization error in file " + filename + ": " + e);
    }
    Error.fatal("should never get here");
    return null;
}

}

