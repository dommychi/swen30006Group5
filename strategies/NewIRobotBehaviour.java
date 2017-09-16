package strategies;

/**
 * Created by neeserg on 15/09/2017.
 */
public interface NewIRobotBehaviour extends OldIRobotBehaviour {

    /**
     * @param priority is that of the mail item which just arrived.
     */
    public boolean priorityArrival(int priority);

}
