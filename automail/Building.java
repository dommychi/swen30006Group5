package automail;

public class Building {
	
	
    /** The number of floors in the building **/
    public final int FLOORS;
    
    /** Represents the ground floor location */
    public final int LOWEST_FLOOR;
    
    /** Represents the mailroom location */
    public final int MAILROOM_LOCATION;

    Building(int FLOORS, int LOWEST_FLOOR, int MAILROOM_LOCATION){
        this.FLOORS = FLOORS;
        this.LOWEST_FLOOR = LOWEST_FLOOR;
        this.MAILROOM_LOCATION = MAILROOM_LOCATION;
    }

}
