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
//  Created by Al Globus on Fri Nov 15 2002.
package gov.nasa.javaGenes.EOSscheduling;

public class SchedulingData implements java.io.Serializable {
protected int duration;
protected SlewRequirement slewRequirement;
protected Slewable slewable;
protected Sensor sensor;
protected int SSRuse;

public void setDuration(int in) {duration = in;}
public int getDuration() {return duration;}
public void setSlewRequirement(SlewRequirement in) {slewRequirement = in;}
public SlewRequirement getSlewRequirement() {return slewRequirement;}
public void setSlewable(Slewable in) {slewable = in;}
public Slewable getSlewable() {return slewable;}
public void setSensor(Sensor in) {sensor = in;}
public Sensor getSensor() {return sensor;}
public int getSSRuse() {return SSRuse;}
public void setSSRuse(int inSSRuse) {SSRuse = inSSRuse;}
}
