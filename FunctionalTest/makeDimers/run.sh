echo "testing MakeDimer"
rm -f runResults
../runJava.sh "gov.nasa.javaGenes.forceFields.MakeDimer runResults Si Si 1 4 50"
echo "diff"
diff runResults correctResults
