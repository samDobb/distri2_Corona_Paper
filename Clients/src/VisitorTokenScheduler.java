import java.rmi.RemoteException;
import java.util.TimerTask;

public class VisitorTokenScheduler extends TimerTask {
    private Visitor v;

    public VisitorTokenScheduler(Visitor v){
        this.v=v;
    }
    @Override
    public void run(){
        try {
            v.sendSessionCapsule();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
