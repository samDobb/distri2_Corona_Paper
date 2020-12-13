import java.rmi.RemoteException;
import java.util.TimerTask;

public class VisitorDayScheduler extends TimerTask {
    private Visitor v;
    public VisitorDayScheduler(Visitor v){
        this.v=v;
    }
    @Override
    public void run(){
        //remove not used tokens
        v.shiftActiveTokens();
        //remove old logs
        v.removeLogs();

        try {
            //get critical logs
            v.getCriticalEntries();
            //get new tokens
            v.setNewTokens();

        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

}
