package org.example;

import java.util.Scanner;

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
         */

public class Main {
    public static void main(String[] arg) {
        //General objects
        Scanner in = new Scanner(System.in);

        //Creates Lanes and an intersection
        Intersection intersection = new Intersection();
        Lane north = new Lane(intersection, 'n');
        Lane south = new Lane(intersection, 's');
        Lane west = new Lane(intersection, 'w');
        Lane east = new Lane(intersection, 'e');
        int[] sSize;
        int[] nSize;
        int[] eSize;
        int[] wSize;

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
            south.generate('s', start, end);
            north.generate('n',start, end);
            east.generate('e', start, end);
            west.generate('w', start, end);

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
    }
}
