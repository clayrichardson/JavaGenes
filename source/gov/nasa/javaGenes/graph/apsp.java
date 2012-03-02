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
/*
Written by W. Todd Wipke and John Lawton, Molecular
Engineering Laboratory, Department of Chemistry, University
of California, Santa Cruz, CA  95064
*/
/**
All-pairs-shortest-path algorithm and supporting functions for
graphs.

@see Graph
*/
public class apsp {
  static public void test(String args[]) {
    int[][] a;
    /* benzamadine */
    int[][] ct = {{2},{2},{0,1,3},{2,4,8},{3,5},{4,6},{5,7},{6,8},{3,7}};
    /* cyclopentane */
    /*int[][] ct = {{1,4},{0,2},{1,3},{2,4},{0,3}};*/
      
    System.out.println("Connection Table:");
    PrintIMat(ct);
    a = CTabToAdjM(ct,ct.length);
    Apsp(a);
    System.out.println("Apsp:");
    PrintIMat(a);
  }
  /**
   *   apsp - Floyd Warshall algorithm for computing All-Pairs
   *   Shortest Paths. Complexity n^3. Mem requirements n^2.
   *   Where n is the number of vertices.
   *
   *   REF: T. H. Cormen et. al. Introduction to Algorithms, (1989) CH 26.
   
   @param d is a adjacency matrix. Usually this will be set up by
   CTabToAdjM()
  */
  static public void Apsp(int[][] d) {
    int i,j,k,n;

    n = d.length;
    int count = 0;
    int maximum = d.length*d.length - d.length;
    for(k=0; k<n; k++)
    for(i=0; i<n; i++) 
	for(j=0; j<n; j++) 
	  if(d[i][j] > d[i][k] + d[k][j])
	  	d[i][j] = d[i][k] + d[k][j];
  }

  /**
   *   CTabToAdjM - Converts a connection table (CT) into an adjcency matrix.
   *   Initially all off diagonal vals are set to n+1 which is
   *   lager than the longest possible path. All diagonals are
   *   set to zero which denotes the identity, i.e. path len 
   *   is zero. Then the connection table is traversed and 
   *   adjacent nodes are set to one.
   *
   *   Algo is of the order n^2 + 4n (where 4 is max connections)
   
   @param ct a connection table. Each row (first index) is an array containing the indices
   		of the vertices that share edges with the vertex at the row index
   @param n the longest possible path in the graph (maximum is the number of vertices-1)
   the return value is a connectivity matrix suitable for use by apsp()
   */   
  static public int[][] CTabToAdjM(int[][] ct, int n) {
    int i,j,k;
    int[][] a = new int[n][n];

    /* init adj mat */
    for(i=0; i<n; i++)
    for(j=0; j<n; j++) 
	  if(i == j) a[i][j] = 0;
      else       a[i][j] = n+1;

    /* turn on adjancencys */
    for(i=0; i<n; i++)
    for(j=0; j<ct[i].length; j++) 
	  a[i][ct[i][j]] = 1;

    return(a);
  }

/**
 Print a two dimensional integer matrix
*/
  static public void PrintIMat(int[][] m) {
    int i,j;

    for(i=0; i<m.length; i++) {
      for(j=0; j<m[i].length; j++)
	    System.out.print(m[i][j] + " ");
        System.out.println(" ");
    }
  }
}


