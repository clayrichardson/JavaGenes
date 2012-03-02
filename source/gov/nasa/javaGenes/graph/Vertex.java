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
package gov.nasa.javaGenes.graph;

import java.lang.CloneNotSupportedException;
import java.util.Hashtable;
import java.lang.Math;
import gov.nasa.alsUtility.Vector3d;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.ExtendedVector;
import gov.nasa.alsUtility.Procedure;
import gov.nasa.alsUtility.integer;

/**
a vertex in a graph. A vertex may have any number of edges connected to it.

@see Graph
@see Edge
*/
public class Vertex extends GraphElement {
protected ExtendedVector edges = new ExtendedVector();
protected double[] xyz = new double[3];
protected String extendedTypeCache = null;
/**
 used to number vertices for various purposes
*/
protected int number = -1;
public Vertex() {}
//public Vertex(TokenizeInput tokenizer) {}
//public void stateSave(TokenizeOutput tokenizer){}
/**
deletes cached information. Call when the vertex changes state.
*/
public void stateChange() {
	extendedTypeCache = null;
}
/**
don't make copies of the edges
*/
public Vertex shallowCopyVertex() {
    try {
        return (Vertex)clone();
    } catch (CloneNotSupportedException e) {
        Error.fatal("can't clone object: " + e);
        return null;
    }
}
/**
don't make copies of the edges
*/
public Object clone() throws CloneNotSupportedException {
    Vertex vertex = (Vertex)super.clone();
    vertex.xyz = new double[3];
    vertex.setXyz(xyz);
    vertex.edges = new ExtendedVector();
    vertex.extendedTypeCache = null;
    vertex.number = -1;
    return vertex;
}
public boolean isSame (Vertex v) {
  return getClass () == v.getClass();
}
public void translate(Vector3d translation) {
  xyz[0] += translation.getX();
  xyz[1] += translation.getY();
  xyz[2] += translation.getZ();
}
public Vector3d getLocationVector() {
  return new Vector3d(xyz[0],xyz[1],xyz[2]);
}
/**
assumes that only already walked vertices are marked true
@see Mark
*/
public void walkVertices(Procedure p) {
    Error.assertTrue(isMarked(false));
    p.execute(this);
    setMark(true);
    for(EdgeIterator i = getEdgeIterator(); i.more(); i.next()) {
        Edge e = i.edge();
        Vertex v = e.otherVertex(this);
        if (v.isMarked(false)) v.walkVertices(p);
    }
}
/**
assumes that only already walked vertices and edges are marked true
@see Mark
*/
public void walkAll(Procedure vertexProcedure, Procedure edgeProcedure) {
    Error.assertTrue(isMarked(false));
    vertexProcedure.execute(this);
    setMark(true);
    for(EdgeIterator i = getEdgeIterator(); i.more(); i.next()) {
        Edge e = i.edge();
        if (e.isMarked(false)) {
            edgeProcedure.execute(e);
            e.setMark(true);
            Vertex v = e.otherVertex(this);
            if (v.isMarked(false))
            	v.walkAll(vertexProcedure,edgeProcedure);
        }
    }
}
/**
@return true if an edge connects this an v
*/
public boolean hasEdgeTo(Vertex v) {
    for(EdgeIterator i = getEdgeIterator(); i.more(); i.next())
        if (i.edge().doesConnect(v))
            return true;
    return false;
}
/**
@return a string suitable as a hashtable key for the extended type of this vertex. The
extended type is the type of the vertex (usually found in a subclass) and the types of
all incident edges. The return value is cached for future use without recalculation.
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
@return an object suitable as a hashtable key for the extended type of this vertex. The
extended type is the type of the vertex (usually found in a subclass) and the types of
all incident edges. Not currently implemented. Atom does implement this.
@see Atom
*/
public Object getExtendedTypeObject() {Error.notImplemented(); return null;}
public Edge getEdge(int i) {return (Edge)edges.elementAt(i);}
public void add(Edge edge) {edges.addElement(edge); stateChange();}
public void removeEdge(Edge edge) {edges.removeElement(edge); stateChange();}
public void stealEdges (Vertex v) {
  for (int i = v.edges.size()-1; i >= 0; i--) {
    Edge edge = v.getEdge (i);
    v.removeEdge (edge);
    add (edge);
    edge.replaceVertex (v, this);
  }
}
public boolean canAcceptEdgesOf(Vertex v) {return true;}
public EdgeIterator getEdgeIterator() {return new EdgeIterator(edges);}
/**
@return true if another edge can be added
*/
public boolean canAcceptEdge() {return canAcceptEdge(null);}
/**
@return true. Subclasses should return true if Edge e can be added
*/
public boolean canAcceptEdge(Edge e) {return true;}
public String getTypeString() {return toString();}
public String toString() {return "vertex";}
/**
@return number
*/
public int getNumber() {return number;}
/**
@set number to n
*/
public void setNumber(int n) {number = n;}
/**
get the physical location of the vertex
@return an array of length 3
*/
public double[] getXyz() {return xyz;}
public void scaleBy(double factor) {
    for(int i = 0; i < xyz.length; i++)
        xyz[i] *= factor;
}
public void moveInsideInterval(int dimension, double low, double high) {
    Error.assertTrue(low < high);
    double value = xyz[dimension];
    if (low <= value && value <= high)
        return;
    double interval = high - low;
    while(value > high)
        value -= interval;
    while(value < low)
        value += interval;
    // deal with round off problems
    if (value > high) value = high;
    if (value < low) value = low;
    xyz[dimension] = value;
}
/**
set the physical location of the vertex
@param replace an array of length 3
*/
public void setXyz(double[] replace) {
	Error.assertTrue(xyz.length == replace.length);
	for(int i = 0; i < xyz.length; i++){
		xyz[i] = replace[i];
	}
}
/**
set the physical location of the vertex
*/
public void setXyz(double x, double y, double z) {
		xyz[0] =  x;
		xyz[1] =  y;
		xyz[2] =  z;
}
/**
@return the Euclidian distance to v
*/
public double getDistanceTo(Vertex v) {
  double xDistance = xyz[0] - v.xyz[0];
  double yDistance = xyz[1] - v.xyz[1];
  double zDistance = xyz[2] - v.xyz[2];
  return Math.sqrt(xDistance*xDistance + yDistance*yDistance + zDistance*zDistance);
}
public double getAngleBetween(Vertex v1, Vertex v2) {
  Error.assertTrue(this != v1);
  Error.assertTrue(this != v2);
  Error.assertTrue(v2 != v1);
  // use triangle equality: a^2 = b^2 + c^2 = 2bc cos(alpha)
  double a =   v1.getDistanceTo(v2);
  double b = this.getDistanceTo(v1);
  double c = this.getDistanceTo(v2);
  if (b == 0 || c == 0)
    return java.lang.Double.NaN;
  double number = (a*a - b*b - c*c) / (-2*b*c);
  if (number < -1) number = -1;
  if (number >  1) number =  1;
  return Math.acos(number);
}
}
