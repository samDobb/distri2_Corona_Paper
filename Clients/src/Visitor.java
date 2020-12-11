import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.util.List;

public interface Visitor extends Remote {

    void setTokens(List<String> tokens, List<byte[]> signatures, PublicKey publicKey) throws RemoteException;
    void collectSignature() throws RemoteException;

}
