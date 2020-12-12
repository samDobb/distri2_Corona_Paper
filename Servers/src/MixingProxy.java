import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.util.Date;
import java.util.List;

public interface MixingProxy extends Remote {

    void addPublicKey(PublicKey key) throws RemoteException;
    byte[] sendCapsule(Date startTime, Date endTime, String token, byte[] signature, String encodedLine, PublicKey publicKey) throws RemoteException;

    List<Capsule> sendCriticalLogs() throws RemoteException;
}
