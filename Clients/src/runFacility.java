
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class runFacility {

    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
        BarOwner b=new BarOwner("Yucca");
        System.out.println(b.toString());
        b.connect();
        b.generateCurrentQR();

    }
}
