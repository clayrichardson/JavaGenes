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


import java.io.Serializable;

/**
Classes that wish to checkpoint should implement this interface.

@see AbstractCheckpointable
*/
interface Checkpointable extends Serializable {
/**
Start the Checkpointable process for the first time.

@param arguments the array of strings past to static void main()
*/
void start(String[] arguments);
/**
Restart the process after a checkpoint and process death
*/
void restart();
/**
Remember the Checkpointer object that will checkpoint "this"
*/
void setCheckpointer(Checkpointer c);
/**
will be called just before a checkpoint.  Can be overridden
to gain control to set timers, clean up state, etc.
*/
void beforeCheckpoint();
/**
will be called just after a checkpoint.  Complements beforeCheckpoint()
*/
void afterCheckpoint();
/**
called to implement a fast checkpoint.  Serialization can also be used but it is slow.
*/
void stateSave(TokenizeOutput tokenizer);
}

