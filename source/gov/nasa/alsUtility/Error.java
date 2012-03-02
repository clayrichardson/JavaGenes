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
package gov.nasa.alsUtility;

import java.lang.RuntimeException;
import gov.nasa.alsUtility.RandomNumber;

/**
handle errors and hand out warnings.
*/
public class Error {
public static void fatal (String m) throws RuntimeException {
    System.err.println (Utility.date() + " " + m);
    fatal();
}
public static void fatal (Exception e) {
    System.err.println (Utility.date() + " " + e.toString());
    fatal();
}
public static void userAssert(boolean condition, String message) {
    if (!condition) {
        System.err.println(message);
        System.exit(1);
    }
}
private static void fatal () {
    printSeed();
    throw new RuntimeException(); // breakpoint here to catch all fatal errors
}
public static void warning ( String where, String what) {
    printSeed();
    System.out.println (Utility.date() + " " + where + ": " + what);
}
public static void mustBeImplementedBySubclass() {fatal("method must implemented by subclass");}
public static void notImplemented() {fatal("method not implemented");}
public static void notApplicable() {fatal("method not applicable");}
public static void assertTrue(boolean b) {
    assertTrue(b,"assertion failed");
}
public static void assertFalse(boolean b) {
    assertFalse(b,"assertion failed");
}
public static void assertFalse(boolean b, String message) {
    assertTrue(!b,message);
}
public static void assertFalse(String message,boolean b) {
    assertTrue(!b,message);
}
/** uses a slightly larger epsilon that Utility.nearlyEqual so EOSscheduling.HBSS.contention tests smoother */
public static void assertNearlyEqual(double a, double b) {
    assertTrue(Utility.nearlyEqual(a,b,1e-3), a + " != " + b);
}
public static void assertNearlyEqual(double a, double b, double epsilon) {
    assertTrue(Utility.nearlyEqual(a,b,epsilon), a + " != " + b);
}
public static void assertEqual(int a, int b) {
    assertTrue(a == b, a + " != " + b);
}

public static void assertTrue(boolean b,String message) {
    if (!b)
        fatal (message);
}
public static void assertTrue(String message,boolean b) {
    assertTrue(b,message);
}
public static void assertNotNull(Object object) {
    assertTrue(object != null, "null object");
}
public static void warning (String what) {
    System.out.println (Utility.date() + ": " + what);
}
/**
print out the seed for the random number generator. Can be used to exactly reproduce
a run. Text is sent to standard error.
*/
public static void printSeed(){System.err.println(" seed = " + RandomNumber.getSeed());}
}
