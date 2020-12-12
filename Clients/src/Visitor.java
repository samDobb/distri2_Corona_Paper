import javax.imageio.spi.RegisterableService;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Visitor {
    private String username;
    private String telephoneNumber;

    private String clientServiceName;
    private String clientAddres;

    private Registrar registrar;

    private List<String> pastTokens;
    private List<String> activeTokens;

    private List<byte[]> signatures;
    private PublicKey publicKey;

    private List<QRcode> qRcodes;
    private List<Calendar> qRtimes;

    private MixingProxy mixingProxy;


    private List<ClientLog> logs;

    private int tokenTime=30; //in minutes
    private int entryTime=14; //in days

    public Visitor(String username, String telephoneNumber) throws RemoteException {
        super();

        this.username = username;
        this.telephoneNumber = telephoneNumber;

        qRcodes=new ArrayList<>();
        qRtimes=new ArrayList<>();
        signatures = new ArrayList<>();
    }

    public void readQr(QRcode qr){
        qRcodes.add(qr);
        qRtimes.add(java.util.Calendar.getInstance());
    }

    //add a log
    public void addLog(int random,String name,byte[] line,byte[] token){
        Date firstDate=java.util.Calendar.getInstance().getTime();

        Calendar c = java.util.Calendar.getInstance();
        c.add(Calendar.MINUTE,30);
        Date secondDate= c.getTime();

        logs.add(new ClientLog(random,name,line,token,firstDate,secondDate));
    }

    //remove all the logs where the time is longer than 2 weeks ago
    public void checkLogs() throws ParseException {

        Date currentDate = java.util.Calendar.getInstance().getTime();

        List<ClientLog> removedLogs=new ArrayList<>();

        //searching for all the logs that need to be removed
        for(ClientLog log:logs) {

            long diffInMillies = Math.abs(currentDate.getTime() - log.getEntryTime().getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if(diff > entryTime){
                removedLogs.add(log);
            }
        }

        //removing the marked logs
        for(ClientLog log:removedLogs){
            logs.remove(log);
        }

    }

    public void shiftActiveTokens(){
        for(String token:activeTokens){
            pastTokens.add(token);
        }
        activeTokens.clear();
    }

    public void getCriticalEntries() throws RemoteException {
        List<Capsule> criticalEntries=mixingProxy.sendCriticalLogs();
    }

    @Override
    public String toString() {
        return "VisitorIMP{" +
                "username='" + username + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                '}';
    }
}