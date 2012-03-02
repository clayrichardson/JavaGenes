echo "testing DutyCyleTest"
rm -rf runDirectory
mkdir runDirectory
cd runDirectory
../../runJava.sh "gov.nasa.javaGenes.core.Checkpointer gov.nasa.javaGenes.EOSscheduling.runEOSscheduling 0 false"
cd ..
echo "log.tsd"
diff runDirectory/log.tsd correctDirectory/log.tsd
echo "Sensor_1.tsd"
diff runDirectory/Sensor_1.tsd correctDirectory/Sensor_1.tsd
