import java.rmi.RemoteException;

public class runServers {
    public static void main(String[] args) throws RemoteException {
        MatchingServiceIMP matchingService = new MatchingServiceIMP();
        RegistrarIMP registrarService=new RegistrarIMP();
        MixingProxyIMP mixingService=new MixingProxyIMP();


    }
}
