package automail;

import exceptions.ExcessiveDeliveryException;
import strategies.Automail;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Properties;


/*changes made so far
* made simulation and object instead of running it
* made reportDelivery an external class and an info expert on deliveries
* turned all static variable in to private ones
* made all long methods shorter
*/
/**
 * This class simulates the behaviour of AutoMail
 */
public class Simulation {

    /** Constant for the mail generator */
    private static final int MAIL_TO_CREATE = 60;
    


    private MailGenerator mailGenerator;
    private Automail automail;
    private ReportDelivery reportDelivery;

    public static void main(String[] args){
/*    	// Should probably be using properties here
    	Properties automailProperties = new Properties();
		// Defaults
		automailProperties.setProperty("Name_of_Property", "20");  // Property value may need to be converted from a string to the appropriate type

		FileReader inStream = null;
		
		try {
			inStream = new FileReader("automail.properties");
			automailProperties.load(inStream);
		} finally {
			 if (inStream != null) {
	                inStream.close();
	            }
		}
		
		int i = Integer.parseInt(automailProperties.getProperty("Name_of_Property"));



*/

        Simulation simulation = new Simulation(args);
        simulation.run();






    }




    public Simulation(String[] args){


        /** Used to see whether a seed is initialized or not */
        HashMap<Boolean, Integer> seedMap = new HashMap<>();


        /** Read the first argument and save it as a seed if it exists */
        if(args.length != 0){
            int seed = Integer.parseInt(args[0]);
            seedMap.put(true, seed);
        } else{
            seedMap.put(false, 0);
        }
        reportDelivery = new ReportDelivery();
        automail = new Automail(reportDelivery);
        /** Initiate all the mail */
        mailGenerator= new MailGenerator(MAIL_TO_CREATE, automail.mailPool, seedMap);

    }



    public void run(){
        mailGenerator.generateAllMail();
        int priority;
        while(reportDelivery.numOfDelivery() != mailGenerator.MAIL_TO_CREATE) {
            //System.out.println("-- Step: "+Clock.Time());
            priority = mailGenerator.step();
            if (priority > 0) automail.robot.behaviour.priorityArrival(priority);
            observeRobot();
            Clock.Tick();
        }
        printResults();

    }


    private void observeRobot(){

        Robot.RobotState prevState = automail.robot.getCurrent_state();
        try {
            automail.robot.step();
        } catch (ExcessiveDeliveryException e) {
            e.printStackTrace();
            System.out.println("Simulation unable to complete..");
            System.exit(0);
        }






    }










    public void printResults(){
        System.out.println("T: "+Clock.Time()+" | Simulation complete!");
        System.out.println("Final Delivery time: "+Clock.Time());
        System.out.printf("Final Score: %.2f%n", reportDelivery.getTotalScore());
    }
}
