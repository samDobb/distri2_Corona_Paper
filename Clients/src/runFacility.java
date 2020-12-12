import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.Security;
import java.security.spec.KeySpec;
import java.util.Base64;

public class runFacility {


    public static void main(String[] args) throws RemoteException {


        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            String line="this is a test";

            byte[] encLine=md.digest(line.getBytes());
            byte[] encLine2=md.digest(line.getBytes());

            for(int i=0;i<encLine.length;i++){
                if(encLine[i]!=encLine2[i])System.out.println("wrong");
            }

            System.out.println(encLine);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
