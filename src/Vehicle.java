public class Vehicle {
int size;
float accel;



//setter methods
public void setSize (int s) {
    this.size = s;
}
public void setAccel (float f) {
    this.accel = f;
    }
}

class Motorcycle extends Vehicle {
    //Vehicle Constructors
    Motorcycle (){}
    Motorcycle (int s,float a) {
        setSize(s);
        setAccel(a);
    }
}

class Truck extends Vehicle {
    Truck (){}
    Truck (int s, float a) {
        setSize(s);
        setAccel(a);
    }
}

class Car extends Vehicle {
    Car (){}
    Car (int s, float a) {
        setSize(s);
        setAccel(a);
    }
}
