import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Visitor extends Remote {

    void setTokens(List<String> tokens) throws RemoteException;

}
