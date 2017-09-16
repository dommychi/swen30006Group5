package automail;

import exceptions.ExcessiveDeliveryException;
import strategies.Automail;
import strategies.NewIRobotBehaviour;
import strategies.IRobotBehaviour;


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
    


    private MailGenerator mailGenerator;
    private Automail automail;
    private ReportDelivery reportDelivery;

    public static void main(String[] args){

        Simulation simulation = new Simulation();
        simulation.run();



    }




    public Simulation(){


        /** Used to see whether a seed is initialized or not */

        /** Read the first argument and save it as a seed if it exists */

        AutoMailFactory autoMailFactory = new AutoMailFactory();

        reportDelivery = new ReportDelivery();
        automail = new Automail(reportDelivery, autoMailFactory);
        mailGenerator = autoMailFactory.generateMailGenerator(automail.mailPool);
        /** Initiate all the mail */




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
