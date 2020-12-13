import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class MatchingServiceIMP extends UnicastRemoteObject implements MatchingService {

    private List<Capsule> entries;

    private List<CriticalEntry> criticalEntries;

    private Registrar registrar;

    private int entryTime=14;

    //constructor
    MatchingServiceIMP() throws RemoteException {

        try{
            //creating rmi registry
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            System.out.println("RMI registry ready");

            //setting the registrar in the registry so the clients can find it
            Naming.rebind("rmi://localhost/MatchingService", this);
            System.out.println("MatchingService Server is running");
            entries=new ArrayList<>();
            criticalEntries=new ArrayList<>();

        }
        catch(Exception e){
            System.out.println("MatchingService Server had problems starting");
            e.printStackTrace();
        }
    }

    public void addCapsules(List<Capsule> capsuleList){
        entries.addAll(capsuleList);
    }

    //removing all the entries that are longer than X weeks
    public void checkEntries() throws ParseException {

        LocalDateTime currentDate = LocalDateTime.now();

        List<Capsule> removedEntries=new ArrayList<>();

        //searching for all the logs that need to be removed
        for(Capsule entry :entries) {


            long diff = Math.abs(entry.getEndTime().until(currentDate, ChronoUnit.DAYS));

            if(diff > entryTime){
                removedEntries.add(entry);
            }
        }

        //removing the marked logs
        for(Capsule entry:removedEntries){
            entries.remove(entry);
        }

    }
    public void removeCapsules(){
        LocalDateTime today=java.time.LocalDateTime.now();
        for(int i=0;i<entries.size();i++){
            Capsule c=entries.get(i);
            long diff = Math.abs(c.getStartTime().until(today, ChronoUnit.DAYS));
            if(diff>entryTime){
                entries.remove(c);
            }
        }
    }
    public void updateCriticalLogs(){

    }

    //getting the logs from the practitioner and searching for the critical entry
    @Override
    public boolean getCriticalLogs(List<ClientLog> logs, byte[] signature, PublicKey publicKey) throws RemoteException {

        //demo purposses 1 surefire entry that must be informed
        entries.add(new Capsule(logs.get(0).getEntryTime().minusMinutes(10),logs.get(0).getStopTime(),logs.get(0).getHash(),"token"));

        try {
            //converting the logs list to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(logs);
            byte[] newLogs = bos.toByteArray();

            //Creating a Signature object
            Signature sign = Signature.getInstance("SHA256withDSA");
            sign.initVerify(publicKey);
            sign.update(newLogs);

            if(!sign.verify(signature)){
                return false;
            }

            for (ClientLog log : logs) {
                //validating the log
                if (validateLog(log)) {

                    for (Capsule entry : entries) {

                        //if the facility is the same then go
                        if (log.getHash().equals(entry.getHash())) {

                            System.out.println("hashes the same");
                            //if the start time from the log is between the times of the entry then go
                            if (log.getEntryTime().isAfter(entry.getStartTime()) && log.getEntryTime().isBefore(entry.getEndTime())) {

                                //if the user is already informed
                                if(entry.getInformed()) {
                                    //if the token is the same then the user is already informed
                                    if (log.getToken().equals(entry.getToken())) {
                                        System.out.println("token is hetzelfde");
                                        entry.setInformed(true);
                                    }
                                }
                                else criticalEntries.add(new CriticalEntry(entry.getStartTime(),entry.getEndTime(),entry.getHash()));
                            }
                            //if the end time from the log is between the times of the entry then go
                            else if(log.getStopTime().isBefore(entry.getEndTime()) && log.getStopTime().isAfter(entry.getStartTime())){
                                //if the user is already informed
                                if(entry.getInformed()) {
                                    //if the token is the same then the user is already informed
                                    if (log.getToken().equals(entry.getToken())) {
                                        System.out.println("token is hetzelfde");
                                        entry.setInformed(true);
                                    }
                                }
                                else criticalEntries.add(new CriticalEntry(entry.getStartTime(),entry.getEndTime(),entry.getHash()));
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            System.out.println("The checking of the Patient logs have been stopped\n");
            e.printStackTrace();
            return false;
        }

        System.out.println(criticalEntries.size());
        return true;
    }

    //checks if the log is valid
    public boolean validateLog(ClientLog log) throws RemoteException, NoSuchAlgorithmException {
        LocalDateTime date = log.getEntryTime();
        int day=date.getDayOfMonth();

        List<PseuLocMessage> pseudonyms= registrar.sendPseudonyms(day);

        String logHash=log.getHash();

        int random = log.getRi();

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        //checking for every pseudonym if the newly made pseudonym
        for(PseuLocMessage pseu:pseudonyms){

            String line = new String(pseu.getPseudonym(), StandardCharsets.ISO_8859_1);
            line=random+line;

            String hash = new String(md.digest(line.getBytes(StandardCharsets.ISO_8859_1)),StandardCharsets.ISO_8859_1);

            if(hash.equals(logHash)){
                    return true;
            }

        }
        return false;
    }

    //RMI: sends the critical logs
    @Override
    public  List<CriticalEntry> sendCriticalLogs(){
        return criticalEntries;
    }

    public void connectRegister() throws RemoteException, NotBoundException, MalformedURLException {

        registrar =(Registrar) Naming.lookup("rmi://localhost/Registrar");
    }

    // setting the given tokens on informed
    @Override
    public void getInfectedTokens(List<String> infectedTokens){
        for(Capsule capsule:entries) {
            for(String token:infectedTokens)
                if(capsule.getToken().equals(token)){
                    capsule.setInformed(true);
                }
        }
    }

    //contact the remaining uninformed
    public void contactUninformed() throws RemoteException {
        List<Capsule> uninformedCrits=new ArrayList<>();

        for(CriticalEntry crit:criticalEntries){
            for(Capsule entry:entries){

                //if the facility is the same then go
                if (crit.getHash().equals(entry.getHash())) {

                    //if the start time from the crit is between the times of the entry then go
                    if (crit.getStartTime().isAfter(entry.getStartTime()) && crit.getStartTime().isBefore(entry.getEndTime())) {
                        if (!entry.getInformed()) {
                            uninformedCrits.add(entry);
                        }
                    }
                }

            }
        }

        if(!uninformedCrits.isEmpty()){
            registrar.getCrits(uninformedCrits);
            uninformedCrits.clear();
        }
    }
    public void testTimer(){
        System.out.println("Timer triggerd Matching service");
    }

}
