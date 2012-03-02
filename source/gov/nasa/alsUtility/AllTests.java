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
import junit.framework.*;

public class AllTests extends TestSuite {
  public static Test suite() {
    TestSuite suite = new TestSuite();
    System.out.println("gov.nasa.alsUtility unit test");



    suite.addTest(new TestSuite(gov.nasa.alsUtility.AmericanWireGaugeTest.class));
    //if (2 > 1) {System.out.println("quit early"); return suite;} // use to stop execution when you only want to do a few tests
    suite.addTest(new TestSuite(gov.nasa.alsUtility.UtilityTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.Brick3dWithSegmentsTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.Brick3dTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.DoubleIntervalTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.Vector3dTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.LogComparisonsTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.IntegerIntervalTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.VecMathTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.KeyCounterTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.MathUtilityTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.WeightedStatisticsTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.ExtendedVectorTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.ExtendedTreeSetTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.ComparativeStatisticsTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.GrowOnlyArrayTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.ReinitializableFloatWithFactorTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.ReinitializableFloatTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.ReinitializableIntTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.RandomNumberTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.SampleTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.ObjectCacheTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.DoublesListTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.PropertiesListTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.MinMaxesTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.IOTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.FieldRecordTextTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.RootMeanSquaresTest.class));
    suite.addTest(new TestSuite(gov.nasa.alsUtility.DoubleIntervalTest.class));

    return suite;
  }
}
