package strategies;

import java.util.ArrayList;
import java.util.Comparator;

import automail.Clock;
import automail.MailItem;
import automail.PriorityMailItem;
import automail.StorageTube;
import exceptions.TubeFullException;

public class SmartRobotBehaviour extends BigSmartRobotBehaviour implements NewIRobotBehaviour {



	@Override
	protected boolean communicatedReturn(StorageTube tube){
		if(newPriorityArrival > 1 && tube.getSize() >= tube.MAXIMUM_CAPACITY/2){
			return true;
		}
		else{
			return false;
		}
	}


	public boolean priorityArrival(int priority) {
		// Record that a new one has arrived
		newPriorityArrival++;
		System.out.println("T: "+Clock.Time()+" | Priority arrived");
		return true;
	}

}
