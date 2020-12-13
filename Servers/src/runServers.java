import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class runServers {
    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
        MatchingServiceIMP matchingService = new MatchingServiceIMP();
        MixingProxyIMP mixingService=new MixingProxyIMP();
        RegistrarIMP registrarService=new RegistrarIMP();
        matchingService.connectRegister();

        LocalDateTime prevTime=  LocalDateTime.now();

        /*while (true){
            LocalDateTime nextTime= LocalDateTime.now().minusDays(1);

            if(prevTime.isAfter(nextTime)){
                prevTime=LocalDateTime.now();

                mixingService.flush();

                matchingService.contactUninformed();

                try {
                    matchingService.checkEntries();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }*/
    }
}
