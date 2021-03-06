package automail;

import exceptions.ExcessiveDeliveryException;
import strategies.IMailPool;
import strategies.IRobotBehaviour;

/**
 * The robot delivers mail!
 */

/*
*
*
 */
public class Robot {

	StorageTube tube;
    IRobotBehaviour behaviour;
    IMailDelivery delivery;
    Building building;
    /** Possible states the robot can be in */
    public enum RobotState { DELIVERING, WAITING, RETURNING }

    public RobotState getCurrent_state() {
        return current_state;
    }

    private RobotState current_state;



    private int current_floor;
    private int destination_floor;
    private IMailPool mailPool;

    private MailItem deliveryItem;
    
    private int deliveryCounter;

    private final int MAIL_ROOM;
    

    /**
     * Initiates the robot's location at the start to be at the mailroom
     * also set it to be waiting for mail.
     * @param behaviour governs selection of mail items for delivery and behaviour on priority arrivals
     * @param delivery governs the final delivery
     * @param mailPool is the source of mail items
     */
    public Robot(IRobotBehaviour behaviour, IMailDelivery delivery, IMailPool mailPool, Building building){
        // current_state = RobotState.WAITING;
    	current_state = RobotState.RETURNING;
    	MAIL_ROOM=6;
        current_floor = MAIL_ROOM;
        tube = new StorageTube();
        this.behaviour = behaviour;
        this.delivery = delivery;
        this.mailPool = mailPool;
        this.deliveryCounter = 0;
        this.building = building;
    }

    /**
     * This is called on every time step
     * @throws ExcessiveDeliveryException if robot delivers more than the capacity of the tube without refilling
     */
    public void step() throws ExcessiveDeliveryException{
    	
    	boolean go = false;
    	
    	switch(current_state) {
    		/** This state is triggered when the robot is returning to the mailroom after a delivery */
    		case RETURNING:
    			/** If its current position is at the mailroom, then the robot should change state */
                if(current_floor == building.MAILROOM_LOCATION){
                	changeState(RobotState.WAITING);
                } else {
                	/** If the robot is not at the mailroom floor yet, then move towards it! */
                    moveTowards(building.MAILROOM_LOCATION);
                	break;
                }

    		case WAITING:
    			/** Tell the sorter the robot is ready */
                go = behaviour.fillStorageTube(mailPool, tube);
                // System.out.println("Tube total size: "+tube.getTotalOfSizes());
                /** If the StorageTube is ready and the Robot is waiting in the mailroom then start the delivery */
                if(go){
                	
                	deliveryCounter = 0; // reset delivery counter
                	setRoute();
                	changeState(RobotState.DELIVERING);
                }
                break;
    		case DELIVERING:
    			/** Check whether or not the call to return is triggered manually **/
    			if(current_floor == destination_floor){ // If already here drop off item
                   makeDelivery();
    			} else
    			{
	    			if(behaviour.returnToMailRoom(tube)){  // Robot requested return
	    				changeState(RobotState.RETURNING);
	    			}
	    			else{
	        			/** The robot is not at the destination yet, move towards it! */
	                    moveTowards(destination_floor);
	    			}
    			}
                break;
    	}
    }

    /**
     * Sets the route for the robot
     */
    private void setRoute(){
        /** Pop the item from the StorageUnit */
        deliveryItem = tube.peek();
        /** Set the destination floor */
        destination_floor = deliveryItem.getDestFloor();
    }

    /** Generic function that moves the robot towards the destination */
    private void moveTowards(int destination){
        if(current_floor < destination){
            current_floor++;
        }
        else{
            current_floor--;
        }
    }
    
    /**
     * Prints out the change in state
     * @param nextState
     */
    private void changeState(RobotState nextState){
    	if (current_state != nextState) {
    		System.out.println("T: "+Clock.Time()+" | Robot changed from "+current_state+" to "+nextState);
    	}
    	current_state = nextState;
    	if(nextState == RobotState.DELIVERING){
    		System.out.println("T: "+Clock.Time()+" | Deliver   " + deliveryItem.toString());
    	}
    }

    private void makeDelivery() throws ExcessiveDeliveryException {
        /** Delivery complete, report this to the simulator! */
        delivery.deliver(deliveryItem);
        tube.pop();
        deliveryCounter++;
        if(deliveryCounter > 4){
            throw new ExcessiveDeliveryException();
        }
        /** Check if want to return or if there are more items in the tube */
        if(tube.isEmpty() || behaviour.returnToMailRoom(tube)){ // No items or robot requested return
            changeState(RobotState.RETURNING);
        }
        else{
            /** If there are more items, set the robot's route to the location to deliver the item */
            setRoute();
            changeState(RobotState.DELIVERING);
        }


    }



}
