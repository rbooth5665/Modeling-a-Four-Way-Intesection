package org.example;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

/*
        My simulation will be based on a busy intersection close to my house. This intersection
        (SR-92 and Bells Ferry) has 4 lanes in each direction; two lanes of direct travel,
        1 left turn lane and 1 right turn lane.

        I am modeling my traffic simulation based on data gathered by the Georgia
        Department of Transportation and there use of Traffic Monitoring systems to
        accurately track vehicles. This information can be found here:
        GA Traffic Data Map
        https://gdottrafficdata.drakewell.com/publicmultinodemap.asp

        SR-92 Traffic Data
        https://gdottrafficdata.drakewell.com/sitedashboard.asp?node=GDOT_PORTABLES&cosit=0000057_0076

        Bells Ferry Traffic Data
        https://gdottrafficdata.drakewell.com/sitedashboard.asp?node=GDOT_PORTABLES&cosit=0000067_0907



        Time Conversions:
        real world -> simulation time
        ~17 milli ->  1 second
        1 second  ->  1 minute
        1 minute  ->  1 hour
*/
public class Main {
    public static void main(String[] arg) {
        Scanner in = new Scanner(System.in);

        //Creates thread safe lane queues to be managed. Initialized here to be manipulated by the lane and intersection classes
        ConcurrentLinkedQueue<Vehicle> nLeftTurn = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Vehicle> nRightTurn = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Vehicle> nStraight1 = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Vehicle> nStraight2 = new ConcurrentLinkedQueue<>();

        ConcurrentLinkedQueue<Vehicle> sLeftTurn = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Vehicle> sRightTurn = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Vehicle> sStraight1 = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Vehicle> sStraight2 = new ConcurrentLinkedQueue<>();

        ConcurrentLinkedQueue<Vehicle> eLeftTurn = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Vehicle> eRightTurn = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Vehicle> eStraight1 = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Vehicle> eStraight2 = new ConcurrentLinkedQueue<>();

        ConcurrentLinkedQueue<Vehicle> wLeftTurn = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Vehicle> wRightTurn = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Vehicle> wStraight1 = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Vehicle> wStraight2 = new ConcurrentLinkedQueue<>();

        //takes start and end times from user
        System.out.println("Which hour would you like to start? (military time 0 - 23)");
        int start = in.nextInt();
        System.out.println("Which hour would you like to end? (Military time 0 - 23)");
        int end = in.nextInt();

        //each lane object will gather direction and shared queues
        Lane north = new Lane('n', start, end, nLeftTurn, nRightTurn, nStraight1, nStraight2);
        Thread n = new Thread(north);
        Lane south = new Lane('s', start, end, sLeftTurn, sRightTurn, sStraight1, sStraight2);
        Thread s = new Thread(south);
        Lane east = new Lane('e', start, end, eLeftTurn, eRightTurn, eStraight1, eStraight2);
        Thread e = new Thread(east);
        Lane west = new Lane('w', start, end, wLeftTurn, wRightTurn, wStraight1, wStraight2);
        Thread w = new Thread(west);

        //each intersection object will gather direction and shared queues
        Intersection nLight = new Intersection('n', (north.getLam() * 60), start, end, nLeftTurn, nRightTurn, nStraight1, nStraight2);
        Thread nl = new Thread(nLight);
        Intersection sLight = new Intersection('s', (south.getLam() * 60), start, end, sLeftTurn, sRightTurn, sStraight1, sStraight2);
        Thread sl = new Thread(sLight);
        Intersection eLight = new Intersection('e', (east.getLam() * 60), start, end, eLeftTurn, eRightTurn, eStraight1, eStraight2);
        Thread el = new Thread(eLight);
        Intersection wLight = new Intersection('w', (west.getLam() * 60), start, end, wLeftTurn, wRightTurn, wStraight1, wStraight2);
        Thread wl = new Thread(wLight);

        //calculates the sum of critical flows for websters equations
        double d = nLight.getcFlow() + sLight.getcFlow() + eLight.getcFlow() + wLight.getcFlow();

        //to determine a full cycle length, get the largest critical flow ratio
        Intersection.websters(d);
        int x;

        //optimizes north/south and east/west based on respective largest cFlow values
        if(nLight.getcFlow() > sLight.getcFlow()) {
            nLight.optimizePhase(d);
            sLight.setPhase(nLight.getPhase());
            x = nLight.getPhase();
        }
        else {
            sLight.optimizePhase(d);
            nLight.setPhase(sLight.getPhase());
            x = sLight.getPhase();
        }
        //because the cycle length is the total length of all lights, cycle length - x will be the remaining amount of time left for east/west phase
        eLight.setPhase(Intersection.getCycle() - x);
        wLight.setPhase(eLight.getPhase());

        //gets start time
        long startTime = System.nanoTime();

        //Start producers
        n.start();
        s.start();
        e.start();
        w.start();

        //Start consumers
        nl.start();
        sl.start();
        el.start();
        wl.start();


        //simulation run time
        long duration = (end - start + 1) * 60L * 1000000000L;
        while(System.nanoTime() - startTime < duration) {
        }


        //takes capture of when the simulation stops running
        long endTime = System.nanoTime();
        long totalTime = (endTime - startTime)/1000000000;
        int[] ns = north.laneTotal();
        int[] ss = south.laneTotal();
        int[] es = east.laneTotal();
        int[] ws=  west.laneTotal();
        for(int i:ns) {
            System.out.print(i +" ");
        }
        System.out.println();
        for(int i:ss) {
            System.out.print(i +" ");
        }
        System.out.println();
        for(int i:es) {
            System.out.print(i +" ");
        }
        System.out.println();
        for(int i:ws) {
            System.out.print(i +" ");
        }
        System.out.println();

        System.out.println(north.getGenerate() +", "+south.getGenerate()+", "+east.getGenerate()+", "+west.getGenerate());
        System.out.println(nLight.getDeparture()+" Have travelled north, "+ sLight.getDeparture()+" have travelled south, "+ eLight.getDeparture()+" have travelled east "+ wLight.getDeparture()+" have travelled west");
        convertTime(totalTime);

        n.interrupt();
        s.interrupt();
        e.interrupt();
        w.interrupt();

        nl.interrupt();
        sl.interrupt();
        el.interrupt();
        wl.interrupt();

    }


    public static void convertTime(long time) {
        int minutes = 0;
        while(time >= 60) {
            minutes++;
            time -= 60;
        }
        System.out.println(minutes +" minute(s) "+time+" second(s)");
    }
}

/*
        System.out.print("At what hour would you like to start your simulation? ");
        int start = in.nextInt();
        System.out.print("At what hour would you like to end your simulation? ");
        int end = in.nextInt();

        int num = 1;

        //the random generation will depend on the hours chosen.
        if(end - start > 0) {
            num = end - start;
        }

        //Because my lanes have not been multithreaded yet, the interface looks a bit different, and is generating an hourly report.
        // I aim to make it fully real time by the end of the course.
        for(int i = 0; i < num; i++) {
            System.out.println("\nhour "+ (i + 1) +" loading....");
            south.generate(start, end);
            north.generate(start, end);
            east.generate(start, end);
            west.generate(start, end);

            sSize = south.size();
            nSize = north.size();
            eSize = east.size();
            wSize = west.size();

            System.out.println("\n\nARRIVING");
            north.totalVehicles();
            for (int j : nSize) {
                System.out.print(j + " ");
            }

            System.out.println();
            south.totalVehicles();
            for (int j : sSize) {
                System.out.print(j + " ");
            }

            System.out.println();
            east.totalVehicles();
            for (int j : eSize) {
                System.out.print(j + " ");
            }

            System.out.println();
            west.totalVehicles();
            for (int j : wSize) {
                System.out.print(j + " ");
            }

            //gets green splits. NS, EW
            double[] splits = intersection.greenSplit(nSize, sSize, eSize, wSize);

            //moves cars through the lane
            north.leave(intersection.getDimension(), (int)(intersection.getCycleLength() * splits[0]));
            south.leave(intersection.getDimension(), (int)(intersection.getCycleLength() * splits[0]));
            east.leave(intersection.getDimension(), (int)(intersection.getCycleLength() * splits[1]));
            west.leave(intersection.getDimension(), (int)(intersection.getCycleLength() * splits[1]));


            sSize = south.size();
            nSize = north.size();
            eSize = east.size();
            wSize = west.size();

            System.out.println("\n\n LEAVING");
            System.out.println("North/South Green-Split value: " +splits[0]+", East/West Green-Split value: "+splits[1]);

            north.totalVehicles();
            System.out.println(north.getDeparture() + " vehicles have left the northbound lanes with an average acceleration of " + Math.round(north.getSpeed() * 100)/100+". " +
                                                        "There is an average of "+ north.getDeparture()/num +" cars per hour");
            for (int j : nSize) {
                System.out.print(j + " ");
            }

            System.out.println();
            south.totalVehicles();
            System.out.println(south.getDeparture() + " vehicles have left the southbound lanes with an average acceleration of " + Math.round(south.getSpeed() * 100)/100 +
                                                        "There is an average of "+ south.getDeparture()/num +" cars per hour");
            for (int j : sSize) {
                System.out.print(j + " ");
            }

            System.out.println();
            east.totalVehicles();
            System.out.println(east.getDeparture() + " vehicles have left the eastbound lanes with an average acceleration of " + Math.round(east.getSpeed() * 100)/100 +
                                                        "There is an average of "+ east.getDeparture()/num +" cars per hour");
            for (int j : eSize) {
                System.out.print(j + " ");
            }

            System.out.println();
            west.totalVehicles();
            System.out.println(west.getDeparture() + " vehicles have left the westbound lanes with an average acceleration of " +Math.round(west.getSpeed() * 100)/100 +
                                                    "There is an average of "+ west.getDeparture()/num +" cars per hour");
            for (int j : wSize) {
                System.out.print(j + " ");
            }
            System.out.println();
        }
 */