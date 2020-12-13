import javax.imageio.spi.RegisterableService;
import java.nio.charset.StandardCharsets;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Visitor {
    private String username;
    private String telephoneNumber;

    private Registrar registrar;

    private List<String> pastTokens;
    private List<String> activeTokens;

    private List<byte[]> signatures;

    private PublicKey publicKey;

    private List<QRcode> qRcodes;
    private List<Calendar> qRtimes;

    private MixingProxy mixingProxy;
    private String sessionSign;

    private List<ClientLog> logs;

    private boolean infected=false;
    private boolean sessionRunning=false;

    private int tokenTime = 30; //in minutes
    private int entryTime = 14; //in days

    public Visitor(String username, String telephoneNumber) throws RemoteException {
        super();

        this.username = username;
        this.telephoneNumber = telephoneNumber;

        qRcodes = new ArrayList<>();
        qRtimes = new ArrayList<>();
        signatures = new ArrayList<>();
        pastTokens=new ArrayList<>();
        logs=new ArrayList<>();

    }
    //making connection to the registry and starting the rmi for this facility
    public boolean startClient() {

        try {
            registrar = (Registrar) Naming.lookup("rmi://localhost/Registrar");
            mixingProxy = (MixingProxy) Naming.lookup("rmi://localhost/MixingProxy");

            //checking if name is not taken
            if (registrar.checkUserName(username) && registrar.checkUserTel(telephoneNumber)) {
                //registering this visitor to the registrar
                String[] details = {username, telephoneNumber};
                registrar.enrollNewUser(details);
                //setting inital tokens
                GetTokenMessage m=registrar.generateUserToken(username,publicKey);
                activeTokens=m.getTokens();
                signatures=m.getSignatures();
                publicKey=m.getPublicKey();
                return true;
            }
            else{
                System.out.println("Problem with username or tel");
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }


    public String readQr(QRcode qr) throws RemoteException {
        qRcodes.add(qr);
        qRtimes.add(java.util.Calendar.getInstance());
        String token=activeTokens.get(0);
        byte[] sign=signatures.get(0);
        pastTokens.add(token);
        activeTokens.remove(token);
        signatures.remove(sign);
        LocalDateTime Dbegin=java.time.LocalDateTime.now();
        LocalDateTime Dend=Dbegin.plusMinutes(30);
        Date begin=Date.from(Dbegin.atZone(ZoneId.systemDefault()).toInstant());
        Date end=Date.from(Dend.atZone(ZoneId.systemDefault()).toInstant());
        byte[]resp=mixingProxy.sendCapsule(begin,end,token,sign,qr.getEncodedLine(),publicKey);
        if(resp==null){
            System.out.println("Error while verifying your access token");
            return null;
        }
        else{
            int a=resp[0];int b= resp[6]; int c= resp[10];
            sessionSign= a+" "+b+" "+c ;
            System.out.println("Session signature received");
            sessionRunning=true;
            this.addLog(qr.getRandomNumber(),qr.getCF(),new String(qr.getEncodedLine(), StandardCharsets.ISO_8859_1),token);
            return sessionSign;
        }
    }
    public void stopSession(){
        sessionRunning=false;
    }

    //add a log
    public void addLog(int Ri, String CF, String hash, String token) {
        Date firstDate = java.util.Calendar.getInstance().getTime();

        Calendar c = java.util.Calendar.getInstance();
        c.add(Calendar.MINUTE, 30);
        Date secondDate = c.getTime();

        logs.add(new ClientLog(Ri, CF, hash, token, firstDate, secondDate));
    }

    //remove all the logs where the time is longer than 2 weeks ago
    public void removeLogs() throws ParseException {

        Date currentDate = java.util.Calendar.getInstance().getTime();

        List<ClientLog> removedLogs = new ArrayList<>();

        //searching for all the logs that need to be removed
        for (ClientLog log : logs) {

            long diffInMillies = Math.abs(currentDate.getTime() - log.getEntryTime().getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (diff > entryTime) {
                removedLogs.add(log);
            }
        }

        //removing the marked logs
        for (ClientLog log : removedLogs) {
            logs.remove(log);
        }

    }

    public void shiftActiveTokens() {
        for (String token : activeTokens) {
            pastTokens.add(token);
        }
        activeTokens.clear();
    }

   /* public void getCriticalEntries() throws RemoteException {
        List<CriticalEntry> criticalEntries = mixingProxy.sendCriticalLogs();
        List<String> infectedTokens = new ArrayList<>();

        for (CriticalEntry entry : criticalEntries) {
            for (ClientLog log : logs) {
                if (entry.getEncodedLine().equals(log.getEncodedLine())) {
                    if(entry.getStartTime().before(log.getStopTime()) && entry.getStartTime().after(log.getEntryTime())){
                        infectedTokens.add(log.getToken());
                        infected=true;
                    }
                }
            }
        }

        mixingProxy.getInfectedTokens(infectedTokens);
    }*/


    @Override
    public String toString() {
        return "VisitorIMP{" +
                "username='" + username + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                '}';
    }
}