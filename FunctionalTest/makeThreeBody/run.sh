echo "testing MakeThreeBodyXyz"
rm -f runResults
../runJava.sh "gov.nasa.javaGenes.forceFields.MakeThreeBodyXyz run.xyz Si F C 0.5 3.0 45 135 5"
echo "diff"
diff run.xyz correct.xyz
