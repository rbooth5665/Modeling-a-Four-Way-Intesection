public class Vehicle {
int size;
float accel;
String type;



//getter and setter methods
    public String getType() {
        return type;
    }
    public float getAccel() {
        return accel;
    }
    public int getSize() {
        return size;
    }
}

class Motorcycle extends Vehicle {
    //Vehicle Constructors
    Motorcycle (){}
    Motorcycle (int s,float a) {
        this.type = "Motorcycle";
        this.size = s;
        this.accel = a;
    }
}

class Truck extends Vehicle {
    Truck (){}
    Truck (int s, float a) {
        this.type = "Truck";
        this.size = s;
        this.accel = a;
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
