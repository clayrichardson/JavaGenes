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
package gov.nasa.javaGenes.chemistry;


public class Purine extends Molecule {

public Purine() {
    for(int i = 0; i < 5; i++)
        add(new Atom(6));
    for(int i = 0; i < 4; i++)
        add(new Atom(7));

    // aromatic ring 1
    makeBond(1, 2, 1);
    makeBond(2, 3, 2);
    makeBond(3, 6, 1);
    makeBond(6, 4, 2);
    makeBond(4, 7, 1);
    makeBond(7, 1, 2);

    // aromatic ring 2
    makeBond(2, 8, 1);
    makeBond(8, 5, 1);
    makeBond(5, 9, 2);
    makeBond(9, 3, 1);
}
}
