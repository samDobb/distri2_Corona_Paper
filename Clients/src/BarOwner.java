import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BarOwner extends Remote{
         void disconnect() throws RemoteException;
         void setPseudonyms(List<String> pseudonyms)throws RemoteException;
}
