package strategies;

import automail.Clock;

import java.util.Properties;

/**
 * Created by neeserg on 16/09/2017.
 */
public class AutoMailDependecies {

    private Properties autoMailPropertes;
    Clock clock;

    public AutoMailDependecies(Properties autoMailPropertes){
        this.autoMailPropertes = autoMailPropertes;





    }

    public IRobotBehaviour generateBehaviour(){

        String robotBehaviour = autoMailPropertes.getProperty("Robot_Type");

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





}
