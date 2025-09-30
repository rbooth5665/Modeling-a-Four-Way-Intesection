package org.example;

import org.apache.commons.math3.distribution.PoissonDistribution;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;

public class Lane {
   //Real world data for Bells Ferry/Highway 92 traffic; December 2024
    static final private int[][] SR92 = {
            //East Travel "Average Hourly Volume" from GDOT
            {76, 48, 56, 69, 144, 409, 1124, 1736, 1473, 1249, 1122, 1119, 1145, 1182, 1211, 1244, 1446, 1696, 1336, 745, 512, 374, 249, 120},
            //West Travel "Average Hourly Volume" from GDOT
            {102, 70, 29, 46, 80, 204, 585, 754, 804, 758, 796, 935, 1096, 1243, 1320, 1422, 1640, 1672, 1510, 1102, 732, 512, 328, 194}
    };
    static final private int[][] BFERRY = {
            //North Travel "Average Hourly Volume" from GDOT
            {92, 58, 38, 30, 40, 84, 262, 438, 533, 532, 548, 592, 720, 794, 940, 1101, 1384, 1336, 1235, 960, 696, 497, 282, 187},
            //South Travel "Average Hourly Volume" from GDOT
            {82, 50, 38, 55, 126, 308, 672, 941, 876, 754, 690, 685, 772, 820, 912, 986, 1126, 1152, 1064, 858, 592, 450, 246, 156}
    };


    //handles the specific lane queues
    Queue<Vehicle> leftTurn = new LinkedList<>();
    Queue<Vehicle> laneOne = new LinkedList<>();
    Queue<Vehicle> laneTwo = new LinkedList<>();
    Queue<Vehicle> rightTurn = new LinkedList<>();

    //access to lights at intersection through intersection object
    Intersection i;
    Timer t = new Timer();



    //Constructors
    Lane(Intersection intersection) {
        i = intersection;
    }

    //General methods
    //Returns total number of vehicle objects held in the lanes
    public int totalCapacity() {
        return this.leftTurn.size() + this.laneOne.size() + this.laneTwo.size() + this.rightTurn.size();
    }

    //returns specific number of vehicles in each lane
    public void totalVehicles() {
        int m = 0, c = 0, t = 0;
        //if queues are not empty, iterate through and parse total number of vehicles
        if(!leftTurn.isEmpty()) {
            for(Vehicle v: leftTurn) {
                switch(v.getType()) {
                    case "Motorcycle" -> m++;
                    case "Truck" -> t++;
                    case "Car" -> c++;
                }
            }
        }
        if(!laneOne.isEmpty()) {
            for(Vehicle v: laneOne) {
                switch(v.getType()) {
                    case "Motorcycle" -> m++;
                    case "Truck" -> t++;
                    case "Car" -> c++;
                }
            }
        }
        if(!laneTwo.isEmpty()) {
            for(Vehicle v: laneTwo) {
                switch(v.getType()) {
                    case "Motorcycle" -> m++;
                    case "Truck" -> t++;
                    case "Car" -> c++;
                }
            }
        }
        if(!rightTurn.isEmpty()) {
            for(Vehicle v: rightTurn) {
                switch(v.getType()) {
                    case "Motorcycle" -> m++;
                    case "Truck" -> t++;
                    case "Car" -> c++;
                }
            }
        }
        System.out.println("this direction of travel has: " +m+ " Motorcycles, "+c+" Cars, and "+t+" Large Trucks.");
    }

    //computes size of each lane from left turn -> right turn, returns it as an array
    public int[] updateSize (int[] s) {
        int size = 0;
        for(Vehicle v: leftTurn) {
            size += v.getSize();
        }
        s[0] = size;
        size = 0;

        for(Vehicle v: laneOne) {
            size += v.getSize();
        }
        s[1] = size;
        size = 0;

        for(Vehicle v: laneTwo) {
            size += v.getSize();
        }
        s[2] = size;
        size = 0;

        for(Vehicle v: rightTurn) {
            size += v.getSize();
        }
        s[3] = size;
        return s;
    }


    //Converts the cars per hour to cars per minute as a whole number
    private static int hourToMinuteRate(int c) {
        return Math.round(((float)c) / 60.0f);
    }

    //calculates the average traffic between passed start/end time
    private static double getLambda(char d, int start, int end) {
        //sums the total cars over each hour based on direction
        double sum = 0;
        double denom = end - start;
        switch (d) {
            case 'n' -> {
                for(int i = start; i <= end; i++) {
                    sum += BFERRY[0][i];
                }
            }
            case 's' -> {
                for(int i = start; i <= end; i++) {
                    sum += BFERRY[1][i];
                }
            }
            case 'e' -> {
                for(int i = start; i <= end; i++) {
                    sum += SR92[0][i];
                }
            }
            case 'w' -> {
                for(int i = start; i <= end; i++) {
                    sum += SR92[1][i];
                }
            }
        }

        //finds average if there is more than 1 hour looked at
        if(denom > 0) {
            sum = sum / denom;
        }
        else {
            return sum/60;
        }

        //returns the number of cars in a cars per minute format
        return sum/60;
    }

    //car generator for lanes
    public void generate (int cars, int start, int end, char d) {
        int carsPerMinute = hourToMinuteRate(cars);
        //creates poisson object with apache libraries, stores calculated mean
        PoissonDistribution p = new PoissonDistribution(getLambda(d, start, end));

        //calculates rounded poisson probability
        int poisson = (int)(p.probability(carsPerMinute) * 100);

        //works to generate vehicle objects
        //array to store changing size of each lane
        int[] sizes = new int[4];
        for(int i = 0; i < cars; i++) {

        }
        //now that poisson has been calculated, begin creating cars and dispersing them to the lanes.
        //weight the turn lanes to be less than the main lanes of travel
    }

}