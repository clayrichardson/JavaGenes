# isn't a very good test because there are no F parameters in the second two cases
echo "testing StillingerWeberEnergies"
rm -f runResults1
../runJava.sh "gov.nasa.javaGenes.forceFields.StillingerWeberEnergies testInput.xyz runResults"
echo "diff"
diff runResults correctResults

echo "testing StillingerWeberEnergies with alternate parameters input"
rm -f runResults2
../runJava.sh "gov.nasa.javaGenes.forceFields.StillingerWeberEnergies testInput.xyz runResults2 SWalternateParameters.tsd"
echo "diff"
diff runResults2 correctResults2
