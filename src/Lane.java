import java.util.LinkedList;
import java.util.Queue;

public class Lane {
    //handles the specific lane queues
    Queue<Vehicle> leftTurn = new LinkedList<>();
    Queue<Vehicle> laneOne = new LinkedList<>();
    Queue<Vehicle> laneTwo = new LinkedList<>();
    Queue<Vehicle> rightTurn = new LinkedList<>();

    private int[][] SR92 = {
           //East Travel "Average Hourly Volume" from GDOT
            {76, 48, 56, 69, 144, 409, 1124, 1736, 1473, 1249, 1122, 1119, 1145, 1182, 1211, 1244, 1446, 1696, 1336, 745, 512, 374, 249, 120},
            //West Travel "Average Hourly Volume" from GDOT
            {102, 70, 29, 46, 80, 204, 585, 754, 804, 758, 796, 935, 1096, 1243, 1320, 1422, 1640, 1672, 1510, 1102, 732, 512, 328, 194}
    };
    private int[][] BFerry = {
            //North Travel "Average Hourly Volume" from GDOT
            {92, 58, 38, 30, 40, 84, 262, 438, 533, 532, 548, 592, 720, 794, 940, 1101, 1384, 1336, 1235, 960, 696, 497, 282, 187},
            //South Travel "Average Hourly Volume" from GDOT
            {82, 50, 38, 55, 126, 308, 672, 941, 876, 754, 690, 685, 772, 820, 912, 986, 1126, 1152, 1064, 858, 592, 450, 246, 156}
    };

    //Variables to store the poisson distribution number at each direction, used by generator
    double poissonN;
    double poissonS;
    double poissonE;
    double poissonW;



    //access to lights at intersection
    Intersection i;



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


    //distribution method to distribute vehicles to separate lanes
    //factorial implementation for Poisson Distribution using long data type to handle large values
    public static long factorial (int a) {
        long r = 1;
        for(int i = 1; i <= a; i++) {
            r *= i;
        }
        return r;
    }

    //Since cars can only arrive in whole numbers, the minute rate will be converted to a whole number
    //Converts the hourly rate to cars per minute rate
    private static int hourToMinuteRate(int c) {
        return Math.round(((float)c) / 60.0f);
    }


    //calculates the average traffic between passed start/end time
    private static double getLambda(int start, int end) {

    }



    /*
    implements poisson distribution, where x is the expected number of cars
    l (lambda) is the average rate of car arrival over an interval of time
    and e is eulers number
     */
    public static double poissonDistribution (int x, double l) {
        //poissons alg -> P(x) = (l^x * e^ -l)/factorial(x)
        //calculate the top half
        double lam = Math.pow(l, x);
        double euler = Math.pow(Math.E, -l);
        double top = lam * euler;

        //returns poissons probability
        return top/factorial(x);
    }



    //car generator for lanes
    public void generate (int cars, int start, int end) {
        /*
        method takes the total number of cars wanted, converts it to cars per minute,
        calculates poissons distribution
         */
        int carsPerMinute = hourToMinuteRate(cars);
        double chance = poissonDistribution(carsPerMinute, .5);

        //now that poisson has been calculated, begin dispersing cars based on this chance.
        //weight the turn lanes to be less than the main lanes of travel
    }

}
