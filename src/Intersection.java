package org.example;

public class Intersection {
    //creates 4 separate lights for each direction
    Boolean north;
    Boolean south;
    Boolean east;
    Boolean west;

    /*
    intersection sizing based on the United States Highway standard of
    12 ft per lane of travel. there are 6 lanes of travel total in each direction,
    as well as a shoulder (standardized 4 ft).
     */
    final int LENGHT = 6 * 12 + 4;

    //because 1 second = 1 minute, ~17 ms = 1 second in my model
    int secondMod = 17;

    //initial cycleLength will be 30 seconds for first light cycle;
    int cycleLength = 500;

    int cycles = 0;

    //default constructor
    Intersection() {
        north = false;
        south = false;
        east = false;
        west = false;
    }

    //getters/setters
    public void setLight(char n, boolean b) {
        //uses a switch to determine which light to set
        //directions opposite each other (north/south and east/west) are assumed to have the same light cycle
        //redundancy included to account for any input of n,s,w,e
        switch(n) {
            case 'n' -> north = south = b;
            case 'e' -> east = west = b;
            case 'w' -> west = east = b;
            case 's' -> south = north = b;
        }
    }
    public boolean getNorthSouthLight() {
        return north;
    }
    public boolean getEastWestLight() {
        return east;
    }
    public void incrementCycleCount() {
        cycles++;
    }
    public int getCycleLength() {
        return cycleLength;
    }
    public int getNumCycles() {
        return cycles;
    }


    //implements critical flow ratios
    public double criticalFlow(int cars) {
        //critical flow ratio = v/s where v = actual flow of cars in a phase (vehicles per hour)
        // and s = saturation flow (how many can move through if it was green all the time)
        return 1.0;

    }
    //implementation of websters formula for finding optimal length of cycle
    public void websters(double cf) {
        //optimal length = (1.5 * time lost) + 5 / 1 - sum of critical flow rations




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