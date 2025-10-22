package org.example;
import org.apache.commons.math3.distribution.PoissonDistribution;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Lane implements Runnable{
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
    static int startHour = 0;
    static int end = 0;
    double lam;
    int generate = 0;

    //Creates thread safe queues
    ConcurrentLinkedQueue<Vehicle> leftTurn;
    ConcurrentLinkedQueue<Vehicle> laneOne;
    ConcurrentLinkedQueue<Vehicle> laneTwo;
    ConcurrentLinkedQueue<Vehicle> rightTurn;

   //objects, structures
    int[] size = new int[4];
    String direction = "";
    PoissonDistribution p;
    static Random rand = new Random();



    //Constructors
    Lane(char d, int s, int e, ConcurrentLinkedQueue<Vehicle> l, ConcurrentLinkedQueue<Vehicle>  r, ConcurrentLinkedQueue<Vehicle> s1, ConcurrentLinkedQueue<Vehicle> s2) {
        switch (d) {
            case 'n' -> direction = "North";

            case 's' -> direction = "South";

            case 'e' -> direction = "East";

            case 'w' -> direction = "West";
        }

        this.leftTurn = l;
        this.rightTurn = r;
        this.laneOne = s1;
        this.laneTwo = s2;
        startHour = s;
        end = e;

        //calculates lambda on creation
        double sum = 0;
        double denom = end - startHour + 1;
        switch (direction) {
            case "North" -> {
                for(int i = startHour; i <= end; i++) {
                    sum += BFERRY[0][i];
                }
            }
            case "South" -> {
                for(int i = startHour; i <= end; i++) {
                    sum += BFERRY[1][i];
                }
            }
            case "East" -> {
                for(int i = startHour; i <= end; i++) {
                    sum += SR92[0][i];
                }
            }
            case "West" -> {
                for(int i = startHour; i <= end; i++) {
                    sum += SR92[1][i];
                }
            }
        }
        //finds average if there is more than 1 hour looked at
        if(denom > 1) {
            sum = sum / denom;
        }
        //cars per minute
        lam = sum/60;
    }

    //General methods
    public int getGenerate() {return generate;}
    public String getDirection() {
        return direction;
    }
    public int[] laneTotal() {
        return new int[]{
                leftTurn.size(),
                laneOne.size(),
                laneTwo.size(),
                rightTurn.size()
        };
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
    public double getLam() {return lam;}
    private static int hourToMinuteRate(String d) {
        int cars = 0;
        switch (d) {
            case "North" -> {
                for (int i = startHour; i <= end; i++) {
                    cars += BFERRY[0][i];
                }
            }
            case "South" -> {
                for (int i = startHour; i <= end; i++) {
                    cars += BFERRY[1][i];
                }
            }
            case "East" -> {
                for (int i = startHour; i <= end; i++) {
                    cars += SR92[0][i];
                }
            }
            case "West" -> {
                for (int i = startHour; i <= end; i++) {
                    cars += SR92[1][i];
                }
            }
        }

        if(end - startHour + 1 > 0) {
            cars = cars / (end - startHour + 1);
        }

        cars = rand.nextInt((int)(cars * .90), (int)(cars * 1.1) + 1);
        return cars / 60;
    }
    private static Vehicle getVehicle(double chance) {
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
        return v;
    }
    @Override
    public void run() {
        int carsPerMinute = hourToMinuteRate(direction);
        size = size();
        double chance = rand.nextDouble();
        //creates poisson object with apache libraries, stores calculated probability
        p = new PoissonDistribution(lam);


        //always generates cars
        try{
            while(true) {
                //poisson
                double poisson = p.probability(carsPerMinute);
                while (chance > poisson) {
                    try {
                        //waits 1 "minute" (1 second real time)
                        Thread.sleep(1000);
                        chance = rand.nextDouble();
                        //increases chance by one standard deviation for every failed chance
                        poisson = poisson * Math.sqrt(carsPerMinute);
                    } catch (InterruptedException e) {
                        System.out.print("Your timer has messed up!");
                    }
                }

                //once poisson passes, generate
                for (int i = 0; i < carsPerMinute; i++) {
                    generate++;
                    chance = rand.nextDouble();
                    Vehicle v = getVehicle(chance);

                    //straight
                    if (rand.nextDouble() < .75) {
                        if (size[1] < size[2]) {
                            //if lane 1 is longer than lane 2, send car to lane 2
                            laneOne.add(v);
                        } else {
                            //if lane 2 is longer than lane 1, send car to lane 1
                            laneTwo.add(v);
                        }
                    }
                    //turning
                    else {
                        //arbitrary lane decider
                        if (rand.nextDouble() < .5) {
                            leftTurn.add(v);
                        } else {
                            rightTurn.add(v);
                        }
                    }
                    size = size();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}