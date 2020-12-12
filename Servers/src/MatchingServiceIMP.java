import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MatchingServiceIMP extends UnicastRemoteObject implements MatchingService {

    private List<Capsule> entries;

    private int entryTime=14;
    //constructor
    MatchingServiceIMP() throws RemoteException {
        super();

        entries=new ArrayList<>();

        try{
            //creating rmi registry
            java.rmi.registry.LocateRegistry.createRegistry(1100);
            System.out.println("MatchingService Server ready");

            //setting the registrar in the registry so the clients can find it
            Naming.rebind("rmi://localhost/MatchingService", this);
            System.out.println("MatchingService Server is running");

        }
        catch(Exception e){
            System.out.println("MatchingService Server had problems starting");
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

}
