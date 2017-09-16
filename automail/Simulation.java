package automail;

import exceptions.ExcessiveDeliveryException;
import strategies.AutoMailDependecies;
import strategies.Automail;
import strategies.NewIRobotBehaviour;
import strategies.IRobotBehaviour;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
   	// Should probably be using properties here
    	Properties automailProperties = new Properties();
		// Property value may need to be converted from a string to the appropriate type

		FileReader inStream = null;
		
		try {
			inStream = new FileReader("automail.properties");
			automailProperties.load(inStream);
		} catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
			 if (inStream != null) {
                 try {
                     inStream.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
		}

        Simulation simulation = new Simulation(automailProperties);
        simulation.run();






    }




    public Simulation(Properties automailProperties){


        /** Used to see whether a seed is initialized or not */

        /** Read the first argument and save it as a seed if it exists */

        AutoMailDependecies autoMailDependecies = new AutoMailDependecies(automailProperties);

        reportDelivery = new ReportDelivery();
        automail = new Automail(reportDelivery,autoMailDependecies);
        /** Initiate all the mail */

        if(automailProperties.containsKey("Seed")){
            int seed = Integer.parseInt(automailProperties.getProperty("Seed"));
            mailGenerator= new MailGenerator(MAIL_TO_CREATE, automail.mailPool, seed);
        } else{
            mailGenerator= new MailGenerator(MAIL_TO_CREATE, automail.mailPool);
        }

    }



    public void run(){
        mailGenerator.generateAllMail();
        int priority;
        while(reportDelivery.numOfDelivery() != mailGenerator.MAIL_TO_CREATE) {
            //System.out.println("-- Step: "+Clock.Time());
            priority = mailGenerator.step();
            if (priority > 0) {
                IRobotBehaviour robotBehaviour = automail.robot.behaviour;
                if (robotBehaviour instanceof  NewIRobotBehaviour) {
                    ((NewIRobotBehaviour) robotBehaviour).priorityArrival(priority);
                }
            }
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
