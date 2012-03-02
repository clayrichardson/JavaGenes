This is the distribution of JavaGenes, developed primarily by Al Globus at NASA Ames Research Center (aglobus@mail.arc.nasa.gov).  It is made available under that NASA open source license.  See NOSA.txt.

JavaGenes is fairly general purpose evolutionary software system written in Java.  It implements the genetic algorithm, simulated annealing, stochastic hill climbing and other search techniques.  JavaGenes has been used to evolve molecules, atomic force field parameters, digital circuits, Earth Observing Satellite schedules, and antennas.  The digital circuit searches didn't work very well and the code isn't here.  The molecule evolution code isn't through the release process and is not yet available open source.  The antenna code is not, and may never be, available for open source distribution.

For a number of papers about  JavaGenes and the results it has generated see http://people.nas.nasa.gov/~globus/home.html.

Here you will find:

source -- the source code.   There are not a lot of comments.  Hopefully, the code is reasonably clear.

libraries -- open source libraries used by JavaGenes.  All the source to these libraries is included.  These are redistributed under their own licenses (see COPYING.txt).

FunctionalTest -- a set of unit and functional tests for JavaGenes.  Use the functional tests to see how to do things.  Run the functional test to make sure JavaGenes is working.  There is a unit test inside the functional test (using JUNIT).

JavaDoc -- documentation for JavaGenes.  There are some errors here and there, and the comments are skimpy.

JavaGenes.jar -- a jar file of the classes and the necessary libraries (except standard java and Java3d).

COPYING.txt -- details about the other libraries and their licenses.

NOSA.txt -- the open source license under which this code is distributed.

makeVersion.sh -- the shell script used to make this distribution.

Note: I use Apple's Project Builder for development, so there is no makefile or other build procedure.  If you want to extend JavaGenes, just run all the source through javac and put it in a jar file in whatever way you wish.  See makeVersion.sh for hints as to how to do this.