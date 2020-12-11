import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class MatchingServiceIMP extends UnicastRemoteObject implements MatchingService {

    //constructor
    MatchingServiceIMP() throws RemoteException {
        super();

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
}
