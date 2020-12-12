import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

        Date currentDate = java.util.Calendar.getInstance().getTime();

        List<Capsule> removedEntries=new ArrayList<>();

        //searching for all the logs that need to be removed
        for(Capsule entry :entries) {

            long diffInMillies = Math.abs(currentDate.getTime() - entry.getStartTime().getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if(diff > entryTime){
                removedEntries.add(entry);
            }
        }

        //removing the marked logs
        for(Capsule entry:removedEntries){
            entries.remove(entry);
        }

    }

    //getting the logs from the practitioner and searching for the critical entry
    public void getCriticalLogs(List<ClientLog> logs) throws RemoteException {
        try {
            for (ClientLog log : logs) {
                //validating the log
                if (validateLog(log)) {
                    for (Capsule entry : entries) {

                        //if the facility is the same then go
                        if (log.getEncodedLine().equals(entry.getEncodedLine())) {

                            //if the start time from the entry is between the times of the log then go
                            if (entry.getStartTime().after(log.getEntryTime()) && entry.getStartTime().before(log.getStopTime())) {

                                //if the user is already informed
                                if(entry.getInformed()) {
                                    //if the token is the same then the user is already informed
                                    if (log.getToken().equals(entry.getToken())) {
                                        entry.setInformed(true);
                                    }
                                    criticalEntries.add(new CriticalEntry(entry.getStartTime(),entry.getEndTime(),entry.getEncodedLine()));
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            System.out.println("The checking of the Patient logs have been stopped\n");
            e.printStackTrace();
        }
    }

    //checks if the log is valid
    public boolean validateLog(ClientLog log) throws RemoteException, NoSuchAlgorithmException {
        String[] split = log.getEntryTime().toString().split(" ");
        int day=Integer.parseInt(split[2]);

        List<PseuLocMessage> pseudonyms= registrar.sendPseudonyms(day);


        byte[] logEncodedLine=log.getEncodedLine();
        int random = log.getRandom();

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        //checking for every pseudonym if the newly made pseudonym
        for(PseuLocMessage pseu:pseudonyms){

            String line = new String(logEncodedLine)+random+pseu.getLocation();
            byte[] encodedLine = md.digest(line.getBytes());

            if(pseu.getPseudonym().equals(encodedLine))return true;

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
                if (crit.getEncodedLine().equals(entry.getEncodedLine())) {

                    //if the start time from the crit is between the times of the entry then go
                    if (crit.getStartTime().after(entry.getStartTime()) && crit.getStartTime().before(entry.getEndTime())) {
                        if (!entry.getInformed()) {
                            uninformedCrits.add(entry);
                        }
                    }
                }

            }
        }

        if(!uninformedCrits.isEmpty())registrar.getCrits(uninformedCrits);
    }

}
