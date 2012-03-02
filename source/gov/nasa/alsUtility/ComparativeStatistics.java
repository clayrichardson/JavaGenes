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
package gov.nasa.alsUtility;


import java.lang.Math;
import java.io.Serializable;
import gov.nasa.alsUtility.Error;

/**
Implements comparative statistics measures on Sample objects.

@see Sample
*/
public class ComparativeStatistics implements Serializable{
public static final int CANNOT_DO_ANALYSIS = -1;

protected static double conflevel1=95.0;
protected static boolean poolsigmas=true;
protected static int althyp1=0;              //0 if two-sided test
protected static double nullhyp1=0.0;        //diff to be tested

/**
implements and f-test between two Samples
*/
public static double ftest (Sample a, Sample b) {
    int count1= a.getN();
    int count2= b.getN();
    double v1 = a.getVariance();    
    double v2 = b.getVariance();
    if(count1<1||count2<1) {
	Error.warning("not enough data in samples, returning -1");
        return -1;
    }
    if(v1<=0.0||v2<=0.0) {
	Error.warning("Cannot compute tests since at least one variance equals 0, returning -1");
        return -1;
    }
    if (v1 > v2)
        return (v1/(a.getN()-1))/(v2/(b.getN()-1));
    else
        return (v2/(b.getN()-1))/(v1/(a.getN()-1));    
}

/**
implements and t-test between two Samples. Returns CANNOT_DO_ANALYSIS if input samples cannot be 
compared by Student's Ttest.
*/
public static double ttest(Sample a, Sample b) {
    int count1= a.getN();
    int count2= b.getN();
    double mean1 = a.getMean();
    double mean2 = b.getMean();
    double sd1 = a.getStandardDeviation();
    double sd2 = b.getStandardDeviation();
    return ttest(count1,count2,mean1,mean2,sd1,sd2);
}
public static double ttest(int count1, int count2, double mean1, double mean2, double stddev1, double stddev2) {
    if(count1<1||count2<1) 
	return CANNOT_DO_ANALYSIS;
    if(stddev1<=0.0||stddev2<=0.0)
	return CANNOT_DO_ANALYSIS;
    double sum1 = 0.0;
    double sum2 = 0.0;
    double numerator = 0.0;
    double denominator = 0.0;
    double N1 = count1 - 1;
    double N2 = count2 - 1;
    
    sum1 = Math.pow(stddev1, 2);
    sum2 = Math.pow(stddev2, 2);
    
    sum1 = N1 * sum1;
    sum2 = N2 * sum2;
    
    numerator = sum1 + sum2;
    denominator = count1 + count2 - 2;
    double total =
            Math.sqrt((numerator / denominator) * (1.0 / count1 + 1.0 / count2));
    
    double t = (mean1 - mean2) / total;
    double Prob;
    Prob =
            BetaI(
                    0.5 * denominator,
                    0.5,
                    denominator / (denominator + Math.pow(t, 2)));
    return Prob;
    
}
public static double BetaI(double A, double B, double X) {
    double BT = 0.0;
    double answer = 0.0;
    
    if (X < 0 || X > 1)
            Error.fatal("bad argument X in BetaI");
    if (X == 0 || X == 1)
            BT = 0.0;
    else {
            BT =
                    Math.exp(
                            Gammln(A + B)
                                    - Gammln(A)
                                    - Gammln(B)
                                    + A * Math.log(X)
                                    + B * Math.log(1.0 - X));
    };
    if (X < (A + 1.0) / (A + B + 2.0)) {
            answer = BT * Betacf(A, B, X) / A;
            return answer;
    }
    else {
            answer = 1.0 - BT * Betacf(B, A, 1.0 - X) / B;
            return answer;
    }
}
    
public static double Betacf(double A, double B, double X) {
    int itmax = 100;
    double eps = 3.e-7;
    boolean flag = false;
    double AM = 1.0;
    double BM = 1.0;
    double AZ = 1.0;
    double QAB = A + B;
    double QAP = A + 1;
    double QAM = A - 1;
    double BZ = 1.0 - QAB * X / QAP;
    for (int M = 1; M <= itmax; M++) {
            int EM = M;
            int TEM = EM + EM;
            double D = EM * (B - M) * X / ((QAM + TEM) * (A + TEM));
            double AP = AZ + D * AM;
            double BP = BZ + D * BM;
            D = - (A + EM) * (QAB + EM) * X / ((A + TEM) * (QAP + TEM));
            double APP = AP + D * AZ;
            double BPP = BP + D * BZ;
            double AOLD = AZ;
            AM = AP / BPP;
            BM = BP / BPP;
            AZ=APP/BPP;
            BZ = 1.0;
            if (Math.abs(AZ - AOLD) < eps * Math.abs(AZ)) {
                    flag = true;
                    break;
            }
    }
    if (flag == false)
            Error.fatal("A or B too big, or itmax too small");
    double answer = AZ;
    return answer;
}
    
public static double Gammln(double XX) {
    double[] COF = {
                    76.18009173,
                    -86.50532033,
                    24.01409822,
                    -1.231739516,
                    .00120858003,
                    -.00000536382 };
    double STP = 2.50662827465;
    double Half = .5;
    double One = 1.0;
    double FPF = 5.5;
    double X = XX-One;
    double TMP = X+FPF;
    TMP = (X+Half)*Math.log(TMP)-TMP;
    double SER = One;
    for(int j = 1; j <=6; j++){
        X = X+One;
        SER = SER+COF[j-1]/X;
    }
    double answer = TMP+Math.log(STP*SER);
    return answer;
}

}
