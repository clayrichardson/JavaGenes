# Creates an open source distribution of JavaGenes from an internal version.
# Before running this you must:
# place the JavaGenes version in a directory here called JavaGenes.<version#>.original
# There should be one argument, the JavaGenes.<version#>

cp -r $1.original $1
cp COPYING.txt NOSA.txt README.txt makeVersion.sh $1
cd $1
# delete files and directories not part of this distribution
rm -r source/gov/nasa/javaGenes/necAntennaSimulation
rm -r source/gov/nasa/javaGenes/wireAntennas

# delete functional tests not part of this distribution
gunzip FunctionalTest.tar.gz
tar xf FunctionalTest.tar
rm FunctionalTest.tar
rm -r FunctionalTest/Antenna
rm -r FunctionalTest/AntennaRandom
rm -r FunctionalTest/AntennaEvaluation
rm -r FunctionalTest/antennaManufacturingErrors
rm -r FunctionalTest/MakeSeeds
rm -r FunctionalTest/Parameters2tsd
rm -r FunctionalTest/CubesatBody
rm -r FunctionalTest/AntennaEnvironment
rm -r FunctionalTest/AntennaTDRSS

find . -name "*.java" -exec /Users/globus/JavaGenes/OpenSource/addHeader.sh {} \;
cd source
javac gov/nasa/*/*.java gov/nasa/*/*/*.java gov/nasa/*/*/*/*.java
jar cf ../JavaGenes.jar gov/nasa/*/*.class gov/nasa/*/*/*.class gov/nasa/*/*/*/*.class  ../libraries/jiggle/jiggle.jar ../libraries/colt/colt.jar ../libraries/junit3.8.1/junit.jar
find. -name "*.class" -delete
cd ..

javadoc -sourcepath source  -d JavaDoc -header "JavaGenes, NASA Ames. Written largely by Al Globus" gov.nasa.alsUtility gov.nasa.javaGenes.chemistry  gov.nasa.javaGenes.core gov.nasa.javaGenes.core.HFC gov.nasa.javaGenes.core.utility  gov.nasa.javaGenes.EOSscheduling  gov.nasa.javaGenes.EOSscheduling.HBSS gov.nasa.javaGenes.evolvableDoubleList gov.nasa.javaGenes.evolvableDoubleList.DeJongExperiment  gov.nasa.javaGenes.forceFields  gov.nasa.javaGenes.graph  gov.nasa.javaGenes.hillClimbing  gov.nasa.javaGenes.main   gov.nasa.javaGenes.permutation  gov.nasa.javaGenes.simulatedAnnealing  gov.nasa.javaGenes.weightNetwork > ../JavaDocOutput.txt

echo "look at JavaDocOutput.txt to see if JavaDoc worked"
echo "remove the antenna related functional tests from runAll.sh. Comments will tell you where they are"
echo "remove wireAntenna unit test (junit) in UnitTest/run.sh"
echo "change FunctionalTest/buildJava.sh and runJava.sh to have ClassPath to <dir>/JavaGenes.jar"
echo "then run functional test"
echo "if everything works, then tar and gzip it for distribution"
