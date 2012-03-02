echo "testing randomizeXYZ"
../runJava.sh "gov.nasa.javaGenes.chemistry.randomizeXYZ 10 0.5 2.0 flat siliconTetrahedron.xyz runResults1 5 951734816260"
echo "diff"
diff runResults1 correctResults1
../runJava.sh "gov.nasa.javaGenes.chemistry.randomizeXYZ 10 0.5 2.0 gaussian siliconTetrahedron.xyz runResults2 1 951734816260"
echo "diff"
diff runResults2 correctResults2

