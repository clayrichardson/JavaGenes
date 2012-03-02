echo "testing BreederElitist"
rm -rf runDirectory
mkdir runDirectory
cd runDirectory
../../runJava.sh "gov.nasa.javaGenes.core.Checkpointer gov.nasa.javaGenes.forceFields.RunChromosome 0 false"
cd ..
echo "log.tsd"
diff runDirectory/log.tsd correctDirectory/log.tsd
echo "transferFunctions.tsd"
diff runDirectory/variationOPeratorPerformance.tsd correctDirectory/variationOPeratorPerformance.tsd
echo "initial population"
diff runDirectory/population/1000000.tsd correctDirectory/population/1000000.tsd
echo "second population"
diff runDirectory/population/1000001.tsd correctDirectory/population/1000001.tsd
echo "final population"
diff runDirectory/population/1000005.tsd correctDirectory/population/1000005.tsd
