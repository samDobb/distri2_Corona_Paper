import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class runServers {
    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
        MatchingServiceIMP matchingService = new MatchingServiceIMP();
        MixingProxyIMP mixingService=new MixingProxyIMP();
        RegistrarIMP registrarService=new RegistrarIMP();
        matchingService.connectRegister();

    }
}
