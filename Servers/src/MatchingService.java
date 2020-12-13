import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.util.List;

public interface MatchingService extends Remote {

    void addCapsules(List<Capsule> capsuleList)throws RemoteException;

    boolean getCriticalLogs(List<ClientLog> logs, byte[] signature, PublicKey publicKey) throws RemoteException;

    List<CriticalEntry> sendCriticalLogs() throws RemoteException;

    void getInfectedTokens(List<String> infectedTokens) throws RemoteException;
}
