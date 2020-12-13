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

        try {
            mx.flush();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        // m.contactUninformed();
            //m.checkEntries();
        /*catch (ParseException | RemoteException e) {
            e.printStackTrace();
        }*/

    }
}
