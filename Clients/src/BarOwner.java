import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BarOwner extends Remote{
        public void disconnect() throws RemoteException;
        public void setPseudonyms(List<String> pseudonyms)throws RemoteException;
}
