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


    //Light cycle algorithm (Green split?)

}
