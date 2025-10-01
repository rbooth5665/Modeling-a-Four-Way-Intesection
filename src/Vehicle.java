package org.example;

import java.util.Random;
public class Vehicle {
    int size;
    double accel;
    String type;
    Random rand = new Random();


    //getter and setter methods
    public String getType() {
        return type;
    }
    public double getAccel() {
        return accel;
    }
    public int getSize() {
        return size;
    }
    //returns the time to cross a given dimension using an acceleration in ft/s/s
    public double timeToCross(int dimension) {
        double partial = (2 * dimension) / accel;
        return Math.sqrt(partial);
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
    }
}
class Truck extends Vehicle {
    Truck (){}
    Truck (int s, float a) {
        this.type = "Truck";
        //with a standard trailer length (53ft), semis peak at 80 ft long
        //a normal box truck has ~10 ft worth of bed, + the length of the cab.
        this.size = rand.nextInt(18, 81);
        //acceleration for semi truck -> box truck
        this.accel = rand.nextInt(1, 4) + rand.nextDouble();;
    }
}
class Car extends Vehicle {
    Car (){}
    Car (int s, float a) {
        this.type = "Car";
        this.size = s;
        this.accel = a;
    }
}