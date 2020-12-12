import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MatchingService extends Remote {

    void addCapsules(List<Capsule> capsuleList)throws RemoteException;

    void getCriticalLogs(List<ClientLog> logs) throws RemoteException;

    List<Capsule> sendCriticalLogs() throws RemoteException;
}
