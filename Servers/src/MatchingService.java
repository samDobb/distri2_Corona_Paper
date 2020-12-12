import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MatchingService extends Remote {

    void addCapsules(List<Capsule> capsuleList)throws RemoteException;


}
