import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

public class runFacility {


    public static void main(String[] args){
        try {
            int random=(int)Math.random()*1000;

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            KeySpec spec = new PBEKeySpec(Integer.toString(random).toCharArray());
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "SHA-256");

            Cipher cipher = Cipher.getInstance("SHA-256");
            cipher.init(Cipher.ENCRYPT_MODE, secret);

            String encodedLine = Base64.getEncoder().encodeToString(cipher.doFinal("pseudonyms.get(day)".getBytes()));

            System.out.println(encodedLine);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
