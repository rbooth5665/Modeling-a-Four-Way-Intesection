package org.example;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Intersection implements Runnable {
    /*
    intersection sizing based on the United States Highway standard of
    12 ft per lane of travel. there are 6 lanes of travel total in each direction,
    as well as a shoulder (standardized 4 ft).
     */
    final int DIMENSION = 6 * 12 + 4;
    int phaseLength;
    static int cycleLength;

   //data tracking
    double speed = 0;
    int departure = 0;
    long wait = 0;
    static int startHour;
    static int end;
    static long startTime;

    /*
    according to the HCM (highway capacity manual),
    the base saturation flow of an intersection is 1900 cars per hour per lane.
                                    ew          sn
    lane width =  Standard          1           1
    heavy vehicles = 1.4%           .986        .986
    grade = flat/hill               1           .97
    pedestrians =  none/minor       1           .9
    bus blockage = Occasional       .95         1
    central business district = n/y 1           .9

    east/west saturation = 1900 * 1 * .986 * 1 * 1 * .95 * 1 ~ 1779
    north/south saturation = 1900 * 1 * .986 * .97 * .9 * 1 * .9 ~ 1471
     */
    double cFlow;
    int saturation;

    ConcurrentLinkedQueue<Vehicle> leftTurn;
    ConcurrentLinkedQueue<Vehicle> laneOne;
    ConcurrentLinkedQueue<Vehicle> laneTwo;
    ConcurrentLinkedQueue<Vehicle> rightTurn;

    //default constructor
    Intersection(char d, double cars, int s, int e, ConcurrentLinkedQueue<Vehicle> l, ConcurrentLinkedQueue<Vehicle>  r, ConcurrentLinkedQueue<Vehicle> s1, ConcurrentLinkedQueue<Vehicle> s2) {
        this.leftTurn = l;
        this.rightTurn = r;
        this.laneOne = s1;
        this.laneTwo = s2;
        startHour = s;
        end = e;

        switch(d) {
            case 'n', 's' -> {saturation = 1471;}
            case 'e', 'w' -> {saturation = 1779;}
        }

        //sets critical flow of period on creation
        cFlow = cars/saturation;

    }

    //getters/setters
    public int getPhaseLength() {return phaseLength;}
    public int getDimension() {return DIMENSION;}
    public double getcFlow() {return cFlow;}
    public int getDeparture() {return departure;}
    public void setPhase(int p){phaseLength = p;}
    public int getPhase() {return phaseLength;}
    public static int getCycle() {return cycleLength;}
    public long avgWait() {
        //returns wait time in milliseconds * 17 to account for my models "seconds"
        if(departure > 0) {
            return ((wait / departure) / 1000000) * 17;
        }
        return wait;
    }
    public double getSpeed() {
        if(departure > 0) {
            return speed/departure;
        }
        return speed;
    }

    public static void websters(double d) {
        /*
        optimal length = (1.5 * time lost) + 5 / 1 - sum of critical flow ratios
        for simplification, we will assume that all factors of time
        lost equate to about 3 seconds per phase (two phases).
         */
        double bottom = Math.abs(1 - d);
        double top = (1.5 * (3 * 2)) + 5;

        //returns the optimal length of one full cycle (modulated to system time), needs to be converted into phase lengths
        cycleLength = (int)(top/bottom) * 17;
    }

    public void optimizePhase(double d) {
        //returns phase lengths for each light
        phaseLength = (int)((cFlow/d) * cycleLength);
    }

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

    //tells lanes when they can leave, then "pulls" them from queues
    @Override
    public void run () {
        try{
            //removes cars in alternating signals
            while(true) {
                //Sleeps thread for the opposing light cycle plus an additional 3 "seconds" to account for slow down and light change
                Thread.sleep(cycleLength - phaseLength + (3 * 17));
                int throughput = 0;
                int left = 0;
                int right = 0;
                Vehicle veh;
                //The left turn arrow will get 10% of the total cycle time
                int leftTime = (int)(phaseLength * .10);
                int pTime = phaseLength - leftTime;

                //protected left turns
                while(leftTime > 0 && !leftTurn.isEmpty()) {
                    veh = leftTurn.peek();
                    leftTime -= (int)veh.timeToCross(DIMENSION);

                    speed += leftTurn.remove().getAccel();
                    departure++;
                }

                //while there is still time left for the green light cycle
                while(pTime > 0 && (!laneOne.isEmpty() || !laneTwo.isEmpty())) {
                    //determines how many cars can be in the intersection from the left lane
                    while (throughput < DIMENSION && (left != laneOne.size())) {
                        for (Vehicle v : laneOne) {
                            throughput += v.getSize();
                            left++;
                        }
                    }

                    //determines how many cars can be in the intersection from the right lane
                    throughput = 0;
                    while (throughput < DIMENSION && (right != laneTwo.size())) {
                        for (Vehicle v : laneTwo) {
                            throughput += v.getSize();
                            right++;
                        }
                    }
                    //determines which lane has more potential cars in the intersection
                    int t = Math.max(left, right);

                    //passes cars through intersection, continues to calculate while phaseLength > 0
                    for(int i = 0; i < t; i++) {
                        if (!laneOne.isEmpty()) {
                            veh = laneOne.peek();
                            pTime -= (int)veh.timeToCross(DIMENSION);

                            wait += System.nanoTime() - veh.getTime();
                            speed += laneOne.remove().getAccel();
                            departure++;
                        }
                        if(!laneTwo.isEmpty()) {
                            veh = laneTwo.peek();

                            wait += System.nanoTime() - veh.getTime();
                            speed += laneTwo.remove().getAccel();
                            departure++;
                        }
                        //if cars can move straight, it is a protected right turn
                        if(!rightTurn.isEmpty()) {
                            veh = rightTurn.peek();

                            wait += System.nanoTime() - veh.getTime();
                            speed += rightTurn.remove().getAccel();
                            departure++;
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}