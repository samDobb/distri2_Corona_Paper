import java.nio.charset.StandardCharsets;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
    private boolean firstTokenSent=false;
    private QRcode currSesQR;

    ClientGUI gui;

    private int tokenTime = 30; //in minutes
    private int entryTime = 14; //in days

    private VisitorTokenScheduler tokenScheduler;
    private VisitorDayScheduler dayScheduler;
    private Timer timer;

    public Visitor(String username, String telephoneNumber, ClientGUI gui) throws RemoteException {
        super();

        this.username = username;
        this.telephoneNumber = telephoneNumber;
        this.gui=gui;
        this.sessionRunning=false;
        qRcodes = new ArrayList<>();
        qRtimes = new ArrayList<>();
        signatures = new ArrayList<>();
        pastTokens=new ArrayList<>();
        logs=new ArrayList<>();
        activeTokens=new ArrayList<>();
        dayScheduler=new VisitorDayScheduler(this);
        timer=new Timer();

    }

    public String getUsername(){
        return username;
    }
    public boolean getInfected(){return infected;}

    public  List<ClientLog> getLogs(){
        return logs;
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
                //execute certain functions every day
                timer.schedule(dayScheduler,0,86400000);

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
    public void setNewTokens() throws RemoteException {
        //setting tokens
        GetTokenMessage m=registrar.generateUserToken(username,publicKey);
        activeTokens=m.getTokens();
        signatures=m.getSignatures();
        publicKey=m.getPublicKey();
    }


    public String readQr(QRcode qr) throws RemoteException {
        byte[]resp;
        String token;
        if(sessionRunning){
            System.out.println("A session is already running");
            return null;
        }
        if(!activeTokens.isEmpty()){
            qRcodes.add(qr);
            qRtimes.add(java.util.Calendar.getInstance());
            token=activeTokens.get(0);
            byte[] sign=signatures.get(0);
            pastTokens.add(token);
            activeTokens.remove(token);
            signatures.remove(sign);
            System.out.println("Remaining tokens: " + activeTokens.size());
            LocalDateTime Dbegin=LocalDateTime.now();
            LocalDateTime Dend=Dbegin.plusMinutes(30);
            resp=mixingProxy.sendCapsule(Dbegin,Dend,token,sign,qr.getEncodedLine(),publicKey);
        }
        else{
            token=null;
            resp=null;
            System.out.println("No more tokens to spend");

        }

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
            //start schedule to send capsule each 30 minutes
            long tsec=tokenTime*100;
            firstTokenSent=true;
            tokenScheduler=new VisitorTokenScheduler(this);
            timer.schedule(tokenScheduler,0,tsec);
            currSesQR=qr;
            return sessionSign;
        }
    }
    public void stopSession(){
        sessionRunning=false;
        //stop sending capsules
        tokenScheduler.cancel();
    }
    public void sendSessionCapsule() throws RemoteException {
        //skip first execution
        if(!firstTokenSent){
            if(!activeTokens.isEmpty()){
                System.out.println("Spending another token");
                String token=activeTokens.get(0);
                byte[] sign=signatures.get(0);
                pastTokens.add(token);
                activeTokens.remove(token);
                System.out.println("Remaining tokens: " + activeTokens.size());
                signatures.remove(sign);
                LocalDateTime Dbegin=LocalDateTime.now();
                LocalDateTime Dend=Dbegin.plusMinutes(30);
                byte[]resp=mixingProxy.sendCapsule(Dbegin,Dend,token,sign,currSesQR.getEncodedLine(),publicKey);
                if(resp==null)System.out.println("An error occured sending the capsule");
            }
            else{
                System.out.println("No more tokens to spend");
            }
        }
        else{
            firstTokenSent=false;
        }



    }

    //add a log
    public void addLog(int Ri, String CF, String hash, String token) {
        LocalDateTime firstDate = LocalDateTime.now();

        LocalDateTime secondDate = LocalDateTime.now().plusMinutes(30);

        logs.add(new ClientLog(Ri, CF, hash, token, firstDate, secondDate));
    }

    //remove all the logs where the time is longer than 2 weeks ago
    public void removeLogs() {

        LocalDateTime currentDate = LocalDateTime.now();

        List<ClientLog> removedLogs = new ArrayList<>();

        //searching for all the logs that need to be removed
        for (ClientLog log : logs) {

            long diff = Math.abs(log.getEntryTime().until(currentDate, ChronoUnit.DAYS));

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
        signatures.clear();
        activeTokens.clear();
    }

    public void getCriticalEntries() throws RemoteException {
        List<CriticalEntry> criticalEntries = mixingProxy.sendCriticalLogs();
        List<String> infectedTokens = new ArrayList<>();

        for (CriticalEntry entry : criticalEntries) {
            for (ClientLog log : logs) {
                if (entry.getHash().equals(log.getHash())) {
                    System.out.println("Hashes match!");
                    if(entry.getStartTime().isBefore(log.getStopTime()) && entry.getStartTime().isAfter(log.getEntryTime())){
                        infectedTokens.add(log.getToken());
                        infected=true;
                        gui.setInformed();
                    }
                }
            }
        }

        mixingProxy.getInfectedTokens(infectedTokens);
    }


    @Override
    public String toString() {
        return "VisitorIMP{" +
                "username='" + username + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                '}';
    }
}