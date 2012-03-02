echo "testing DeJongAndHFC"
rm -rf runDirectory
mkdir runDirectory
cd runDirectory
../../runJava.sh "gov.nasa.javaGenes.evolvableDoubleList.DeJongExperiment.Main"
cd ..
echo "log.tsd"
diff runDirectory/log.tsd correctDirectory/log.tsd
echo "breederReport.tsd"
diff runDirectory/breederReport.tsd correctDirectory/breederReport.tsd
echo "parameters.txt"
diff runDirectory/parameters.txt correctDirectory/parameters.txt
echo "population/1000010.tsd"
diff runDirectory/population/1000010.tsd correctDirectory/population/1000010.tsd
echo "population/1000050.tsd"
diff runDirectory/population/1000050.tsd correctDirectory/population/1000050.tsd
echo "population/1000100.tsd"
diff runDirectory/population/1000100.tsd correctDirectory/population/1000100.tsd
echo "variationOPeratorPerformance.tsd"
diff runDirectory/variationOperatorPerformance.tsd correctDirectory/variationOperatorPerformance.tsd
echo "variationOPeratorPerformance/1000001.tsd"
diff runDirectory/variationOperatorPerformance/1000001.tsd correctDirectory/variationOperatorPerformance/1000001.tsd
echo "variationOPeratorPerformance/1000100.tsd"
diff runDirectory/variationOperatorPerformance/1000100.tsd correctDirectory/variationOperatorPerformance/1000100.tsd
echo "variationOperatorsForBestEvolvable.tsd"
diff runDirectory/variationOperatorsForBestEvolvable.tsd correctDirectory/variationOperatorsForBestEvolvable.tsd
