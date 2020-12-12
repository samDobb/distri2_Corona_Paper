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

public class VisitorIMP extends UnicastRemoteObject implements Visitor {
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

    private MatchingService matchingService;


    private List<ClientLog> logs;

    private int entryTime=14;

    public VisitorIMP(String username, String telephoneNumber) throws RemoteException {
        super();

        this.username = username;
        this.telephoneNumber = telephoneNumber;

        qRcodes=new ArrayList<>();
        qRtimes=new ArrayList<>();
        signatures = new ArrayList<>();
    }

    //making connection to the registry and starting the rmi for this facility
    public boolean startClient() {

        clientServiceName = "Visitor_" + username;
        clientAddres = "localhost";

        try {
            registrar = (Registrar) Naming.lookup("rmi://localhost/Registrar");
            matchingService = (MatchingService) Naming.lookup("rmi://localhost/MatchingService");

            //checking if name is not taken
            if (!registrar.checkUserName(username) && !registrar.checkUserTel(telephoneNumber)) {

                //starting rmi for this facility
                Naming.rebind("rmi://" + clientAddres + "/" + clientServiceName, this);
                System.out.println(username + " is running\n");

                //registering this facility to the registrar
                String[] details = {username, telephoneNumber, clientAddres, clientServiceName};
                registrar.enrollNewUser(details);

            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void setTokens(List<String> tokens, List<byte[]> signatures, PublicKey publicKey) {
        shiftActiveTokens();
        this.activeTokens = tokens;
        this.signatures=signatures;
        this.publicKey=publicKey;
    }

    public void readQr(QRcode qr){
        qRcodes.add(qr);
        qRtimes.add(java.util.Calendar.getInstance());
    }

    //add a log
    public void addLog(int random,String name,String line){
        logs.add(new ClientLog(random,name,line,java.util.Calendar.getInstance().getTime()));
    }

    //remove all the logs where the time is longer than 2 weeks ago
    public void checkLogs() throws ParseException {

        Date currentDate = java.util.Calendar.getInstance().getTime();

        List<ClientLog> removedLogs=new ArrayList<>();

        //searching for all the logs that need to be removed
        for(ClientLog log:logs) {

            long diffInMillies = Math.abs(currentDate.getTime() - log.getEntrytime().getTime());
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

}
