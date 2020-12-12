import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MatchingServiceIMP extends UnicastRemoteObject implements MatchingService {

    private List<Capsule> entries;

    private List<Capsule> criticalEntries;

    private Registrar registrar;

    private int entryTime=14;

    //constructor
    MatchingServiceIMP() throws RemoteException {
        super();

        entries=new ArrayList<>();

        try{
            //creating rmi registry
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            System.out.println("MatchingService Server ready");

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

    //removing all the entries that are longer than 2 weeks ago
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

    //getting the logs from the practitionar and searching for the critical entry
    public void getCriticalLogs(List<ClientLog> logs) throws RemoteException {
        for(ClientLog log:logs){

            //validating the log
            if(validateLog(log)) {
                for (Capsule entry : entries) {

                    //if the facility is the same then go
                    if (log.getEncodedLine().equals(entry.getEncodedLine())) {

                        //if the start time from the entry is between the times of the log then go
                        if (entry.getStartTime().after(log.getEntryTime()) && entry.getStartTime().before(log.getStopTime())) {

                            //if the token is the same then the user is already informed
                            if (log.getToken().equals(entry.getToken())) {
                                entry.setInformed(true);
                            }
                            criticalEntries.add(entry);
                        }
                    }
                }
            }
        }
    }

    //checks if the log is valid
    public boolean validateLog(ClientLog log) throws RemoteException {
        String[] split = log.getEntryTime().toString().split(" ");
        int day=Integer.parseInt(split[2]);

        List<String> pseudonyms= registrar.sendPseudonyms(day);

        String logPseu=" ";

        for(String pseu:pseudonyms){
            if(pseu.equals(logPseu))return true;
        }

        return false;
    }

    //RMI: sends the critical logs
    public  List<Capsule> sendCriticalLogs(){
        return criticalEntries;
    }

    public void connectRegister() throws RemoteException, NotBoundException, MalformedURLException {

        registrar =(Registrar) Naming.lookup("rmi://localhost/Registrar");
    }

}
