import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.TimerTask;

public class PerDayScheduler extends TimerTask {
    private MatchingServiceIMP m;
    private MixingProxyIMP mx;
    public PerDayScheduler(MatchingServiceIMP m,MixingProxyIMP mx){
        this.m=m;
        this.mx=mx;
    }
    @Override
    public void run(){

        //remove keys of the day
        mx.removeAllPublicKeys();
        //remove overtime capsules
        m.removeCapsules();
        try {
            System.out.println("Going to flush the received capsules");
            mx.flush();
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }
}
