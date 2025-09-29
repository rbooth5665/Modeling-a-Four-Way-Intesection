public class Vehicle {
int size;
float accel;
char type;



//getter and setter methods
    public char getType() {
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
        this.type = 'm';
        this.size = s;
        this.accel = a;
    }
}

class Truck extends Vehicle {
    Truck (){}
    Truck (int s, float a) {
        this.type = 't';
        this.size = s;
        this.accel = a;
    }
}

class Car extends Vehicle {
    Car (){}
    Car (int s, float a) {
        this.type = 'c';
        this.size = s;
        this.accel = a;
    }
}
