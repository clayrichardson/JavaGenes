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


import java.lang.CloneNotSupportedException;
import java.util.Hashtable;
import java.io.Serializable;
import gov.nasa.alsUtility.Vector3d;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.integer;
import gov.nasa.alsUtility.ExtendedVector;
import gov.nasa.javaGenes.graph.Vertex;
import gov.nasa.javaGenes.graph.Edge;
import gov.nasa.javaGenes.graph.EdgeIterator;

/**
Represent a chemical atom.
*/
public class Atom extends Vertex {
protected Element element;

public Atom(int number) {element = new Element(number);}
public Atom(Element e) {element = e;}
public Atom(String symbol) {element = Element.getElement(symbol);}
public Atom(Atom atom) {this(atom.toString());}

public boolean isElement(String symbol) {
  Element e = Element.getElement(symbol);
  if (e == null) return false;
  return element.equals(e);
}

public void translateWithNeighbors(Vector3d translation, Atom[] keepStationary) {
  translate(translation);
  for(EdgeIterator i = getEdgeIterator(); i.more(); i.next()) {
    Edge e = i.edge();
    Atom a = (Atom)e.otherVertex(this);
    if (!a.isIn(keepStationary))
      a.translate(translation);
  }
}
public boolean isIn(Atom[] array) {
  for(int i = 0; i < array.length; i++)
    if (this == array[i]) return true;
  return false;
}
/*
public Atom(TokenizeInput tokenizer) {
	super(tokenizer);
	element = new Element(tokenizer.getInteger());
}

public void stateSave(TokenizeOutput tokenizer) {
	super.stateSave(tokenizer);
	tokenizer.putInteger(element.getAtomicNumber());
}
*/ 
/**
Will adding another bond exceed valence?
*/
public boolean canAcceptEdge() {
    Error.assertTrue(this instanceof Atom);
    return getValence() > bondValenceSum();
}
/**
can all of the edges of v be connected to this?
*/
public boolean canAcceptEdgesOf(Vertex v) {
  return getValence() - bondValenceSum() >= ((Atom)v).bondValenceSum();
}
/**
Will attaching to this bond exceed valence?
@param e must be a Bond
*/
public boolean canAcceptEdge(Edge e) {
    Error.assertTrue(this instanceof Atom);
    if (e == null)
        return canAcceptEdge();
    Error.assertTrue(e instanceof Bond);
    Bond bond = (Bond)e;
    return remainingValence() >= bond.getValence();
}
/**
What portion of the valence is used by existing bonds?
*/
public int bondValenceSum() {
    int sum = 0;
    for(int i = 0; i < edges.size(); i++){
        sum += getBond(i).getValence();
    }
    return sum;
}
/**
is v exactly this?
*/
public boolean isSame (Vertex v) {
  Atom a = (Atom) v;
  return a.element.getAtomicNumber() == element.getAtomicNumber();
}

/**
Create a string to represent the extended type of this atom.
The extended type is the atomic symbol plus the number of
single, double, and triple bonds.
*/
  public String getExtendedTypeString() { 
    if (extendedTypeCache != null) return extendedTypeCache;
    Hashtable edgeCount = new Hashtable();
    StringBuffer s = new StringBuffer(getTypeString());
    for(EdgeIterator i = getEdgeIterator(); i.more(); i.next()) {
        String key = i.edge().getTypeString();
        if (edgeCount.containsKey(key)) {
            integer ii = (integer)edgeCount.get(key);
            ii.increment();
        } else
            edgeCount.put(key, new integer(1));
    }
    ExtendedVector keys = new ExtendedVector(edgeCount.keys());
    keys.sortByString();
    for (int i = 0; i < keys.size(); i++) {
         String key = (String)keys.elementAt(i);
         s.append("(");
         s.append(key);
         s.append(",");
         s.append(edgeCount.get(key));
         s.append(")");;
    }
    extendedTypeCache = s.toString();
    return extendedTypeCache;
}
/**
Represents the extended type of this atom for use as a hashtable key.
The extended type is the atomic symbol plus the number of
single, double, and triple bonds. 
<p>
This provides an efficiency advantage over the String returned by
getExtendedTypeString() when used in a hashtable but requires
a little bit of care. Don't modify these
objects in any way. This class is not thread safe
*/
protected static class ExtendedTypeKey implements Serializable {
	protected int[] bondCounts;
	protected int atomicNumber;

	// DANGEROUS: not thread safe. Avoids object creation.
	protected static ExtendedTypeKey temporary = new ExtendedTypeKey();
	protected static Hashtable permanentKeys = new Hashtable();
	// DANGEROUS: do not modify the objects returned!
	public static ExtendedTypeKey getOne(Atom atom){
		temporary.atomicNumber = atom.getAtomicNumber();
		for(int i = 0; i < temporary.bondCounts.length; i++)
			temporary.bondCounts[i] = 0;
	    for(int i = 0; i < atom.edges.size(); i++) // DANGEROUS: doesn't use iterator
	    	temporary.bondCounts[((Bond)atom.edges.elementAt(i)).getBondTypeIntegerCode()]++;		
		if (permanentKeys.containsKey(temporary))
			return(ExtendedTypeKey)permanentKeys.get(temporary);
		ExtendedTypeKey permanent = new ExtendedTypeKey(atom);
		permanentKeys.put(permanent,permanent);
		return permanent;
	}
	protected ExtendedTypeKey(){
		bondCounts = new int[Bond.getMaximumBondTypes()];
	}

	protected ExtendedTypeKey(Atom atom){
		atomicNumber = atom.getAtomicNumber();
		bondCounts = new int[Bond.getMaximumBondTypes()];
	    for(int i = 0; i < atom.edges.size(); i++) // DANGEROUS: doesn't use iterator
	    	bondCounts[((Bond)atom.edges.elementAt(i)).getBondTypeIntegerCode()]++;		
	}
	public boolean equals(Object object) {
		ExtendedTypeKey other = (ExtendedTypeKey)object;
		if (atomicNumber != other.atomicNumber)
			return false;
		for(int i = 0; i < bondCounts.length; i++)
			if (bondCounts[i] != other.bondCounts[i])
				return false;
		return true;
	}
	public int hashCode() {
    	int code = 0;
    	int shift = 0;
    	for(int i = 0; i < bondCounts.length; i++){
    		code += bondCounts[i] << shift;
    		shift += 3; // expect that bondCounts[i] < 8
    	}
    	code += atomicNumber << shift;
    	return code;
    }
}
/**
returns a ExtendedTypeKey (a internal class) to represent the extended type of this atom,
usually as a hashtable key.
The extended type is the atomic symbol plus the number of
single, double, and triple bonds. 
<p>
This provides an efficiency advantage over the String returned by
getExtendedTypeString() but requires
a little bit of care. Don't modify the
returned object in any way. And ExtendedTypeKey is not thread safe
*/
public Object getExtendedTypeObject() {return ExtendedTypeKey.getOne(this);}

/**
return the amount of valence unused by existing bonds
*/
public int remainingValence() {return getValence() - bondValenceSum();}
/**
Return the valence of this element
*/
public int getValence() {return element.getValence();}
public int getAtomicNumber() {return element.getAtomicNumber();}
/**
Return the i'th bond
*/
public Bond getBond(int i) {return (Bond)getEdge(i);}
public String toString() {return element.getSymbol();}
}
