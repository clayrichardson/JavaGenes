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
package gov.nasa.javaGenes.forceFields;
import java.util.Hashtable;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.DoubleInterval;

public class StillingerWeberPartialSiF extends StillingerWeberSiF {
public StillingerWeberPartialSiF(String[] multibodyNames,
  DoubleInterval factorInterval,DoubleInterval exponentInterval,DoubleInterval cutoffInterval) {
  super(factorInterval,exponentInterval,cutoffInterval);
  String[] names = Utility.removeDuplicates(multibodyNames);
  for (int i = 0; i < names.length; i++) {
    need(names[i],i);
//    String duplicate = getDuplicate(names[i]);
//    if (!duplicate.equals(names[i]))
//      alleleSetup.add(duplicate,i);
  }
}
public void formSetup(){setIndices();}
/*
public String getDuplicate(String name) {
  if (name.equals("SiF")) {return "FSi";}
  if (name.equals("FSi")) {return "SiF";}
  if (name.equals("SiSiF")) {return "FSiSi";}
  if (name.equals("FSiSi")) {return "SiSiF";}
  if (name.equals("SiFF")) {return "FFSi";}
  if (name.equals("FFSi")) {return "SiFF";}
  return name;
}
*/

} 