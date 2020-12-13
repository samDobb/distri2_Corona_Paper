import java.rmi.RemoteException;
import java.util.TimerTask;

public class TokenScheduler extends TimerTask {
    private Visitor v;

    public TokenScheduler(Visitor v){
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
