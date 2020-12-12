import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.util.Date;
import java.util.List;

public interface MixingProxy extends Remote {

    void addPublicKey(PublicKey key) throws RemoteException;
    void removePublicKey(PublicKey key) throws RemoteException;
    byte[] sendCapsule(Date startTime, Date endTime, String token, byte[] signature, byte[] encodedLine, PublicKey publicKey) throws RemoteException;

    List<CriticalEntry> sendCriticalLogs() throws RemoteException;
    void getInfectedTokens(List<String> infectedTokens) throws RemoteException;
}
