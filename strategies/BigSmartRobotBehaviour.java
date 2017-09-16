package strategies;

import automail.Clock;
import automail.MailItem;
import automail.PriorityMailItem;
import automail.StorageTube;
import exceptions.TubeFullException;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by neeserg on 15/09/2017.
 */
public class BigSmartRobotBehaviour implements IRobotBehaviour {

    public int newPriorityArrival;
    
    @Override
    public boolean returnToMailRoom(StorageTube tube) {
        // Check if my tube contains only priority items
        if(!tube.isEmpty()){
            int priorityCount = 0;
            int nonPriorityCount = 0;
            // There has to be more priority than non-priority to keep going
            for(MailItem m : tube.tube){
                if(m instanceof PriorityMailItem){
                    priorityCount++;
                }
                else{
                    nonPriorityCount++;
                }
            }

            if(priorityCount >= nonPriorityCount){
                return false;
            }
            else{
                // Check if there is more than 1 priority arrival and the size of the tube is greater than or equal to half
                return communicatedReturn(tube);


            }
        }
        else{
            return true;
        }
    }

    protected boolean communicatedReturn(StorageTube tube){


            return false;
    }



    @Override
    public boolean fillStorageTube(IMailPool mailPool, StorageTube tube) {

        ArrayList<MailItem> tempTube = new ArrayList<MailItem>();

        // Empty my tube
        while(!tube.tube.isEmpty()){
            mailPool.addToPool(tube.pop());
        }

        // Grab priority mail
        while(tempTube.size() < tube.MAXIMUM_CAPACITY){
            if(containMail(mailPool,MailPool.PRIORITY_POOL)){
                tempTube.add(mailPool.getHighestPriorityMail());
            }
            else{
                // Fill it up with non priority
                if(containMail(mailPool,MailPool.NON_PRIORITY_POOL)){
                    tempTube.add(mailPool.getNonPriorityMail());
                }
                else{
                    break;
                }

            }
        }

        // Sort tempTube based on floor
        tempTube.sort(new BigSmartRobotBehaviour.ArrivalComparer());

        // Iterate through the tempTube
        while(tempTube.iterator().hasNext()){
            try {
                tube.addItem(tempTube.remove(0));
            } catch (TubeFullException e) {
                e.printStackTrace();
            }
        }

        // Check if there is anything in the tube
        if(!tube.tube.isEmpty()){
            newPriorityArrival = 0;
            return true;
        }
        return false;
    }

    private boolean containMail(IMailPool m, String mailPoolIdentifier){
        if(mailPoolIdentifier.equals(MailPool.PRIORITY_POOL) && m.getPriorityPoolSize() > 0){
            return true;
        }
        else if(mailPoolIdentifier.equals(MailPool.NON_PRIORITY_POOL) && m.getNonPriorityPoolSize() > 0){
            return true;
        }
        else{
            return false;
        }
    }

    private class ArrivalComparer implements Comparator<MailItem> {

        @Override
        public int compare(MailItem m1, MailItem m2) {
            return MailPool.compareArrival(m1, m2);
        }

    }

}
