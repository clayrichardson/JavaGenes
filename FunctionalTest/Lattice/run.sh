echo "testing Lattice"
rm -rf tempDir
../runJava.sh "gov.nasa.javaGenes.main.LatticeConstantEnergies Si8unitCell.xyz tempDir SiParameters.tsd 80 0.9 1.1 3 0.1 3 1"
echo energies
diff correctDir/energies.tsd tempDir/energies.tsd 
echo minimum
diff correctDir/minimum.xyz tempDir/minimum.xyz
echo 1
diff correctDir/1.xyz tempDir/1.xyz


