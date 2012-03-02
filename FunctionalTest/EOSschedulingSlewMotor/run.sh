echo "testing EOSschedulingSlewMotor (also, squeeky wheel operators)"
rm -rf runDirectory
mkdir runDirectory
cd runDirectory
../../runJava.sh "gov.nasa.javaGenes.core.Checkpointer gov.nasa.javaGenes.EOSscheduling.runEOSscheduling 0 false"
cd ..
echo "log.tsd"
diff runDirectory/log.tsd correctDirectory/log.tsd
echo "initial population"
diff runDirectory/population/1000000.tsd correctDirectory/population/1000000.tsd
echo "final population"
diff runDirectory/population/1000005.tsd correctDirectory/population/1000005.tsd
echo "tasks.tsd"
diff runDirectory/ModelDirectory/tasks.tsd correctDirectory/ModelDirectory/tasks.tsd
echo "accessWindows.tsd"
diff runDirectory/ModelDirectory/accessWindows.tsd correctDirectory/ModelDirectory/accessWindows.tsd
echo "slewing"
diff runDirectory/SlewMotor_1.tsd correctDirectory/SlewMotor_1.tsd
echo "sensor use"
diff runDirectory/Sensor_1.tsd correctDirectory/Sensor_1.tsd
echo "variationOPeratorPerformance.tsd"
diff runDirectory/variationOPeratorPerformance.tsd correctDirectory/variationOPeratorPerformance.tsd
echo "tasksWithNoWindows functions"
diff runDirectory/tasksWithNoWindows.tsd correctDirectory/tasksWithNoWindows.tsd
