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
package gov.nasa.javaGenes.weightNetwork;

public class UpdateAndPropagateList extends gov.nasa.alsUtility.GrowOnlyArray implements java.io.Serializable {
protected int which = 1;
static final public int INITIAL_SIZE = 100;
static final public int GROW_BY = 100;

public UpdateAndPropagateList() {this(1);}
/**
used when same objects in multiple lists with different processing
*/
public UpdateAndPropagateList(int inWhich) {
    super(INITIAL_SIZE,GROW_BY);
    which = inWhich;
}

public void updateAndPropagate() {
    for(int i = 0; i < size(); i++)
        getUpdateAndPropagatable(i).startUpdateAndPropagate();
    for(int i = 0; i < size(); i++) {
        UpdateAndPropagatable u = getUpdateAndPropagatable(i);
        if (u.mustToUpdateAndPropagate()) {
            u.updateAndPropagate(which);
            u.updateAndPropagateDone();
        }
    }
    reinitialize();
}
public UpdateAndPropagatable getUpdateAndPropagatable(int i) {
    return (UpdateAndPropagatable)get(i);
}
}