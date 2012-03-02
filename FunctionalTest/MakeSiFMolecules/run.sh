echo "testing MakeSiFMolecules"
rm -rf runDirectory
mkdir runDirectory
cp *.mol runDirectory
cd runDirectory
../../runJava.sh "gov.nasa.javaGenes.forceFields.MakeSiFMolecules 3 4 5 0.1 1.0"
cd ..
echo "diff"
diff runDirectory/SiSi.xyz correctDirectory/SiSi.xyz
diff runDirectory/SiF.xyz correctDirectory/SiF.xyz
echo "these files should have the same number of lines (wc)"
wc runDirectory/SiSiSi.xyz correctDirectory/SiSiSi.xyz

