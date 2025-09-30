package org.example;

import java.util.Random;
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
        Random rand = new Random();
        Scanner in = new Scanner(System.in);

        //Creates Lanes and an intersection
        Intersection intersection = new Intersection();
        Lane north = new Lane(intersection);
        Lane south = new Lane(intersection);
        Lane west = new Lane(intersection);
        Lane east = new Lane(intersection);

        System.out.print("What hour would you like to start monitoring? (choose an hour incremented time 0-23)");
        int start = in.nextInt();
        System.out.println("What hour would you like to stop monitoring on? (choose an hour increment greater than your start time)");
        int end = in.nextInt();

        //the random generation will depend on the hours chosen.

        north.generate(480,5, 8, 'n');




    }
}