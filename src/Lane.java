package org.example;

import org.apache.commons.math3.distribution.PoissonDistribution;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

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
    int[] size = new int[4];
    String direction;
    //access to lights at intersection through intersection object
    Intersection i;
    PoissonDistribution p;
    static Random rand = new Random();

    //Constructors
    Lane(Intersection intersection, char d) {
       switch (d) {
           case 'n' -> direction = "North";

           case 's' -> direction = "South";

           case 'e' -> direction = "East";

           case 'w' -> direction = "West";
       }
        i = intersection;
    }

    //General methods
    public String getdirection() {
        return direction;
    }
    public int[] laneTotal() {
        int[] cap = new int[4];
        if(!leftTurn.isEmpty()) {
            for(Vehicle v: leftTurn) {
                cap[0]++;
            }
        }
        if(!laneOne.isEmpty()) {
            for(Vehicle v: laneOne) {
                cap[1]++;
            }
        }
        if(!laneTwo.isEmpty()) {
            for(Vehicle v: laneTwo) {
                cap[2]++;
            }
        }
        if(!rightTurn.isEmpty()) {
            for(Vehicle v: rightTurn) {
                cap[3]++;
            }
        }
        return cap;
    }
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
        System.out.println(direction + " lane has: " +m+ " Motorcycles, "+c+" Cars, and "+t+" Large Trucks.");
    }
    public int[] size () {
        int[] s = new int[4];
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


    //generates a random carPerMinute rate based on the start/end time, avoids a deadlock by maintaining a realistic car arrival process for the given time.
    private static int hourToMinuteRate(char d, int start, int end) {
        int cars = 0;
        switch (d) {
            case 'n' -> {
                for (int i = start; i <= end; i++) {
                    cars += BFERRY[0][i];
                }
            }
            case 's' -> {
                for (int i = start; i <= end; i++) {
                    cars += BFERRY[1][i];
                }
            }
            case 'e' -> {
                for (int i = start; i <= end; i++) {
                    cars += SR92[0][i];
                }
            }
            case 'w' -> {
                for (int i = start; i <= end; i++) {
                    cars += SR92[1][i];
                }
            }
        }

        if(end - start > 0) {
            cars = cars / (end - start);
        }

        cars = rand.nextInt((int)(cars * .80), (int)(cars * 1.2) + 1);
        return cars / 60;
    }
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
    public void generate (char d, int start, int end) {
        int carsPerMinute = hourToMinuteRate(d, start, end);
        size = size();
        double chance = rand.nextDouble();
        //creates poisson object with apache libraries, stores calculated probability
        p = new PoissonDistribution(getLambda(d, start, end));
        double poisson = p.probability(carsPerMinute);

        //1 minute = 1 second
        //artificial wait for poisson, poisson doubles as well
        while(chance > poisson) {
            try {
                Thread.sleep(1000);
                chance = rand.nextDouble();
                poisson = poisson * 2;
            } catch (InterruptedException e) {
                System.out.print("Your timer has messed up!");
            }
        }

        //Car generator
        for(int i = 0; i < carsPerMinute * 60; i++) {
            chance = rand.nextDouble();
            Vehicle v;
            //values based on USDOT vehicle statistics
            if(chance < 0.948676) {
                //generate car
                v = new Car();

            }
            else if(chance > .948676 && chance < .986998) {
                //generate motorcycle
                v = new Motorcycle();
            }
            else {
                //generate truck
                v = new Truck();
            }

            //straight
            if(rand.nextDouble() < .75) {
                if(size[1] < size[2]) {
                    //if lane 1 is longer than lane 2, send car to lane 2
                    laneOne.add(v);
                }
                else {
                    //if lane 2 is longer than lane 1, send car to lane 1
                    laneTwo.add(v);
                }
            }
            //turning
            else {
                //arbitrary lane decider
                if(rand.nextDouble() < .5) {
                    leftTurn.add(v);
                }
                else {
                    rightTurn.add(v);
                }
            }
            size = size();
        }
    }
    public void leave (int dimension, int cycle) {
        //takes size of intersection, calculates how many cars can go through for each lane
        int throughput = 0;
        int left = 0;
        int right = 0;
        Vehicle veh;
        while(cycle > 0 && !laneOne.isEmpty()) {
            while (throughput < dimension && (left != laneOne.size())) {
                for (Vehicle v : laneOne) {
                    throughput += v.getSize();
                    left++;
                }
            }

            throughput = 0;
            while (throughput < dimension && (right != laneTwo.size())) {
                for (Vehicle v : laneTwo) {
                    throughput += v.getSize();
                    right++;
                }
            }
            //uses cycle to measure how many cars will be let through, if there are any cars
            for(int i = 0; i < left; i++) {

                veh = laneOne.remove();

                if(!laneTwo.isEmpty()) {
                    laneTwo.remove();
                    right--;
                }

                cycle -= veh.timeToCross(dimension);
            }
        }
    }
}