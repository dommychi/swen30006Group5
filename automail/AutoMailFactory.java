package automail;

import automail.Clock;
import strategies.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by neeserg on 16/09/2017.
 */


/*
*thuis class reads the property file and iitializes any states that depend full on the properties
*
*
 */
public class AutoMailFactory {

    private Properties automailProperties;
    Clock clock;
    Building building;

    public AutoMailFactory(){
        this.automailProperties = fillProperty(new Properties());
        building = initializeBuilding();

    }
//initializes the thebuliding from the property
    private Building initializeBuilding(){
        int floors = Integer.parseInt(automailProperties.getProperty("Number_of_Floors"));
        int lowestFloor = Integer.parseInt(automailProperties.getProperty("Lowest_Floor"));
        int mailroomLocation = Integer.parseInt(automailProperties.getProperty("Location_of_MailRoom"));

        Building building = new Building(floors,lowestFloor,mailroomLocation);
        return building;
    }


//fills up the automailProperty object
    private Properties fillProperty(Properties automailProperties){
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

        return automailProperties;
    }

    public IRobotBehaviour generateBehaviour(){

        String robotBehaviour = automailProperties.getProperty("Robot_Type");

        if (robotBehaviour.equals("Small_Comms_Simple")){
            IRobotBehaviour oldBehaviour = new SimpleRobotBehaviour();
            return oldBehaviour;
        }

        else if (robotBehaviour.equals("Small_Comms_Smart")){
            IRobotBehaviour oldBehaviour = new SmartRobotBehaviour();
            return oldBehaviour;
        }

        else {
            IRobotBehaviour oldBehaviour = new BigSmartRobotBehaviour();
            return oldBehaviour;
        }


    }

    public MailGenerator generateMailGenerator(IMailPool mailPool){

        int mailToCreate = Integer.parseInt(automailProperties.getProperty("Mail_to_Create"));
        MailGenerator mailGenerator;
        if(automailProperties.containsKey("Seed")){
            int seed = Integer.parseInt(automailProperties.getProperty("Seed"));
            mailGenerator= new MailGenerator(mailToCreate, mailPool, seed, building);
        } else{
            mailGenerator= new MailGenerator(mailToCreate, mailPool, building);
        }
        return mailGenerator;
    }


    public Building getBuilding(){
        return building;

    }


}
