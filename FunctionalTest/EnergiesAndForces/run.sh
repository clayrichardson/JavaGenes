echo "testing StillingerWeberPrintEnergiesAndForces"
rm -f runResults
../runJava.sh "gov.nasa.javaGenes.forceFields.StillingerWeberPrintEnergiesAndForces"
echo "diffs"
diff SiSiForcesAndEnergies.tsd SiSiCorrect.tsd
diff SiFForcesAndEnergies.tsd SiFCorrect.tsd
diff FFForcesAndEnergies.tsd FFCorrect.tsd

