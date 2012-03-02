//
// Copyright (C) 2005 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA.txt at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//
package gov.nasa.javaGenes.main;

import gov.nasa.alsUtility.Error;
import gov.nasa.alsUtility.FieldRecordText;
import java.util.Vector;
import java.io.PrintWriter;
import gov.nasa.alsUtility.IO;
import java.lang.Math;
import gov.nasa.alsUtility.Utility;

// depends on the output format used by Anna Pryor's program
public class compareTechniquesMain {
static String separator = "\t";
static final double TOO_CLOSE_TO_ZERO = 0.00001;
static final int RUN_COLUMN = 0;
static final int MEASURE_COLUMN = 1;
static final int MEAN_COLUMN = 2;
static final int RATIO = 0;
static final int DIFFERENCE = 1;

public static void main(String[] arguments) {
    if (arguments.length < 2) {
        System.out.println("format: java dataAnalysis.comoparTechniquesMain referenceTechnique dataFile ...");
        System.exit(-1);
    }
    
    String referenceTechnique = arguments[0];
    String[] dataFiles = getDataFiles(arguments);
    PrintWriter[][] output = setUpOutputFiles(dataFiles[0]);
    String[] techniques = getTechniques(referenceTechnique,dataFiles[0]);
    int numberOfTechniques = techniques.length + 1;

    for(int i = 0; i < output.length; i++)
    for(int j = 0; j < output[i].length; j++)
        printHeader(output[i][j],techniques);
    for(int i = 0; i < dataFiles.length; i++)
        processFile(referenceTechnique, numberOfTechniques,dataFiles[i], output);
    for(int i = 0; i < output.length; i++)
    for(int j = 0; j < output[i].length; j++)
        output[i][j].close();
}
static void printHeader(PrintWriter out, String[] techniques) {
    for(int i = 0; i < techniques.length; i++)
         out.print(separator + techniques[i]);
     out.println();
}
static String[] getTechniques(String referenceTechnique, String dataFile) {
    Vector techniques = new Vector();
    FieldRecordText in = new FieldRecordText(dataFile, separator);
    in.readLine(); // skip header
    String currentTechnique = "";
    while(true) {
        String[] line = in.readLine();
        if (line == null || line.length < 2)
            break;
        if (!currentTechnique.equals(line[RUN_COLUMN]) && !referenceTechnique.equals(line[RUN_COLUMN])) {
            techniques.add(line[RUN_COLUMN]);
            currentTechnique = line[RUN_COLUMN];
        }
    }
    in.close();

    String[] array = new String[techniques.size()];
    for(int i = 0; i < array.length; i++)
        array[i] = (String)techniques.get(i);
    return array;
}
static String[] getDataFiles(String[] arguments) {
    String[] dataFiles = new String[arguments.length - 1];
    for(int i = 1; i < arguments.length; i++)
        dataFiles[i-1] = arguments[i];
    return dataFiles;
}
static PrintWriter[][] setUpOutputFiles(String dataFile) {
    Vector outputFiles = new Vector();
    Vector measures = new Vector();
    FieldRecordText in = new FieldRecordText(dataFile, separator);
    in.readLine(); // skip header
    String technique = null;
    boolean firstTime = true;
    while(true) {
        String[] line = in.readLine();
        if (line == null || line.length < 2)
            break;
        if (firstTime) {
            technique = line[RUN_COLUMN];
            firstTime = false;
        }
        if (!technique.equals(line[RUN_COLUMN]))
            break;
        outputFiles.add(line[MEASURE_COLUMN]);
    }
    in.close();

    PrintWriter[][] array = new PrintWriter[outputFiles.size()][2];
    for(int i = 0; i < array.length; i++) {
        array[i][RATIO] = IO.getPrintWriter(((String)outputFiles.get(i)) + "Ratio.tsd");
        array[i][DIFFERENCE] = IO.getPrintWriter(((String)outputFiles.get(i)) + "Difference.tsd");
    }
    return array;
}


static void processFile(String referenceRun, int numberOfRuns, String dataFile, PrintWriter[][] outputFiles) {
    int numberOfMeasures = outputFiles.length;
    for(int i = 0; i < numberOfMeasures; i++)
    for(int j = 0; j < outputFiles[i].length; j++)
        outputFiles[i][j].print(dataFile);
    double[] referenceValues = getReferenceValues(referenceRun, numberOfMeasures, dataFile);
    FieldRecordText in = new FieldRecordText(dataFile, separator);
    in.readLine(); // skip header
    for(int t = 0; t < numberOfRuns; t++) {
        for(int m = 0; m < numberOfMeasures; m++) {
            String[] line = in.readLine();
            if (line[RUN_COLUMN].equals(referenceRun)) {
                for(int i = 1; i < numberOfMeasures; i++)
                    in.readLine();
                break;
            }
            double value = Utility.string2double(line[MEAN_COLUMN]);
            double ratio;
            if (Math.abs(referenceValues[m]) < TOO_CLOSE_TO_ZERO)
                ratio = 0;
            else
                ratio = value / referenceValues[m];
            outputFiles[m][RATIO].print(separator + ratio);
            outputFiles[m][DIFFERENCE].print(separator + (referenceValues[m] - value));
        }
    }
            
    in.close();
    for(int i = 0; i < numberOfMeasures; i++)
    for(int j = 0; j < outputFiles[i].length; j++)
        outputFiles[i][j].println();
}
static double[] getReferenceValues(String referenceRun, int numberOfMeasures, String dataFile) {
    double[] values = new double[numberOfMeasures];
    FieldRecordText in = new FieldRecordText(dataFile, separator);
    in.readLine(); // skip header
    while(true) {
        String[] line = in.readLine();
        if (line[RUN_COLUMN].equals(referenceRun)) {
            values[0] = Utility.string2double(line[MEAN_COLUMN]);
            for(int i = 1; i < values.length; i++)
                values[i] = Utility.string2double(in.readLine()[MEAN_COLUMN]);
            break;
        }
    }
    return values;
}

}
