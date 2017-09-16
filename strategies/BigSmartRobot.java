package strategies;

import automail.StorageTube;

/**
 * Created by neeserg on 15/09/2017.
 */
public class BigSmartRobot extends SmartRobotBehaviour implements OldIRobotBehaviour{
    protected boolean communicatedReturn(StorageTube tube){
        return false;
        }

}
