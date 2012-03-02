This directory contains a functional test of JavaGenes.  Each sub-directory represents a test.  These tests run a program and check the output files against known 'correct' output files using diff. Thus, the test is sensitive to unimportant changes in output.  

There is one exception to this approach, the UnitTest is also here for convenience. UnitTest output is ala JUnit with one warning about an input file format. This warning can be ignored.

If these tests run and the checks for identical output files pass, JavaGenes is probably working ok.  If the output files aren't identical to what they 'should' be, JavaGenes might still be ok since these are strict text comparisons.

To run the tests:

1) Change runJava.sh so that the directory name in the class path there corresponds to the directory of your JavaGenes installation, rather than mine.

2) Change buildJava.sh so that the directory name in the class path corresponds to the directory of your JavaGenes installation, rather than mine.

3) Execute runAll.sh to run all of the functional tests.  (Must be executed from this directory.)


The files:

runAll.sh  -- runs all of the functional tests.  Must be executed from this directory.

runAllOutput.txt -- typical output of runAll.sh

buildJava.sh -- used to compile java source in the functional tests.  Uses hard coded absolute directory names for necessary jar files in the CLASSPATH.  You will probably have to change these.

runJava.sh -- used to run most functional tests.  Uses hard coded absolute directory names for necessary jar files in the CLASSPATH.  You will probably have to change these.

Inside each directory there is a run.sh and, sometimes, a build.sh.  run.sh runs a single functional test using runJava.sh.  build.sh builds the jar and/or class files unique to the functional test using buildJava.sh.