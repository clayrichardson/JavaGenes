echo "testing SWmultiObjective"
rm -rf runDirectory
mkdir runDirectory
cd runDirectory
../../runJava.sh "gov.nasa.javaGenes.core.Checkpointer gov.nasa.javaGenes.forceFields.RunChromosome 0 false"
cd ..
echo "log.tsd"
diff runDirectory/log.tsd correctDirectory/log.tsd
echo "pareto.tsd"
diff runDirectory/pareto.tsd correctDirectory/pareto.tsd
echo "initial population"
diff runDirectory/population/1000000.tsd correctDirectory/population/1000000.tsd
echo "final population"
diff runDirectory/population/1000003.tsd correctDirectory/population/1000003.tsd
