import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BarOwner extends Remote{
        public void disconnect() throws RemoteException;
}
