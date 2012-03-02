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
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Enumeration;
import java.lang.Math;
import gov.nasa.alsUtility.Utility;
import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.ExtendedVector;
import gov.nasa.alsUtility.Predicate;
import gov.nasa.alsUtility.Procedure;
import gov.nasa.alsUtility.SetMark;
import gov.nasa.alsUtility.IsMarked;
import gov.nasa.alsUtility.KeyCounter;
import gov.nasa.javaGenes.core.Evolvable;

/**
A information theoretic graph, in other words, a set of vertices and a set of edges.
Each edge connects two vertices. This implementation supports genetic software with
a crossover operator and a distance function that uses all-pairs-shortest-path.

@see Vertice
@see Edge
@see apsp
*/
public class Graph extends Evolvable {
protected ExtendedVector vertices = new ExtendedVector();
protected ExtendedVector edges = new ExtendedVector();

/**
cache the results of all-pairs-shortest-path since this operation is compute intensive.
*/
protected KeyCounter nodeTypePairsShortestTrailCounts = null;

public Graph() {;}
/**
copy the graph and make copies of all edges and vertices
*/
public Graph deepCopyGraph() {
    try {
        return (Graph)clone();
    } catch (CloneNotSupportedException e) {
        Error.fatal("can't clone object: " + e);
        return null;
    }
}
/**
copy the graph and make copies of all edges and vertices

@exception CloneNotSupportedException
*/
public Object clone() throws CloneNotSupportedException {
    Graph graph = (Graph)super.clone();
    graph.vertices = new ExtendedVector();
    graph.edges = new ExtendedVector();
    graph.nodeTypePairsShortestTrailCounts = null;
    for(VertexIterator i = getVertexIterator(); i.more(); i.next())
        graph.add((Vertex)i.vertex().clone());
    for(EdgeIterator i = getEdgeIterator(); i.more(); i.next())
        graph.add((Edge)i.edge().clone());
    for(int i = 0; i < vertices.size(); i++) {
        Vertex vOld = getVertex(i);
        Vertex vNew = graph.getVertex(i);
        for(EdgeIterator e = vOld.getEdgeIterator(); e.more(); e.next())
            vNew.add(graph.getEquivalent(this,e.edge()));
    }
    numberVertices(0);
    for(int i = 0; i < edges.size(); i++){
        Edge edge = getEdge(i);
        Vertex v0 = graph.getVertex(edge.getVertex(0).getNumber());
        Vertex v1 = graph.getVertex(edge.getVertex(1).getNumber());
        graph.getEdge(i).setVertices(v0,v1);
    }
    return graph;
}
/**
copy the graph and don't make copies of all edges and vertices

@exception CloneNotSupportedException
*/
public Object shallowClone() throws CloneNotSupportedException {
    Graph graph = (Graph)super.clone();
    graph.vertices = new ExtendedVector();
    graph.edges = new ExtendedVector();
    graph.nodeTypePairsShortestTrailCounts = null;
    return graph;
}
public String toString() {
    StringBuffer s = new StringBuffer();
    numberVertices(1);
    s.append("(");
    for(VertexIterator i = getVertexIterator(); i.more(); i.next())
        s.append(i.vertex()).append(",");
    s.setLength(s.length()-1);
    s.append(")");
    for(EdgeIterator i = getEdgeIterator(); i.more(); i.next()){
        Edge e = i.edge();
        s.append("(");
        s.append(e.getVertex(0).getNumber());
        s.append(",");
        s.append(e.getVertex(1).getNumber());
        s.append(",");
        s.append(e);
        s.append(")");
    }
    return s.toString();
}    

/**
Generates a random graph. First creates a random spanning tree then randomly inserts
numberOfCycles edges.

@param provider will provide random edges and vertices for the graph
*/
public Graph(VertexAndEdgeProvider provider, int numberOfVertices, int numberOfCycles) {
    // generate spanning tree
    add(provider.getVertex());
    AddVertex v = new AddVertex(provider);
    for(int i = 1; i < numberOfVertices; i++)
      if (v.makeChild(this) == null)
      	return;

    // put in cycles
   AddEdge e = new AddEdge(provider);
   for(int j = 0; j < numberOfCycles; j++)
      if (e.makeChild(this) == null)
      	return;
}
public boolean hasVertexPairCloserThan(double distance) {
    for(int i = 0; i < vertices.size(); i++)
    for(int j = i+1; j < vertices.size(); j++) {
      Vertex a = (Vertex)vertices.elementAt(i);
      Vertex b = (Vertex)vertices.elementAt(j);
      if (a.getDistanceTo(b) < distance)
        return true;
    }
    return false;
}
private class AddVertexToGraph implements Procedure {
	protected Graph graph;
	public AddVertexToGraph(Graph g) {graph = g;}
	public void execute(Object object) {graph.add((Vertex)object);}
	}
private class AddEdgeToGraph implements Procedure {
	protected Graph graph;
	public AddEdgeToGraph(Graph g) {graph = g;}
	public void execute(Object object) {graph.add((Edge)object);}
}
/**
Walks the graph starting at Vertex v adding copies of all edges and
vertices to the new graph.

@see Mark
*/
public Graph getConnectedSubgraph(Vertex v) {
	Graph graph = (Graph)Utility.newInstance(this);
  setAllMarks(false);
  v.walkAll(new AddVertexToGraph(graph), new AddEdgeToGraph(graph));
  return graph;
} 
/**
@return the shortest path between vertices v1 and v2.
*/ 
public Trail getTrailBetween(Vertex v1, Vertex v2) {
    for(VertexAndTrailIterator i = getVertexAndTrailIterator(v1); i.more(); i.next())
        if (i.vertex().equals(v2))
            return i.trail();
    return null;
}
/**
@return a new iterator
*/
public VertexAndTrailIterator getVertexAndTrailIterator(Vertex v) {
    return new VertexAndTrailIterator(this,v);
}
/**
@return the Tanimoto distance between this and Evolvable other (which must be a graph).
Uses all-pairs-shortest-path on extended vertex types for the distance measure. 0 is
closest, 1 is farthest away.
*/
public double distanceFrom(Evolvable other) {
    KeyCounter myPairs = getNodeTypePairsShortestTrailCounts();
    Graph graph = (Graph)other;
    KeyCounter otherPairs = graph.getNodeTypePairsShortestTrailCounts();
    return myPairs.tanimotoDistance(otherPairs);
}
/**
@return Uses all-pairs-shortest-path on extended vertex types for the distance measure.
*/
public KeyCounter getNodeTypePairsShortestTrailCounts(){
	if (nodeTypePairsShortestTrailCounts != null)
		return nodeTypePairsShortestTrailCounts;
  nodeTypePairsShortestTrailCounts = new KeyCounter();
	numberVertices(0);
    int[][] table = getConnectionTable();
    int[][] matrix = apsp.CTabToAdjM(table,table.length);
    apsp.Apsp(matrix);
    Object[] keys = new Object[getVerticesSize()];
    for(int i = 0; i < keys.length; i++)
    	keys[i] = getVertex(i).getExtendedTypeObject();
    for(int i = 0; i < matrix.length; i++){
    	for(int j = i + 1; j < matrix.length; j++){
    		Object key = TrailKey.getOne(keys[i],keys[j],matrix[i][j]);
            nodeTypePairsShortestTrailCounts.add(key);
    	}
    }
    return nodeTypePairsShortestTrailCounts;
}
/**
@return data structure containing counts of extended vertex types
*/
public KeyCounter getExtendedVertexTypesCounter() {
	// PERFORMANCE: cache
	KeyCounter counter = new KeyCounter();
	for(VertexIterator i = getVertexIterator(); i.more(); i.next())
		counter.add(i.vertex().getExtendedTypeObject());
	return counter;
}	

/*
public Hashtable getNodeTypePairsShortestTrailCounts(){
	if (nodeTypePairsShortestTrailCounts != null)
		return nodeTypePairsShortestTrailCounts;
    nodeTypePairsShortestTrailCounts = new Hashtable();
	numberVertices(0);
    int[][] table = getConnectionTable();
    int[][] matrix = apsp.CTabToAdjM(table,table.length);
    apsp.Apsp(matrix);
    Object[] keys = new Object[getVerticesSize()];
    for(int i = 0; i < keys.length; i++)
    	keys[i] = getVertex(i).getExtendedTypeObject();
    for(int i = 0; i < matrix.length; i++){
    	for(int j = i + 1; j < matrix.length; j++){
    		Object key = TrailKey.getOne(keys[i],keys[j],matrix[i][j]);
    		//Object key = getStringKey(getVertex(i),getVertex(j),matrix[i][j]);
            if (nodeTypePairsShortestTrailCounts.containsKey(key)) {
                integer i1 = (integer)nodeTypePairsShortestTrailCounts.get(key);
                i1.increment();
            } else
                nodeTypePairsShortestTrailCounts.put(key, new integer(1));
    	}
    }
    return nodeTypePairsShortestTrailCounts;
}
*/
/*
public Hashtable oldSlowGetNodeTypePairsShortestTrailCounts(){
    Hashtable hashtable = new Hashtable();
    for(VertexIterator i1 = getVertexIterator(); i1.more(); i1.next()) {
        Vertex v1 = i1.vertex();
        for(VertexAndTrailIterator i2 = getVertexAndTrailIterator(v1); i2.more(); i2.next()) {
            String key = i2.trail().getStringKey();
            if (hashtable.contains(key)) {
                integer i = (integer)hashtable.get(key);
                i.increment();
            } else
                hashtable.put(key, new integer(1));
        }
    }
    for (Enumeration e = hashtable.elements(); e.hasMoreElements();)
        ((integer)e.nextElement()).divideBy(2); // each pair of vertices gets counted twice
    return hashtable;
}
*/
/**
@return a connection table suitable for apsp.
Assumes vertices are already numbered as desired.

@see apsp
*/
public int[][] getConnectionTable() {
	int[][] table = new int[getVerticesSize()][];
	int[] connections = new int[getVerticesSize()];
    for(VertexIterator it = getVertexIterator(); it.more(); it.next()){
    	Vertex v = it.vertex();
    	int numberOfConnections = 0;
    	for(EdgeIterator i = v.getEdgeIterator(); i.more(); i.next()){
        	Vertex other = i.edge().otherVertex(v);
        	connections[numberOfConnections++] = other.getNumber();
        }
        int[] finalConnections = new int[numberOfConnections];
        System.arraycopy(connections,0,finalConnections,0,numberOfConnections);
        table[v.getNumber()] = finalConnections;
    }
    return table;
}
/**
An object that efficiently represents a path for use in as a key Hashtables and KeyCounters.
*/
protected static class TrailKey implements Serializable {
	protected Object end1;
	protected Object end2;
	protected int length;

	// DANGEROUS: not thread safe. Avoids object creation.
	protected static TrailKey temporary = new TrailKey();
	protected static Hashtable permanentKeys = new Hashtable();
	/**
	DANGEROUS: do not modify the objects returned!
	*/
	public static TrailKey getOne(Object v1,Object v2,int count){
		temporary.end1 = v1;
		temporary.end2 = v2;
		temporary.length = count;
		if (permanentKeys.containsKey(temporary))
			return(TrailKey)permanentKeys.get(temporary);
		TrailKey permanent = new TrailKey(v1,v2,count);
		permanentKeys.put(permanent,permanent);
		return permanent;
	}
	protected TrailKey(){}

	public TrailKey(Object v1,Object v2,int count){
		end1 = v1;
		end2 = v2;
		length = count;
	}
	public boolean equals(Object object) {
		TrailKey other = (TrailKey)object;
		return length == other.length
			   && (   (end1.equals(other.end1) && end2.equals(other.end2))
			   	   || (end1.equals(other.end2) && end2.equals(other.end1)));
	}
	public int hashCode() {
    	return end1.hashCode() * end2.hashCode() * length;
    }
}
/**
Represents a path with a string. Not used due to inefficiency but useful
for debugging.
*/
public String getStringKey(Vertex v1,Vertex v2,int count){
    final String to = " to ";
    String s1 = v1.getExtendedTypeString();
    String s2 = v2.getExtendedTypeString();
    StringBuffer r = new StringBuffer();
    if (s1.compareTo(s2) < 0) {
    	r.append(s1);
    	r.append(to);
    	r.append(s2);
    } else { 
        r.append(s2);
    	r.append(to);
    	r.append(s1);
	}
	r.append(" distance ");
	r.append(count);
    return r.toString();
}
/**
@return true if the graph is completely connected (every pair of edges connected by
at least one path)
*/
public boolean isConnected() {
    if (vertices.size() == 0) return true;
    setVertexMarks(false);
    getVertex(0).walkVertices(new SetMark(true));
    return !vertices.check(new IsMarked(false));
}
/**
can I perform crossover with graph?
*/
public boolean canMate(Graph graph) {return true;}

/**
 Must be called when the graph changes. Used to implement caching of
 data structures representing aspects of the graph that are compute intensive
 to create.
*/
protected void changed() {nodeTypePairsShortestTrailCounts = null;}
/**
set marks on all edges and vertices
@see Mark
*/
public void setAllMarks(boolean m) {setVertexMarks(m); setEdgeMarks(m);}
/**
add actual (not copies) vertices and edges of g to this. 
Caller can deepCopyGraph() before calling if desired
*/
public void add(Graph g) {
    vertices.addVector(g.vertices);
    edges.addVector(g.edges);
    changed();
}
/**
@return number of vertices plus number of edges
*/
public int getSize() {return vertices.size() + edges.size();}
public int getNumberOfCycles() {return edges.size() - vertices.size() + 1;}

public int getVerticesSize() {return vertices.size();}
public void add(Edge e) {edges.addElement(e); changed();}
/**
Removes Edge e from graph.

@see #delete
*/
public void remove(Edge e) {Error.assertTrue(edges.removeElement(e)); changed();}
/**
@see Mark
*/
public void setEdgeMarks(boolean m) {edges.executeOnAll(new SetMark(m));}
public EdgeIterator getEdgeIterator() {return new EdgeIterator(edges);}
public Edge getRandomEdge() {return (Edge)edges.getRandomElement();}
/**
@return a random edge that satisfies Predicate p
*/
public Edge getRandomEdge(Predicate p) {return (Edge)edges.getRandomElement(p);}
/**
@return the ith edge. Start counting at 0.
*/
public Edge getEdge(int i) {return  (Edge)edges.elementAt(i);}
public void executeOnEdges(Procedure p) {edges.executeOnAll(p);}
/**
@return the equivalent to Edge edge in Graph graph. Graph graph must be isomorphic to this.
*/
public Edge getEquivalent(Graph graph, Edge edge) {
	int index = graph.edges.indexOf(edge);
	Error.assertTrue(index != -1);
    return getEdge(index);
}
/**
Removes Edge e from graph, removes the vertices from e, and removes e from its vertices.

@see Edge#removeVertices
*/
public void delete(Edge e) {
    remove(e);
    e.removeVertices();
    changed();
}
public void replaceEdge(Edge oldEdge,Edge newEdge) {
	int index = edges.indexOf(oldEdge);
  newEdge.stealVertices(oldEdge);
  edges.setElementAt(newEdge, index);
  changed();
}
public void replaceVertex (Vertex oldVertex, Vertex newVertex) {
  int index = vertices.indexOf (oldVertex);
  newVertex.stealEdges (oldVertex);
  vertices.setElementAt (newVertex, index);
  changed ();
}
public int getEdgesSize() {return edges.size();}
public void add(Vertex v) {vertices.addElement(v); changed();}
/**
Removes Vertex v from graph.
*/
public void remove(Vertex v) {Error.assertTrue(vertices.removeElement(v)); changed();}
public void removeLastNVertices(int n) {
    Error.assertTrue(getVerticesSize() >= n);
    for(int i = 0; i < n; i++)
        vertices.remove(getVerticesSize()-1);
}
    
/**
@see Mark
*/
public void setVertexMarks(boolean m) {vertices.executeOnAll(new SetMark(m));}
public VertexIterator getVertexIterator() {return new VertexIterator(vertices);}
public Vertex getRandomVertex() {return (Vertex)vertices.getRandomElement();}
/**
@return random vertex that satisfies Predicate p
*/
public Vertex getRandomVertex(Predicate p) {return (Vertex)vertices.getRandomElement(p);}
/**
@return the ith vertex starting at 0
*/
public Vertex getVertex(int i) {return (Vertex)vertices.elementAt(i);}
/**
Execute Procedure p on all vertices
*/
public void executeOnVertices(Procedure p) {vertices.executeOnAll(p);}
/**
@return the equivalent to Vertex vertex in Graph graph. Graph graph must be isomorphic to this.
For better performance use vertex numbering.
*/
public Vertex getEquivalent(Graph graph, Vertex vertex) {
	int index = graph.vertices.indexOf(vertex);
	if (index == -1)
  	return null;
  else
    return getVertex(index);
}
/**
Number the vertices starting with int start.

@see Vertex#setNumber
*/
public void numberVertices(int start) {
    for(int i = 0; i < vertices.size(); i++)
         getVertex(i).setNumber(start++);
}
public void numberVerticesReverse(int start) {
		int number = vertices.size() + start - 1;
    for(int i = 0; i < vertices.size(); i++)
         getVertex(i).setNumber(number--);
}
public boolean isLegal() {return true;}
}
