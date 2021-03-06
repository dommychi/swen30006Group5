package strategies;

import automail.AutoMailFactory;
import automail.IMailDelivery;
import automail.Robot;

public class Automail {


	      
    public Robot robot;
    public IMailPool mailPool;
    
    public Automail(IMailDelivery delivery, AutoMailFactory autoMailFactory) {
    	// Swap between simple provided strategies and your strategies here
    	    	
    	/** Initialize the MailPool */
    	mailPool = new MailPool();
    	
        /** Initialize the RobotAction */
    	// IRobotBehaviour robotBehaviour = new SimpleRobotBehaviour();
    	IRobotBehaviour robotBehaviour = autoMailFactory.generateBehaviour();
    	    	
    	/** Initialize robot */
    	robot = new Robot(robotBehaviour, delivery, mailPool, autoMailFactory.getBuilding());
    	
    }



    
}
