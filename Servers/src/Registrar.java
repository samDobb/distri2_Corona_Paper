import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Registrar extends Remote {

    //methods for the bar owners
     boolean checkFacilityName(String name)throws RemoteException;
     void enrollFacility(String[] details) throws RemoteException;
     void disconnectFacility(String name) throws RemoteException;
     List<byte[]> getPseudonyms(String name,String location) throws RemoteException;

    //methods for the clients/visitors
     void enrollNewUser(String[] details) throws RemoteException;
     boolean checkUserName(String username) throws RemoteException;
     boolean checkUserTel(String telephoneNumber) throws RemoteException;
     GetTokenMessage generateUserToken(String tel) throws RemoteException;

     //methods for matching service
    List<PseuLocMessage> sendPseudonyms(int day) throws RemoteException;

    void getCrits(List<Capsule> criticalEntries) throws RemoteException;
}
