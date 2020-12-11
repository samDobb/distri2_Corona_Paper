import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;

public interface MixingProxy extends Remote {

    boolean sendCapsule(String time, String token, byte[] signature, String encodedLine, PublicKey publicKey) throws RemoteException;
}
