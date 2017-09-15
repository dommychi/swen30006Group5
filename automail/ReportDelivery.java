package automail;

import exceptions.MailAlreadyDeliveredException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neeserg on 15/09/2017.
 */
public class ReportDelivery implements IMailDelivery {

    public double getTotalScore() {
        return totalScore;
    }

    private double totalScore;

    private List <MailItem> MAIL_DELIVERED;

    public int numOfDelivery(){
        return MAIL_DELIVERED.size();
    }

    public ReportDelivery(){
        totalScore = 0;
        MAIL_DELIVERED = new ArrayList<MailItem>();
    }

    /** Confirm the delivery and calculate the total score */
    public void deliver(MailItem deliveryItem){
        if(!MAIL_DELIVERED.contains(deliveryItem)){
            System.out.println("T: "+Clock.Time()+" | Delivered " + deliveryItem.toString());
            MAIL_DELIVERED.add(deliveryItem);
            // Calculate delivery score
            totalScore += calculateDeliveryScore(deliveryItem);
        }
        else{
            try {
                throw new MailAlreadyDeliveredException();
            } catch (MailAlreadyDeliveredException e) {
                e.printStackTrace();
            }
        }
    }

    private  double calculateDeliveryScore(MailItem deliveryItem) {
        // Penalty for longer delivery times
        final double penalty = 1.1;
        double priority_weight = 0;
        // Take (delivery time - arrivalTime)**penalty * (1+sqrt(priority_weight))
        if(deliveryItem instanceof PriorityMailItem){
            priority_weight = ((PriorityMailItem) deliveryItem).getPriorityLevel();
        }
        return Math.pow(Clock.Time() - deliveryItem.getArrivalTime(),penalty)*(1+Math.sqrt(priority_weight));
    }

}
