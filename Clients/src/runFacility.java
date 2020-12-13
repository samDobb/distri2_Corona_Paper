import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.Security;
import java.security.spec.KeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

public class runFacility {

    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
        BarOwner b=new BarOwner("Yucca");
        System.out.println(b.toString());
        b.connect();
        b.generateCurrentQR();

    }
}
