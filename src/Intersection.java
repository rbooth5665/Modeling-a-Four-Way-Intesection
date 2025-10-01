package org.example;

public class Intersection {
    //creates 4 separate lights for each direction
    Boolean north;
    Boolean south;
    Boolean east;
    Boolean west;

    //default constructor
    Intersection() {
        north = false;
        south = false;
        east = false;
        west = false;
    }

    //getters/setters
    public void setLight(char n, boolean b) {
        //uses a switch to determine which light to set
        //directions opposite each other (north/south and east/west) are assumed to have the same light cycle
        //redundancy included to account for any input of n,s,w,e
        switch(n) {
            case 'n' -> north = south = b;
            case 'e' -> east = west = b;
            case 'w' -> west = east = b;
            case 's' -> south = north = b;
        }
    }
    public boolean getNorthSouthLight() {
        return north;
    }
    public boolean getEastWestLight() {
        return east;
    }


    //Light cycle algorithm (Green split?) takes all lane sizes in, factors to set light
    public void greenSplit(int[] n, int[] s, int[] e, int[] w) {
       //hardcoded as the size arrays will always be in the same format. Gathers size of straight moving lanes
        int eastWest = e[1] + e[2] + w[1] + w[2];
        int northSouth = n[1] + n[2] + s[1] + s[2];

        if(eastWest > northSouth) {
            //set east-west light for a calculated amount of time
        }
        else {
            //set north-south light for calculated amount of time
        }


    }
}