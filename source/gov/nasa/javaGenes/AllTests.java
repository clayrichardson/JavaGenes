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
package gov.nasa.javaGenes;
import junit.framework.*;

public class AllTests extends TestSuite {
  public static Test suite() {
    TestSuite suite = new TestSuite();
    System.out.println("gov.nasa.javaGenes unit test");


    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.HFC.SteadyStateDiscreteBreederTest.class)); 
    //if (2 > 1) {System.out.println("quit early"); return suite;}  // use to stop execution when you only want to do a few tests
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.utility.CoreUtilityTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.ChildMakerEvolvingProvider2Test.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.ChildMakerDownFractionComparatorTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.ChildMakerProviderTest.class));
	suite.addTest(new TestSuite(gov.nasa.javaGenes.core.ChildMakerTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.ChildMakerEvolvingProviderTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.DeJongFitnessFunctionsTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.TournamentLocalTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.AntiTournamentLocalTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.AntiTournamentTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.TournamentTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.PopulationTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.FitnessFunctionWorstFitnessTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.FitnessFunctionWorstFitnessManyTriesTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.CrossoverAdewayaTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.EvolvableTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.CrossoverPickOneTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.SelectTwoNeighboringPairsTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.SelectOneNeighboringPairTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.MutationInsertTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.SelectModuloTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.CrossoverTwoPointsTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.CrossoverOnePointTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.CrossoverTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.CrossoverTwoPointsEachTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.CrossoverOnePointEachTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.ProductFitnessFunctionTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.FitnessFunctionBadSizeTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.SelectOneTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.SelectFixedIndicesTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.SelectChunkTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.CrossoverIntervalTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.MutationDeleteTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.SelectFixedNumberTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.Mutation3parentsTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.SelectAllTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.MutationFixedStdDevTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.SelectByProbabilityTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.IndicesTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.EvolvableDoubleListTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.evolvableDoubleList.EvolvableDoubleTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.MultiStageFitnessFunctionTest.class));

    suite.addTest(new TestSuite(gov.nasa.javaGenes.forceFields.LowestToHighestEnergyGradualFitnessTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.forceFields.MMEFreferenceRMSTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.forceFields.crystals.SimpleCubicTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.forceFields.TwoBodyTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.forceFields.ThreeBodyTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.forceFields.MultiBodiesForOneEnergyTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.HBSS.SchedulerTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.HBSS.TaskWeightTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.HBSS.contention.AccessWindowWeightTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.HBSS.contention.TestNetwork.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.HBSS.contention.SchedulerTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.HBSS.contention.TwoSatellitesTest.class));
    // suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.CrossTrackSlewTest.class)); needs test data that was lost
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.DutyCycleConstraintTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.AvailabilityTimelineTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.EOSschedulingEvolvableTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.SensorTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.AccessWindowTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.WeightedSumFitnessTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.forceFields.ChromosomeMutationTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.forceFields.LowestToHighestEnergyFitnessTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.SSRTimelineTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.SlewTimelineTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.SlewMotorTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.weightNetwork.RouletteWheelChooseLowWeightTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.weightNetwork.RouletteWheelTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.weightNetwork.WeightListTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.RouletteWheelTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.ChangingWeightsObjectTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.DescendingWeightsComparatorTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.TimelineTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.AvailableNodeTest.class));  
    suite.addTest(new TestSuite(gov.nasa.javaGenes.forceFields.BodiesTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.forceFields.TersoffTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.forceFields.AlleleTest.class));    
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.SqueakyWheelShiftForwardTest.class));  
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.SqueakyWheelPlacedTSMTest.class));    
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.SqueakyWheelTournamentSwapMutationTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.hillClimbing.AcceptImprovementTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.hillClimbing.AcceptEqualOrBetterTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.hillClimbing.RestartAfterNChildrenWithoutImprovementTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.hillClimbing.RestartAfterNChildrenWithoutAcceptanceTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.hillClimbing.RestartEveryNChildrenTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.permutation.PermutationKCutMutationTest.class));
     suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.TaskTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.EOSModelTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.simulatedAnnealing.AccepterTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.SSRNodeTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.EarliestFirstPlacerTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.forceFields.FirstClusterLowestEnergyFitnessFunctionTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.ParametersSerializationTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.TaskAndAccessWindowGeneratorTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.STKAccessFileTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.NodeArrayTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.HorizonTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.EOSscheduling.NodeTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.chemistry.UnitCellTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.forceFields.ChromosomeXoverWithMutationTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.forceFields.StillingerWeberTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.StudentTeacherTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.forceFields.AlleleTemplateTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.permutation.PermutationPositionCrossoverTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.permutation.PermutationOrderMutationTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.FitnessMultiObjectiveTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.forceFields.ChromosomeIntervalCrossoverTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.forceFields.ForceFitnessRMSTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.chemistry.AtomTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.core.ChangeFunctionByGenerationTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.forceFields.ImmigrantsTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.forceFields.ImmigrantTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.forceFields.AssumedParametersTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.graph.VertexTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.graph.GraphTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.forceFields.StillingerWeberSiFTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.forceFields.StillingerWeberSiTest.class));
    suite.addTest(new TestSuite(gov.nasa.javaGenes.chemistry.MoleculeTest.class));

    return suite;
  }
}
