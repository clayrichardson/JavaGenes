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
/*
Written by W. Todd Wipke and Richard McClellan, Molecular
Engineering Laboratory, Department of Chemistry, University
of California, Santa Cruz, CA  95064
*/

import java.util.*;
import java.io.*;
import java.util.Hashtable;
import java.lang.Character;
import java.io.Serializable;
import java.io.BufferedReader;
import gov.nasa.alsUtility.Error;

/**
  represents elements in the periodic table.
  Normally only one object per element will ever be needed.
 */
public class Element  implements Serializable {    
    private int atomicNumber= 0; // not -1 cause it makes trouble for arrays :-)
    private double atomicWeight;
    private String symbol;
    private int group;
    private int period;
    private int valence;
    
    private static final int TABLEN= 40;
    private static Element[] elementAr= new Element[TABLEN];
    private static Hashtable elementHashtable;    								
    private static boolean DIAGNOSTICS= false;    
    

    public Element() {}
    public Element(int atomicNumber) {
    	this.atomicNumber= atomicNumber;
    }
    
	public Element getElement(int atomicNumber) {
    	if (DIAGNOSTICS) {
        	System.out.println("In getElement(int atomicNumber).");
            System.out.println("  Value of atomicNumber is "+atomicNumber);
        }
    	Element element= new Element();
        this.atomicNumber= atomicNumber;
        return element;
    }
	public static Element getElement(String symbolOrName) {
    	if (DIAGNOSTICS) {
        	System.out.println("In getElement(String symbolOrName)");
        	System.out.println("  value of symbolOrName is "+symbolOrName);
        }
        symbolOrName= correctSymbol(symbolOrName);
        if (elementHashtable == null) {
        	if (DIAGNOSTICS) {
        		System.out.println("hashtable being initialized!");
            }
        	buildAtomicNumberTable();
        }        
        return (Element)elementHashtable.get(symbolOrName);
    }
    public int getAtomicNumber() {
    	    return atomicNumber;
	}
	public int getValence() {
            return valenceAr[atomicNumber];
	}
	public String getSymbol() {
    	    return symbolAr[atomicNumber];
	}
	public double getAtomicWeight() {
    	    return atomicWeightAr[atomicNumber];
	}
	public int getPeriod() {
    	    return periodAr[atomicNumber];
	}
	public int getGroup() {
    	    return groupAr[atomicNumber];
	}
	public String toString() {
    	return getSymbol();
    }
	public boolean equals(Element element) {
    	return getAtomicNumber() == element.getAtomicNumber();
    }
	public int hashCode() {
    	return getAtomicNumber();
    }
    
 	/**
 	perform alchemy, transmute this element. Not recommended.
 	*/
    public void setAtomicNumber(int atomicNumber) {
    	this.atomicNumber= atomicNumber;
    }

    public static void initPeriodicTable() {
    	if (DIAGNOSTICS) {
        	System.out.println("in initPeriodicTable");
        }
    	for (int anum= 0; anum < symbolAr.length; anum++) {
        	Element element= new Element();
        	element.atomicNumber= anum;
            element.atomicWeight= atomicWeightAr[anum];
            element.symbol= symbolAr[anum];
            element.group= groupAr[anum];
            element.period= periodAr[anum];
            element.valence= valenceAr[anum];
            elementAr[anum]= element;
        }
    }      

    private static void buildAtomicNumberTable() {
    	elementHashtable= new Hashtable(223); // 223 is a nice prime number
        if (elementAr == null) {
        	System.out.println("Error! elementAr has not been initialized!");
            Error.fatal("Element: elementAr has not been initialized!");
        }
        for (int i= 0; i < symbolAr.length; i++ ) {
        	elementHashtable.put(symbolAr[i], new Element(i));
        }
    }
	private static String correctSymbol(String symbol) {//converts to correct case
        if (DIAGNOSTICS) {
        	System.out.println("In correctSymbols.");
            System.out.println("Correcting capatalization of "+symbol);
        }
        symbol= symbol.toUpperCase(); // cap all chars
        if (DIAGNOSTICS) {
	        System.out.println("all caps symbol is "+symbol);        
        }
        if (symbol.length() > 1) {
        	if (DIAGNOSTICS) {
				System.out.println("Symbol contains more than 1 character.");
            }    
        	// copy first (correctly capped) char into new string
            String corrected_symbol= new String(symbol.substring(0,1));
            // make sure second letter is lower case (always)
            Character tmp= new Character(symbol.charAt(1));
            corrected_symbol+= tmp.toLowerCase(symbol.charAt(1));
            return corrected_symbol;
        }
        return symbol;
    }
        	
    
    /* driver and internal printing routines ****/
    public static void test(String args[]) throws IOException{
		BufferedReader in= new BufferedReader(new InputStreamReader(System.in));
        System.out.println("enter an element symbol to see details of that element.");
        System.out.println("type 'quit' to exit.");
        initPeriodicTable();
        while (true) {        	
        	System.out.print("> ");
            String line= in.readLine();
            if ((line== null) || (line.equals("quit")) ) {
            	initPeriodicTable();
                dumpElementArray();
            	break;
            }
            Element element= getElement(line);
            dumpElement(element);
        }           
        System.out.println("Exiting the Element class self-contained driver normally!");
    }
	private static void dumpElement(Element element) {
        if (DIAGNOSTICS) {
        	System.out.println("In dumpElement.");
        }
        if (element != null) {
            System.out.println("  symbol:         "+element.getSymbol());
            System.out.println("  atomic number:  "+element.getAtomicNumber());
            System.out.println("  valence:        "+element.getValence());
            System.out.println("  atomic weight:  "+element.getAtomicWeight());
            System.out.println("  period:         "+element.getPeriod());
            System.out.println("  group:          "+element.getGroup());
		}
        System.out.println();
    }    
    private static void dumpElementArray() {
    	if (DIAGNOSTICS) {	        	
    		System.out.println("In dumpElementArray.");
        }
        Element element= new Element();
        for (int i= 0; i < symbolAr.length; i++) {
        	element= elementAr[i];
            dumpElement(element);
        }
    }   

    /**** the periodic table of the elements ****/
    private static final String[] symbolAr= 
    {"UNK",
    "H",                                                                                                "He", 
    "Li","Be",                                                             "B",  "C", "N",   "O",  "F", "Ne",
    "Na","Mg",                                                            "Al", "Si", "P",   "S", "Cl", "Ar",
    "K", "Ca", "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co", "Ni", "Cu", "Zn", "Ga", "Ge", "As", "Se", "Br", "Kr",
             };
    private static final double[] atomicWeightAr= 
    {-1,   
    1.008,                                                                                                4.003, 
    6.941,9.012,                                                            10.81,12.01,14.01,16.00,19.00,20.18,
    22.99,24.31,                                                            26.98,28.09,30.97,32.07,35.45,39.95,
    29.10,40.08,44.96,47.90,50.94,52.00,54.94,55.85,58.93,58.71,63.55,65.37,69.72,72.60,74.92,78.96,79.90,83.80 };
    private static final int[] groupAr= 
    {-1,    
    1,                                                                                     18, 
    1,   2,                                                       13,  14,  15,  16,  17,  18,
    1,   2,                                                       13,  14,  15,  16,  17,  18,
    1,   2,   3,   4,   5,   6,   7,   8,   9,   10,   11,   12,  13,  14,  15,  16,  17,  18 }; 
    private static final int[] periodAr= 
    {-1,    
    1,                                                                                     1, 
    2, 2,                                                         2,   2,   2,   2,   2,   2,
    3, 3,                                                         3,   3,   3,   3,   3,   3,
    4,   4,   4,   4,   4,   4,   4,   4,   4,    4,    4,    4,  4,   4,   4,   4,   4,   4 };

    private static final int[] valenceAr= 
    {-1,    
    1,                                                                                     0,
    1,   2,                                                       3,   4,   3,   2,    1,  0,
    1,   2,                                                       3,   4,   5,   6,    1,  0,
    1,   2,  3,   -1,  -1,  -1,  -1,  -1,  -1,   -1,   -1,   -1,   3,  -1,  -1,  -1,   -1,  0 };
	   


}

