echo "testing Molecule"
rm -rf runDirectory
mkdir runDirectory
cd runDirectory
../../runJava.sh "gov.nasa.javaGenes.chemistry.MoleculeMain ../benzene.mol"
cd ..
echo "log.tsd"
diff runDirectory/log.tsd correctDirectory/log.tsd
echo "population/1000000.tsd"
diff runDirectory/population/1000000.tsd correctDirectory/population/1000000.tsd
echo "population/1000010.tsd"
diff runDirectory/population/1000010.tsd correctDirectory/population/1000010.tsd
echo "variationOPeratorPerformance.tsd"
diff runDirectory/variationOperatorPerformance.tsd correctDirectory/variationOperatorPerformance.tsd
echo "variationOPeratorPerformance/1000001.tsd"
diff runDirectory/variationOperatorPerformance/1000001.tsd correctDirectory/variationOperatorPerformance/1000001.tsd
echo "variationOPeratorPerformance/1000009.tsd"
diff runDirectory/variationOperatorPerformance/1000009.tsd correctDirectory/variationOperatorPerformance/1000009.tsd
echo "variationOperatorsForBestEvolvable.tsd"
diff runDirectory/variationOperatorsForBestEvolvable.tsd correctDirectory/variationOperatorsForBestEvolvable.tsd
