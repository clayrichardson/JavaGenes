echo "testing lengths and angles"
rm -f runResults
../runJava.sh "gov.nasa.javaGenes.forceFields.GetLengthsAndAngles SiF4RandomEnergies.xyz runResults"
echo "diff"
diff runResults correctResults
