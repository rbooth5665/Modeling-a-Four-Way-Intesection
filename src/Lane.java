import java.util.LinkedList;
import java.util.Queue;
public class Lane {
    //handles the specific lane queues
    boolean light;
    Queue<Vehicle> leftTurn = new LinkedList<>();
    Queue<Vehicle> laneOne = new LinkedList<>();
    Queue<Vehicle> laneTwo = new LinkedList<>();
    Queue<Vehicle> rightTurn = new LinkedList<>();

    //Constructors
    Lane() {};

    //General methods

    //sets light for each lane of travel
    public void setLight(boolean b) {
        this.light = b;
    }

    //Returns total number of vehicle objects held in the lanes
    public int totalCapacity() {
        return this.leftTurn.size() + this.laneOne.size() + this.laneTwo.size() + this.rightTurn.size();
    }

    //returns specific number of vehicles in each lane
    public String totalVehicles() {
        int m = 0, c = 0, t = 0;
        //if queues are not empty, iterate through and parse total number of vehicles
        if(!leftTurn.isEmpty()) {
            for(Vehicle v: leftTurn) {
                switch(v.getType()) {
                    case 'm' -> m++;

                    case 't' -> t++;

                    case 'c' -> c++;
                }
            }
        }
        if(!laneOne.isEmpty()) {
            for(Vehicle v: laneOne) {
                switch(v.getType()) {
                    case 'm' -> m++;

                    case 't' -> t++;

                    case 'c' ->
                            c++;
                }
            }
        }
        if(!laneTwo.isEmpty()) {
            for(Vehicle v: laneTwo) {
                switch(v.getType()) {
                    case 'm' -> m++;

                    case 't' -> t++;

                    case 'c' ->
                            c++;
                }
            }
        }
        if(!rightTurn.isEmpty()) {
            for(Vehicle v: rightTurn) {
                switch(v.getType()) {
                    case 'm' -> m++;

                    case 't' -> t++;

                    case 'c' ->
                            c++;
                }
            }
        }
        return "this direction of travel has: " +m+ "Motorcycles, "+c+"Cars, and "+t+"Large Trucks.";
    }




    //distribution method to distribute vehicles to separate lanes
    //factorial implementation for Poisson Distribution
    public static int factorial (int a) {
        if (a == 0 || a == 1) {
            return 1;
        }
        else {
            return a * factorial(a - 1);
        }
    }

    //implement random generation. Time block will determine a number of cars that will show up over a 6 hour block.
    //This number will then be converted into cars per minute. The Poisson method will return the probability of a car arriving each minute.
    //This will be used in tandem with a wait call to see if a car will be generated every minute.
    public static double poissonDistribution (double x) {
        //poissons alg -> P(x) = (l^x * e^ -l)/factorial(x)
        return -1.0;
    }

}
