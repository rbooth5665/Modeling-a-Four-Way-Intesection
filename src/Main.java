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
        int[] sSize;
        int[] nSize;
        int[] eSize;
        int[] wSize;


        //the random generation will depend on the hours chosen.
        south.generate('s', 6, 9);
        north.generate('n',6, 9);
        east.generate('e', 6, 9);
        west.generate('w', 6, 9);

        south.totalVehicles();
        north.totalVehicles();
        east.totalVehicles();
        west.totalVehicles();

        sSize = south.size();
        nSize = north.size();
        eSize = east.size();
        wSize = west.size();

        for (int k : sSize) {
            System.out.print(k + " ");
        }
        System.out.println();
        for (int j : nSize) {
            System.out.print(j + " ");
        }
        System.out.println();
        for (int k : eSize) {
            System.out.print(k + " ");
        }
        System.out.println();
        for (int k : wSize) {
            System.out.print(k + " ");
        }


        //will call light algo here
        double[] splits = intersection.greenSplit(nSize, sSize, eSize, wSize);

        System.out.println();
        for (double k : splits) {
            System.out.print(k + " ");
        }


    }
}