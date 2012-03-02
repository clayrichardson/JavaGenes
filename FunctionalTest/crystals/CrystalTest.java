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
import gov.nasa.alsUtility.Error;
import gov.nasa.javaGenes.forceFields.crystals.*;
import gov.nasa.javaGenes.forceFields.*;
import gov.nasa.javaGenes.chemistry.*;

public class CrystalTest {

public static void main(String[] arguments) {
    makeLongerBonds(2,new SimpleCubic("Si",1),"SimpleCubicSmall");
    makeLongerBonds(2,new SimpleCubic("Si",2),"SimpleCubic");
    makeLongerBonds(2,new BodyCenteredCubic("Si",1),"BodyCenteredCubic");
    makeLongerBonds(3,new FaceCenteredCubic("Si",1),"FaceCenteredCubic");
    makeLongerBonds(4,new Diamond("Si",1),"Diamond");
}
public static void makeLongerBonds(double bondLength, MultiBodiesForOneEnergy mb, String baseName) {
    Molecule m = mb.getMolecule();
    m.scaleBy(bondLength);
    mb.makeMoleculeAndBodiesFiles(baseName);
}
}
