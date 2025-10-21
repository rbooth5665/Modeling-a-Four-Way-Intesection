package org.example;

public class Intersection {
    /*
    intersection sizing based on the United States Highway standard of
    12 ft per lane of travel. there are 6 lanes of travel total in each direction,
    as well as a shoulder (standardized 4 ft).
     */
    final int LENGTH = 6 * 12 + 4;

    //initial phaseLength will be 1 "minute" (1 second) for first phase cycle
    int ewPhaseLength = 1000;
    int nsPhaseLength = 1000;
    int cycles = 0;
    //ew, ns critical flow array
    static double[] cFlow = new double[2];

    //default constructor
    Intersection() {}

    //getters/setters
    public void incrementCycleCount() {cycles++;}
    public int getnsPhaseLength() {return nsPhaseLength;}
    public int getewPhaseLength() {return ewPhaseLength;}
    public int getNumCycles() {return cycles;}
    public int getDimension() {return LENGTH;}
    public double[] getcFlow() {return cFlow;}

    //saturation flows for east/west and north/south will be static
    //according to the HCM (highway capacity manual),
    //the base saturation flow of an intersection is 1900 cars per hour per lane.
    /*
                                    ew          sn
    lane width =  Standard          1           1
    heavy vehicles = 1.4%           .986        .986
    grade = flat/hill               1           .97
    pedestrians =  none/minor       1           .9
    bus blockage = Occasional       .95         1
    central business district = n/y 1           .9
     */
    static double ewSaturation = 1900 * 1 * .986 * 1 * 1 * .95 * 1; // ~1779
    static double nsSaturation = 1900 * 1 * .986 * .97 * .9 * 1 * .9; // ~1471

    private static void criticalFlow(int ewCars, int nsCars) {
        /*
        critical flow ratio = v/s where v = actual flow of cars in a phase (vehicles per hour)
        and s = saturation flow (how many can move through if it was green all the time)
        The actual flow of cars will equal the average calculated amount over a given period
         */
        cFlow[0] = ewCars/ewSaturation;
        cFlow[1] = nsCars/nsSaturation;
    }

    private double websters(int ewCars, int nsCars) {
        /*
        optimal length = (1.5 * time lost) + 5 / 1 - sum of critical flow ratios
        for simplification, we will assume that all factors of time
        lost equate to about 3 seconds per phase (two phases).
         */
        criticalFlow(ewCars, nsCars);
        double bottom = Math.abs(1 - (cFlow[0] + cFlow[1]));
        double top = (1.5 * (3 * 2)) + 5;

        //returns the optimal green time, needs to be converted into phase lengths
        return top/bottom;
    }

    public void optimizePhase(int ewCars, int nsCars) {
        //takes green time, returns array holding phase lengths for each lane
        double cycle = websters(ewCars, nsCars);
        double denom = cFlow[0] + cFlow[1];

        //ew, ns phase lengths, accounts for systems secondModulation
        ewPhaseLength = (int)((cFlow[0]/denom) * Math.round(cycle)) * 17;
        nsPhaseLength = (int)((cFlow[1]/denom) * Math.round(cycle)) * 17;
    }

    //green split determines what percentage of the total light cycle each direction will get. to start, each light will get 25% of the total cycle
    public double[] greenSplit(int[] n, int[] s, int[] e, int[] w) {
        //Total cars moving parallel to each other, as well as total cars at intersection
        int eastWest = e[1] + e[2] + w[1] + w[2];
        int northSouth = n[1] + n[2] + s[1] + s[2];
        int total = eastWest+northSouth;
        double[] split = new double[2];
        split[0] = (double) northSouth /total;
        split[1] = (double) eastWest /total;

        //returns a double array with all green split values. returned as NS and EW
        return split;
    }
}