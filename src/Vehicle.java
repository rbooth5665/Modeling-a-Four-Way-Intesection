package org.example;

import java.util.Random;
public class Vehicle {
    int size;
    double accel;
    String type;
    Random rand = new Random();
    long time;


    //getter and setter methods
    public long getTime() {
        return time;
    }
    public String getType() {
        return type;
    }
    public double getAccel() {
        return accel;
    }
    public int getSize() {
        return size;
    }

    //returns the time to cross a given dimension (in seconds) using an acceleration in ft/s/s
    //because 1 second = 1 minute, ~17 ms = 1 second in my model
    int secondMod = 17;
    public double timeToCross(int dimension) {
        double partial = (2 * dimension) / accel;
        return Math.sqrt(partial) * secondMod;
    }
}

//Liberties will be taken with size and acceleration,
// as there is not a standardized length or acceleration for vehicles.
class Motorcycle extends Vehicle {
    Motorcycle () {
        this.type = "Motorcycle";
        // bounds set by a Honda Grom (5ft) and Harley Road Glide (9ft).
        this.size = rand.nextInt(5, 10);
        //grom acceleration = 6.77ft/s/s s1000rr acceleration = 46.5 ft/s/s
        //Acceleration can be anywhere between these 2 values
        this.accel = rand.nextInt(6, 46) + rand.nextDouble();
        this.time = System.nanoTime();
    }
}
class Truck extends Vehicle {
    Truck () {
        this.type = "Truck";
        //with a standard trailer length (53ft), semis average ~80 ft long
        //a normal box truck has ~10 ft worth of bed, + the length of the cab.
        this.size = rand.nextInt(18, 81);
        //acceleration for semi truck -> box truck
        this.accel = rand.nextInt(1, 4) + rand.nextDouble();
        this.time = System.nanoTime();
    }
}
class Car extends Vehicle {
    Car () {
        this.type = "Car";
        //smart cars are ~8ft, longest consumer car is ~22
        this.size = rand.nextInt(8, 22);
        //The Mitsubishi mirage ranks as the worlds slowest production car with 1.06 ft/s/s acceleration (actually)
        //The Rimac Nevera ranks as one of the fastest production accelerations at ~34.5 ft/s/s
        this.accel = rand.nextInt(1, 34) + rand.nextDouble();
        this.time = System.nanoTime();
    }
}