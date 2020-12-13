import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class runServers {
    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
        MatchingServiceIMP matchingService = new MatchingServiceIMP();
        MixingProxyIMP mixingService=new MixingProxyIMP();
        RegistrarIMP registrarService=new RegistrarIMP();
        matchingService.connectRegister();

        //Schedulers
        PerDayScheduler perDay=new PerDayScheduler(matchingService,mixingService);
        LocalDateTime start=  LocalDateTime.now();
        System.out.println(start.toString());
        Timer t= new Timer();
        t.schedule(perDay,0,86400000);


    }
}
